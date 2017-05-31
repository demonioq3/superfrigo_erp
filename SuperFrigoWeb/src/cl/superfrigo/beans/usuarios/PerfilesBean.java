package cl.superfrigo.beans.usuarios;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.CapacidadDAO;
import cl.superfrigo.dao.PerfilDAO;
import cl.superfrigo.dao.UsuarioDAO;
import cl.superfrigo.entity.Capacidad;
import cl.superfrigo.entity.Perfil;
import cl.superfrigo.entity.Usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blueprints on 8/19/2015.
 */

@ManagedBean(name="perfilesBean")
@ViewScoped
public class PerfilesBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /* Views Objects */
    public List<Perfil> Perfils; // all Perfils created.
    public Perfil selectItem; // Current selected item to edit or create.
    private List<Capacidad> selectCapacities; // Current selected capacites by user.

    /* Many Checkbox on edit values */
    private List<String> capacities;
    private List<String> perfil;
    private String manyCheckboxText = "Seleccionar todo";

    private boolean showMain;

    /* EJB Objects*/
    @EJB
    private PerfilDAO perfilDAO;

    @EJB
    private CapacidadDAO capacidadDAO;

    @EJB
    private UsuarioDAO usuarioDAO;


    @PostConstruct
    public void init(){
        Perfils = new ArrayList<Perfil>();
        selectCapacities = new ArrayList<Capacidad>();
        showMain = true;
        capacities = new ArrayList<String>();
    }

    public void newPerfil(){
        selectItem = new Perfil();
        showMain = false;
    }

    /**
     * This method return all Perfils
     * @return <code>List</code> of Perfils;
     */
    public List<Perfil> getAllPerfils() {
        Perfils = perfilDAO.findAll();
        return Perfils;
    }

    public void delete(Perfil perfil) {
        boolean errors = false;
        List<Usuario> users = usuarioDAO.getUserByPerfil(perfil.getId());

        if (users==null || users.size()>0){
            showError("Error","El perfil se encuentra asociado a \"" +users.size() + "\" usuario(s)"); errors=true;}

        if (errors)return;;


        if(this.perfilDAO.delete(perfil)){
            showInfo("Aviso", "Se eliminó el perfil \"" + perfil.getNombre() + "\" correctamente.");
        } else {
            showError("Error", "Hubo un error al eliminar el perfil \"" + perfil.getNombre() + "\"");
        }

        Perfils = null;
    }

    public void save() {
        boolean error = false;

        this.selectCapacities = new ArrayList<Capacidad>();
        if (this.selectItem.getNombre().equals("") || this.selectItem.getNombre()==null){
            showError("Error","Debe indicar el NOMBRE del perfil");error=true;}

        if (capacities.size() <= 0){
            showError("Error","Debe asignar una funcionalidad como mínino, al perfil");error=true;}

        if (error) return;

        if(capacities.size() > 0){
            for (String Capacidad : capacities) {
                Capacidad cap = capacidadDAO.getByName(Capacidad);
                this.selectCapacities.add(cap);
            }
        }
        if ( this.selectItem.getId() == null ) {
            // Create id to the new Perfil.
            Perfil Perfil = this.perfilDAO.create(this.selectItem);
            // Assign the capacities to the created Perfil.
            Perfil.setCapacities(this.selectCapacities);
            Perfil PerfilWithCapacites = perfilDAO.update(Perfil);

            showInfo("Aviso","El perfil \"" + Perfil.getNombre() + "\" se guardó correctamente");
            Perfils = null;
        } else {
            this.selectItem.setCapacities(this.selectCapacities);
            this.perfilDAO.update(this.selectItem);
            showInfo("Aviso","El perfil \"" + selectItem.getNombre() + "\" se guardó correctamente");
        }

        selectItem = new Perfil();
        showMain = true;
    }

    public List<Capacidad> getAllCapacities() {
        List<Capacidad> capacities = capacidadDAO.findAll();
        return capacities;
    }

    public Perfil getSelectItem() {
        if(selectItem == null){
            selectItem = new Perfil();
        }
        return selectItem;
    }

    public void close(){
        showMain = true;
    }

    public void setSelectItem(Perfil selectItem) {
        this.selectItem = selectItem;
        this.capacities = new ArrayList<String>();

        for (Capacidad cap : selectItem.getCapacities()) {
            capacities.add(cap.getNombre());
        }

        showMain = false;
    }

    public void selectAllCheckbox(){
        if(manyCheckboxText.equals("Seleccionar todo")){
            List<Capacidad> CapacidadList = capacidadDAO.findAll();
            this.capacities = new ArrayList<String>();
            for (Capacidad Capacidad : CapacidadList) {
                this.capacities.add(Capacidad.getNombre());
            }

            manyCheckboxText = "Quitar todo";
        } else {
            this.capacities = new ArrayList<String>();
            manyCheckboxText = "Seleccionar todo";
        }
    }

    public List<Capacidad> getSelectCapacities() {
        return selectCapacities;
    }

    public void setSelectCapacities(List<Capacidad> selectCapacities) {
        this.selectCapacities = selectCapacities;
    }

    public boolean isShowMain() {
        return showMain;
    }

    public void setShowMain(boolean showMain) {
        this.showMain = showMain;
    }

    public List<String> getCapacities() {
        return capacities;
    }
    public void setCapacities(List<String> capacities) {
        this.capacities = capacities;
    }

    public String getManyCheckboxText() {
        return manyCheckboxText;
    }

    public void setManyCheckboxText(String manyCheckboxText) {
        this.manyCheckboxText = manyCheckboxText;
    }

    public List<String> getPerfil() {
        return perfil;
    }
}

