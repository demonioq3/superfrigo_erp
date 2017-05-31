package cl.superfrigo.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by Blueprints on 4/7/2015.
 */
public class BaseBean {

    protected void addMessage(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(title, message));
    }

    protected void showInfo(String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
    }

    protected void showError(String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
    }

    protected void showWarn(String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, message));
    }

    protected void showFatal(String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, title, message));
    }

    protected void showException(Exception e) {
        if ( e == null ) {
            showFatal("Error", "Ocurrió una excepción inesperada.");
        } else {
            String message = e.getMessage();
            Throwable throwable = e.getCause();
            if (message == null) {
                showFatal("Error: " + e.getClass().getSimpleName(), throwable != null ? throwable.getMessage() : "Excepción inesperada: " + e);
            } else {
                showFatal("Error: " + e.getClass().getSimpleName(), message);
            }
        }
    }

}
