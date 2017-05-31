package cl.superfrigo.beans.registrohh;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import cl.superfrigo.entity.registros_hh.TrabajadorHH;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.RegistroHHLazyDataModel;
import cl.superfrigo.model.TrabajadorHHLazyDataModel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class RegistroHHBean extends BaseBean implements Serializable {

    // Area seleccionada en la vista.
    private Long areaId;
    // Fecha seleccionada en la vista.
    private Date fecha;

    // Tabla lazy de registros.
    private RegistroHHLazyDataModel registrosHH;

    // Registro seleccionado para ser editado.
    private RegistroHH registroSeleccionado;

    // Lista lazy de trabajadores,
    private TrabajadorHHLazyDataModel trabajadoresHH;

    // Boolean para renderizar elementos de la vista.
    public boolean mostrarMain = true;
    public boolean mostrarTabla = false;    // Por defecto es falso.
    public boolean mostrarDetalleRegistro = false;

    @EJB RegistroHHDAO registroHHDAO;
    @EJB TrabajadorHHDAO trabajadorHHDAO;


    public void buscar(){
        if(areaId == 0 || areaId == null || fecha == null){
            showError("Error", "Debe ingresar área y fecha para realizar la búsqueda.");
            return;
        }

        registrosHH = null;
        mostrarTabla = true;
    }

    public void editarRegistro(RegistroHH registroHH){
        registroSeleccionado = registroHH;
        mostrarMain = false;
        mostrarTabla = false;
        mostrarDetalleRegistro = true;
    }

    public void eliminarRegistro(RegistroHH registroHH){
        if(registroHHDAO.delete(registroHH)){
            showInfo("Aviso", "El registro se eliminó correctamente.");

            registrosHH = null;
        }
    }

    public void agregarRegistro(){
        registroSeleccionado = new RegistroHH();
        registroSeleccionado.setFecha(fecha);
        registroSeleccionado.setTrabajadorHH(new TrabajadorHH());

        // Renderiando detalle.
        mostrarMain = false;
        mostrarDetalleRegistro = true;
    }

    public void seleccionarTrabajador(TrabajadorHH trabajadorHH){
        registroSeleccionado.setTrabajadorHH(trabajadorHH);
        registroSeleccionado.setTrabajadorHHId(trabajadorHH.getId());

        registroSeleccionado.setHh(null);
        registroSeleccionado.setCentroDeCostoId(null);
        registroSeleccionado.setOrdenDeTrabajoId(null);
    }

    public void guardarRegistro(){
        if(registroSeleccionado.getTrabajadorHH().getId() == null){
            showError("Error", "Debe ingresar un trabajador para guardar un registro.");
            return;
        }

        if(registroSeleccionado.getHh() == null || registroSeleccionado.getHh() == 0){
            showError("Error", "Debe ingresar la hora hombre (HH) para guardar un registro.");
            return;
        }

        if(registroSeleccionado.getId() != null){
            registroSeleccionado.setAreaHHId(registroSeleccionado.getTrabajadorHH().getAreaHHId());
            registroSeleccionado.setTrabajadorHH(null);

            registroHHDAO.update(registroSeleccionado);
            showInfo("Aviso", "Se editó de forma satisfactoria el registro");

            mostrarMain = true;
            mostrarTabla = true;
            mostrarDetalleRegistro = false;

            registrosHH = null;
            return;
        }

        registroSeleccionado.setAreaHHId(registroSeleccionado.getTrabajadorHH().getAreaHHId());
        registroSeleccionado.setTrabajadorHH(null);
        RegistroHH registroHH = registroHHDAO.create(registroSeleccionado);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showInfo("Aviso", "Se creo de forma satisfactoria el registro asociado a la fecha " + sdf.format(this.fecha));

        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public void limpiar(){
        registroSeleccionado = new RegistroHH();
        registroSeleccionado.setTrabajadorHH(new TrabajadorHH());
    }

    public void atrasRegistro(){
        mostrarMain = true;
        mostrarTabla = true;
        mostrarDetalleRegistro = false;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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

    public RegistroHHLazyDataModel getRegistrosHH() {
        if ( registrosHH == null )
            registrosHH = new RegistroHHLazyDataModel(registroHHDAO, areaId, fecha);
        return registrosHH;
    }

    public void setRegistrosHH(RegistroHHLazyDataModel registrosHH) {
        this.registrosHH = registrosHH;
    }

    public RegistroHH getRegistroSeleccionado() {
        return registroSeleccionado;
    }

    public void setRegistroSeleccionado(RegistroHH registroSeleccionado) {
        this.registroSeleccionado = registroSeleccionado;
    }

    public boolean isMostrarDetalleRegistro() {
        return mostrarDetalleRegistro;
    }

    public boolean isMostrarMain() {
        return mostrarMain;
    }

    public TrabajadorHHLazyDataModel getTrabajadoresHH() {
        if ( trabajadoresHH == null )
            trabajadoresHH = new TrabajadorHHLazyDataModel(trabajadorHHDAO);
        return trabajadoresHH;
    }

    public void setTrabajadoresHH(TrabajadorHHLazyDataModel trabajadoresHH) {
        this.trabajadoresHH = trabajadoresHH;
    }
}

