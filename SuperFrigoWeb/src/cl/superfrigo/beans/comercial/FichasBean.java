package cl.superfrigo.beans.comercial;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.ComunaDAO;
import cl.superfrigo.dao.TipoFichaAuxiliarDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.Comuna;
import cl.superfrigo.entity.TipoFichaAuxiliar;

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
public class FichasBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<FichaAuxiliar> fichaAuxiliars;
    private FichaAuxiliar fichaAuxiliarSeleccionado;

    /* Selección de region*/
    private List<Comuna> comunas;

    /* Tipos seleccionados */
    private List<String> tipos;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private FichaAuxiliarDAO fichaAuxiliarDAO;

    @EJB
    private ComunaDAO comunaDAO;

    @EJB
    private TipoFichaAuxiliarDAO tipoFichaAuxiliarDAO;


    public void guardar(){
        if(fichaAuxiliarSeleccionado.getId() == null){
            List<TipoFichaAuxiliar> tiposList = new ArrayList<TipoFichaAuxiliar>();

            // Asociando el/los tipo a la ficha.
            tiposList = asociarTipos(tiposList);
            fichaAuxiliarSeleccionado.setTipos(tiposList);

            FichaAuxiliar fichaAuxiliarCreado = fichaAuxiliarDAO.create(fichaAuxiliarSeleccionado);
            showInfo("Aviso", "Se creó correctamente el cliente rut: " + fichaAuxiliarCreado.getRut() + ".");
        } else {
            List<TipoFichaAuxiliar> tiposList = new ArrayList<TipoFichaAuxiliar>();

            // Asociando el/los tipo a la ficha.
            tiposList = asociarTipos(tiposList);
            fichaAuxiliarSeleccionado.setTipos(tiposList);

            fichaAuxiliarDAO.update(fichaAuxiliarSeleccionado);
            showInfo("Aviso", "Se editó correctamente el cliente rut: " + fichaAuxiliarSeleccionado.getRut() + ".");
        }

        showMain = true;
        fichaAuxiliars = null;
    }

    private List<TipoFichaAuxiliar> asociarTipos(List<TipoFichaAuxiliar> tiposList) {
        for (String tipo : tipos) {
            if(tipo.equals("Cliente")){
                TipoFichaAuxiliar tipoCliente = tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.CLIENTE);
                tiposList.add(tipoCliente);
            }
            if(tipo.equals("Proveedor")){
                TipoFichaAuxiliar tipoProveedor = tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.PROVEEDOR);
                tiposList.add(tipoProveedor);
            }
        }

        return tiposList;
    }

    public void borrar(FichaAuxiliar fichaAuxiliar){
        if(fichaAuxiliarDAO.delete(fichaAuxiliar)){
            showInfo("Aviso", "El fichaAuxiliar rut: " + fichaAuxiliar.getRut() + " se eliminó correctamente.");
        } else {
            showError("Error", "Hubo un error al eliminar el fichaAuxiliar.");
        }

        fichaAuxiliars = null;
    }

    public void seleccionarComunasDeRegion(){
        comunas = comunaDAO.findByPadre(this.fichaAuxiliarSeleccionado.getRegionId());
    }

    public List<FichaAuxiliar> getAllClientes(){
        return fichaAuxiliarDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newCliente(){
        fichaAuxiliarSeleccionado = new FichaAuxiliar();
        fichaAuxiliarSeleccionado.setTipos(null);

        // Deseleccionando los tipos anteriormente seleccionados.
        tipos = new ArrayList<String>();
        showMain = false;
    }

    public List<FichaAuxiliar> getFichaAuxiliars() {
        if(fichaAuxiliars == null){
            fichaAuxiliars = fichaAuxiliarDAO.findAll();
        }
        return fichaAuxiliars;
    }

    public void setFichaAuxiliars(List<FichaAuxiliar> fichaAuxiliars) {
        this.fichaAuxiliars = fichaAuxiliars;
    }

    public FichaAuxiliar getFichaAuxiliarSeleccionado() {
        return fichaAuxiliarSeleccionado;
    }

    public void setFichaAuxiliarSeleccionado(FichaAuxiliar fichaAuxiliarSeleccionado) {
        tipos = new ArrayList<String>();
        this.fichaAuxiliarSeleccionado = fichaAuxiliarSeleccionado;

        for (TipoFichaAuxiliar tipoFichaAuxiliar : fichaAuxiliarSeleccionado.getTipos()) {
            if(tipoFichaAuxiliar.getId().equals(TipoFichaAuxiliar.CLIENTE)){
                tipos.add("Cliente");
            } else if(tipoFichaAuxiliar.getId().equals(TipoFichaAuxiliar.PROVEEDOR)){
                tipos.add("Proveedor");
            }
        }

        comunas = comunaDAO.findByPadre(this.fichaAuxiliarSeleccionado.getRegionId());
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }

    public List<Comuna> getComunas() {
        return comunas;
    }

    public void setComunas(List<Comuna> comunas) {
        this.comunas = comunas;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }
}
