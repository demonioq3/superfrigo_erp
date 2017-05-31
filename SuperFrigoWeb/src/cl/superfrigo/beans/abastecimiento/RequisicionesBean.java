package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.auth.AuthenticationBean;
import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.abastecimiento.*;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.RequisicionesLazyDataModel;
import org.hibernate.annotations.SourceType;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean(name = "requisicionesBean", eager = true)
@ViewScoped
public class RequisicionesBean extends BaseBean implements Serializable {

    /* Requisición */
    private Requisicion editItem;

    /* Lista lazy de requisiciones creadas (búsqueda) */
    private RequisicionesLazyDataModel requisiciones;

    /* Lista de productos asociados a la requisicion */
    private List<ProductoRequisicion> productos;

    /* Lista lazy de fichas producto creadas (búsqueda) */
    private FichasProductoLazyDataModel fichasProducto;

    @ManagedProperty(value = "#{authBean}")
    private AuthenticationBean authenticationBean;

    @EJB private AreaDAO areaDAO;
    @EJB private CentroDeCostoDAO centroDeCostoDAO;
    @EJB private EstadoRequisicionDAO estadoRequisicionDAO;
    @EJB private RequisicionDAO requisicionDAO;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private ProductoRequisicionDAO productoRequisicionDAO;

    @PostConstruct
    public void init(){
        editItem = new Requisicion();

        // Obteniendo el nombre de solicitante (usuario autenticado) en la nueva requisición.
        editItem.setSolicitante(authenticationBean.getNombreFormatted());

        // Asignando el estado por defecto de una requisición inicial.
        editItem.setEstadoRequisicion(estadoRequisicionDAO.getById(EstadoRequisicion.PENDIENTE));
        editItem.setEstadoRequisicionId(EstadoRequisicion.PENDIENTE);

        // Asignando fecha de emisión actual del servidor.
        editItem.setFechaEmision(new Date());

        // Se inicializa la lista de productos.
        productos = new ArrayList<ProductoRequisicion>();
    }

    public void buscarPorRequisicion(){
        Requisicion reqBuscada = requisicionDAO.getById(editItem.getId());

        if(reqBuscada != null){
            this.editItem = reqBuscada;

            // Se crea la lista de subgrupos para renderiar en la vista.
            productos = reqBuscada.getProductos();

            showInfo("Aviso", "Se cargó la guía de salida " + this.editItem.getId() + " de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            Long codigoBuscado = this.editItem.getId();

             /* Inicialización de objetos */
            init();

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "La requisición " + codigoBuscado +" no se encuentra registrada.");
        }
    }

    public void seleccionarRequisicion(Requisicion requisicion){
        editItem = requisicion;

        // Se crea la lista de subgrupos para renderiar en la vista.
        productos = requisicion.getProductos();

        // Se puede guardar.
        //sePuedeGuardar = true;

        showInfo("Aviso", "Se cargó la requisicion " + this.editItem.getId() + " de forma correcta.");
    }

    public void agregarProducto(FichaProducto fichaProducto){
        ProductoRequisicion nuevoProducto = new ProductoRequisicion();
        nuevoProducto.setFichaProductoId(fichaProducto.getId());
        nuevoProducto.setFichaProducto(fichaProducto);

        nuevoProducto.setCantidad(1D);

        productos.add(nuevoProducto);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void limpiar(){
        init();
    }

    public List<Area> getAreas(){
        return areaDAO.findAll();
    }

    public void guardar(){
        cambiarZerosANull();

        // Nueva requisición
        if(this.editItem.getId() == null){
            try {
                List<ProductoRequisicion> productoRequisicion = this.productos;

                editItem.setProductos(null);
                Requisicion nuevaReq = requisicionDAO.create(editItem);

                // Asignando los productos a la requisición.
                if(productoRequisicion != null){
                    for (ProductoRequisicion producto : productoRequisicion) {
                        if(producto.getCantidad() < 1){
                            throw new IllegalArgumentException("Cantidad no puede ser menor a 1");
                        }

                        producto.setRequisicionId(nuevaReq.getId());

                        // Se guarda la asociación de productos a la requisición.
                        productoRequisicionDAO.create(producto);
                    }
                }

                if(nuevaReq != null){
                    showInfo("Aviso", "Se creó de forma satisfactoria la requisición. El código de la requisición es : " + nuevaReq.getId());
                }

                // Limpiando la vista.
                init();

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
            } catch (EJBException e) {
                if(e.getLocalizedMessage().contains("ConstraintViolationException")){
                    super.showError("Error", "La orden de trabajo ingresada no existe.");
                } else {
                    super.showError("Error", "Hubo un error al crear la requisición. Consulte con su administrador.");
                }

                // Move to bottom
                RequestContext.getCurrentInstance().scrollTo("main-content");
            }

        } else {
            // Requisición a editar.
            try {
                // Antes de guardar la edición de la requisición, se verifica si la requisición ya está aprobada, para que no se pueda realizar la edición.
                if(this.editItem.getEstadoRequisicionId().equals(EstadoRequisicion.APROBADA)){
                    showError("Error", "La requisición ya está aprobada, por lo que no se puede editar.");

                    // Move to bottom
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }

                // Se proceden a eliminar todos los productos asociados.
                Requisicion oldReq = requisicionDAO.getById(editItem.getId());
                List<ProductoRequisicion> productosRequisicion = oldReq.getProductos();

                // Elimino cada uno de los productos si la lista es mayor a 0 y distinto de nulo.
                if(productosRequisicion != null && productosRequisicion.size() > 0){
                    for (ProductoRequisicion productoRequisicion : productosRequisicion) {
                        if(productoRequisicionDAO.delete(productoRequisicion)) {
                            System.out.println("Se eliminó el producto "+ productoRequisicion.getId());
                        }
                    }
                }

                // Guardo los productos agregados.
                for (ProductoRequisicion productoRequisicion : editItem.getProductos()) {
                    productoRequisicion.setId(null);
                    productoRequisicion.setRequisicion(null);
                    productoRequisicion.setRequisicionId(editItem.getId());

                    productoRequisicionDAO.create(productoRequisicion);
                }

                // Se procede a guardar la requisicion .
                editItem.setProductos(null);
                requisicionDAO.update(this.editItem);

                super.showInfo("Aviso", "Se editó de manera correcta la requisición " + editItem.getId() + ".");

                // Limpiando la vista.
                init();

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
            } catch (EJBException ejb){
                super.showError("Error", "Hubo un error al editar la requisicion.");
            }

        }
    }

    private void cambiarZerosANull() {
        if(this.editItem.getId() == 0){
            this.editItem.setId(null);
        }

        if(this.editItem.getCentroDeCostoId() == 0){
            this.editItem.setCentroDeCostoId(null);
        }

        if(this.editItem.getOrdenDeTrabajoId() == 0){
            this.editItem.setOrdenDeTrabajoId(null);
        }
    }

    public List<EstadoRequisicion> getAllEstadosRequisicion(){
        return estadoRequisicionDAO.findAll();
    }

    public void borrarProducto(ProductoRequisicion productoRequisicion){
        editItem.getProductos().remove(productoRequisicion);
    }

    public List<Requisicion> getRequisicionesPendientes(){
        return requisicionDAO.findRequisicionesPendientes();
    }

    public List<CentroDeCosto> getCentrosDeCosto(){
        return centroDeCostoDAO.findAll();
    }

    public String generarOrdenDeCompra(Requisicion requisicion){
        return "OrdenDeCompra.xhtml?faces-redirect=true&amp;id_requisicion=" + requisicion.getId();
    }

    public Requisicion getEditItem() {
        return editItem;
    }

    public void setEditItem(Requisicion editItem) {
        this.editItem = editItem;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public RequisicionesLazyDataModel getRequisiciones() {
        if ( requisiciones == null )
            requisiciones = new RequisicionesLazyDataModel(requisicionDAO);
        return requisiciones;
    }

    public void setRequisiciones(RequisicionesLazyDataModel requisiciones) {
        this.requisiciones = requisiciones;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public List<ProductoRequisicion> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoRequisicion> productos) {
        this.productos = productos;
    }
}
