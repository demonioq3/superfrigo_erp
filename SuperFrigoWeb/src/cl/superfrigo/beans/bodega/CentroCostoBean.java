package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.CentroDeCostoDAO;
import cl.superfrigo.dao.UnidadMedidaDAO;
import cl.superfrigo.entity.abastecimiento.CentroDeCosto;
import cl.superfrigo.entity.bodega.UnidadMedida;
import org.hibernate.exception.ConstraintViolationException;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class CentroCostoBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<CentroDeCosto> centros;
    private CentroDeCosto centroSeleccionado;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private CentroDeCostoDAO centroDeCostoDAO;


    public void guardar(){
        if(centroSeleccionado.getId() == null){
            // Ver si el código de grupo está utilizado.
            if(centroDeCostoDAO.existe(this.centroSeleccionado.getCodigo())){
                showError("Error", "El código ingresado ya está registrado. Favor intentar con otro.");
                return;
            }

            CentroDeCosto centroCreado = centroDeCostoDAO.create(centroSeleccionado);

            showInfo("Aviso", "Se creó correctamente el centro de costos con código: " + centroCreado.getCodigo() + ".");
        } else {
            centroDeCostoDAO.update(centroSeleccionado);
            showInfo("Aviso", "Se editó correctamente el centro de costos con código: " + centroSeleccionado.getCodigo() + ".");
        }

        showMain = true;
        centros = null;
    }

    public void borrar(CentroDeCosto centroDeCosto){
        try {
            if(centroDeCostoDAO.delete(centroDeCosto)){
                showInfo("Aviso", "El centro de costos código: " + centroDeCosto.getCodigo() + " se eliminó correctamente.");
            } else {
                showError("Error", "Hubo un error al eliminar el centro de costos.");
            }

            centros = null;
        } catch (EJBTransactionRolledbackException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                // Chequeando si arroja la excepción de violación de llave foranea.
                showError("Error", "No se puede eliminar el centro de costos ya que está asociado a otro elemento de la base de datos..");
            }
        }

    }

    public List<CentroDeCosto> getAllCentros(){
        return centroDeCostoDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newCentro(){
        centroSeleccionado = new CentroDeCosto();
        showMain = false;
    }

    public List<CentroDeCosto> getCentros() {
        if(centros == null){
            centros = centroDeCostoDAO.findAll();
        }
        return centros;
    }

    public void setCentros(List<CentroDeCosto> centros) {
        this.centros = centros;
    }

    public CentroDeCosto getCentroSeleccionado() {
        return centroSeleccionado;
    }

    public void setCentroSeleccionado(CentroDeCosto centroSeleccionado) {
        this.centroSeleccionado = centroSeleccionado;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
