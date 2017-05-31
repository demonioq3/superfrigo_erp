package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.PrecioProductoDAO;
import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.abastecimiento.PrecioProducto;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionListener;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 29-Feb-16.
 */
@ManagedBean(name = "preciosProductosBean")
@ViewScoped
public class PreciosProductosBean extends BaseBean implements Serializable {

    /* Variables de vista */
    private List<PrecioProducto> precios;
    private PrecioProducto editItem;

    /* Rendered de fragment*/
    private boolean showMain = true;


    @EJB
    private PrecioProductoDAO precioProductoDAO;


    public void guardar(){
        precioProductoDAO.update(editItem);
        showInfo("Aviso", "Se editó correctamente el precio del producto : " + editItem.getFichaProducto().getCodigoProducto() + ".");


        showMain = true;
        precios = null;
    }

    public void atras(){
        showMain = true;
    }

    public void setProducto(PrecioProducto precioProducto){
        editItem = precioProducto;
        showMain = false;
    }

    public List<PrecioProducto> getPrecios() {
        if(precios == null){
            precios = precioProductoDAO.findAll();
        }
        return precios;
    }

    public void setBodegas(List<PrecioProducto> precios) {
        this.precios = precios;
    }

    public PrecioProducto getEditItem() {
        return editItem;
    }

    public void setEditItem(PrecioProducto precioProducto) {
        this.editItem = precioProducto;
        showMain = false;
    }

    public boolean isShowMain() {
        return showMain;
    }
}
