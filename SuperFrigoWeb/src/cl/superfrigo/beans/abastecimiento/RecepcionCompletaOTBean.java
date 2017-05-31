package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class RecepcionCompletaOTBean extends BaseBean implements Serializable {

    /**
     * ID del proceso que se desa ejecutar.
     * 1: Corresponde a toma de inventario.
     * 2: Corresponde a Ajustar toma de inventario.
     */
    private Long idOpcion;

    /**
     * ID del tipo de toma inventario.
     * 1: Digitación de stock en forma manual.
     * 2: Captura en stock.
     */
    private Long idTipoTomaInventario;

    // Bodega ID
    private Long bodegaId;

    // Fecha seleccionada para la toma de inventario.
    private Date fecha;

    // Archivo excel subido para cargar en la tabla.
    private UploadedFile archivoExcelSubido;

    // Lista de productos lazy para agregar a la tabla.
    private FichasProductoLazyDataModel fichasProducto;

    // Productos asociados a una determinada toma de inventario
    private List<ProductoTomaInventario> productosTomaInventario;

    // Toma de inventario seleccionada para guardar o editar.
    private TomaInventario tomaInventario;

    /* Renders de fragments en la vista */
    private boolean mostrarPrimeraPantalla = true;
    private boolean mostrarSegundaPantalla = false;
    private boolean bloquearGoSegundaPantalla = true;

    // Pantalla de ajustar
    private Date fechaAjuste;
    private Long bodegaAjusteId;
    private List<ProductoTomaInventario> productosParaAjustar;

    // Pantalla de informe
    private Date fechaInforme;
    private Long bodegaInformeId;
    private List<String> tiposInforme;

    /* EJB's de persistencia */
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;
    @EJB private TomaInventarioDAO tomaInventarioDAO;
    @EJB private ProductoTomaInventarioDAO productotomaInventarioDAO;
    @EJB private BodegaDAO bodegaDAO;

    public void goSegundaPantalla(){
        if(this.idOpcion.equals(1L)){
            mostrarPrimeraPantalla = false;
            mostrarSegundaPantalla = true;
        } else if(this.idOpcion.equals(2L)){
            mostrarPrimeraPantalla = false;
            mostrarSegundaPantalla = true;
        }
    }

    /*
        Proceso ajax que revisa si seleccionaron un proceso.
     */
    public void chequearCambioProceso(){
        if(idOpcion.equals(1L) || idOpcion.equals(2L)){
            bloquearGoSegundaPantalla = false;
        } else {
            bloquearGoSegundaPantalla = true;
        }
    }

    public Long getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Long idOpcion) {
        this.idOpcion = idOpcion;
    }

    public boolean isMostrarPrimeraPantalla() {
        return mostrarPrimeraPantalla;
    }

    public boolean isMostrarSegundaPantalla() {
        return mostrarSegundaPantalla;
    }

    public boolean isBloquearGoSegundaPantalla() {
        return bloquearGoSegundaPantalla;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


}

