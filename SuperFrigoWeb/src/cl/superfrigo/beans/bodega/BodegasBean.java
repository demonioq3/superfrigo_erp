package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.BodegaDAO;
import cl.superfrigo.dao.BodegaStockProductoDAO;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.hibernate.exception.ConstraintViolationException;

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
public class BodegasBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<Bodega> bodegas;
    private Bodega bodegaSeleccionada;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private BodegaDAO bodegaDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    @EJB
    private BodegaStockProductoDAO bodegaStockProductoDAO;


    public void guardar(){
        if(bodegaSeleccionada.getId() == null){
            // Ver si el código de grupo está utilizado.
            if(bodegaDAO.existe(this.bodegaSeleccionada.getCodigo())){
                showError("Error", "El código ingresado ya está registrado. Favor intentar con otro.");
                return;
            }

            Bodega bodegaCreada = bodegaDAO.create(bodegaSeleccionada);

            // Creamos stock de cada producto existente para la bodega en 0.
            // Para ellos obtenemos todos los productos.
            List<FichaProducto> fichaProductos = fichaProductoDAO.findAll();
            // Iteramos por cada producto y creando el stock asociado a la bodega.
            for (FichaProducto fichaProducto : fichaProductos) {
                BodegaStockProducto bodegaStockProducto = new BodegaStockProducto();
                bodegaStockProducto.setCantidad(0D);
                bodegaStockProducto.setFichaProductoId(fichaProducto.getId());
                bodegaStockProducto.setBodegaId(bodegaCreada.getId());

                // Se crea el stock de bodega asociado al producto.
                bodegaStockProductoDAO.create(bodegaStockProducto);
            }

            showInfo("Aviso", "Se creó correctamente la bodega con código: " + bodegaCreada.getCodigo() + ".");
        } else {
            bodegaDAO.update(bodegaSeleccionada);
            showInfo("Aviso", "Se editó correctamente la bodega con código: " + bodegaSeleccionada.getCodigo() + ".");
        }

        showMain = true;
        bodegas = null;
    }

    public void borrar(Bodega bodega){
        try {
            if(bodegaDAO.delete(bodega)){
                showInfo("Aviso", "La bodega código: " + bodega.getCodigo() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar la bodega.");
            }

            bodegas = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "La bodega no se puede eliminar ya que está siendo referenciado por otro elemento de la base de datos.");
            }
        }
    }

    public List<Bodega> getAllBodegas(){
        return bodegaDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newBodega(){
        bodegaSeleccionada = new Bodega();
        showMain = false;
    }

    public List<Bodega> getBodegas() {
        if(bodegas == null){
            bodegas = bodegaDAO.findAll();
        }
        return bodegas;
    }

    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }

    public Bodega getBodegaSeleccionada() {
        return bodegaSeleccionada;
    }

    public void setBodegaSeleccionada(Bodega bodegaSeleccionada) {
        this.bodegaSeleccionada = bodegaSeleccionada;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }

}

