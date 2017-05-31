package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.comercial.ProductoOrdenTrabajo;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.model.ControlDespachosLazyDataModel;
import cl.superfrigo.model.StockRecetaModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class StockRecetaBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long ordenDeTrabajoId;

    // Productos by orden de trabajo;
    private List<ProductoOrdenTrabajo> productos;

    // Recetas por orden de trabajo.
    private List<Receta> recetasByOT;

    // Producto-receta seleccionada;
    private Long productoId;

    // Productos de la receta escogida.
    private List<StockRecetaModel> productosReceta;

    // Header tabla;
    private String header;

    @EJB private RecetaDAO recetaDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;



    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;

    @EJB private ControlDespachoDAO controlDespachoDAO;
    @EJB private OrdenDeTrabajoDAO ordenDeTrabajoDAO;
    @EJB private ProductoOrdenTrabajoDAO productoOrdenTrabajoDAO;


    public void buscar(){
        if(ordenDeTrabajoId == null ){
            showError("Error", "Debe ingresar OT para realizar la búsqueda.");
            ordenDeTrabajoId = null;
            return;
        }

        OrdenDeTrabajo ordenDeTrabajo = ordenDeTrabajoDAO.getById(ordenDeTrabajoId);
        productos = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ordenDeTrabajo.getId());

        if(ordenDeTrabajo == null){
            showError("Error", "Debe ingresar una OT válida y registrada en el sistema.");
            return;
        }

    }

    public void cambioReceta(){
        productosReceta = new ArrayList<StockRecetaModel>();

        for (ProductoOrdenTrabajo producto : productos) {
            if(productoId.equals(producto.getId())){
                header = "Receta " + producto.getReceta().getCodigo();

                Receta receta = recetaDAO.findById(producto.getRecetaId());

                for (ProductoReceta productoReceta : receta.getProductosAsociados()) {
                    StockRecetaModel stockRecetaModel = new StockRecetaModel();
                    stockRecetaModel.setFichaProducto(productoReceta.getProducto());

                    List<BodegaStockProducto> stocks = bodegaStockProductoDAO.findByFichaProductoId(productoReceta.getProductoId());
                    Double stockTotal = 0D;

                    for (BodegaStockProducto stock : stocks) {
                        stockTotal += stock.getCantidad();
                    }

                    stockRecetaModel.setCantidadEnBodega(stockTotal);
                    stockRecetaModel.setCantidadPresupuestada(productoReceta.getCantidad());

                    double diferencia = stockRecetaModel.getCantidadEnBodega() - stockRecetaModel.getCantidadPresupuestada();
                    if(diferencia < 0)
                        stockRecetaModel.setDiferencia(diferencia * -1);
                    else
                        stockRecetaModel.setDiferencia(diferencia);

                    productosReceta.add(stockRecetaModel);
                }
            }
        }
    }

    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

    public boolean isMostrarTabla() {
        return mostrarTabla;
    }

    public List<Receta> getRecetasByOT() {
        return recetasByOT;
    }

    public void setRecetasByOT(List<Receta> recetasByOT) {
        this.recetasByOT = recetasByOT;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public List<StockRecetaModel> getProductosReceta() {
        return productosReceta;
    }

    public void setProductosReceta(List<StockRecetaModel> productosReceta) {
        this.productosReceta = productosReceta;
    }

    public List<ProductoOrdenTrabajo> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenTrabajo> productos) {
        this.productos = productos;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

