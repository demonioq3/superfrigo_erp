package cl.superfrigo.beans.registrohh;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.abastecimiento.PrecioProducto;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.registros_hh.AreaHH;
import cl.superfrigo.entity.registros_hh.EstadoHH;
import cl.superfrigo.entity.registros_hh.TrabajadorHH;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.TrabajadorHHLazyDataModel;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class FichaTrabajadoresBean extends BaseBean implements Serializable {

    // Trabajador seleccionado actualmente en la vista.
    private TrabajadorHH trabajadorSeleccionado;

    // Lista lazy de trabajadores.
    private TrabajadorHHLazyDataModel trabajadoresHH;

    // Boolean para saber si el trabajador se puede guardar.
    private Boolean sePuedeGuardar = false;

    // Boolean para mostrar botón de eliminar.
    private Boolean mostrarBotonEliminar = false;

    // Persistencia
    @EJB private AreaHHDAO areaHHDAO;
    @EJB private TrabajadorHHDAO trabajadorHHDAO;


    @PostConstruct
    public void init(){
        trabajadorSeleccionado = new TrabajadorHH();
        trabajadorSeleccionado.setEstadoHHId(EstadoHH.ESTADO_VIGENTE);
    }

    public void guardar(){
        if(this.trabajadorSeleccionado.getNombres() == null || this.trabajadorSeleccionado.getNombres().equals("")){
            showError("Error", "Debe ingresar el/los nombres asociados al trabajador.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        if(this.trabajadorSeleccionado.getApellidos() == null || this.trabajadorSeleccionado.getApellidos().equals("")){
            showError("Error", "Debe ingresar el/los apellidos asociados al trabajador.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        if(!validarRut(this.trabajadorSeleccionado.getRut())){
            showError("Error", "Debe ingresar un rut válido.");

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            return;
        }

        if(!sePuedeGuardar){
            // Validacion: Validando que el RUT no exista anteriormente.
            TrabajadorHH trabajadorExistente = trabajadorHHDAO.findByRut(this.trabajadorSeleccionado.getRut());
            if(trabajadorExistente != null){
                showError("Error", "El rut del trabajador ya está registrado. Para editarlo, debes realizar la búsqueda por rut y modificarlo.");

                // Move to top
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }
        }

        if(this.trabajadorSeleccionado.getId() == null){

            TrabajadorHH trabajador = trabajadorHHDAO.create(this.trabajadorSeleccionado);

            showInfo("Aviso", "Se creó de manera correcta la ficha de trabajador. El RUT del trabajador creado es el " + this.trabajadorSeleccionado.getRut() + ".");

            // Después del mensaje...
            // Se limpia el formulario cuando se crea un producto
            this.trabajadorSeleccionado = new TrabajadorHH();
            // Ya no se puede guardar.
            sePuedeGuardar = false;
        } else {

            sePuedeGuardar = true;
            trabajadorHHDAO.update(this.trabajadorSeleccionado);
            showInfo("Aviso", "Se editó de manera correcta la ficha de trabajador rut: " + this.trabajadorSeleccionado.getRut() + "");
        }

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
    }

    public void eliminar(){
        if(trabajadorHHDAO.delete(this.trabajadorSeleccionado)){

            showInfo("Aviso", "Se eliminó correctamente la ficha de trabajador rut: " + this.trabajadorSeleccionado.getRut());

            mostrarBotonEliminar = false;

            this.trabajadorSeleccionado = new TrabajadorHH();
        } else {
            showError("Error", "Hubo un error al eliminar la ficha de trabajador.");
        }

        this.trabajadorSeleccionado = new TrabajadorHH();
    }

    public void limpiar(){
        if(this.trabajadorSeleccionado != null){
            this.trabajadorSeleccionado = new TrabajadorHH();

            if(mostrarBotonEliminar == true){
                mostrarBotonEliminar = false;
            }
        }
    }

    public void seleccionarTrabajador(TrabajadorHH trabajadorHH){
        trabajadorSeleccionado = trabajadorHH;

        // Mostrar el botón eliminar.
        mostrarBotonEliminar = true;

        // Se puede guardar.
        sePuedeGuardar = true;

        showInfo("Aviso", "Se cargó el trabajador " + this.trabajadorSeleccionado.getNombres() + " de forma correcta.");
    }

    public List<AreaHH> getAllAreasHH(){
        return areaHHDAO.findAll();
    }

    public TrabajadorHH getTrabajadorSeleccionado() {
        return trabajadorSeleccionado;
    }

    public void setTrabajadorSeleccionado(TrabajadorHH trabajadorSeleccionado) {
        this.trabajadorSeleccionado = trabajadorSeleccionado;
    }

    public Boolean getMostrarBotonEliminar() {
        return mostrarBotonEliminar;
    }

    public void setMostrarBotonEliminar(Boolean mostrarBotonEliminar) {
        this.mostrarBotonEliminar = mostrarBotonEliminar;
    }

    public TrabajadorHHLazyDataModel getTrabajadoresHH() {
        if ( trabajadoresHH == null )
            trabajadoresHH = new TrabajadorHHLazyDataModel(trabajadorHHDAO);
        return trabajadoresHH;
    }

    public void setTrabajadoresHH(TrabajadorHHLazyDataModel trabajadoresHH) {
        this.trabajadoresHH = trabajadoresHH;
    }



    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return validacion;
    }
}
