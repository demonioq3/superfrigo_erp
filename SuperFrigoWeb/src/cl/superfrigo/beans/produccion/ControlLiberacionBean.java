package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.ControlLiberacionDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.produccion.ControlLiberacion;
import cl.superfrigo.model.ControlDespachosLazyDataModel;
import cl.superfrigo.model.ControlLiberacionLazyDataModel;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class ControlLiberacionBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long ordenDeTrabajoId;
    // Fecha seleccionada en la vista.
    private Date fecha;

    // Tabla lazy de controles.
    private ControlLiberacionLazyDataModel controles;

    // Control seleccionado para ser editado.
    private ControlLiberacion controlSeleccionado;

    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;    // Por defecto es falso.
    public boolean mostrarDetalleRegistro = false;

    @EJB private ControlLiberacionDAO controlLiberacionDAO;
    @EJB private OrdenDeTrabajoDAO ordenDeTrabajoDAO;


    public void buscar(){
        if(ordenDeTrabajoId == null || fecha == null){
            showError("Error", "Debe ingresar OT y fecha para realizar la búsqueda.");
            ordenDeTrabajoId = null;
            return;
        }

        if(ordenDeTrabajoId == 0){
            showError("Error", "Debe ingresar OT y fecha para realizar la búsqueda.");
            return;
        }

        OrdenDeTrabajo ordenDeTrabajo = ordenDeTrabajoDAO.getById(ordenDeTrabajoId);

        if(ordenDeTrabajo == null){
            showError("Error", "Debe ingresar una OT válida y registrada en el sistema.");
            return;
        }

        controles = null;
        mostrarTabla = true;
    }

    public void editarRegistro(ControlLiberacion controlLiberacion){
        controlSeleccionado = controlLiberacion;
        mostrarMain = false;
        mostrarTabla = false;
        mostrarDetalleRegistro = true;
    }

    public void eliminarRegistro(ControlLiberacion controlLiberacion){
        if(controlLiberacionDAO.delete(controlLiberacion)){
            showInfo("Aviso", "El control se eliminó correctamente.");

            controles = null;
        }
    }

    public void agregarRegistro(){
        controlSeleccionado = new ControlLiberacion();
        controlSeleccionado.setFecha(fecha);
        controlSeleccionado.setFechaLiberacion(fecha);

        // Renderiando detalle.
        mostrarMain = false;
        mostrarDetalleRegistro = true;
    }
    public void guardarRegistro(){
        if(controlSeleccionado.getFechaLiberacion() == null){
            showError("Error", "Debe ingresar fecha de liberación para guardar un control de liberación.");
            return;
        }

        if(controlSeleccionado.getId() != null){
            controlLiberacionDAO.update(controlSeleccionado);
            showInfo("Aviso", "Se editó de forma satisfactoria el control de liberación");

            mostrarMain = true;
            mostrarTabla = true;
            mostrarDetalleRegistro = false;

            controles = null;
            return;
        }

        controlSeleccionado.setOrdenDeTrabajoId(ordenDeTrabajoId);
        ControlLiberacion controlLiberacion = controlLiberacionDAO.create(controlSeleccionado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showInfo("Aviso", "Se creo de forma satisfactoria el registro asociado a la fecha " + sdf.format(this.fecha));

        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void limpiar(){
        controlSeleccionado = new ControlLiberacion();
    }

    public void atrasRegistro(){
        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isMostrarTabla() {
        return mostrarTabla;
    }

    public ControlLiberacionLazyDataModel getControles() {
        if ( controles == null )
            controles = new ControlLiberacionLazyDataModel(controlLiberacionDAO, ordenDeTrabajoId, fecha);
        return controles;
    }

    public void setControles(ControlLiberacionLazyDataModel controles) {
        this.controles = controles;
    }

    public ControlLiberacion getControlSeleccionado() {
        return controlSeleccionado;
    }

    public void setControlSeleccionado(ControlLiberacion controlSeleccionado) {
        this.controlSeleccionado = controlSeleccionado;
    }

    public boolean isMostrarDetalleRegistro() {
        return mostrarDetalleRegistro;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

}

