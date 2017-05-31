package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.ControlLiberacionDAO;
import cl.superfrigo.dao.ControlProgramacionDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.produccion.ControlLiberacion;
import cl.superfrigo.entity.produccion.ControlProgramacion;
import cl.superfrigo.model.ControlLiberacionLazyDataModel;
import cl.superfrigo.model.ControlProgramacionLazyDataModel;

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
public class ControlProgramacionBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long ordenDeTrabajoId;
    // Fecha seleccionada en la vista.
    private Date fecha;

    // Tabla lazy de controles.
    private ControlProgramacionLazyDataModel controles;

    // Control seleccionado para ser editado.
    private ControlProgramacion controlSeleccionado;

    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;    // Por defecto es falso.
    public boolean mostrarDetalleRegistro = false;

    @EJB private ControlProgramacionDAO controlProgramacionDAO;
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

    public void editarRegistro(ControlProgramacion controlProgramacion){
        controlSeleccionado = controlProgramacion;
        mostrarMain = false;
        mostrarTabla = false;
        mostrarDetalleRegistro = true;
    }

    public void eliminarRegistro(ControlProgramacion controlProgramacion){
        if(controlProgramacionDAO.delete(controlProgramacion)){
            showInfo("Aviso", "El control se eliminó correctamente.");

            controles = null;
        }
    }

    public void agregarRegistro(){
        controlSeleccionado = new ControlProgramacion();
        controlSeleccionado.setFecha(fecha);
        controlSeleccionado.setFechaEntrega(fecha);
        controlSeleccionado.setFechaReal(fecha);

        // Renderiando detalle.
        mostrarMain = false;
        mostrarDetalleRegistro = true;
    }
    public void guardarRegistro(){
        if(controlSeleccionado.getFechaEntrega() == null){
            showError("Error", "Debe ingresar fecha de entrega para guardar un control de programación.");
            return;
        }

        if(controlSeleccionado.getId() != null){
            controlProgramacionDAO.update(controlSeleccionado);
            showInfo("Aviso", "Se editó de forma satisfactoria el control de programación");

            mostrarMain = true;
            mostrarTabla = true;
            mostrarDetalleRegistro = false;

            controles = null;
            return;
        }

        controlSeleccionado.setOrdenDeTrabajoId(ordenDeTrabajoId);
        ControlProgramacion controlProgramacion = controlProgramacionDAO.create(controlSeleccionado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showInfo("Aviso", "Se creo de forma satisfactoria el registro asociado a la fecha " + sdf.format(this.fecha));

        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void limpiar(){
        controlSeleccionado = new ControlProgramacion();
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

    public ControlProgramacionLazyDataModel getControles() {
        if ( controles == null )
            controles = new ControlProgramacionLazyDataModel(controlProgramacionDAO, ordenDeTrabajoId, fecha);
        return controles;
    }

    public void setControles(ControlProgramacionLazyDataModel controles) {
        this.controles = controles;
    }

    public ControlProgramacion getControlSeleccionado() {
        return controlSeleccionado;
    }

    public void setControlSeleccionado(ControlProgramacion controlSeleccionado) {
        this.controlSeleccionado = controlSeleccionado;
    }

    public boolean isMostrarDetalleRegistro() {
        return mostrarDetalleRegistro;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

}

