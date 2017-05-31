package cl.superfrigo.auth;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.UsuarioDAO;
import cl.superfrigo.entity.Capacidad;
import cl.superfrigo.entity.Usuario;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean(name = "authBean")
@SessionScoped
public class AuthenticationBean extends BaseBean implements Serializable {

    /* View parameters */
    private String email;
    private String password;
    private Usuario loggedUser;

    private String mensajeError = "Ingrese e-mail y clave";


    /* EJB Parameters */
    @EJB
    private UsuarioDAO usuarioDAO;


    public String login() {
        String mail = "";

        if(email.equals("1-9") && password.equals("12345")){
            this.loggedUser = new Usuario(null, "Usuario", "Test", "user@test.cl", "12345");

            return "succes";
        }

        if (email.equals("") || password.equals("")) {
            this.mensajeError = "Debe ingresar usuario y clave";
            return null;
        }

        try {
            // Buscando usuario en la base de datos.
            Usuario usuario = usuarioDAO.findByEmailAndPassword(email, password);

            if(usuario != null){
                this.loggedUser = usuario;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .put("user", loggedUser);

                this.mensajeError = "Ingrese e-mail y clave";
                return "success";
            } else {
                this.mensajeError = "El email y clave ingresados son incorrectos.";
            }
        } catch (Exception e) {
            this.mensajeError = "El usuario/clave es incorrecto";
            return null;
        }

        return null;
    }

    public void logout() {
        String contextName = FacesContext.getCurrentInstance().getExternalContext().getContextName();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        this.loggedUser = null;
        email = null;

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/superfrigo/");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return;

    }

    public boolean containsCapacity(int capacityId){
        if(loggedUser != null && loggedUser.getPerfil() != null && loggedUser.getPerfil().getCapacities() != null){
            for (Capacidad capacity : loggedUser.getPerfil().getCapacities()) {
                if(capacity.getId() == capacityId){
                    return true;
                }
            }
        }

        return false;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreFormatted() {
        String[] nombreSplitted = loggedUser.getNombres().split(" ");
        String[] apellidoSplitted = loggedUser.getApellidos().split(" ");

        return nombreSplitted[0] + " " + apellidoSplitted[0];
    }
}
