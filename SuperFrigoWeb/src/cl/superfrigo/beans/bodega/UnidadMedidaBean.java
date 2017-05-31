package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.BodegaDAO;
import cl.superfrigo.dao.BodegaStockProductoDAO;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.UnidadMedidaDAO;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.UnidadMedida;
import org.hibernate.exception.ConstraintViolationException;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class UnidadMedidaBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<UnidadMedida> unidades;
    private UnidadMedida unidadSeleccionada;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private UnidadMedidaDAO unidadMedidaDAO;


    public void guardar(){
        if(unidadSeleccionada.getId() == null){
            // Ver si el código de grupo está utilizado.
            if(unidadMedidaDAO.existe(this.unidadSeleccionada.getCodigo())){
                showError("Error", "El código ingresado ya está registrado. Favor intentar con otro.");
                return;
            }

            UnidadMedida unidadCreada = unidadMedidaDAO.create(unidadSeleccionada);

            showInfo("Aviso", "Se creó correctamente la unidad de medida con código: " + unidadCreada.getCodigo() + ".");
        } else {
            unidadMedidaDAO.update(unidadSeleccionada);
            showInfo("Aviso", "Se editó correctamente la unidad de medida con código: " + unidadSeleccionada.getCodigo() + ".");
        }

        showMain = true;
        unidades = null;
    }

    public void borrar(UnidadMedida unidadMedida){
        try {
            if(unidadMedidaDAO.delete(unidadMedida)){
                showInfo("Aviso", "La unidad de medida código: " + unidadMedida.getCodigo() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar la unidad de medida.");
            }

            unidades = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "No se puede eliminar la unidad de medida ya que está siendo utilizada en una ficha de producto.");
            }
        }

    }

    public List<UnidadMedida> getAllUnidades(){
        return unidadMedidaDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newUnidad(){
        unidadSeleccionada = new UnidadMedida();
        showMain = false;
    }

    public List<UnidadMedida> getUnidades() {
        if(unidades == null){
            unidades = unidadMedidaDAO.findAll();
        }
        return unidades;
    }

    public void setUnidades(List<UnidadMedida> unidades) {
        this.unidades = unidades;
    }

    public UnidadMedida getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(UnidadMedida unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
