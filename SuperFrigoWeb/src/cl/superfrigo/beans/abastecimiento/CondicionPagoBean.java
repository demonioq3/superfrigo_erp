package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.CondicionPagoDAO;
import cl.superfrigo.dao.PrecioProductoDAO;
import cl.superfrigo.entity.abastecimiento.CondicionPago;
import cl.superfrigo.entity.abastecimiento.PrecioProducto;
import org.hibernate.exception.ConstraintViolationException;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 29-Feb-16.
 */
@ManagedBean(name = "condicionDePagoBean")
@ViewScoped
public class CondicionPagoBean extends BaseBean implements Serializable {

    /* Lista de condiciones de pago*/
    private List<CondicionPago> condiciones;

    /* Condicion de pago seleccionada o nueva */
    private CondicionPago editItem;

    /* Rendered de fragment*/
    private boolean showMain = true;

    @EJB private CondicionPagoDAO condicionPagoDAO;


    public List<CondicionPago> getCondiciones() {
        if(condiciones == null){
            condiciones = condicionPagoDAO.findAll();
        }
        return condiciones;
    }

    public void newCondicion(){
        editItem = new CondicionPago();
        showMain = false;
    }

    public void borrar(CondicionPago condicionPago){
        try {
            if(condicionPagoDAO.delete(condicionPago)){
                showInfo("Aviso", "La condición de pago: " + condicionPago.getDescripcion() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar la condición de pago.");
            }

            condiciones = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "No se puede eliminar la condición de pago ya que está siendo utilizada en una orden de compra.");
            }
        }

    }

    public void guardar(){
        if(editItem.getId() == null){
            CondicionPago unidadCreada = condicionPagoDAO.create(editItem);

            showInfo("Aviso", "Se creó correctamente la condición de pago: " + unidadCreada.getDescripcion() + ".");
        } else {
            condicionPagoDAO.update(editItem);
            showInfo("Aviso", "Se editó correctamente la condición de pago: " + editItem.getDescripcion() + ".");
        }

        showMain = true;
        condiciones = null;
    }

    public void atras(){
        showMain = true;
    }

    public void setCondiciones(List<CondicionPago> condiciones) {
        this.condiciones = condiciones;
    }

    public CondicionPago getEditItem() {
        return editItem;
    }

    public void setEditItem(CondicionPago editItem) {
        this.editItem = editItem;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
