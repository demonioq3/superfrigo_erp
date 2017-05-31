package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.ControlFacturacionDAO;
import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.dao.TipoFichaAuxiliarDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.produccion.ControlFacturacion;
import cl.superfrigo.model.ControlDespachosLazyDataModel;
import cl.superfrigo.model.ControlFacturacionLazyDataModel;
import org.primefaces.context.RequestContext;

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
public class ControlFacturacionBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long ordenDeTrabajoId;
    // Fecha seleccionada en la vista.
    private Date fecha;

    // Tabla lazy de controles.
    private ControlFacturacionLazyDataModel controles;

    // Control seleccionado para ser editado.
    private ControlFacturacion controlSeleccionado;

    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;    // Por defecto es falso.
    public boolean mostrarDetalleRegistro = false;

    @EJB private ControlFacturacionDAO controlFacturacionDAO;
    @EJB private OrdenDeTrabajoDAO ordenDeTrabajoDAO;
    @EJB private FichaAuxiliarDAO fichaAuxiliarDAO;
    @EJB private TipoFichaAuxiliarDAO tipoFichaAuxiliarDAO;


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

    public void editarRegistro(ControlFacturacion controlFacturacion){
        controlSeleccionado = controlFacturacion;
        mostrarMain = false;
        mostrarTabla = false;
        mostrarDetalleRegistro = true;
    }

    public void eliminarRegistro(ControlFacturacion controlFacturacion){
        if(controlFacturacionDAO.delete(controlFacturacion)){
            showInfo("Aviso", "El control se eliminó correctamente.");

            controles = null;
        }
    }

    public void agregarRegistro(){
        controlSeleccionado = new ControlFacturacion();
        controlSeleccionado.setFecha(fecha);
        controlSeleccionado.setFechaEmision(fecha);
        controlSeleccionado.setFichaAuxiliar(new FichaAuxiliar());

        // Renderiando detalle.
        mostrarMain = false;
        mostrarDetalleRegistro = true;
    }
    public void guardarRegistro(){
        if(controlSeleccionado.getNumero_factura() == null || controlSeleccionado.getNumero_factura() == 0){
            showError("Error", "Debe ingresar número de factura para guardar un control de facturación.");
            return;
        }

        if(controlSeleccionado.getId() != null){
            controlFacturacionDAO.update(controlSeleccionado);
            showInfo("Aviso", "Se editó de forma satisfactoria el control de facturación");

            mostrarMain = true;
            mostrarTabla = true;
            mostrarDetalleRegistro = false;

            controles = null;
            return;
        }

        controlSeleccionado.setOrdenDeTrabajoId(ordenDeTrabajoId);
        controlSeleccionado.setFichaAuxiliar(null);
        ControlFacturacion controlFacturacion = controlFacturacionDAO.create(controlSeleccionado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showInfo("Aviso", "Se creo de forma satisfactoria el registro asociado a la fecha " + sdf.format(this.fecha));

        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void limpiar(){
        controlSeleccionado = new ControlFacturacion();
    }

    public void atrasRegistro(){
        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void buscarPorRutCliente(){
        FichaAuxiliar ficha = fichaAuxiliarDAO.findByRut(this.controlSeleccionado.getFichaAuxiliar().getRut().replace(".", ""));

        if(ficha == null){
            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            showInfo("Aviso", "No existe cliente asociado al rut " + this.controlSeleccionado.getFichaAuxiliar().getRut());
        } else {
            if(ficha.getTipos().contains(tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.CLIENTE))){
                this.controlSeleccionado.getFichaAuxiliar().setNombreRazonSocial(ficha.getNombreRazonSocial());
                this.controlSeleccionado.setFichaAuxiliarId(ficha.getId());
            } else {
                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                showInfo("Aviso", "El rut buscado está guardado, pero no es de tipo cliente");
            }
        }
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

    public ControlFacturacionLazyDataModel getControles() {
        if ( controles == null )
            controles = new ControlFacturacionLazyDataModel(controlFacturacionDAO, ordenDeTrabajoId, fecha);
        return controles;
    }

    public void setControles(ControlFacturacionLazyDataModel controles) {
        this.controles = controles;
    }

    public ControlFacturacion getControlSeleccionado() {
        return controlSeleccionado;
    }

    public void setControlSeleccionado(ControlFacturacion controlSeleccionado) {
        this.controlSeleccionado = controlSeleccionado;
    }

    public boolean isMostrarDetalleRegistro() {
        return mostrarDetalleRegistro;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

}

