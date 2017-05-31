package cl.superfrigo.beans.solicitud_material;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.EtapaOrdenCompra;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.solicitud_material.ProductoSolicitudMaterial;
import cl.superfrigo.entity.solicitud_material.SolicitudMaterial;
import cl.superfrigo.jms.ManagerRegistroEntrada;
import cl.superfrigo.jms.ManagerRegistroSalida;
import cl.superfrigo.jms.ManagerRegistroTransferencias;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.GuiasEntradaLazyDataModel;
import cl.superfrigo.model.SolicitudMaterialLazyDataModel;
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
public class SolicitudMaterialBean extends BaseBean implements Serializable {

    /* Guia entrada actual */
    private SolicitudMaterial editItem;

    /* Todas las guias de entradas lazy */
    private SolicitudMaterialLazyDataModel solicitudes;

    /* Lista de productos asociados a una guia de entrada */
    private List<ProductoSolicitudMaterial> productos;

    /* Rendered de inputs */
    private boolean showFragmentVariable = false;
    private boolean mostrarBotonEliminar = false;


    /* Boolean para saber si es edición  */
    private boolean sePuedeGuardar = false;

    private FichasProductoLazyDataModel fichasProducto;

    @EJB
    private SolicitudMaterialDAO solicitudMaterialDAO;

    @EJB
    private ProductoSolicitudMaterialDAO productoSolicitudMaterialDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    private final static Logger logger = LoggerFactory.getLogger(SolicitudMaterialBean.class);


    @PostConstruct
    public void init() {
        editItem = new SolicitudMaterial();

        /* Inicialización de objetos */
        productos = new ArrayList<ProductoSolicitudMaterial>();

        // Colocando fecha actual a la guia de entrada.
        editItem.setFecha(new Date());
    }

    public SolicitudMaterial getEditItem() {
        return editItem;
    }

    public void setEditItem(SolicitudMaterial editItem) {
        this.editItem = editItem;
    }

    public void buscarPorSolicitud() {
        SolicitudMaterial solicitudBuscada = solicitudMaterialDAO.findById(editItem.getId());

        if(solicitudBuscada != null){
            this.editItem = solicitudBuscada;

            // Se crea la lista de subgrupos para renderiar en la vista.
            productos = solicitudBuscada.getProductosAsociados();

            // Mostrar el botón eliminar.
            mostrarBotonEliminar = true;

            // Se puede guardar la edición.
            sePuedeGuardar = true;

            showInfo("Aviso", "Se cargó la solicitud " + this.editItem.getId() + " de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            Long codigoBuscado = this.editItem.getId();

             /* Inicialización de objetos */
            this.editItem = new SolicitudMaterial();
            productos = new ArrayList<ProductoSolicitudMaterial>();

            // Colocando fecha actual a la guia de entrada.
            editItem.setFecha(new Date());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "La solicitud " + codigoBuscado +" no se encuentra registrada.");
        }
    }

    public void guardar(){

        // Validar si existe
        if(!sePuedeGuardar){
            // Validacion: Validando que el id de guia no exista anteriormente.
            SolicitudMaterial solicitudExistente = solicitudMaterialDAO.findById(this.editItem.getId());
            if(solicitudExistente != null){
                showError("Error", "La solicitud ya está registrada.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        if(this.editItem.getId() == null || this.editItem.getId() == 0){

            try{
                // Eliminando proveedor object //
                this.editItem.setId(null);

                SolicitudMaterial solicitudCreada = solicitudMaterialDAO.create(this.editItem);

                this.editItem = solicitudCreada;

                // Asociando los productos a la guia de entrada.
                for (ProductoSolicitudMaterial producto : productos) {
                    producto.setSolicitudMaterialId(solicitudCreada.getId());

                    productoSolicitudMaterialDAO.create(producto);
                }

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                // Mostrar botón borrar.
                mostrarBotonEliminar = false;
                showInfo("Aviso", "Se creó de forma satisfactoria la solicitud. El código de la guía es :" + this.editItem.getId());

                // Se procede con la limpieza del formulario por requerimiento.
                this.editItem = new SolicitudMaterial();
                this.productos = new ArrayList<ProductoSolicitudMaterial>();

                // Se setea la nueva fecha al formulario limpio
                this.editItem.setFecha(new Date());

            } catch (Exception e){
                showError("Error", "Hubo un error al guardar la solicitud.");
            }

        } else {
            solicitudMaterialDAO.update(this.editItem);

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "Se editó de manera correcta la solicitud " + this.editItem.getId() + "");
        }
    }

    public void eliminar(){


        solicitudMaterialDAO.delete(this.editItem);

        // Borrando el archivo borrado de la vista.
        this.editItem = new SolicitudMaterial();

        // Eliminando los productos
        productos = new ArrayList<ProductoSolicitudMaterial>();

        // Borrando el botón eliminar.
        mostrarBotonEliminar = false;

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
        showInfo("Aviso", "Se eliminó correctamente la solicitud.");
    }

    public void limpiar(){
        this.editItem = new SolicitudMaterial();

        // Colocando la fecha actual por defecto.
        this.editItem.setFecha(new Date());

        productos = new ArrayList<ProductoSolicitudMaterial>();

        // No se puede guardar.
        sePuedeGuardar = false;

        mostrarBotonEliminar = false;
    }

    public void seleccionarSolicitud(SolicitudMaterial solicitudMaterial){
        editItem = solicitudMaterial;

        // Se crea la lista de subgrupos para renderiar en la vista.
        productos = solicitudMaterial.getProductosAsociados();

        // Mostrar el botón eliminar.
        mostrarBotonEliminar = true;

        // Se puede guardar.
        sePuedeGuardar = true;

        showInfo("Aviso", "Se cargó la solicitud " + this.editItem.getId() + " de forma correcta.");
    }



    public List<ProductoSolicitudMaterial> getProductos() {
        return productos;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void buscarPorCodigoProducto(ProductoSolicitudMaterial producto){
        FichaProducto fichaEncontrada = fichaProductoDAO.findByCodigoProducto(producto.getProducto().getCodigoProducto());

        if(fichaEncontrada != null){
            producto.setProducto(fichaEncontrada);
        }
    }

    public void agregarProducto(FichaProducto fichaProducto){
        ProductoSolicitudMaterial nuevoProducto = new ProductoSolicitudMaterial();
        nuevoProducto.setProductoId(fichaProducto.getId());
        nuevoProducto.setProducto(fichaProducto);

        nuevoProducto.setCantidad(0D);

        productos.add(nuevoProducto);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void calcularMontoTotal(EntradaProductoCantidad entradaProductoCantidad){
        if(entradaProductoCantidad.getPrecioUnitario() != null && entradaProductoCantidad.getPrecioUnitario() != 0 && entradaProductoCantidad.getCantidad() != null && entradaProductoCantidad.getCantidad() > 0){
            entradaProductoCantidad.setPrecioTotal(entradaProductoCantidad.getPrecioUnitario() * entradaProductoCantidad.getCantidad());
        }
    }

    public void setProductos(List<ProductoSolicitudMaterial> productos) {
        this.productos = productos;
    }

    public boolean isShowFragmentVariable() {
        return showFragmentVariable;
    }

    public SolicitudMaterialLazyDataModel getSolicitudes() {
        if ( solicitudes == null )
            solicitudes = new SolicitudMaterialLazyDataModel(solicitudMaterialDAO);
        return solicitudes;
    }

    public void setSolicitudes(SolicitudMaterialLazyDataModel solicitudes) {
        this.solicitudes = solicitudes;
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
}
