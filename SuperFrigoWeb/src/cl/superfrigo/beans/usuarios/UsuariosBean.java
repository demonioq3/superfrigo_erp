package cl.superfrigo.beans.usuarios;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.UsuarioDAO;
import cl.superfrigo.entity.Usuario;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class UsuariosBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private UsuarioDAO usuarioDAO;


    public void guardar(){
        // Check if profile is selected.
        if(this.usuarioSeleccionado.getPerfilId() == null){
            showError("Error", "Debe ingresar un perfil para guardar un usuario.");
        }

        if(usuarioSeleccionado.getId() == null){
            Usuario usuarioCreado = usuarioDAO.create(usuarioSeleccionado);
            showInfo("Aviso", "Se creó correctamente el usuario " + usuarioCreado.getNombres() + " " + usuarioCreado.getApellidos() + ".");
        } else {
            usuarioDAO.update(usuarioSeleccionado);
            showInfo("Aviso", "Se editó correctamente el usuario " + usuarioSeleccionado.getNombres() + " " + usuarioSeleccionado.getApellidos() + ".");
        }

        showMain = true;
        usuarios = null;
    }

    public void borrar(Usuario usuario){
        if(usuarioDAO.delete(usuario)){
            showInfo("Aviso", "El usuario " + usuario.getNombres() + " " + usuario.getApellidos() + " se eliminó correctamente.");
        } else {
            showError("Error", "Hubo un error al eliminar el usuario.");
        }

        usuarios = null;
    }

    public List<Usuario> getAllUsuarios(){
        return usuarioDAO.findAll();
    }

    public void atras(){
        showMain = true;
    }

    public void newUsuario(){
        usuarioSeleccionado = new Usuario();
        showMain = false;
    }

    public List<Usuario> getUsuarios() {
        if(usuarios == null){
            usuarios = usuarioDAO.findAll();
        }
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
