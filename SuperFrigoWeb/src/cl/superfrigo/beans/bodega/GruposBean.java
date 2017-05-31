package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.BodegaDAO;
import cl.superfrigo.dao.BodegaStockProductoDAO;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.GrupoDAO;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.Grupo;
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
public class GruposBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<Grupo> grupos;
    private Grupo grupoSeleccionado;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private GrupoDAO grupoDAO;


    public void guardar(){
        if(grupoSeleccionado.getId() == null){
            // Ver si el código de grupo está utilizado.
            if(grupoDAO.existe(this.grupoSeleccionado.getCodigo())){
                showError("Error", "El código ingresado ya está registrado. Favor intentar con otro.");
                return;
            }
            Grupo grupoCreado = grupoDAO.create(grupoSeleccionado);

            showInfo("Aviso", "Se creó correctamente el grupo con código: " + grupoCreado.getCodigo() + ".");
        } else {
            grupoDAO.update(grupoSeleccionado);
            showInfo("Aviso", "Se editó correctamente el grupo con código: " + grupoSeleccionado.getCodigo() + ".");
        }

        showMain = true;
        grupos = null;
    }

    public void borrar(Grupo grupo){
        try {
            if(grupoDAO.delete(grupo)){

                showInfo("Aviso", "El grupo con código: " + grupo.getCodigo() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar el grupo.");
            }

            grupos = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "No se puede eliminar el grupo debido a que está asociado a una ficha de producto.");
            }
        }

    }

    public List<Grupo> getAllGrupos(){
        return grupoDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newGrupo(){
        grupoSeleccionado = new Grupo();
        showMain = false;
    }

    public List<Grupo> getGrupos() {
        if(grupos == null){
            grupos = grupoDAO.findAll();
        }
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public Grupo getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
