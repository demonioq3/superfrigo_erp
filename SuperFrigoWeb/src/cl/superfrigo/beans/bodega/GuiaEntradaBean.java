package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.EtapaOrdenCompra;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.jms.ManagerRegistroEntrada;
import cl.superfrigo.jms.ManagerRegistroSalida;
import cl.superfrigo.jms.ManagerRegistroTransferencias;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.GuiasEntradaLazyDataModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class GuiaEntradaBean extends BaseBean implements Serializable {

    /* Guia entrada actual */
    private GuiaEntrada editItem;

    /* Todas las guias de entradas lazy */
    private GuiasEntradaLazyDataModel guiaEntradas;

    /* Lista de productos asociados a una guia de entrada */
    private List<EntradaProductoCantidad> productos;

    /* Rendered de inputs */
    private boolean showFragmentVariable = false;
    private boolean mostrarBotonEliminar = false;

    private boolean showRut = false;
    private boolean showNombre = false;
    private boolean showNumeroFactura = false;
    private boolean showNumeroGuiaDespacho = false;
    private boolean showFechaFactura = false;
    private boolean showFechaGuiaDespacho = false;
    private boolean showOrdenCompra = false;
    private boolean showOrdenTrabajo = false;
    private boolean showCentroCosto = false;
    private boolean showCodigoBodegaOrigen = false;
    private boolean showAgregarEditarProducto = true;

    /* Boolean para saber si es edición  */
    private boolean sePuedeGuardar = false;

    private FichasProductoLazyDataModel fichasProducto;

    @EJB
    private ConceptoEntradaDAO conceptoEntradaDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    @EJB
    private PrecioProductoDAO precioProductoDAO;

    @EJB
    private GuiaEntradaDAO guiaEntradaDAO;

    @EJB
    private EntradaProductoCantidadDAO entradaProductoCantidadDAO;

    @EJB
    private BodegaStockProductoDAO bodegaStockProductoDAO;

    @EJB
    private OrdenDeCompraDAO ordenDeCompraDAO;

    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    @EJB
    private BodegaDAO bodegaDAO;

    @EJB
    private FichaAuxiliarDAO fichaAuxiliarDAO;

    @EJB
    private TipoFichaAuxiliarDAO tipoFichaAuxiliarDAO;

    @EJB
    private ManagerRegistroEntrada register;

    @EJB
    private ManagerRegistroSalida outRegister;

    @EJB
    private ManagerRegistroTransferencias transferRegister;

    @EJB
    private GuiaSalidaDAO guiaSalidaDAO;

    @EJB
    private SalidaProductoCantidadDAO salidaProductoCantidadDAO;

    @EJB
    private ProductoOrdenCompraDAO productoOrdenCompraDAO;

    private final static Logger logger = LoggerFactory.getLogger(GuiaEntradaBean.class);


    @PostConstruct
    public void init() {
        editItem = new GuiaEntrada();

        /* Inicialización de objetos */
        productos = new ArrayList<EntradaProductoCantidad>();
        editItem.setProveedor(new FichaAuxiliar());

        // Colocando fecha actual a la guia de entrada.
        editItem.setFecha(new Date());
    }

    public List<GuiaEntrada> getTodasLasGuiasDeEntrada(){
        return guiaEntradaDAO.findAll();
    }

    public List<ConceptoEntrada> getAllConceptosEntrada(){
        return conceptoEntradaDAO.findAll();
    }

    public GuiaEntrada getEditItem() {
        return editItem;
    }

    public void setEditItem(GuiaEntrada editItem) {
        this.editItem = editItem;
    }

    public void buscarPorGuia() {
        GuiaEntrada guiaBuscada = guiaEntradaDAO.findById(editItem.getId());

        if(guiaBuscada != null){
            this.editItem = guiaBuscada;

            if(this.editItem.getProveedor() == null){
                this.editItem.setProveedor(new FichaAuxiliar());
            }

            // Se crea la lista de subgrupos para renderiar en la vista.
            productos = guiaBuscada.getProductos();

            renderByConcepto(guiaBuscada.getConceptoEntradaId());

            // Mostrar el botón eliminar.
            mostrarBotonEliminar = true;

            // Se puede guardar la edición.
            sePuedeGuardar = true;

            showInfo("Aviso", "Se cargó la guía de entrada " + this.editItem.getId() + " de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            Long codigoBuscado = this.editItem.getId();

             /* Inicialización de objetos */
            this.editItem = new GuiaEntrada();
            productos = new ArrayList<EntradaProductoCantidad>();
            editItem.setProveedor(new FichaAuxiliar());

            // Colocando fecha actual a la guia de entrada.
            editItem.setFecha(new Date());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "La guía " + codigoBuscado +" no se encuentra registrada.");
        }
    }

    public void guardar(){
        // Dejar todos los input no ingresados de ID de 0 a null, para evitar errores.
        cambioInputNull();

        // Validar si existe
        if(!sePuedeGuardar){
            // Validacion: Validando que el id de guia no exista anteriormente.
            GuiaEntrada guiaEntradaExistente = guiaEntradaDAO.findById(this.editItem.getId());
            if(guiaEntradaExistente != null){
                showError("Error", "La guía de entrada ya está registrada.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        // Ver si ingreso bodega.
        if(this.editItem.getBodegaId() == null || this.editItem.getBodegaId() == 0){
            showError("Error", "Debe ingresar la bodega para guardar una guía de entrada.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        // Ver si ingreso concepto de entrada.
        if(this.editItem.getConceptoEntradaId() == null || this.editItem.getConceptoEntradaId() == 0){
            showError("Error", "Debe ingresar concepto de entrada para guardar una guía de entrada.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        // Primera validación de orden de compra con productos ingresados.
        if(this.editItem.getOrdenDeCompraId() != null && this.editItem.getOrdenDeCompraId() != 0){
            OrdenDeCompra ordenDeCompra = ordenDeCompraDAO.findById(this.editItem.getOrdenDeCompraId());

            for (EntradaProductoCantidad producto : this.productos) {
                for (ProductoOrdenCompra productoOrdenCompra : ordenDeCompra.getProductos()) {
                    if(producto.getFichaProductoId().equals(productoOrdenCompra.getProductoRequisicion().getFichaProductoId())){
                        if(producto.getCantidad() > (productoOrdenCompra.getProductoRequisicion().getCantidad() + productoOrdenCompra.getRecibido())){
                            showError("Error", "No se puede guardar la guía de entrada, debido a que el producto " +producto.getFichaProducto().getCodigoProducto() + " - " +producto.getFichaProducto().getDescripcion()+ " " +
                                    "supera la cantidad de la orden de compra número " + this.editItem.getOrdenDeCompraId());

                            // Move to top
                            RequestContext.getCurrentInstance().scrollTo("main-content");
                            return;
                        }
                    }
                }
            }
        }


        // Validar por concepto de entrada.
        // 1) Validar si la guía es de concepto compra con guia de despacho.
        if(ConceptoEntrada.COMPRA_CON_GUIA_DESPACHO.equals(this.editItem.getConceptoEntradaId())){

            // Validar orden de compra.
            if(this.editItem.getOrdenDeCompraId() != null){
                OrdenDeCompra oc = ordenDeCompraDAO.findById(this.editItem.getOrdenDeCompraId());

                if(oc == null){
                    showError("Error", "La orden de compra ingresada no existe. La guia de entrada no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de compra si el concepto de entrada es \"Compra con guia de despacho\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }

            // Validar orden de trabajo.
            if(this.editItem.getOrdenDeTrabajoId() != null){
                OrdenDeTrabajo ot = ordenDeTrabajoDAO.getById(this.editItem.getOrdenDeTrabajoId());

                if(ot == null){
                    showError("Error", "La orden de trabajo ingresada no existe. La guia de entrada no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de trabajo si el concepto de entrada es \"Compra con guia de despacho\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }

            // Validar si seleccionó el centro de costos
            if(this.editItem.getCentroDeCostoId() == null){
                showError("Error", "Debe ingresar el centro de costos si el concepto de entrada es \"Compra con guia de despacho\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }

        }

        // 2) Validar si la guía es de concepto compra con factura.
        if(ConceptoEntrada.COMPRA_CON_FACTURA.equals(this.editItem.getConceptoEntradaId())){

            // Validar orden de compra.
            if(this.editItem.getOrdenDeCompraId() != null){
                OrdenDeCompra oc = ordenDeCompraDAO.getById(this.editItem.getOrdenDeCompraId());

                if(oc == null){
                    showError("Error", "La orden de compra ingresada no existe. La guia de entrada no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    this.editItem.setId(null);
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de compra si el concepto de entrada es \"Compra con factura\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                this.editItem.setId(null);
                return;
            }

            // Validar orden de trabajo.
            if(this.editItem.getOrdenDeTrabajoId() != null){
                OrdenDeTrabajo ot = ordenDeTrabajoDAO.getById(this.editItem.getOrdenDeTrabajoId());

                if(ot == null){
                    showError("Error", "La orden de trabajo ingresada no existe. La guia de entrada no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    this.editItem.setId(null);
                    return;
                }
            } else {
                showError("Error", "Debe ingresar la orden de trabajo si el concepto de entrada es \"Compra con factura\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                this.editItem.setId(null);
                return;
            }

            // Validar si seleccionó el centro de costos
            /*
            if(this.editItem.getCentroDeCostoId() == null){
                showError("Error", "Debe ingresar el centro de costos si el concepto de entrada es \"Compra con factura\". La guia de entrada no se puede guardar.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }*/

        }

        boolean esTransferencia = false;

        // 3) Validar si la guía es de concepto transferencia entre bodega.
        if(ConceptoEntrada.TRANSFERENCIA_ENTRE_BODEGA.equals(this.editItem.getConceptoEntradaId())){
            logger.debug("Se está generando una guia de entrada de transferencia entre bodegas.");

            // Primero, validar si ingresó la bodega de origen.
            if(this.editItem.getBodegaOrigenId() == null){
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                this.editItem.setId(null);
                showError("Error", "Debe ingresar la bodega de origen para generar la guía de entrada.");
                return;
            }

            // Validar si es la misma bodega a la cual se está haciendo la transferencia. Si es el caso, se debe mostrar mensaje de error.
            if(this.editItem.getBodegaId().equals(this.editItem.getBodegaOrigenId())){
                this.editItem.setId(null);

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                showError("Error", "No se puede generar una guia de entrada de tipo \"Transferencia de bodega\" solicitando la misma bodega de origen.");
                return;
            } else {
                // Revisando la cantidad de productos, si no ha agregado productos muestra mensaje de error
                if(productos.size() == 0){
                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    showError("Error", "Debe ingresar productos a la guia de entrada para solicitar la transferencia entre bodega.");
                    return;
                } else {
                    // Si tiene productos, validamos si existe stock de ése producto en la bodega.
                    for (EntradaProductoCantidad producto : productos) {
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
                            GuiaSalida guiaSalida = new GuiaSalida();
                            guiaSalida.setFecha(new Date());

                            // Se guarda la bodega de origen como de salida.
                            guiaSalida.setBodegaId(this.editItem.getBodegaOrigenId());
                            guiaSalida.setCentroDeCostoId(this.editItem.getCentroDeCostoId());
                            guiaSalida.setConceptoSalidaId(ConceptoSalida.TRANSFERENCIA_HACIA_OTRA_BODEGA);
                            guiaSalida.setProveedorId(this.editItem.getProveedorId());
                            guiaSalida.setOrdenDeTrabajoId(this.editItem.getOrdenDeTrabajoId());
                            guiaSalida.setObservaciones(this.editItem.getObservaciones());

                            // Se guarda la guia de salida
                            GuiaSalida guiaSalidaCreada = guiaSalidaDAO.create(guiaSalida);

                            // Se asignan los productos
                            for (EntradaProductoCantidad entradaProductoCantidad : productos) {
                                SalidaProductoCantidad salidaProductoCantidad = new SalidaProductoCantidad();
                                salidaProductoCantidad.setGuiaSalidaId(guiaSalidaCreada.getId());
                                salidaProductoCantidad.setCantidad(entradaProductoCantidad.getCantidad());
                                salidaProductoCantidad.setFichaProductoId(entradaProductoCantidad.getFichaProductoId());

                                // Se crean las salidas.
                                SalidaProductoCantidad salidaProductoCreada = salidaProductoCantidadDAO.create(salidaProductoCantidad);
                            }

                            guiaSalidaCreada = guiaSalidaDAO.findById(guiaSalidaCreada.getId());

                            // Se encola la guía de salida.
                            transferRegister.record(guiaSalidaCreada);
                            esTransferencia = Boolean.TRUE;
                        }

                    }
                }
            }

        }

        // Fuera si es transferencia.
        if(esTransferencia == Boolean.TRUE){
            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            // Mostrar botón borrar.
            mostrarBotonEliminar = false;
            showInfo("Aviso", "Se creó de forma satisfactoria la guía de entrada. El código de la guía es :" + this.editItem.getId());

            // Se procede con la limpieza del formulario por requerimiento.
            this.editItem = new GuiaEntrada();
            this.productos = new ArrayList<EntradaProductoCantidad>();
            this.editItem.setProveedor(new FichaAuxiliar());

            // Se setea la nueva fecha al formulario limpio
            this.editItem.setFecha(new Date());

            return;
        }

        // 4 y 5) Validar si la guía es de concepto compra con factura.
        if(ConceptoEntrada.DEVOLUCION_PRODUCTO_A_OT_CC.equals(this.editItem.getConceptoEntradaId())){

            // Validar orden de trabajo.
            if(this.editItem.getOrdenDeTrabajoId() != null){
                OrdenDeTrabajo ot = ordenDeTrabajoDAO.getById(this.editItem.getOrdenDeTrabajoId());

                if(ot == null){
                    showError("Error", "La orden de trabajo ingresada no existe. La guia de entrada no se puede guardar.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }
            }

        }

        if(this.editItem.getId() == null || this.editItem.getId() == 0){

            try{
                // Eliminando proveedor object //
                this.editItem.setProveedor(null);
                this.editItem.setId(null);

                GuiaEntrada guiaEntradaCreada = guiaEntradaDAO.create(this.editItem);

                // encolando en JMS colaEntradas.
                register.record(guiaEntradaCreada);

                this.editItem = guiaEntradaCreada;

                // Asociando los productos a la guia de entrada.
                for (EntradaProductoCantidad producto : productos) {
                    // Se inicia llenado de orden de compra.
                    if(guiaEntradaCreada.getOrdenDeCompraId() != null){
                        OrdenDeCompra ordenDeCompra = ordenDeCompraDAO.findById(guiaEntradaCreada.getOrdenDeCompraId());
                        List<ProductoOrdenCompra> productosFaltantesParaRecepcionCompleta = new ArrayList<ProductoOrdenCompra>();

                        for (ProductoOrdenCompra productoOrdenCompra : ordenDeCompra.getProductos()) {
                            if(producto.getFichaProductoId().equals(productoOrdenCompra.getProductoRequisicion().getFichaProductoId())){
                                productoOrdenCompra.setRecibido(productoOrdenCompra.getRecibido() + producto.getCantidad());

                                // Se calcula el saldo.
                                productoOrdenCompra.setSaldo(productoOrdenCompra.getProductoRequisicion().getCantidad() - productoOrdenCompra.getRecibido());

                                productoOrdenCompraDAO.update(productoOrdenCompra);
                            }

                            if(productoOrdenCompra.getSaldo() != 0){
                                productosFaltantesParaRecepcionCompleta.add(productoOrdenCompra);
                            }
                        }

                        if(productosFaltantesParaRecepcionCompleta.size() > 0){
                            ordenDeCompra.setEtapaOrdenCompraId(EtapaOrdenCompra.PARCIAL);
                            ordenDeCompra.setEtapaOrdenCompra(null);

                            ordenDeCompraDAO.update(ordenDeCompra);
                        }

                        if(productosFaltantesParaRecepcionCompleta.size() == 0){
                            ordenDeCompra.setEtapaOrdenCompraId(EtapaOrdenCompra.RECEPCION_COMPLETA);
                            ordenDeCompra.setEtapaOrdenCompra(null);

                            ordenDeCompraDAO.update(ordenDeCompra);
                        }
                    }

                    producto.setGuiaEntradaId(guiaEntradaCreada.getId());

                    // Dejando solo el ID de la ficha producto.
                    FichaProducto ficha = producto.getFichaProducto();
                    producto.setFichaProducto(null);

                    // Asignandole cantidad utilizada 0 al producto por defecto.
                    producto.setCantidadUtilizada(0D);

                    // Se guarda el producto asociado a la guia de entrada.
                    EntradaProductoCantidad productoAsociadoAGuia = entradaProductoCantidadDAO.create(producto);
                    producto.setFichaProducto(ficha);

                    // Agregando el stock a la bodega.
                    BodegaStockProducto bodegaStock = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), guiaEntradaCreada.getBodegaId());
                    if(bodegaStock == null){
                        BodegaStockProducto nuevoRegistro = new BodegaStockProducto();
                        nuevoRegistro.setFichaProductoId(producto.getFichaProductoId());
                        nuevoRegistro.setBodegaId(guiaEntradaCreada.getBodegaId());
                        nuevoRegistro.setCantidad(producto.getCantidad());

                        // Se guarda el nuevo registro de stock de bodega.
                        bodegaStockProductoDAO.create(nuevoRegistro);
                    } else {
                        bodegaStock.setCantidad(bodegaStock.getCantidad() + producto.getCantidad());

                        // Guardando el stock a la bodega.
                        bodegaStockProductoDAO.update(bodegaStock);
                    }

                }

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                // Mostrar botón borrar.
                mostrarBotonEliminar = false;
                showInfo("Aviso", "Se creó de forma satisfactoria la guía de entrada. El código de la guía es :" + this.editItem.getId());

                // Se procede con la limpieza del formulario por requerimiento.
                this.editItem = new GuiaEntrada();
                this.productos = new ArrayList<EntradaProductoCantidad>();
                this.editItem.setProveedor(new FichaAuxiliar());

                // Se setea la nueva fecha al formulario limpio
                this.editItem.setFecha(new Date());

            } catch (Exception e){
                showError("Error", "Hubo un error al guardar la guía de entrada.");
            }

        } else {
            guiaEntradaDAO.update(this.editItem);

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "Se editó de manera correcta la guia de entrada " + this.editItem.getId() + "");
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

        if(this.editItem.getConceptoEntradaId() != null){
            if(this.editItem.getConceptoEntradaId() == 0){
                this.editItem.setConceptoEntradaId(null);
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

        if(this.editItem.getBodegaOrigenId() != null){
            if(this.editItem.getBodegaOrigenId() == 0){
                this.editItem.setBodegaOrigenId(null);
            }
        }

    }

    public void eliminar(){
        for (EntradaProductoCantidad producto : productos) {
            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), producto.getGuiaEntrada().getBodegaId());

            if((bodegaStockProducto.getCantidad() - producto.getCantidad()) < 0){
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");

                showError("Error", "No se pudo eliminar la guía de entrada, debido a que no hay stock en bodega.");
                return;
            } else {
                // Se procede a descontar el stock y guardar
                bodegaStockProducto.setCantidad(bodegaStockProducto.getCantidad() - producto.getCantidad());
                bodegaStockProductoDAO.update(bodegaStockProducto);

                // Finalmente se procede a la eliminación del producto asociado.
                entradaProductoCantidadDAO.delete(producto);
            }

        }

        guiaEntradaDAO.delete(this.editItem.getId());

        // Borrando el archivo borrado de la vista.
        this.editItem = new GuiaEntrada();
        this.editItem.setProveedor(new FichaAuxiliar());

        // Eliminando los productos
        productos = new ArrayList<EntradaProductoCantidad>();

        // Borrando el botón eliminar.
        mostrarBotonEliminar = false;

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
        showInfo("Aviso", "Se eliminó correctamente la guia de entrada.");
    }

    public void limpiar(){
        this.editItem = new GuiaEntrada();
        this.editItem.setProveedor(new FichaAuxiliar());

        // Colocando la fecha actual por defecto.
        this.editItem.setFecha(new Date());

        productos = new ArrayList<EntradaProductoCantidad>();

        // No se puede guardar.
        sePuedeGuardar = false;

        mostrarBotonEliminar = false;
    }

    public void buscarPorRutProveedor(){
        FichaAuxiliar ficha = fichaAuxiliarDAO.findByRut(this.editItem.getProveedor().getRut().replace(".", ""));

        if(ficha == null){
            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "No existe proveedor asociado al rut " + this.editItem.getProveedor().getRut());
        } else {
            if(ficha.getTipos().contains(tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.PROVEEDOR))){
                this.editItem.getProveedor().setNombreRazonSocial(ficha.getNombreRazonSocial());
                this.editItem.setProveedorId(ficha.getId());
            } else {
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                showInfo("Aviso", "El rut buscado está guardado, pero no es de tipo proveedor");
            }
        }
    }

    public void buscarProductosPorOrdenDeCompra(){
        OrdenDeCompra ordenDeCompra = ordenDeCompraDAO.findById(editItem.getOrdenDeCompraId());
        productos = new ArrayList<EntradaProductoCantidad>();

        if(ordenDeCompra != null){
            if(ordenDeCompra.getProductos() != null){
                for (ProductoOrdenCompra productoOrdenCompra : ordenDeCompra.getProductos()) {
                    EntradaProductoCantidad entrada = new EntradaProductoCantidad();
                    entrada.setFichaProducto(productoOrdenCompra.getProductoRequisicion().getFichaProducto());
                    entrada.setFichaProductoId(productoOrdenCompra.getProductoRequisicion().getFichaProducto().getId());
                    entrada.setCantidad(productoOrdenCompra.getProductoRequisicion().getCantidad());
                    entrada.setPrecioUnitario(productoOrdenCompra.getPrecioUnitario());
                    entrada.setPrecioTotal(entrada.getCantidad() * entrada.getPrecioUnitario());
                    entrada.setCantidadUtilizada(0D);

                    productos.add(entrada);
                }
            }
        } else {
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "No existe la orden de compra buscada. Favor intente con otro número de orden de compra.");
        }
    }

    public void cambioConceptoEntrada(final AjaxBehaviorEvent event){
        renderByConcepto(this.editItem.getConceptoEntradaId());

        this.productos = new ArrayList<EntradaProductoCantidad>();
    }

    public void seleccionarGuiaEntrada(GuiaEntrada guiaEntrada){
        editItem = guiaEntrada;

        if(this.editItem.getProveedor() == null){
            this.editItem.setProveedor(new FichaAuxiliar());
        }

        // Se crea la lista de subgrupos para renderiar en la vista.
        productos = guiaEntrada.getProductos();

        renderByConcepto(guiaEntrada.getConceptoEntradaId());

        // Mostrar el botón eliminar.
        mostrarBotonEliminar = true;

        // Se puede guardar.
        sePuedeGuardar = true;

        showInfo("Aviso", "Se cargó la guía de entrada " + this.editItem.getId() + " de forma correcta.");
    }

    private void renderByConcepto(Long conceptoEntradaId) {
        if(ConceptoEntrada.COMPRA_CON_GUIA_DESPACHO.equals(conceptoEntradaId)){
            showFragmentVariable = true;
            showRut = true;
            showNombre = true;
            showNumeroGuiaDespacho = true;
            showFechaGuiaDespacho = true;
            showNumeroFactura = true;
            showFechaFactura = true;
            showOrdenCompra = true;
            showOrdenTrabajo = true;
            showCentroCosto = true;
            showCodigoBodegaOrigen = false;

            /**
             * Se bloquean inputs para poder agregar y editar productos asociados a la entrada.
             * Éste proceso se realiza debido a que la orden de compra contiene
             * los productos asociados a la guía de entrada, por ende no se deben
             * poder agregar más productos, solo editar cantidades asociadas.
             */
            showAgregarEditarProducto = false;
        }

        if(ConceptoEntrada.COMPRA_CON_FACTURA.equals(conceptoEntradaId)){
            showFragmentVariable = true;
            showRut = true;
            showNombre = true;
            showNumeroGuiaDespacho = false;
            showFechaGuiaDespacho = false;
            showNumeroFactura = true;
            showFechaFactura = true;
            showOrdenCompra = true;
            showOrdenTrabajo = true;
            showCentroCosto = true;
            showCodigoBodegaOrigen = false;

            /**
             * Se bloquean inputs para poder agregar y editar productos asociados a la entrada.
             * Éste proceso se realiza debido a que la orden de compra contiene
             * los productos asociados a la guía de entrada, por ende no se deben
             * poder agregar más productos, solo editar cantidades asociadas.
             */
            showAgregarEditarProducto = false;
        }

        if(ConceptoEntrada.DEVOLUCION_PRODUCTO_A_OT_CC.equals(conceptoEntradaId)){
            showFragmentVariable = true;
            showRut = false;
            showNombre = false;
            showNumeroGuiaDespacho = false;
            showFechaGuiaDespacho = false;
            showNumeroFactura = false;
            showFechaFactura = false;
            showOrdenCompra = false;
            showCodigoBodegaOrigen = false;
            showOrdenTrabajo = true;
            showCentroCosto = true;

            /**
             * Se bloquean inputs para poder agregar y editar productos asociados a la entrada.
             * Éste proceso se realiza debido a que la orden de compra contiene
             * los productos asociados a la guía de entrada, por ende no se deben
             * poder agregar más productos, solo editar cantidades asociadas.
             */
            showAgregarEditarProducto = true;
        }

        if(ConceptoEntrada.TRANSFERENCIA_ENTRE_BODEGA.equals(conceptoEntradaId)){
            showFragmentVariable = true;
            showRut = false;
            showNombre = false;
            showNumeroGuiaDespacho = false;
            showFechaGuiaDespacho = false;
            showNumeroFactura = false;
            showFechaFactura = false;
            showOrdenCompra = false;
            showCodigoBodegaOrigen = true;
            showOrdenTrabajo = false;
            showCentroCosto = false;

            /**
             * Se bloquean inputs para poder agregar y editar productos asociados a la entrada.
             * Éste proceso se realiza debido a que la orden de compra contiene
             * los productos asociados a la guía de entrada, por ende no se deben
             * poder agregar más productos, solo editar cantidades asociadas.
             */
            showAgregarEditarProducto = true;
        }
    }

    public List<EntradaProductoCantidad> getProductos() {
        return productos;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void buscarPorCodigoProducto(EntradaProductoCantidad productoCantidad){
        FichaProducto fichaEncontrada = fichaProductoDAO.findByCodigoProducto(productoCantidad.getFichaProducto().getCodigoProducto());

        if(fichaEncontrada != null){
            productoCantidad.setFichaProducto(fichaEncontrada);
        }
    }

    public void agregarProducto(FichaProducto fichaProducto){
        EntradaProductoCantidad nuevoProducto = new EntradaProductoCantidad();
        nuevoProducto.setFichaProductoId(fichaProducto.getId());
        nuevoProducto.setFichaProducto(fichaProducto);

        nuevoProducto.setCantidad(0D);
        nuevoProducto.setPrecioUnitario(0D);
        nuevoProducto.setPrecioTotal(0D);

        productos.add(nuevoProducto);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void calcularMontoTotal(EntradaProductoCantidad entradaProductoCantidad){
        if(entradaProductoCantidad.getPrecioUnitario() != null && entradaProductoCantidad.getPrecioUnitario() != 0 && entradaProductoCantidad.getCantidad() != null && entradaProductoCantidad.getCantidad() > 0){
            entradaProductoCantidad.setPrecioTotal(entradaProductoCantidad.getPrecioUnitario() * entradaProductoCantidad.getCantidad());
        }
    }

    public void setProductos(List<EntradaProductoCantidad> productos) {
        this.productos = productos;
    }

    public boolean isShowFragmentVariable() {
        return showFragmentVariable;
    }

    public boolean isShowRut() {
        return showRut;
    }

    public boolean isShowNombre() {
        return showNombre;
    }

    public boolean isShowNumeroFactura() {
        return showNumeroFactura;
    }

    public boolean isShowNumeroGuiaDespacho() {
        return showNumeroGuiaDespacho;
    }

    public boolean isShowFechaFactura() {
        return showFechaFactura;
    }

    public boolean isShowFechaGuiaDespacho() {
        return showFechaGuiaDespacho;
    }

    public boolean isShowOrdenCompra() {
        return showOrdenCompra;
    }

    public boolean isShowOrdenTrabajo() {
        return showOrdenTrabajo;
    }

    public boolean isShowCentroCosto() {
        return showCentroCosto;
    }

    public boolean isShowCodigoBodegaOrigen() {
        return showCodigoBodegaOrigen;
    }

    public GuiasEntradaLazyDataModel getGuiaEntradas() {
        if ( guiaEntradas == null )
            guiaEntradas = new GuiasEntradaLazyDataModel(guiaEntradaDAO);
        return guiaEntradas;
    }

    public void setGuiaEntradas(GuiasEntradaLazyDataModel guiaEntradas) {
        this.guiaEntradas = guiaEntradas;
    }

    public boolean isMostrarBotonEliminar() {
        return mostrarBotonEliminar;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public boolean isShowAgregarEditarProducto() {
        return showAgregarEditarProducto;
    }
}
