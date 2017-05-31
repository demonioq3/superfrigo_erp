package cl.superfrigo.beans.proyectos;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
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
public class RecetasBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private FichaProducto fichaSeleccionada;

    /* Todas las fichas (POP-UP) */
    private FichasProductoLazyDataModel fichasProducto;

    /* Recetas asociadas al producto */
    private List<Receta> recetasAsociadas;

    /* Receta seleccionada */
    private Receta recetaSeleccionada;

    /* Productos asociados a la receta */
    private List<ProductoReceta> productosReceta;

    /* Renders */
    private boolean mostrarMain = true;
    private boolean mostrarTabla = false;
    private boolean mostrarReceta = false;

    /* Rendered de fragment*/
    private boolean showMain = true;

    /* Boolean para ver si el usuario está editando una receta */
    private boolean estaEditando = false;

    @EJB private RecetaDAO recetaDAO;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private ProductoRecetaDAO productoRecetaDAO;

    @PostConstruct
    public void init(){
        fichaSeleccionada = new FichaProducto();
    }

    public void seleccionarProducto(FichaProducto fichaProducto){
        this.fichaSeleccionada = fichaProducto;

        recetasAsociadas = recetaDAO.getByFichaProductoId(fichaProducto.getId());

        mostrarTabla = true;

        showInfo("Aviso", "Se cargaron las recetas asociadas al producto código: \"" + fichaProducto.getCodigoProducto() + "\" de forma correcta.");
    }

    public void buscarPorCodigoProducto(){
        /*fichaSeleccionada = fichaProductoDAO.findByCodigoProducto(this.fichaSeleccionada.getCodigoProducto());

        if(fichaSeleccionada != null){
            recetasAsociadas = recetaDAO.findByCodigoProducto(fichaSeleccionada.getCodigoProducto());

            mostrarTabla = true;

            showInfo("Aviso", "Se cargaron las recetas asociadas al producto código \"" + fichaSeleccionada.getCodigoProducto() + "\" de forma correcta.");
        } else {
            mostrarTabla = false;
            showError("Error", "El código de producto ingresado no está registrado.");
        }*/
    }

    public List<Receta> getAllRecetas(){
        return recetaDAO.findAll();
    }

    public void nuevaReceta(){
        recetaSeleccionada = new Receta();
        recetaSeleccionada.setProducto(fichaSeleccionada);
        recetaSeleccionada.setProductoId(fichaSeleccionada.getId());

        // Inicializando productos asociados a la receta
        productosReceta = new ArrayList<ProductoReceta>();

        mostrarMain = false;
        mostrarReceta = true;

        // El usuario no está editando una receta.
        estaEditando = false;

        showInfo("Aviso", "Llene los datos para generar la receta.");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void guardarReceta(){
        // Chequeando si hay datos sin ingresar.
        if(existenVacios())
            return;

        if(!estaEditando){
            // Chequeando si el código ya está creado.
            if(estaCreadaLaReceta(recetaSeleccionada.getCodigo())){
                showError("Error", "El código ya está registrado en otra receta. Intenta con otro código.");
                return;
            }
        }

        // Si la receta es nueva.
        if(recetaSeleccionada.getId() == null){
            if(recetaSeleccionada.getBase().equals(Boolean.TRUE)){
                recetaDAO.marcarTodosBaseFalse();
            }

            Receta receta = recetaDAO.create(recetaSeleccionada);

            // Se asocia cada producto a la receta ya creada.
            for (ProductoReceta productoReceta : productosReceta) {
                productoReceta.setRecetaId(receta.getId());
                // Se guarda cada producto.

                productoRecetaDAO.create(productoReceta);
            }

            mostrarReceta = false;
            mostrarMain = true;

            // Obteniendo nuevamente la lista de recetas asociadas al producto.
            recetasAsociadas = recetaDAO.getByFichaProductoId(fichaSeleccionada.getId());

            showInfo("Aviso", "Se guardó correctamente la receta codigo "+ receta.getCodigo() +", asociada a la ficha " + fichaSeleccionada.getCodigoProducto());
        } else {
            if(recetaSeleccionada.getBase().equals(Boolean.TRUE)){
                recetaDAO.marcarTodosBaseFalse();
            }

            List<ProductoReceta> productosAGuardar = recetaSeleccionada.getProductosAsociados();

            Receta recetaActual = recetaDAO.getById(recetaSeleccionada.getId());

            // Se eliminan todos los productos asociados a la receta persistentes en la bd.
            for (ProductoReceta productoReceta : recetaActual.getProductosAsociados()) {
                productoRecetaDAO.delete(productoReceta);
            }

            // Se deja nula la actual lista de productos a guardar, para guardar la receta sin productos.
            recetaSeleccionada.setProductosAsociados(null);

            // Se guarda la receta.
            Receta recetaGuardada = recetaDAO.update(recetaSeleccionada);

            for (ProductoReceta productoReceta : productosAGuardar) {
                productoReceta.setId(null);
                productoReceta.setRecetaId(recetaGuardada.getId());
                productoRecetaDAO.create(productoReceta);
            }

            mostrarReceta = false;
            mostrarMain = true;

            // Obteniendo nuevamente la lista de recetas asociadas al producto.
            recetasAsociadas = recetaDAO.getByFichaProductoId(fichaSeleccionada.getId());

            // Se cambia el está editando a falso.
            this.estaEditando = false;

            showInfo("Aviso", "Se guardó correctamente la receta codigo "+ recetaSeleccionada.getCodigo() +", asociada a la ficha " + fichaSeleccionada.getCodigoProducto());
        }
    }


    private boolean existenVacios() {
        if(recetaSeleccionada.getCodigo() == null || recetaSeleccionada.getCodigo().equals("")){
            showError("Error", "El código es obligatorio.");
            return true;
        }

        if(recetaSeleccionada.getCantidadBase() == null || recetaSeleccionada.getCantidadBase() == 0L || recetaSeleccionada.getCantidadBase() < 0){
            showError("Error", "La cantidad base debe ser un número mayor a 0.");
            return true;
        }

        return false;
    }

    private boolean estaCreadaLaReceta(String codigo) {
        Receta receta = recetaDAO.getByCodigoReceta(codigo);

        if(receta == null){
            return false;
        }

        return true;
    }

    public void editarReceta(Receta receta){
        recetaSeleccionada = receta;

        productosReceta = recetaSeleccionada.getProductosAsociados();

        // El usuario está editando, por lo tanto el boolean cambia a "Está editando".
        this.estaEditando = true;

        mostrarMain = false;
        mostrarReceta = true;
    }

    public void eliminarReceta(Receta receta){


        for (ProductoReceta productoReceta: receta.getProductosAsociados()) {
            if(!productoRecetaDAO.delete(productoReceta)){
                showError("Error", "Hubo un error al eliminar los productos asociados a la receta.");
                return;
            }
        }

        receta.setProductosAsociados(null);

        if(recetaDAO.delete(receta)){
            showInfo("Aviso", "La receta se eliminó correctamente.");

            // Obteniendo nuevamente la lista de recetas asociadas al producto.
            recetasAsociadas = recetaDAO.getByFichaProductoId(fichaSeleccionada.getId());
        }

        mostrarMain = true;
        mostrarReceta = false;
    }

    public void atras(){
        showMain = true;
    }

    public boolean isShowMain() {
        return showMain;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void agregarProductoAReceta(FichaProducto fichaProducto){
        ProductoReceta productoReceta = new ProductoReceta();
        productoReceta.setProducto(fichaProducto);
        productoReceta.setProductoId(fichaProducto.getId());

        productosReceta.add(productoReceta);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void borrarProductoReceta(ProductoReceta productoReceta){
        productosReceta.remove(productoReceta);
    }

    public void atrasReceta(){
        mostrarMain = true;
        mostrarReceta = false;

        estaEditando = false;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

    public boolean isMostrarTabla() {
        return mostrarTabla;
    }

    public boolean isMostrarReceta() {
        return mostrarReceta;
    }

    public FichaProducto getFichaSeleccionada() {
        return fichaSeleccionada;
    }

    public void setFichaSeleccionada(FichaProducto fichaSeleccionada) {
        this.fichaSeleccionada = fichaSeleccionada;
    }

    public List<Receta> getRecetasAsociadas() {
        return recetasAsociadas;
    }

    public void setRecetasAsociadas(List<Receta> recetasAsociadas) {
        this.recetasAsociadas = recetasAsociadas;
    }

    public Receta getRecetaSeleccionada() {
        return recetaSeleccionada;
    }

    public void setRecetaSeleccionada(Receta recetaSeleccionada) {
        this.recetaSeleccionada = recetaSeleccionada;
    }

    public List<ProductoReceta> getProductosReceta() {
        return productosReceta;
    }

    public void setProductosReceta(List<ProductoReceta> productosReceta) {
        this.productosReceta = productosReceta;
    }

    public boolean isEstaEditando() {
        return estaEditando;
    }
}

