package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.model.FichasLazyDataModel;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.OrdenesDeCompraLazyDataModel;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean(name = "busquedasOrdenCompraBean")
@ViewScoped
public class BusquedasOrdenCompraBean extends BaseBean implements Serializable {

    // Búsqueda por proveedor.
    private FichaAuxiliar proveedor;

    // Búsqueda por ficha de producto;
    private FichaProducto fichaProducto;

    // Render de tabla de ordenes de compra.
    private Boolean mostrarTabla = false;

    // Todas las fichas auxiliares.
    private FichasLazyDataModel fichas;

    // Todas las fichas de producto.
    private FichasProductoLazyDataModel fichasProducto;

    // Ordenes de compra por proveedor.
    private OrdenesDeCompraLazyDataModel ordenes;

    // Ordenes de compra por producto.
    private List<ProductoOrdenCompra> productosOrden;


    @EJB private FichaAuxiliarDAO fichaAuxiliarDAO;
    @EJB private OrdenDeCompraDAO ordenDeCompraDAO;
    @EJB private RequisicionDAO requisicionDAO;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private ProductoOrdenCompraDAO productoOrdenCompraDAO;


    @PostConstruct
    public void init(){
        proveedor = new FichaAuxiliar();
        fichaProducto = new FichaProducto();
    }

    public void seleccionarFicha(FichaAuxiliar ficha){
        proveedor = ficha;

        ordenes = null;
        mostrarTabla = true;

        showInfo("Aviso", "Se cargaron las ordenes de compras asociadas a la ficha auxiliar rut \"" + ficha.getRut() + "\" de forma correcta.");
    }

    public void seleccionarProducto(FichaProducto fichaProducto){
        this.fichaProducto = fichaProducto;

        productosOrden = productoOrdenCompraDAO.findByCodigoProducto(fichaProducto.getCodigoProducto());

        for (ProductoOrdenCompra productoOrdenCompra : productosOrden) {
            productoOrdenCompra.setOrdenDeCompra(ordenDeCompraDAO.getById(productoOrdenCompra.getOrdenDeCompraId()));
        }

        mostrarTabla = true;

        showInfo("Aviso", "Se cargaron las órdenes de compras asociados al producto código \"" + fichaProducto.getCodigoProducto() + "\" de forma correcta.");
    }

    public void buscarPorRut(){
        FichaAuxiliar ficha = fichaAuxiliarDAO.findByRut(proveedor.getRut());

        if(ficha != null){
            // Se guardar la ficha buscada como proveedor actual de la vista.
            this.proveedor = ficha;

            // Se muestra la tabla.
            mostrarTabla = true;

            showInfo("Aviso", "Se realizo la búsqueda por el rut \"" + proveedor.getRut() + "\" de forma correcta.");
        } else {
            // Se guarda el código anterior buscado
            String rut = this.proveedor.getRut();

             /* Inicialización de objetos */
            this.proveedor = new FichaAuxiliar();

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showError("Error", "El rut \"" + rut + "\" no existe como proveedor.");
        }
    }

    public void buscarPorCodigoProducto(){
        productosOrden = productoOrdenCompraDAO.findByCodigoProducto(fichaProducto.getCodigoProducto());

        for (ProductoOrdenCompra productoOrdenCompra : productosOrden) {
            productoOrdenCompra.setOrdenDeCompra(ordenDeCompraDAO.getById(productoOrdenCompra.getOrdenDeCompraId()));
        }

        mostrarTabla = true;

        showInfo("Aviso", "Se cargaron las órdenes de compras asociados al producto código \"" + fichaProducto.getCodigoProducto() + "\" de forma correcta.");
    }

    public FichaAuxiliar getProveedor() {
        return proveedor;
    }

    public void setProveedor(FichaAuxiliar proveedor) {
        this.proveedor = proveedor;
    }

    public Boolean getMostrarTabla() {
        return mostrarTabla;
    }

    public FichasLazyDataModel getFichas() {
        if ( fichas == null )
            fichas = new FichasLazyDataModel(fichaAuxiliarDAO);
        return fichas;
    }

    public void setFichas(FichasLazyDataModel fichas) {
        this.fichas = fichas;
    }

    public OrdenesDeCompraLazyDataModel getOrdenes() {
        if ( ordenes == null )
            ordenes = new OrdenesDeCompraLazyDataModel(ordenDeCompraDAO, requisicionDAO, proveedor.getId());
        return ordenes;
    }

    public void setOrdenes(OrdenesDeCompraLazyDataModel ordenes) {
        this.ordenes = ordenes;
    }

    public List<ProductoOrdenCompra> getProductosOrden() {
        return productosOrden;
    }

    public void setProductosOrden(List<ProductoOrdenCompra> productosOrden) {
        this.productosOrden = productosOrden;
    }

    public FichaProducto getFichaProducto() {
        return fichaProducto;
    }

    public void setFichaProducto(FichaProducto fichaProducto) {
        this.fichaProducto = fichaProducto;
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
