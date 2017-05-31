package cl.superfrigo.beans.registrohh;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.AreaHHDAO;
import cl.superfrigo.dao.DefinicionHHDAO;
import cl.superfrigo.dao.EstadoHHDAO;
import cl.superfrigo.entity.registros_hh.AreaHH;
import cl.superfrigo.entity.registros_hh.DefinicionHH;
import cl.superfrigo.entity.registros_hh.EstadoHH;
import org.hibernate.exception.ConstraintViolationException;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class MantenedorAreaHHBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<AreaHH> areas;
    private AreaHH areaSeleccionada;

    /* Rendered de fragment*/
    private boolean showMain = true;

    /* Persistence */
    @EJB private AreaHHDAO areaHHDAO;
    @EJB private EstadoHHDAO estadoHHDAO;


    public void guardar(){
        if(areaSeleccionada.getId() == null){
            // Ver si el nombre de área ya existe
            if(areaHHDAO.existe(this.areaSeleccionada.getNombre())){
                showError("Error", "El nombre de área ya está registrado. Favor intentar con otro.");
                return;
            }

            AreaHH areaCreada = areaHHDAO.create(areaSeleccionada);

            showInfo("Aviso", "Se creó correctamente el área de nombre: " + areaCreada.getNombre() + ".");
        } else {
            areaHHDAO.update(areaSeleccionada);
            showInfo("Aviso", "Se editó correctamente el área seleccionada.");
        }

        showMain = true;
        areas = null;
    }

    public void borrar(AreaHH areaHH){
        try {
            if(areaHHDAO.delete(areaHH)){
                showInfo("Aviso", "El área HH de nombre: " + areaHH.getNombre() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar la área.");
            }

            areas = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "El área no se puede eliminar ya que está siendo referenciado por otro elemento de la base de datos.");
            }
        }
    }

    public List<AreaHH> getAllAreasHH(){
        return areaHHDAO.findAll();
    }

    public List<EstadoHH> getAllEstadosHH() {
        return estadoHHDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newArea(){
        areaSeleccionada = new AreaHH();
        showMain = false;
    }

    public List<AreaHH> getAreas() {
        if(areas == null){
            areas = areaHHDAO.findAll();
        }
        return areas;
    }

    public void setAreas(List<AreaHH> areas) {
        this.areas = areas;
    }

    public AreaHH getAreaSeleccionada() {
        return areaSeleccionada;
    }

    public void setAreaSeleccionada(AreaHH areaSeleccionada) {
        this.areaSeleccionada = areaSeleccionada;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }

}

