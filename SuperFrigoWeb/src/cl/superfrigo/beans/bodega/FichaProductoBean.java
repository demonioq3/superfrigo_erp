package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.abastecimiento.PrecioProducto;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class FichaProductoBean extends BaseBean implements Serializable {

    /* Ficha de producto */
    private FichaProducto editItem;
    // Caracteristicas del producto editItem.
    private List<String> caracteristicas;

    /* Rendered de botón eliminar */
    public boolean mostrarBotonEliminar = false;

    /* Subgrupos del grupo seleccionado actual */
    private List<SubGrupo> subgrupos;

    /* Lazy lista de productos */
    private FichasProductoLazyDataModel fichasProductos;

    /* Lazy lista de productos inventariables */
    private FichasProductoLazyDataModel fichasProductosInventariables;

    /* Textos por defectos */
    private boolean showBorrarFichaProducto = false;

    /* String para guardar codigo en vista Consulta de stock */
    private String codigoParaConsultaStock;
    private String decripcionParaConsultaStock;
    private boolean mostrarTablaStock = false;
    private List<BodegaStockProducto> listaBodegasStock;

    /* Boolean para saber si es edición  */
    private boolean sePuedeGuardar = false;

    /* Persistencia */
    @EJB
    private GrupoDAO grupoDAO;

    @EJB
    private SubGrupoDAO subGrupoDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    @EJB
    private UnidadMedidaDAO unidadMedidaDAO;

    @EJB
    private BodegaStockProductoDAO bodegaStockProductoDAO;

    @EJB
    private BodegaDAO bodegaDAO;


    /**
     * Método inicial. Al iniciar la vista creo una nueva Ficha la cual será la que el usuario ingresará
     * a través del formulario.
     */
    @PostConstruct
    public void init(){
        editItem = new FichaProducto();
    }

    public List<Grupo> getAllGrupos(){
        return grupoDAO.findAll();
    }

    public List<FichaProducto> getAllFichasProducto(){
        return fichaProductoDAO.findAll();
    }

    public void seleccionarSubGruposPorGrupo(){
        subgrupos = subGrupoDAO.findByPadreId(this.editItem.getGrupoId());
    }

    public void seleccionarFicha(FichaProducto fichaProducto){
        editItem = fichaProducto;

        // Se crea la lista de subgrupos para renderiar en la vista.
        subgrupos = subGrupoDAO.findByPadreId(editItem.getGrupoId());

        // Mostrar si es para venta y compra;
        caracteristicas = new ArrayList<String>();

        if(fichaProducto.getVenta().equals("SI")){
            caracteristicas.add("VENTA");
        }

        if(fichaProducto.getCompra().equals("SI")){
            caracteristicas.add("COMPRA");
        }

        mostrarBotonEliminar = true;
        sePuedeGuardar = true;
    }

    public void buscarPorCodigo(){
        FichaProducto fichaBuscada = fichaProductoDAO.findByCodigoProducto(editItem.getCodigoProducto());

        if(fichaBuscada != null){
            this.editItem = fichaBuscada;

            caracteristicas = new ArrayList<String>();
            if(this.editItem.getVenta().equals("SI")){
                caracteristicas.add("VENTA");
            }

            if(this.editItem.getCompra().equals("SI")){
                caracteristicas.add("COMPRA");
            }

            mostrarBotonEliminar = true;

            // Busqueda de subgrupos.
            subgrupos = subGrupoDAO.findByPadreId(this.editItem.getGrupoId());

            // Se puede guardar, debido a que se encontró el producto en la búsqueda.
            sePuedeGuardar = true;
            showInfo("Aviso", "La ficha de producto código " + editItem.getCodigoProducto() + " se encontró correctamente.");
        } else {
            String codigoAntiguo = editItem.getCodigoProducto();

            this.editItem = new FichaProducto();
            this.editItem.setCodigoProducto(codigoAntiguo);
            showError("Error", "La ficha de producto " + this.editItem.getCodigoProducto() + " no está registrada.");
        }
    }

    public void guardar(){
        if(!sePuedeGuardar){
            // Validacion: Validando que el código de producto no exista anteriormente.
            FichaProducto fichaExistente = fichaProductoDAO.findByCodigoProducto(this.editItem.getCodigoProducto());
            if(fichaExistente != null){
                showError("Error", "El código de producto ya está registrado. Para editarlo, debes acceder al buscador de fichas de productos y seleccionarlo.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        if(this.editItem.getId() == null){
            // Agregando si es venta o compra.
            if(caracteristicas.contains("COMPRA")){
                this.editItem.setCompra("SI");
            } else {
                this.editItem.setCompra("NO");
            }

            if(caracteristicas.contains("VENTA")){
                this.editItem.setVenta("SI");
            } else {
                this.editItem.setVenta("NO");
            }

            FichaProducto fichaProducto = fichaProductoDAO.create(this.editItem);

            // Se crea el stock por cada bodega existente solo si es inventariable.
            if(this.editItem.getTipoFichaProductoId().equals(TipoFichaProducto.INVENTARIABLE)){
                List<Bodega> bodegas = bodegaDAO.findAll();
                for (Bodega bodega : bodegas) {
                    BodegaStockProducto bodegaStockProducto = new BodegaStockProducto();
                    bodegaStockProducto.setCantidad(0D);
                    bodegaStockProducto.setBodegaId(bodega.getId());
                    bodegaStockProducto.setFichaProductoId(fichaProducto.getId());

                    bodegaStockProductoDAO.create(bodegaStockProducto);
                }
            }

            showInfo("Aviso", "Se creó de manera correcta la ficha de producto. El código del producto es el " + this.editItem.getCodigoProducto() + ".");

            // Después del mensaje...
            // Se limpia el formulario cuando se crea un producto
            this.editItem = new FichaProducto();
            // Se limpian las caracteristicas.
            this.caracteristicas = new ArrayList<String>();
            // Ya no se puede guardar.
            sePuedeGuardar = false;
        } else {
            // Agregando si es venta o compra.
            if(caracteristicas.contains("COMPRA")){
                this.editItem.setCompra("SI");
            } else {
                this.editItem.setCompra("NO");
            }

            if(caracteristicas.contains("VENTA")){
                this.editItem.setVenta("SI");
            } else {
                this.editItem.setVenta("NO");
            }

            // Si se hizo el cambio a inventariable, debo crear los stock en bodega.
            if(this.editItem.getTipoFichaProductoId().equals(TipoFichaProducto.INVENTARIABLE)){
                // Chequeando si anteriormente tuvo stock.
                List<BodegaStockProducto> bodegaStockProductos = bodegaStockProductoDAO.findByFichaProductoId(this.editItem.getId());
                // Si existe stock en bodega de esa ficha de producto, la borramos y los creamos denuevo.
                if(bodegaStockProductos != null && bodegaStockProductos.size() > 0){
                    for (BodegaStockProducto bodegaStockProducto : bodegaStockProductos) {
                        bodegaStockProductoDAO.delete(bodegaStockProducto);
                    }
                }

                // Procedemos a la creación del stock
                List<Bodega> bodegas = bodegaDAO.findAll();
                for (Bodega bodega : bodegas) {
                    BodegaStockProducto bodegaStockProducto = new BodegaStockProducto();
                    bodegaStockProducto.setCantidad(0D);
                    bodegaStockProducto.setBodegaId(bodega.getId());
                    bodegaStockProducto.setFichaProductoId(this.editItem.getId());

                    bodegaStockProductoDAO.create(bodegaStockProducto);
                }

            } else {
                // Chequeando si anteriormente tuvo stock.
                List<BodegaStockProducto> bodegaStockProductos = bodegaStockProductoDAO.findByFichaProductoId(this.editItem.getId());
                // Si existe stock en bodega de esa ficha de producto, solo los borramos.
                if(bodegaStockProductos != null && bodegaStockProductos.size() > 0){
                    for (BodegaStockProducto bodegaStockProducto : bodegaStockProductos) {
                        bodegaStockProductoDAO.delete(bodegaStockProducto);
                    }
                }
            }

            sePuedeGuardar = true;
            fichaProductoDAO.update(this.editItem);
            showInfo("Aviso", "Se editó de manera correcta la ficha de producto código " + this.editItem.getCodigoProducto() + "");
        }

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
    }

    public void eliminar(){
        // Viendo si el producto tiene stock.
        List<BodegaStockProducto> stock = bodegaStockProductoDAO.finByCodigoProducto(this.editItem.getCodigoProducto());
        if(stock.size() > 0){
            for (BodegaStockProducto bodegaStockProducto : stock) {
                if(bodegaStockProducto.getCantidad() > 0){
                    showError("Error", "La ficha del producto no se puede eliminar debido a que existe stock en bodega de ése producto.");

                    // Move to top
                    RequestContext.getCurrentInstance().scrollTo("main-content");
                    return;
                }

            }
        } else {
            try{
                for (BodegaStockProducto bodegaStockProducto : stock) {
                    bodegaStockProductoDAO.delete(bodegaStockProducto);
                }
            } catch (Exception e){
                showError("Error", "Hubo un error al eliminar el stock en bodegas de la ficha de producto. Favor consulte con su administrador.");
            }

        }

        // Eliminar el stock en todas las bodegas de ese producto.
        List<BodegaStockProducto> bodegaStockProducto = bodegaStockProductoDAO.finByCodigoProducto(this.editItem.getCodigoProducto());
        for (BodegaStockProducto stockProducto : bodegaStockProducto) {
            bodegaStockProductoDAO.delete(stockProducto);
        }

        if(fichaProductoDAO.delete(this.editItem)){
            showInfo("Aviso", "Se eliminó correctamente la ficha de producto código: " + this.editItem.getCodigoProducto());

            mostrarBotonEliminar = false;

            resetearEditItem();
        } else {
            showError("Error", "Hubo un error al eliminar la ficha de producto.");
        }

        this.editItem = new FichaProducto();
    }

    private void resetearEditItem() {
        this.editItem = new FichaProducto();

        this.caracteristicas = new ArrayList<String>();
    }

    public void limpiar(){
        if(this.editItem != null){
            this.editItem = new FichaProducto();

            caracteristicas = new ArrayList<String>();

            if(mostrarBotonEliminar == true){
                mostrarBotonEliminar = false;
            }
        }
    }

    public void seleccionarFichaYConsultarStock(FichaProducto fichaProducto) {
        listaBodegasStock = bodegaStockProductoDAO.findByProductoId(fichaProducto.getId());
        codigoParaConsultaStock = fichaProducto.getCodigoProducto();
        decripcionParaConsultaStock = fichaProducto.getDescripcion();

        mostrarTablaStock = true;
    }

    public void buscarPorCodigoProducto(){
        listaBodegasStock = bodegaStockProductoDAO.finByCodigoProducto(codigoParaConsultaStock);
        if(listaBodegasStock != null && listaBodegasStock.size() > 0) {
            decripcionParaConsultaStock = fichaProductoDAO.findByCodigoProducto(codigoParaConsultaStock).getDescripcion();
        }

        mostrarTablaStock = true;
    }

    public List<FichaProducto> getAll(){
        return fichaProductoDAO.findAll();
    }

    public List<UnidadMedida> getAllUnidadesMedida(){
        return unidadMedidaDAO.findAll();
    }

    private boolean inventariable = true;

    public boolean isInventariable() {
        return inventariable;
    }

    public void setInventariable(boolean inventariable) {
        this.inventariable = inventariable;
    }

    public FichaProducto getEditItem() {
        return editItem;
    }

    public void setEditItem(FichaProducto editItem) {
        this.editItem = editItem;
    }

    public List<SubGrupo> getSubgrupos() {
        return subgrupos;
    }

    public void setSubgrupos(List<SubGrupo> subgrupos) {
        this.subgrupos = subgrupos;
    }

    public boolean isShowBorrarFichaProducto() {
        return showBorrarFichaProducto;
    }

    public void setShowBorrarFichaProducto(boolean showBorrarFichaProducto) {
        this.showBorrarFichaProducto = showBorrarFichaProducto;
    }

    public FichasProductoLazyDataModel getFichasProductos() {
        if ( fichasProductos == null )
            fichasProductos = new FichasProductoLazyDataModel(fichaProductoDAO);
        return fichasProductos;
    }

    public void setFichasProductos(FichasProductoLazyDataModel fichasProductos) {
        this.fichasProductos = fichasProductos;
    }

    public FichasProductoLazyDataModel getFichasProductosInventariables() {
        if ( fichasProductosInventariables == null )
            fichasProductosInventariables = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProductosInventariables;
    }

    public void setFichasProductosInventariables(FichasProductoLazyDataModel fichasProductosInventariables) {
        this.fichasProductosInventariables = fichasProductosInventariables;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public boolean isMostrarBotonEliminar() {
        return mostrarBotonEliminar;
    }

    public String getCodigoParaConsultaStock() {
        return codigoParaConsultaStock;
    }

    public void setCodigoParaConsultaStock(String codigoParaConsultaStock) {
        this.codigoParaConsultaStock = codigoParaConsultaStock;
    }

    public boolean isMostrarTablaStock() {
        return mostrarTablaStock;
    }

    public List<BodegaStockProducto> getListaBodegasStock() {
        return listaBodegasStock;
    }

    public String getDecripcionParaConsultaStock() {
        return decripcionParaConsultaStock;
    }

    public void setDecripcionParaConsultaStock(String decripcionParaConsultaStock) {
        this.decripcionParaConsultaStock = decripcionParaConsultaStock;
    }
}
