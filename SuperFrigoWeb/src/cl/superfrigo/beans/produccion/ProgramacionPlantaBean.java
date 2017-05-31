package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.model.ControlDespachosLazyDataModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class ProgramacionPlantaBean extends BaseBean implements Serializable {

    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;


    public List<OrdenDeTrabajo> getAllOT(){
        return ordenDeTrabajoDAO.cotizacionesAprobadas();
    }

}

