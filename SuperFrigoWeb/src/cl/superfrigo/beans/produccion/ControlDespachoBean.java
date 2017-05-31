package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.dao.RegistroHHDAO;
import cl.superfrigo.dao.TrabajadorHHDAO;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import cl.superfrigo.entity.registros_hh.TrabajadorHH;
import cl.superfrigo.model.ControlDespachosLazyDataModel;
import cl.superfrigo.model.RegistroHHLazyDataModel;
import cl.superfrigo.model.TrabajadorHHLazyDataModel;

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
public class ControlDespachoBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long ordenDeTrabajoId;
    // Fecha seleccionada en la vista.
    private Date fecha;

    // Tabla lazy de controles.
    private ControlDespachosLazyDataModel controles;

    // Control seleccionado para ser editado.
    private ControlDespacho controlSeleccionado;

    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;    // Por defecto es falso.
    public boolean mostrarDetalleRegistro = false;

    @EJB private ControlDespachoDAO controlDespachoDAO;
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

    public void editarRegistro(ControlDespacho controlDespacho){
        controlSeleccionado = controlDespacho;
        mostrarMain = false;
        mostrarTabla = false;
        mostrarDetalleRegistro = true;
    }

    public void eliminarRegistro(ControlDespacho controlDespacho){
        if(controlDespachoDAO.delete(controlDespacho)){
            showInfo("Aviso", "El control se eliminó correctamente.");

            controles = null;
        }
    }

    public void agregarRegistro(){
        controlSeleccionado = new ControlDespacho();
        controlSeleccionado.setFechaDespacho(fecha);

        // Renderiando detalle.
        mostrarMain = false;
        mostrarDetalleRegistro = true;
    }
    public void guardarRegistro(){
        if(controlSeleccionado.getNumeroGuia() == null || controlSeleccionado.getNumeroGuia() == 0){
            showError("Error", "Debe ingresar número de guía para guardar un control de despacho.");
            return;
        }

        if(controlSeleccionado.getId() != null){
            controlDespachoDAO.update(controlSeleccionado);
            showInfo("Aviso", "Se editó de forma satisfactoria el control de despacho");

            mostrarMain = true;
            mostrarTabla = true;
            mostrarDetalleRegistro = false;

            controles = null;
            return;
        }

        controlSeleccionado.setOrdenDeTrabajoId(ordenDeTrabajoId);
        ControlDespacho controlDespacho = controlDespachoDAO.create(controlSeleccionado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showInfo("Aviso", "Se creo de forma satisfactoria el registro asociado a la fecha " + sdf.format(this.fecha));

        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void limpiar(){
        controlSeleccionado = new ControlDespacho();
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

    public ControlDespachosLazyDataModel getControles() {
        if ( controles == null )
            controles = new ControlDespachosLazyDataModel(controlDespachoDAO, ordenDeTrabajoId, fecha);
        return controles;
    }

    public void setControles(ControlDespachosLazyDataModel controles) {
        this.controles = controles;
    }

    public ControlDespacho getControlSeleccionado() {
        return controlSeleccionado;
    }

    public void setControlSeleccionado(ControlDespacho controlSeleccionado) {
        this.controlSeleccionado = controlSeleccionado;
    }

    public boolean isMostrarDetalleRegistro() {
        return mostrarDetalleRegistro;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

}

