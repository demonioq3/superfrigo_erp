package cl.superfrigo.beans.produccion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.bodega.GuiaSalida;
import cl.superfrigo.entity.bodega.SalidaProductoCantidad;
import cl.superfrigo.entity.comercial.EstadoCotizacion;
import cl.superfrigo.entity.comercial.EstadoOrdenTrabajo;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.comercial.ProductoOrdenTrabajo;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.registros_hh.DefinicionHH;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import cl.superfrigo.model.ControlDespachosLazyDataModel;
import cl.superfrigo.model.ControlFacturacionLazyDataModel;
import cl.superfrigo.model.ControlLiberacionLazyDataModel;
import cl.superfrigo.model.ControlProgramacionLazyDataModel;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.primefaces.event.TabChangeEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class OrdenTrabajoBean extends BaseBean implements Serializable {

    // Id de orden de trabajo.
    private Long ordenDeTrabajoId;

    // Render de datos obtenidos de una orden de trabajo.
    private boolean mostrarDatosOT = false;

    // OT Buscada;
    private OrdenDeTrabajo ot;

    // Productos asociados a la OT.
    private List<ProductoOrdenTrabajo> productos;

    // Posición del tab.
    private Integer indexTab = 0;

    // Materiales consumidos
    private List<SalidaProductoCantidad> materialesConsumidos;

    // Registros HH
    private List<RegistroHH> registrosHH;

    // Mensuaje de actualizacion de estado;
    private String mensajeActualizacion = "";

    // Lista de controles asociadas a la OT.
    private ControlDespachosLazyDataModel controlesDespacho;
    private ControlLiberacionLazyDataModel controlesLiberacion;
    private ControlProgramacionLazyDataModel controlesProgramacion;
    private ControlFacturacionLazyDataModel controlesFacturacion;

    // EJB's de persistencia.
    @EJB private OrdenDeTrabajoDAO ordenDeTrabajoDAO;
    @EJB private ControlDespachoDAO controlDespachoDAO;
    @EJB private ControlLiberacionDAO controlLiberacionDAO;
    @EJB private ControlProgramacionDAO controlProgramacionDAO;
    @EJB private ControlFacturacionDAO controlFacturacionDAO;
    @EJB private ProductoOrdenTrabajoDAO productoOrdenTrabajoDAO;
    @EJB private GuiaSalidaDAO guiaSalidaDAO;
    @EJB private RegistroHHDAO registroHHDAO;
    @EJB private DefinicionHHDAO definicionHHDAO;
    @EJB private SalidaProductoCantidadDAO salidaProductoCantidadDAO;


    @PostConstruct
    public void init(){
        productos = new ArrayList<ProductoOrdenTrabajo>();

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if(req.getParameterMap().containsKey("ot")){
            Long idCotizacion = Long.parseLong((String) req.getParameter("ot"));
            ordenDeTrabajoId = idCotizacion;
            ot = ordenDeTrabajoDAO.findById(idCotizacion);
            productos = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ot.getId());
            if(productos == null)
                productos = new ArrayList<ProductoOrdenTrabajo>();

            mostrarDatosOT = true;

        }

    }

    public void buscar(){
        if(ordenDeTrabajoId == null || ordenDeTrabajoId == 0){
            showError("Error", "Debe ingresar OT para realizar la búsqueda.");
            ordenDeTrabajoId = null;
            return;
        }

        ot = ordenDeTrabajoDAO.findById(ordenDeTrabajoId);
        productos = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ordenDeTrabajoId);

        if(ot == null){
            showError("Error", "No existe la orden de trabajo buscada.");
            mostrarDatosOT = false;
        } else {
            if(!ot.getEstadoId().equals(EstadoCotizacion.APROBADA)){
                showError("Error", "El número ingresado corresponde a una cotización, por consiguiente, debe pasar los procesos de validación para ser definida como OT.");
                mostrarDatosOT = false;
                return;
            }
            mostrarDatosOT = true;
        }
    }

    public void onTabChange(TabChangeEvent event) {
        String tabTitle = event.getTab().getTitle(),
                tabClientId = event.getTab().getClientId(),
                tabId = event.getTab().getId();
        if (tabTitle.equals("Datos proveedor")) {
            indexTab = 0;
        }

        if (tabTitle.equals("Datos t&eacute;cnicos")) {
            indexTab = 1;
        }

    }

    public void cambiarEstado(){
        OrdenDeTrabajo updated = ordenDeTrabajoDAO.update(ot);
        if(updated.getEstadoOTId().equals(1L)){
            mensajeActualizacion = "La OT fue cambiada a estado vigente.";
        } else if(updated.getEstadoOTId().equals(2L)){
            mensajeActualizacion = "La OT fue cambiada a estado cerrada.";
        } else if(updated.getEstadoOTId().equals(3L)){
            mensajeActualizacion = "La OT fue cambiada a estado nula.";
        }

    }

    public Double getResumenValorOT(){
        return productoOrdenTrabajoDAO.findValorOTByOrdenDeTrabajoId(ordenDeTrabajoId);
    }

    public Double getResumenValorFacturado(){
        return controlFacturacionDAO.findValorFacturacionByOrdenDeTrabajoId(ordenDeTrabajoId);
    }

    public Double getResumenCostoMateriales(){
        List<GuiaSalida> guiaSalidas = guiaSalidaDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);

        Double valorTotal = 0D;
        for (GuiaSalida guiaSalida : guiaSalidas) {
            for (SalidaProductoCantidad salidaProductoCantidad : guiaSalida.getProductos()) {
                valorTotal *= salidaProductoCantidad.getPrecioTotal();
            }
        }

        return valorTotal;
    }

    public Long getResumenCostoHH(){
        List<RegistroHH> registros = registroHHDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);

        Long totalHH = 0L;
        for (RegistroHH registro : registros) {
            DefinicionHH definicionHH = definicionHHDAO.findByAreaHHId(registro.getAreaHHId());

            if(definicionHH != null){
                totalHH += registro.getHh() * definicionHH.getValor();
            }
        }

        return totalHH;
    }

    public String getPorcentajeCostos(){
        Double valorOT = productoOrdenTrabajoDAO.findValorOTByOrdenDeTrabajoId(ordenDeTrabajoId);

        List<RegistroHH> registros = registroHHDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);

        Long valorHH = 0L;
        for (RegistroHH registro : registros) {
            DefinicionHH definicionHH = definicionHHDAO.findByAreaHHId(registro.getAreaHHId());

            if(definicionHH != null){
                valorHH += registro.getHh() * definicionHH.getValor();
            }
        }

        List<GuiaSalida> guiaSalidas = guiaSalidaDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);

        Double valorMateriales = 0D;
        for (GuiaSalida guiaSalida : guiaSalidas) {
            for (SalidaProductoCantidad salidaProductoCantidad : guiaSalida.getProductos()) {
                valorMateriales *= salidaProductoCantidad.getPrecioTotal();
            }
        }

        Double response = 100 * (valorMateriales + valorHH)/valorOT;
        DecimalFormat df = new DecimalFormat("#.#");

        return df.format(response);
    }

    public boolean isMostrarDatosOT() {
        return mostrarDatosOT;
    }

    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public Integer getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(Integer indexTab) {
        this.indexTab = indexTab;
    }

    public OrdenDeTrabajo getOt() {
        return ot;
    }

    public void setOt(OrdenDeTrabajo ot) {
        this.ot = ot;
    }

    public ControlDespachosLazyDataModel getControlesDespacho() {
        controlesDespacho = new ControlDespachosLazyDataModel(controlDespachoDAO, ordenDeTrabajoId);
        return controlesDespacho;
    }

    public void setControlesDespacho(ControlDespachosLazyDataModel controlesDespacho) {
        this.controlesDespacho = controlesDespacho;
    }

    public ControlLiberacionLazyDataModel getControlesLiberacion() {
        controlesLiberacion = new ControlLiberacionLazyDataModel(controlLiberacionDAO, ordenDeTrabajoId);
        return controlesLiberacion;
    }

    public void setControlesLiberacion(ControlLiberacionLazyDataModel controlesLiberacion) {
        this.controlesLiberacion = controlesLiberacion;
    }

    public ControlProgramacionLazyDataModel getControlesProgramacion() {
        controlesProgramacion = new ControlProgramacionLazyDataModel(controlProgramacionDAO, ordenDeTrabajoId);
        return controlesProgramacion;
    }

    public void setControlesProgramacion(ControlProgramacionLazyDataModel controlesProgramacion) {
        this.controlesProgramacion = controlesProgramacion;
    }

    public ControlFacturacionLazyDataModel getControlesFacturacion() {
        controlesFacturacion = new ControlFacturacionLazyDataModel(controlFacturacionDAO, ordenDeTrabajoId);
        return controlesFacturacion;
    }

    public void setControlesFacturacion(ControlFacturacionLazyDataModel controlesFacturacion) {
        this.controlesFacturacion = controlesFacturacion;
    }

    public List<ProductoOrdenTrabajo> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenTrabajo> productos) {
        this.productos = productos;
    }

    public String getMensajeActualizacion() {
        return mensajeActualizacion;
    }

    public List<SalidaProductoCantidad> getMaterialesConsumidos() {
        materialesConsumidos = salidaProductoCantidadDAO.findByOrdenDeTrabajoIdAndConceptoConsumoOt(ordenDeTrabajoId);
        return materialesConsumidos;
    }

    public List<RegistroHH> getRegistrosHH() {
        registrosHH = registroHHDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);
        for (RegistroHH registroHH : registrosHH) {
            DefinicionHH definicionHH = definicionHHDAO.findByAreaHHId(registroHH.getAreaHHId());
            registroHH.setValor(definicionHH.getValor().doubleValue());
        }
        return registrosHH;
    }
}

