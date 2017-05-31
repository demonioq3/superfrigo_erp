package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaJasperObject;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaObject;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.jms.ManagerRegistroSalida;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.GuiasSalidaLazyDataModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.jms.*;
import javax.naming.InitialContext;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class GuiaSalidaBean extends BaseBean implements Serializable {

    private GuiaSalida editItem;

    /* Lista de productos asociados a una guia de salida */
    private List<SalidaProductoCantidad> productos;

    /* Todas las guias de entradas lazy */
    private GuiasSalidaLazyDataModel guiasSalida;

    /* Render de botón eliminar */
    private boolean mostrarBotonEliminar = false;

    /* Render de conceptos */
    private boolean showFragmentVariable = false;
    private boolean showCodigoBodegaOrigen = false;
    private boolean showProveedor = false;      // Rut y nombre.
    private boolean showOrdenCompra = false;
    private boolean showNotaCredito = false;
    private boolean showOrdenTrabajo = false;
    private boolean showCentroCosto = false;

    /* Boolean para saber si es edición  */
    private boolean sePuedeGuardar = false;

    private FichasProductoLazyDataModel fichasProducto;

    @EJB
    private GuiaEntradaDAO guiaEntradaDAO;

    @EJB
    private GuiaSalidaDAO guiaSalidaDAO;

    @EJB
    private ConceptoSalidaDAO conceptoSalidaDAO;

    @EJB
    private PrecioProductoDAO precioProductoDAO;

    @EJB
    private SalidaProductoCantidadDAO salidaProductoCantidadDAO;

    @EJB
    private BodegaStockProductoDAO bodegaStockProductoDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    @EJB
    private FichaAuxiliarDAO fichaAuxiliarDAO;

    @EJB
    private TipoFichaAuxiliarDAO tipoFichaAuxiliarDAO;

    @EJB
    private OrdenDeCompraDAO ordenDeCompraDAO;

    @EJB
    private BodegaDAO bodegaDAO;

    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    @EJB
    private EntradaProductoCantidadDAO entradaProductoCantidadDAO;

    @EJB
    private ManagerRegistroSalida register;


    @PostConstruct
    public void init() {
        editItem = new GuiaSalida();

        /* Inicialización de objetos */
        productos = new ArrayList<SalidaProductoCantidad>();
        editItem.setProveedor(new FichaAuxiliar());

        // Colocando fecha actual a la guia de entrada.
        editItem.setFecha(new Date());
    }

    public void buscarPorGuia() {
        GuiaSalida guiaBuscada = guiaSalidaDAO.findById(editItem.getId());

        if(guiaBuscada != null){
            this.editItem = guiaBuscada;

            if(this.editItem.getProveedor() == null){
                this.editItem.setProveedor(new FichaAuxiliar());
            }

            // Se crea la lista de subgrupos para renderiar en la vista.
            productos = guiaBuscada.getProductos();

            renderByConcepto(guiaBuscada.getConceptoSalidaId());

            // Mostrar el botón eliminar.
            mostrarBotonEliminar = true;

            // Se puede guardar.
            sePuedeGuardar = true;

            showInfo("Aviso", "Se cargó la guía de salida " + this.editItem.getId() + " de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            Long codigoBuscado = this.editItem.getId();

             /* Inicialización de objetos */
            this.editItem = new GuiaSalida();
            productos = new ArrayList<SalidaProductoCantidad>();
            editItem.setProveedor(new FichaAuxiliar());

            // Colocando fecha actual a la guia de entrada.
            editItem.setFecha(new Date());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "La guía " + codigoBuscado +" no se encuentra registrada.");
        }
    }

    public List<ConceptoSalida> getAllConceptos(){
        return conceptoSalidaDAO.findAll();
    }

    public void seleccionarGuiaSalida(GuiaSalida guiaSalida){
        editItem = guiaSalida;

        if(this.editItem.getProveedor() == null){
            this.editItem.setProveedor(new FichaAuxiliar());
        }

        // Se crea la lista de subgrupos para renderiar en la vista.
        productos = guiaSalida.getProductos();

        renderByConcepto(guiaSalida.getConceptoSalidaId());

        // Mostrar el botón eliminar.
        mostrarBotonEliminar = true;

        // Se puede guardar.
        sePuedeGuardar = true;

        showInfo("Aviso", "Se cargó la guía de salida " + this.editItem.getId() + " de forma correcta.");
    }

    public void cambioConcepto() {
        renderByConcepto(this.editItem.getConceptoSalidaId());
    }

    private void renderByConcepto(Long conceptoSalidaId) {
        if(ConceptoSalida.DEVOLUCION_MATERIAL_A_PROVEEDOR.equals(conceptoSalidaId)){
            showFragmentVariable = true;
            showProveedor = true;
            showOrdenCompra = true;
            showNotaCredito = true;
            showCodigoBodegaOrigen = false;
            showCentroCosto = false;
            showOrdenTrabajo = false;
        }

        if(ConceptoSalida.TRANSFERENCIA_HACIA_OTRA_BODEGA.equals(conceptoSalidaId)){
            showFragmentVariable = true;
            showProveedor = false;
            showOrdenCompra = false;
            showNotaCredito = false;
            showCodigoBodegaOrigen = true;
            showCentroCosto = false;
            showOrdenTrabajo = false;
        }

        if(ConceptoSalida.CONSUMO_POR_CENTRO_DE_COSTO.equals(conceptoSalidaId)){
            showFragmentVariable = true;
            showProveedor = false;
            showOrdenCompra = false;
            showNotaCredito = false;
            showCodigoBodegaOrigen = false;
            showCentroCosto = true;
            showOrdenTrabajo = false;
        }

        if(ConceptoSalida.CONSUMO_POR_ORDEN_DE_TRABAJO.equals(conceptoSalidaId)){
            showFragmentVariable = true;
            showProveedor = false;
            showOrdenCompra = false;
            showNotaCredito = false;
            showCodigoBodegaOrigen = false;
            showCentroCosto = false;
            showOrdenTrabajo = true;
        }

        if(ConceptoSalida.SALIDA_POR_TOMA_INVENTARIO.equals(conceptoSalidaId)){
            showFragmentVariable = false;
            showProveedor = false;
            showOrdenCompra = false;
            showNotaCredito = false;
            showCodigoBodegaOrigen = false;
            showCentroCosto = false;
            showOrdenTrabajo = false;
        }
    }

    public void agregarProducto(FichaProducto fichaProducto){
        if(this.editItem.getBodegaId() == null || this.editItem.getBodegaId() == 0){
            mostrarError("Para ingresar productos asociados a la guia de entrada, debes seleccionar antes la bodega a la cual solicitas la extracción de productos.");
        } else {
            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(fichaProducto.getId(), this.editItem.getBodegaId());

            if(bodegaStockProducto != null && bodegaStockProducto.getCantidad() > 0){
                SalidaProductoCantidad nuevoProducto = new SalidaProductoCantidad();
                nuevoProducto.setFichaProductoId(fichaProducto.getId());
                nuevoProducto.setFichaProducto(fichaProducto);
                nuevoProducto.setCantidad(0D);
                nuevoProducto.setPrecioUnitario(0D);
                nuevoProducto.setPrecioTotal(0D);

                if(productos == null){
                    productos = new ArrayList<SalidaProductoCantidad>();
                }

                productos.add(nuevoProducto);
            } else {
                Bodega bodega = bodegaDAO.getById(this.editItem.getBodegaId());

                mostrarError("No hay stock del producto " + fichaProducto.getCodigoProducto() + " en la bodega " + bodega.getDescripcion());
            }

        }
    }

    private void mostrarError(String s) {
        showError("Error", s);
        RequestContext.getCurrentInstance().scrollTo("main-content");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void guardar() throws JMSException, NamingException {

        // Dejar todos los input no ingresados de ID de 0 a null, para evitar errores.
        cambioInputNull();


        if(!sePuedeGuardar){
            // Validacion: Validando que el id de guia no exista anteriormente.
            GuiaSalida guiaSalidaExistente = guiaSalidaDAO.findById(this.editItem.getId());
            if(guiaSalidaExistente != null){
                showError("Error", "La guía de salida ya está registrada.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        // Ver si ingreso bodega.
        if(this.editItem.getBodegaId() == null || this.editItem.getBodegaId() == 0){
            showError("Error", "Debe ingresar la bodega para guardar una guía de salida.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        // Ver si ingreso concepto de entrada.
        if(this.editItem.getConceptoSalidaId() == null || this.editItem.getConceptoSalidaId() == 0){
            showError("Error", "Debe ingresar concepto de entrada para guardar una guía de entrada.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        // Validar por concepto de salida.
        // 1) Validar si la guía es de concepto devolución material a proveedor|.
        if(ConceptoSalida.DEVOLUCION_MATERIAL_A_PROVEEDOR.equals(this.editItem.getConceptoSalidaId())){

            // Validar orden de compra.
            if(this.editItem.getOrdenDeCompraId() != null){
                OrdenDeCompra oc = ordenDeCompraDAO.getById(this.editItem.getOrdenDeCompraId());

                if(oc == null){
                    showError("Error", "La orden de compra ingresada no existe. La guia de salida no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de compra si el concepto de entrada es \"Devolución de metarial a proveedor\". La guia de salida no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }

        }

        // 2) Validar si la guía es de concepto transferencia entre bodega.
        if(ConceptoSalida.TRANSFERENCIA_HACIA_OTRA_BODEGA.equals(this.editItem.getConceptoSalidaId())) {
            // Validar si es la misma bodega a la cual se está haciendo la transferencia. Si es el caso, se debe mostrar mensaje de error.
            if (this.editItem.getBodegaId().equals(this.editItem.getBodegaOrigenId())) {
                // TODO: Se deja en null id para que no cambie a 0 en cargada ajax.
                this.editItem.setId(null);

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                showError("Error", "No se puede generar una guia de salida de tipo \"Transferencia de bodega\" solicitando la misma bodega de origen.");
                return;
            } else {
                // Revisando la cantidad de productos, si no ha agregado productos muestra mensaje de error
                if (productos.size() == 0) {
                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    showError("Error", "Debe ingresar productos a la guia de salida para solicitar la transferencia entre bodega.");
                    return;
                } else {
                    showError("Error", "Error.");
                    return;

                    // Si tiene productos, validamos si existe stock de ése producto en la bodega.
                    /*
                    for (SalidaProductoCantidad producto : productos) {

                        BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), this.editItem.getBodegaOrigenId());
                        if(bodegaStockProducto.getCantidad() < producto.getCantidad()){
                            // Move to top
                            RequestContext.getCurrentInstance().scrollTo("main-content");
                            showError("Error", "No hay stock en la bodega \"" + bodegaDAO.getById(this.editItem.getBodegaOrigenId()).getCodigo()
                                    + "-" + bodegaDAO.getById(this.editItem.getBodegaOrigenId()).getDescripcion() + "\"  para el producto " + producto.getFichaProducto().getCodigoProducto()
                                    + "-" + producto.getFichaProducto().getDescripcion());
                            return;
                        } else {
                            // Generamos la guia de salida de la bodega de origen.
                            GuiaEntrada guiaEntrada = new GuiaEntrada();
                            guiaEntrada.setFecha(new Date());

                            // Se guarda la bodega de origen como de salida.
                            guiaEntrada.setBodegaId(this.editItem.getBodegaOrigenId());


                        }
                    }
                    */
                }
            }
        }

        // 3) Validacion de concetpo "Consumo por centro de costo"
        if(ConceptoSalida.CONSUMO_POR_CENTRO_DE_COSTO.equals(this.editItem.getConceptoSalidaId())){
            // Validar si seleccionó el centro de costos
            if(this.editItem.getCentroDeCostoId() == null){
                showError("Error", "Debe ingresar el centro de costos si el concepto de entrada es \"Consumo por centro de costo\". La guia de salida no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        // 4) Validacion de concetpo "Consumo por OT"
        if(ConceptoSalida.CONSUMO_POR_ORDEN_DE_TRABAJO.equals(this.editItem.getConceptoSalidaId())){
            // Validar orden de trabajo.
            if(this.editItem.getOrdenDeTrabajoId() != null){
                OrdenDeTrabajo ot = ordenDeTrabajoDAO.getById(this.editItem.getOrdenDeTrabajoId());

                if(ot == null){
                    showError("Error", "La orden de trabajo ingresada no existe. La guia de salida no se puede guardar.");
                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de trabajo si el concepto de entrada es \"Consumo por orden de trabajo\". La guia de salida no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }



        // Chequeando stocks por cada producto ingresado en la vista.
        for (SalidaProductoCantidad producto : productos) {
            BodegaStockProducto bodega = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), this.editItem.getBodegaId());
            if(producto.getCantidad() > bodega.getCantidad()){
                showError("Error", "El stock de la bodega es insuficiente para sacar productos de código " + producto.getFichaProducto().getCodigoProducto());
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        if(this.editItem.getId() == null || this.editItem.getId() == 0){
            // Se eliminar el proveedor asignado. FIXME
            editItem.setId(null);
            editItem.setProveedor(null);

            try{
                GuiaSalida guiaSalidaCreada = guiaSalidaDAO.create(this.editItem);

                // Asociando los productos a la guia de entrada.
                for (SalidaProductoCantidad producto : productos) {
                    producto.setGuiaSalidaId(guiaSalidaCreada.getId());

                    // Dejando solo el ID de la ficha producto.
                    FichaProducto ficha = producto.getFichaProducto();
                    producto.setFichaProducto(null);
                    producto.setFichaProductoId(ficha.getId());

                    // Se guarda el producto asociado a la guia de entrada.
                    SalidaProductoCantidad productoAsociadoAGuia = salidaProductoCantidadDAO.create(producto);
                }

                this.editItem = guiaSalidaDAO.findById(guiaSalidaCreada.getId());

                // encolando en cola JMS colaSalidas.
                register.record(this.editItem);

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                // Mostrar botón borrar.
                mostrarBotonEliminar = true;
                showInfo("Aviso", "Se creó de forma satisfactoria la guía de salida. El código de la guía es :" + this.editItem.getId());

                // Se procede con la limpieza del formulario por requerimiento.
                init();
            } catch (Exception e){
                showError("Error", "Hubo un error al guardar la guía de salida.");
            }

        } else {
            this.editItem.setProveedor(null);
            guiaSalidaDAO.update(this.editItem);

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "Se editó de manera correcta la guia de salida " + this.editItem.getId() + "");
        }
    }

    public void verificarCantidadIngresada(SalidaProductoCantidad salidaProductoCantidad){
        BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(salidaProductoCantidad.getFichaProductoId(), this.editItem.getBodegaId());

        if(salidaProductoCantidad.getCantidad() > bodegaStockProducto.getCantidad()){
            mostrarError("El stock actual del producto en la bodega es " + bodegaStockProducto.getCantidad() + ". Intente con un número igual o inferior a " + bodegaStockProducto.getCantidad());
            salidaProductoCantidad.setCantidad(bodegaStockProducto.getCantidad());
        }

    }

    private void cambioInputNull() {
        if(this.editItem.getOrdenDeTrabajoId() != null){
            if(this.editItem.getOrdenDeTrabajoId() == 0){
                this.editItem.setOrdenDeTrabajoId(null);
            }
        }

        if(this.editItem.getOrdenDeCompraId() != null){
            if(this.editItem.getOrdenDeCompraId() == 0){
                this.editItem.setOrdenDeCompraId(null);
            }
        }

        if(this.editItem.getConceptoSalidaId() != null){
            if(this.editItem.getConceptoSalidaId() == 0){
                this.editItem.setConceptoSalida(null);
            }
        }

        if(this.editItem.getBodegaOrigenId() != null){
            if(this.editItem.getBodegaOrigenId() == 0){
                this.editItem.setBodegaOrigenId(null);
            }
        }

        if(this.editItem.getCentroDeCostoId() != null){
            if(this.editItem.getCentroDeCostoId() == 0){
                this.editItem.setCentroDeCostoId(null);
            }
        }

        if(this.editItem.getProveedorId() != null){
            if(this.editItem.getProveedorId() == 0){
                this.editItem.setProveedorId(null);
            }
        }
    }

    public void buscarPorRutProveedor(){
        FichaAuxiliar ficha = fichaAuxiliarDAO.findByRut(this.editItem.getProveedor().getRut());

        if(ficha == null){
            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "No existe proveedor asociado al rut " + this.editItem.getProveedor().getRut());
        } else {
            if(ficha.getTipos().contains(tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.PROVEEDOR))){
                this.editItem.getProveedor().setNombreRazonSocial(ficha.getNombreRazonSocial());
            } else {
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                showInfo("Aviso", "El rut buscado está guardado, pero no es de tipo proveedor");
            }
        }
    }

    public void eliminar(){
        for (SalidaProductoCantidad producto : productos) {
            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), producto.getGuiaSalida().getBodegaId());

            // Se procede a descontar el stock y guardar
            bodegaStockProducto.setCantidad(bodegaStockProducto.getCantidad() + producto.getCantidad());
            bodegaStockProductoDAO.update(bodegaStockProducto);

            // Finalmente se procede a la eliminación del producto asociado.
            salidaProductoCantidadDAO.delete(producto);

        }

        guiaSalidaDAO.delete(this.editItem.getId());

        // Borrando el archivo borrado de la vista.
        this.editItem = new GuiaSalida();
        this.editItem.setProveedor(new FichaAuxiliar());

        // Eliminando los productos
        productos = new ArrayList<SalidaProductoCantidad>();

        // Borrando el botón eliminar.
        mostrarBotonEliminar = false;

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
        showInfo("Aviso", "Se eliminó correctamente la guia de entrada.");
    }

    public void limpiar(){
        this.editItem = new GuiaSalida();
        this.editItem.setProveedor(new FichaAuxiliar());

        // Colocando la fecha actual por defecto.
        this.editItem.setFecha(new Date());

        productos = new ArrayList<SalidaProductoCantidad>();

        // No se puede guardar.
        sePuedeGuardar = false;

        mostrarBotonEliminar = false;
    }

    public void imprimirGuiaPDF() {
        try {
            Locale locale = new Locale("es","CL");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> parametros = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            parametros.put("id_guia", this.editItem.getId().toString());
            parametros.put("bodega", this.editItem.getBodega().getCodigo() + " - " + this.editItem.getBodega().getDescripcion());
            parametros.put("concepto", this.editItem.getConceptoSalida().getDescripcion());
            parametros.put("fecha", df.format(new Date()));
            parametros.put("ot", this.editItem.getOrdenDeTrabajoId().toString());
            parametros.put("observaciones", this.editItem.getObservaciones());


            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("jasper_guia_salida.jasper"));

            GuiaSalidaJasperObject cb = new GuiaSalidaJasperObject();
            List<GuiaSalidaObject> productosFormatted = new ArrayList<GuiaSalidaObject>();

            if(this.productos != null && this.productos.size() > 0){
                for (SalidaProductoCantidad producto : this.productos) {
                    GuiaSalidaObject nuevo = new GuiaSalidaObject();
                    nuevo.setId(producto.getId().toString());
                    nuevo.setCodigo(producto.getFichaProducto().getCodigoProducto());
                    nuevo.setDescripcion(producto.getFichaProducto().getDescripcion());
                    nuevo.setCantidad(producto.getCantidad().toString());
                    nuevo.setPunitario(producto.getPrecioUnitario().toString());
                    nuevo.setTotal(producto.getPrecioTotal().toString());
                    nuevo.setUmedida(producto.getFichaProducto().getUnidadMedida().getNombre());

                    productosFormatted.add(nuevo);
                }
            }

            cb.setProductos(productosFormatted);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(cb), false);

            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, dataSource);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=guia_salida_impr.pdf\"");
            response.setContentLength(bytes.length);
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cambioBodega(){
        productos = null;
    }

    public boolean isMostrarBotonEliminar() {
        return mostrarBotonEliminar;
    }

    public GuiaSalida getEditItem() {
        return editItem;
    }

    public void setEditItem(GuiaSalida editItem) {
        this.editItem = editItem;
    }

    public GuiasSalidaLazyDataModel getGuiasSalida() {
        if ( guiasSalida == null )
            guiasSalida = new GuiasSalidaLazyDataModel(guiaSalidaDAO);

        return guiasSalida;
    }

    public void setGuiasSalida(GuiasSalidaLazyDataModel guiasSalida) {
        this.guiasSalida = guiasSalida;
    }

    public boolean isShowFragmentVariable() {
        return showFragmentVariable;
    }

    public boolean isShowCodigoBodegaOrigen() {
        return showCodigoBodegaOrigen;
    }

    public boolean isShowProveedor() {
        return showProveedor;
    }

    public boolean isShowOrdenCompra() {
        return showOrdenCompra;
    }

    public boolean isShowNotaCredito() {
        return showNotaCredito;
    }

    public boolean isShowOrdenTrabajo() {
        return showOrdenTrabajo;
    }

    public boolean isShowCentroCosto() {
        return showCentroCosto;
    }

    public List<SalidaProductoCantidad> getProductos() {
        return productos;
    }

    public void setProductos(List<SalidaProductoCantidad> productos) {
        this.productos = productos;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public void verificarSiExisteStockEnBodega() {
        if(this.editItem.getBodegaId() == null || this.editItem.getBodegaId() == 0 ){
            mostrarError("Debe seleccionar bodega para realizar una búsqueda de productos");
        } else {
            RequestContext.getCurrentInstance().execute("PF('dialogFichasProductosVar').show()");
        }
    }

}
