package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.GrupoDAO;
import cl.superfrigo.dao.SubGrupoDAO;
import cl.superfrigo.entity.bodega.Grupo;
import cl.superfrigo.entity.bodega.SubGrupo;
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
public class SubGruposBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<SubGrupo> subGrupos;
    private SubGrupo subGrupoSeleccionado;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private SubGrupoDAO subGrupoDAO;


    public void guardar(){
        // Validar que haya seleccionado el grupo padre.
        if(subGrupoSeleccionado.getPadreId().equals(0L)){
            // Si no lo selecciona, el subgrupo igual se puede eliminar.
            subGrupoSeleccionado.setPadreId(null);
        }

        if(subGrupoSeleccionado.getId() == null){
            // Validar que el código no exista registrado.
            if(subGrupoDAO.existe(this.subGrupoSeleccionado.getCodigo())){
                showError("Error", "El código ingresado ya está registrado. Favor intentar con otro.");
                return;
            }

            SubGrupo subGrupoCreado = subGrupoDAO.create(subGrupoSeleccionado);

            showInfo("Aviso", "Se creó correctamente el subgrupo con código: " + subGrupoCreado.getCodigo() + ".");
        } else {
            subGrupoDAO.update(subGrupoSeleccionado);
            showInfo("Aviso", "Se editó correctamente el subgrupo con código: " + subGrupoSeleccionado.getCodigo() + ".");
        }

        showMain = true;
        subGrupos = null;
    }

    public void borrar(SubGrupo subGrupo){
        try {
            if(subGrupoDAO.delete(subGrupo)){
                showInfo("Aviso", "El subgrupo con código: " + subGrupo.getCodigo() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar el subgrupo.");
            }

            subGrupos = null;
        } catch (Exception e) {
            showError("Error", "No se puede eliminar el subgrupo ya que tiene asociado 1 o más .");
        }
    }

    public List<SubGrupo> getAllSubGrupos(){
        return subGrupoDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newSubGrupo(){
        subGrupoSeleccionado = new SubGrupo();
        showMain = false;
    }

    public List<SubGrupo> getSubGrupos() {
        if(subGrupos == null){
            subGrupos = subGrupoDAO.findAll();
        }
        return subGrupos;
    }

    public void setSubGrupos(List<SubGrupo> subGrupos) {
        this.subGrupos = subGrupos;
    }

    public SubGrupo getSubGrupoSeleccionado() {
        return subGrupoSeleccionado;
    }

    public void setSubGrupoSeleccionado(SubGrupo subGrupoSeleccionado) {
        this.subGrupoSeleccionado = subGrupoSeleccionado;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
