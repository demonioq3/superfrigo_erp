package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.RequisicionDAO;
import cl.superfrigo.entity.abastecimiento.EstadoRequisicion;
import cl.superfrigo.entity.abastecimiento.Requisicion;
import cl.superfrigo.model.RequisicionesLazyDataModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * Created by asgco on 17-Apr-16.
 */
@ManagedBean
@ViewScoped
public class ValidarRequisicionesBean extends BaseBean implements Serializable {

    private RequisicionesLazyDataModel requisiciones;

    @EJB private RequisicionDAO requisicionDAO;


    public RequisicionesLazyDataModel getRequisiciones() {
        if ( requisiciones == null )
            requisiciones = new RequisicionesLazyDataModel(requisicionDAO);
        return requisiciones;
    }

    public void aprobarRequisicion(Requisicion requisicion){
        requisicion.setEstadoRequisicion(null);
        requisicion.setEstadoRequisicionId(EstadoRequisicion.APROBADA);

        try{
            requisicionDAO.update(requisicion);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar la requisición");
        }

        showInfo("Aviso", "Se aprobó la requisición N° " + requisicion.getId() + ".");
    }

    public void rechazarRequisicion(Requisicion requisicion){
        requisicion.setEstadoRequisicion(null);
        requisicion.setEstadoRequisicionId(EstadoRequisicion.RECHAZADA);

        try{
            requisicionDAO.update(requisicion);
        } catch (Exception e){
            showError("Error", "Hubo un error al rechazar la requisición");
        }

        showInfo("Aviso", "Se rechazó la requisición N° " + requisicion.getId() + ".");
    }

    public void setRequisiciones(RequisicionesLazyDataModel requisiciones) {
        this.requisiciones = requisiciones;
    }
}
