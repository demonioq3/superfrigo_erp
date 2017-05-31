package cl.superfrigo.beans.comercial;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.entity.comercial.EstadoCotizacion;
import cl.superfrigo.entity.comercial.EstadoOrdenTrabajo;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.*;

/**
 * Created by asgco on 19-Mar-16.
 */
@ManagedBean(name = "cotizacionBean")
@ViewScoped
public class CotizacionBean extends BaseBean implements Serializable {

    // Persistencia
    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    public List<OrdenDeTrabajo> getTodasLasCotizaciones(){
        return ordenDeTrabajoDAO.findAll();
    }

    public List<OrdenDeTrabajo> getCotizacionesEnEstudio(){
        return ordenDeTrabajoDAO.cotazionesEnEstudio();
    }

    public List<OrdenDeTrabajo> getCotizacionesPorAprobacionComercial(){
        return ordenDeTrabajoDAO.cotizacionesEnEsperaAprobacionComercial();
    }

    public List<OrdenDeTrabajo> getCotizacionesPorAprobacionFinanciera(){
        return ordenDeTrabajoDAO.cotizacionesEnEsperaAprobacionFinanciera();
    }

    public List<OrdenDeTrabajo> getCotizacionesPorAprobacionCliente(){
        return ordenDeTrabajoDAO.cotizacionesEnEsperaAprobacionCliente();
    }

    public void aprobarComercialmente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.ESPERA_APROBACION_FINANCIERA);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar comercialmente la cotización");
        }

        showInfo("Aviso", "Se aprobó comercialmente la OT N° " + ot.getId() + ", ahora pasará a la etapa de validación financiera.");
    }

    public void rechazarComercialmente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.RECHAZADA);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar comercialmente la cotización");
        }

        showInfo("Aviso", "Se rechazó correctamente la OT N° " + ot.getId());
    }

    public void aprobarFinancieramente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.ESPERA_APROBACION_CLIENTE);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar financieramente la cotización");
        }

        showInfo("Aviso", "Se aprobó financieramente la OT N° " + ot.getId() + ".");
    }

    public void rechazarFinancieramente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.RECHAZADA);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar financieramente la cotización");
        }

        showInfo("Aviso", "Se rechazó correctamente la OT N° " + ot.getId());
    }

    public void aprobarCliente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.APROBADA);
        ot.setEstadoOTId(EstadoOrdenTrabajo.VIGENTE);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar la cotización");
        }

        showInfo("Aviso", "Se aprobó por cliente la OT N° " + ot.getId() + ".");
    }

    public void rechazarCliente(OrdenDeTrabajo ot){
        ot.setEstado(null);
        ot.setEstadoId(EstadoCotizacion.RECHAZADA);

        try{
            ordenDeTrabajoDAO.update(ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al aprobar la cotización");
        }

        showInfo("Aviso", "Se rechazó correctamente la OT N° " + ot.getId());
    }


}
