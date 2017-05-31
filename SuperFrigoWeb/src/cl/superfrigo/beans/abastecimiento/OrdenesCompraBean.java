package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaJasperObject;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaObject;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.*;
import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.SalidaProductoCantidad;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.OrdenesDeCompraLazyDataModel;
import cl.superfrigo.model.ProveedoresLazyDataModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean(name = "ordenesCompraBean")
@ViewScoped
public class OrdenesCompraBean extends BaseBean implements Serializable {

    // Orden de compra actual.
    private OrdenDeCompra editItem;

    /* Lista lazy de ordenes de compra creadas (búsqueda) */
    private OrdenesDeCompraLazyDataModel ordenes;

    /* Lista lazy de proveedores (búsqueda) */
    private ProveedoresLazyDataModel proveedores;

    /* Lista de productos asociados a la orden de compra */
    private List<ProductoOrdenCompra> productos;

    /* Lista lazy de fichas producto creadas (búsqueda) */
    private FichasProductoLazyDataModel fichasProducto;

    // Boolean para bloquear inputs si es que proviene de requisición.
    private Boolean bloquearDatosCargados = false;

    // Boolean parar mostrar botón g|uardar.
    private Boolean mostrarBotonGuardar;

    @EJB private CondicionPagoDAO condicionPagoDAO;
    @EJB private EstadoOrdenCompraDAO estadoOrdenCompraDAO;
    @EJB private EtapaOrdenCompraDAO etapaOrdenCompraDAO;
    @EJB private OrdenDeCompraDAO ordenDeCompraDAO;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private ProductoOrdenCompraDAO productoOrdenCompraDAO;
    @EJB private RequisicionDAO requisicionDAO;
    @EJB private PrecioProductoDAO precioProductoDAO;
    @EJB private FichaAuxiliarDAO fichaAuxiliarDAO;

    @PostConstruct
    public void init(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        mostrarBotonGuardar = false;

        // Se verifica si viene por get el ID de la orden de compra
        try {
            editItem = ordenDeCompraDAO.findById(Long.parseLong((String) req.getParameter("id_orden")));
            return;
        } catch (Exception e){
            System.out.println("Hubo un error al cargar la orden de compra por get. Favor consultar a su administrador");
        }

        editItem = new OrdenDeCompra();
        editItem.setFecha(new Date());
        editItem.setProveedor(new FichaAuxiliar());

        // Se define estado actual como Pendiente.
        editItem.setEstadoOrdenCompraId(EstadoOrdenCompra.PENDIENTE);

        // Se define la etapa actual.
        editItem.setEtapaOrdenCompra(etapaOrdenCompraDAO.getById(EtapaOrdenCompra.SIN_RECEPCION));
        editItem.setEtapaOrdenCompraId(EtapaOrdenCompra.SIN_RECEPCION);

        // Se verifica si viene por get el ID de requisición.
        try {
            // Se edita id a null por problema con render.
            editItem.setId(null);

            Requisicion requisicionRequest = requisicionDAO.getById(Long.parseLong((String) req.getParameter("id_requisicion")));

            // Se asignan los datos de la requisición obtenida, si proviene de una requisición.
            editItem.setRequisicion(requisicionRequest);
            editItem.setRequisicionId(requisicionRequest.getId());

            if(requisicionRequest.getCentroDeCosto() != null){
                editItem.setCentroDeCosto(requisicionRequest.getCentroDeCosto());
                editItem.setCentroDeCostoId(requisicionRequest.getCentroDeCostoId());
            }

            if(requisicionRequest.getOrdenDeTrabajo() != null){
                editItem.setOrdenDeTrabajo(requisicionRequest.getOrdenDeTrabajo());
                editItem.setOrdenDeTrabajoId(requisicionRequest.getOrdenDeTrabajoId());
            }

            if(requisicionRequest.getFechaRequerida() != null){
                editItem.setEntregaFinal(requisicionRequest.getFechaEmision());
            }

            // Se bloquean los datos.
            bloquearDatosCargados = true;

            editItem.setProductos(new ArrayList<ProductoOrdenCompra>());

            for (ProductoRequisicion productoRequisicion : editItem.getRequisicion().getProductos()) {
                ProductoOrdenCompra producto = new ProductoOrdenCompra();

                // Se le asigna el producto de la requisición obtenida a un nuevo producto de orden de compra.
                producto.setProductoRequisicion(productoRequisicion);
                producto.setProductoRequisicionId(productoRequisicion.getId());

                // Datos por defecto.
                producto.setRecibido(0D);
                producto.setSaldo(0D);

                editItem.getProductos().add(producto);

                // Se muestra botón guardar.
                mostrarBotonGuardar = true;

                super.addMessage("Aviso", "Se cargaron los datos de la requisición correctamente.");
                RequestContext.getCurrentInstance().update("msgs");
            }

        } catch (NumberFormatException e){
            System.out.println(e);
        }
    }

    public void buscarPorOrden(){
        OrdenDeCompra ordenDeCompra = ordenDeCompraDAO.getById(editItem.getId());

        if(ordenDeCompra != null){
            this.editItem = ordenDeCompra;

            // Se crea la lista de subgrupos para renderiar en la vista.
            productos = ordenDeCompra.getProductos();

            showInfo("Aviso", "Se cargó la orden de compra " + this.editItem.getId() + " de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            Long codigoBuscado = this.editItem.getId();

             /* Inicialización de objetos */
            this.editItem = new OrdenDeCompra();
            productos = new ArrayList<ProductoOrdenCompra>();
            this.editItem.setCentroDeCosto(new CentroDeCosto());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "La orden de compra " + codigoBuscado +" no se encuentra registrada.");
        }
    }

    public void seleccionarOrdenDeCompra(OrdenDeCompra ordenDeCompra){
        editItem = ordenDeCompra;

        // Se crea la lista de subgrupos para renderiar en la vista.
        productos = ordenDeCompra.getProductos();

        // Se puede guardar.
        //sePuedeGuardar = true;

        mostrarBotonGuardar = true;

        showInfo("Aviso", "Se cargó la orden de compra " + this.editItem.getId() + " de forma correcta.");
    }

    public void seleccionarProveedor(FichaAuxiliar ficha){
        editItem.setProveedor(ficha);
        editItem.setProveedorId(ficha.getId());

        showInfo("Aviso", "Se agregó el proveedor a la orden de compra de forma correcta.");
    }

    public void guardar(){
        cambiarZerosANull();

        // Nueva orden de compra.
        if(this.editItem.getId() == null){
            try {
                if(this.editItem.getProveedor() == null){
                    showError("Error", "Debe ingresar un probeedor para guardar la orden de compra.");

                    // Move to bottom
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }


                // Se guardan los productos en el bean.
                productos = editItem.getProductos();
                editItem.setProductos(null);

                editItem.setEstadoOrdenCompraId(EstadoOrdenCompra.APROBADA);
                editItem.setEstadoOrdenCompra(null);

                OrdenDeCompra nuevaOrden = ordenDeCompraDAO.create(editItem);

                // Asignando los productos a la orden de compra.
                for (ProductoOrdenCompra producto : productos) {

                    producto.setOrdenDeCompraId(nuevaOrden.getId());

                    // Se guarda la asociación de productos a la orden de compra.
                    productoOrdenCompraDAO.create(producto);
                }

                if(nuevaOrden != null){
                    showInfo("Aviso", "Se creó de forma satisfactoria la orden de compra. El código de la orden de compra es : " + nuevaOrden.getId());
                }

                // Limpiando la vista.
                editItem = new OrdenDeCompra();
                productos = new ArrayList<ProductoOrdenCompra>();

                // Move to bottom
                RequestContext.getCurrentInstance().scrollTo("main-content");
            } catch (EJBException e) {
                // Move to bottom
                RequestContext.getCurrentInstance().scrollTo("main-content");
                super.showError("Error", "Hubo un error al crear la orden de compra. Consulte con su administrador.");
            }

        } else {
            // Orden de compra a editar.
            try {
                // Antes de guardar la edición de la orden de compra, se verifica si la orden de compra ya está aprobada, para que no se pueda realizar la edición.
                if(this.editItem.getEstadoOrdenCompraId().equals(EstadoOrdenCompra.APROBADA)){
                    showError("Error", "La orden de compra ya está aprobada, por lo que no se puede editar.");

                    // Move to bottom
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }

                editItem.setEstadoOrdenCompra(null);

                // Se procede a guardar la orden de compra .
                ordenDeCompraDAO.update(this.editItem);

                super.showInfo("Aviso", "Se editó de manera correcta la orden de compra " + editItem.getId() + ".");

                // Limpiando la vista.
                editItem = new OrdenDeCompra();
                productos = new ArrayList<ProductoOrdenCompra>();

                // Move to bottom
                RequestContext.getCurrentInstance().scrollTo("main-content");
            } catch (EJBException ejb){
                // Move to bottom
                RequestContext.getCurrentInstance().scrollTo("main-content");
                super.showError("Error", "Hubo un error al editar la orden de compra .");
            }

        }
    }

    private void cambiarZerosANull() {
        if(this.editItem.getId() == 0){
            this.editItem.setId(null);
        }

        if(this.editItem.getRequisicionId() != null && this.editItem.getRequisicionId() == 0){
            this.editItem.setRequisicionId(null);
        }

        if(this.editItem.getCondicionPagoId() != null && this.editItem.getCondicionPagoId() == 0){
            this.editItem.setCondicionPagoId(null);
        }

        if(this.editItem.getCentroDeCostoId() != null && this.editItem.getCentroDeCostoId() == 0){
            this.editItem.setCentroDeCostoId(null);
        }

        if(this.editItem.getOrdenDeTrabajoId() != null && this.editItem.getOrdenDeTrabajoId() == 0){
            this.editItem.setOrdenDeTrabajoId(null);
        }
    }

    public void limpiar(){
        this.editItem = new OrdenDeCompra();
        this.editItem.setId(null);

        cambiarZerosANull();

        // Colocando la fecha actual por defecto.
        this.editItem.setFecha(new Date());

        productos = new ArrayList<ProductoOrdenCompra>();
    }

    public void buscarPorRut(){
        FichaAuxiliar fichaAuxiliar = fichaAuxiliarDAO.findByRut(editItem.getProveedor().getRut());

        if(fichaAuxiliar == null){
            showInfo("Aviso", "El fichaAuxiliar RUT: " + editItem.getProveedor().getRut() + " no está registrado. Debes ingresar los datos del fichauxiliar manualmente.");
        } else {
            editItem.setProveedor(fichaAuxiliar);
            editItem.setProveedorId(fichaAuxiliar.getId());
        }

    }

    public List<EstadoOrdenCompra> getAllEstados(){
        return estadoOrdenCompraDAO.findAll();
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void calcularMontoTotal(ProductoOrdenCompra productoOrdenCompra){
        productoOrdenCompra.setTotal(productoOrdenCompra.getPrecioUnitario() * productoOrdenCompra.getProductoRequisicion().getCantidad());
    }

    public void imprimirOrdenDeCompra(){
        try {
            Locale locale = new Locale("es","CL");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> parametros = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            parametros.put("id", this.editItem.getId().toString());
            parametros.put("n_requisicion", this.editItem.getRequisicionId().toString());
            parametros.put("rut", (this.editItem.getProveedor() == null) ? "N/A" : this.editItem.getProveedor().getRut());
            parametros.put("nombre", (this.editItem.getProveedor() == null) ? "N/A" : this.editItem.getProveedor().getNombreRazonSocial());
            parametros.put("condicion_pago", (this.editItem.getCondicionPago() == null) ? "N/A" : this.editItem.getCondicionPago().getDescripcion());
            parametros.put("centro_costo", (this.editItem.getCentroDeCosto() == null) ? "N/A" : this.editItem.getCentroDeCosto().getNombre());
            parametros.put("fecha_oc", sdf.format(this.editItem.getFecha()));
            parametros.put("fecha_entrega", sdf.format(this.editItem.getEntregaFinal()));
            parametros.put("estado", this.editItem.getEstadoOrdenCompra().getDescripcion());
            parametros.put("etapa", this.editItem.getEtapaOrdenCompra().getDescripcion());
            parametros.put("ot", this.editItem.getOrdenDeTrabajoId().toString());
            parametros.put("observaciones", this.editItem.getObservaciones());



            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("jasper_orden_compra.jasper"));

            GuiaSalidaJasperObject cb = new GuiaSalidaJasperObject();
            List<GuiaSalidaObject> productosFormatted = new ArrayList<GuiaSalidaObject>();

            double total_neto = 0;
            if(this.productos != null && this.productos.size() > 0){
                for (ProductoOrdenCompra producto : this.productos) {
                    GuiaSalidaObject nuevo = new GuiaSalidaObject();
                    nuevo.setId(producto.getId().toString());
                    nuevo.setCodigo(producto.getProductoRequisicion().getFichaProducto().getCodigoProducto());
                    nuevo.setDescripcion(producto.getProductoRequisicion().getFichaProducto().getDescripcion());
                    nuevo.setCantidad("" + producto.getProductoRequisicion().getCantidad());

                    nuevo.setPunitario("$ " + String.format("%,.2f", producto.getPrecioUnitario()).replace(".00", ""));
                    nuevo.setTotal("$ " + String.format("%,.2f", producto.getTotal()).replace(".00", ""));
                    nuevo.setUmedida(producto.getProductoRequisicion().getFichaProducto().getUnidadMedida().getCodigo());

                    productosFormatted.add(nuevo);
                    total_neto += total_neto + producto.getTotal();
                }
            }

            parametros.put("total_neto", "$ " + String.format("%,.2f", total_neto).replace(".00", ""));
            double iva = total_neto * 0.19;
            parametros.put("iva", "$ " + String.format("%,.2f", iva).replace(".00", ""));
            double total = total_neto + iva;
            parametros.put("total", "$ " + String.format("%,.2f", total).replace(".00", ""));

            cb.setProductos(productosFormatted);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(cb), false);

            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, dataSource);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=orden_compra_impr.pdf\"");
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

    public void borrarProductoOrden(ProductoOrdenCompra productoOrdenCompra){
        productos.remove(productoOrdenCompra);
    }

    public List<CondicionPago> getCondicionesDePago(){
        return condicionPagoDAO.findAll();
    }

    public OrdenDeCompra getEditItem() {
        return editItem;
    }

    public void setEditItem(OrdenDeCompra editItem) {
        this.editItem = editItem;
    }

    public OrdenesDeCompraLazyDataModel getOrdenes() {
        if ( ordenes == null )
            ordenes = new OrdenesDeCompraLazyDataModel(ordenDeCompraDAO, requisicionDAO);
        return ordenes;
    }

    public void setOrdenes(OrdenesDeCompraLazyDataModel ordenes) {
        this.ordenes = ordenes;
    }

    public boolean isBloquearDatosCargados() {
        return bloquearDatosCargados;
    }

    public List<ProductoOrdenCompra> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenCompra> productos) {
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

    public ProveedoresLazyDataModel getProveedores() {
        if ( proveedores == null )
            proveedores = new ProveedoresLazyDataModel(fichaAuxiliarDAO, TipoFichaAuxiliar.PROVEEDOR);
        return proveedores;
    }

    public void setProveedores(ProveedoresLazyDataModel proveedores) {
        this.proveedores = proveedores;
    }

    public Boolean getMostrarBotonGuardar() {
        return mostrarBotonGuardar;
    }
}
