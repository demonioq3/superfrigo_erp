package cl.superfrigo.beans.abastecimiento.converter;

import cl.superfrigo.beans.abastecimiento.RequisicionesBean;
import cl.superfrigo.dao.CentroDeCostoDAO;
import cl.superfrigo.entity.abastecimiento.CentroDeCosto;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by asgco on 14-Apr-16.
 */
@FacesConverter("centroCostoConverter")
public class CentroCostoConverter implements Converter {

    @EJB
    public CentroDeCostoDAO centroDeCostoDAO;

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                centroDeCostoDAO = new CentroDeCostoDAO();
                return centroDeCostoDAO.findAll().get(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((CentroDeCosto) object).getId());
        }
        else {
            return null;
        }
    }
}
