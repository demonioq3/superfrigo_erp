package cl.superfrigo.beans.registrohh;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;
import cl.superfrigo.entity.bodega.FichaProducto;
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
public class MantenedorHHBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<DefinicionHH> definiciones;
    private DefinicionHH definicionSeleccionada;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB private DefinicionHHDAO definicionHHDAO;
    @EJB private AreaHHDAO areaHHDAO;
    @EJB private EstadoHHDAO estadoHHDAO;


    public void guardar(){
        if(definicionSeleccionada.getId() == null){
            // Ver si el tipo de hh está utilizado.
            if(definicionHHDAO.existe(this.definicionSeleccionada.getTipoHH())){
                showError("Error", "El tipo HH ya está registrado. Favor intentar con otro.");
                return;
            }

            deletingZeros();

            DefinicionHH definicionCreada = definicionHHDAO.create(definicionSeleccionada);

            showInfo("Aviso", "Se creó correctamente la definición HH de tipo: " + definicionSeleccionada.getTipoHH() + ".");
        } else {
            deletingZeros();

            definicionHHDAO.update(definicionSeleccionada);
            showInfo("Aviso", "Se editó correctamente la definición HH de tipo: " + definicionSeleccionada.getTipoHH() + ".");
        }

        showMain = true;
        definiciones = null;
    }

    private void deletingZeros() {
        if(this.definicionSeleccionada.getValor() == 0L){
            this.definicionSeleccionada.setValor(null);
        }

        if(this.definicionSeleccionada.getAreaHHId() == 0L){
            this.definicionSeleccionada.setAreaHHId(null);
        }
    }

    public void borrar(DefinicionHH definicionHH){
        try {
            if(definicionHHDAO.delete(definicionHH)){
                showInfo("Aviso", "La definición HH de tipo: " + definicionHH.getTipoHH() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar la definición.");
            }

            definiciones = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "La definición no se puede eliminar ya que está siendo referenciado por otro elemento de la base de datos.");
            }
        }
    }

    public List<DefinicionHH> getAllDefiniciones(){
        return definicionHHDAO.findAll();
    }

    public List<AreaHH> getAllAreasHH() {
        return areaHHDAO.findAll();
    }

    public List<EstadoHH> getAllEstadosHH() {
        return estadoHHDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newDefinicion(){
        definicionSeleccionada = new DefinicionHH();
        definicionSeleccionada.setFecha(new Date());
        showMain = false;
    }

    public List<DefinicionHH> getDefiniciones() {
        if(definiciones == null){
            definiciones = definicionHHDAO.findAll();
        }
        return definiciones;
    }

    public void setDefiniciones(List<DefinicionHH> definiciones) {
        this.definiciones = definiciones;
    }

    public DefinicionHH getDefinicionSeleccionada() {
        return definicionSeleccionada;
    }

    public void setDefinicionSeleccionada(DefinicionHH definicionSeleccionada) {
        this.definicionSeleccionada = definicionSeleccionada;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }

}

