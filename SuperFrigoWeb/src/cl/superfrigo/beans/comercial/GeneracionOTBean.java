package cl.superfrigo.beans.comercial;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaJasperObject;
import cl.superfrigo.beans.bodega.utils.GuiaSalidaObject;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.*;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.SalidaProductoCantidad;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.entity.bodega.TipoFormulario;
import cl.superfrigo.entity.comercial.*;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import cl.superfrigo.model.ProveedoresLazyDataModel;
import cl.superfrigo.model.RecetasLazyDataModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 19-Mar-16.
 */
@ManagedBean(name = "generacionOTBean")
@ViewScoped
public class GeneracionOTBean extends BaseBean implements Serializable {

    // OT Creada al ingresar por defecto.
    private OrdenDeTrabajo ot;
    private boolean readOnlyOT = true;
    private String placeHolderOT = "El número de OT es auto-generado al hacer click en el botón guardar.";   // Corresponde al mensaje que se muestra en el input de ingreso de OT.
    private String mensajeNuevaAntiguaOT = "busqueda por ot";
    private FichasProductoLazyDataModel fichasProducto;

    // Productos asociados a la cotizacion
    private List<ProductoOrdenTrabajo> productos;

    /* Lista lazy de proveedores (búsqueda) */
    private ProveedoresLazyDataModel proveedores;

    // Recetas asociadas a un producto de la OT;
    private RecetasLazyDataModel recetasByProducto;
    private String currentRecetaCodigoProducto;
    private ProductoOrdenTrabajo currentProducto;

    // Parámetros de búsqueda.
    private String rutBusqueda;
    private boolean deshabilitarRut = false;

    // Comunas de region seleccionada;
    private List<Comuna> comunas;

    // Persistencia
    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    @EJB
    private FichaAuxiliarDAO fichaAuxiliarDAO;

    @EJB
    private RegionDAO regionDAO;

    @EJB
    private ComunaDAO comunaDAO;

    @EJB
    private FichaProductoDAO fichaProductoDAO;

    @EJB
    private RecetaDAO recetaDAO;

    @EJB
    private ProductoOrdenTrabajoDAO productoOrdenTrabajoDAO;

    @EJB
    private PanelFormDAO panelFormDAO;

    @EJB
    private MolduraFormDAO molduraFormDAO;

    @EJB
    private PuertaFormDAO puertaFormDAO;

    @EJB
    private CamaraFormDAO camaraFormDAO;

    @EJB
    private RefrigeracionFormDAO refrigeracionFormDAO;


    @PostConstruct
    public void init(){
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if(req.getParameterMap().containsKey("cotizacion")){
            Long idCotizacion = Long.parseLong((String) req.getParameter("cotizacion"));
            ot = ordenDeTrabajoDAO.findById(idCotizacion);
            productos = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ot.getId());
            if(productos == null)
                productos = new ArrayList<ProductoOrdenTrabajo>();

            comunas = comunaDAO.findByPadre(ot.getFichaAuxiliar().getRegionId());
        }

        if(ot == null){
            ot = new OrdenDeTrabajo();
            ot.setFecha(new Date());
            FichaAuxiliar nuevoFichaAuxiliar = new FichaAuxiliar();
            ot.setFichaAuxiliar(nuevoFichaAuxiliar);
            productos = new ArrayList<ProductoOrdenTrabajo>();
        } else {
            mensajeNuevaAntiguaOT = "nueva ot";
        }

    }

    public void buscarPorRut(){
        FichaAuxiliar fichaAuxiliar = fichaAuxiliarDAO.findByRut(this.ot.getFichaAuxiliar().getRut().replace(".", ""));

        if(fichaAuxiliar == null){
            showInfo("Aviso", "El fichaAuxiliar RUT: " + rutBusqueda + " no está registrado. Debes ingresar los datos del fichaAuxiliar manualmente.");
            deshabilitarRut = true;
        } else {
            ot.setFichaAuxiliar(fichaAuxiliar);
            ot.setClienteId(fichaAuxiliar.getId());

            comunas = comunaDAO.findByPadre(fichaAuxiliar.getRegionId());
            deshabilitarRut = true;
        }

    }

    public void buscarPorOTClick(){
        if(mensajeNuevaAntiguaOT.equals("busqueda por ot")){
            placeHolderOT = "Inserte el número de OT y presione enter.";
            readOnlyOT = false;
            mensajeNuevaAntiguaOT = "nueva ot";
            this.ot = new OrdenDeTrabajo();
            this.ot.setFichaAuxiliar(new FichaAuxiliar());
        } else if( mensajeNuevaAntiguaOT.equals("nueva ot")){
            placeHolderOT = "El número de OT es auto-generado al hacer click en el botón guardar.";
            readOnlyOT = true;
            mensajeNuevaAntiguaOT = "busqueda por ot";
            this.ot = new OrdenDeTrabajo();
            this.ot.setFecha(new Date());
            this.ot.setFichaAuxiliar(new FichaAuxiliar());
        }
    }

    public void buscarPorOt(){
        if(this.ot.getId() == null){
            showError("Error", "Debe ingresar numero de OT para realizar la búsqueda.");
            return;
        }

        OrdenDeTrabajo ot = ordenDeTrabajoDAO.findById(this.ot.getId());

        if(ot == null){
            showError("Error", "No existe la OT N° " + this.ot.getId());
            return;
        }

        this.ot = ot;
        this.productos = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ot.getId());
        comunas = comunaDAO.findByPadre(this.ot.getFichaAuxiliar().getRegionId());
    }

    public void nuevaBusquedaRut(){
        ot.setClienteId(null);
        ot.setFichaAuxiliar(new FichaAuxiliar());
        rutBusqueda = "";

        deshabilitarRut = false;
    }

    public void guardarOT(){
        if(this.ot.getFichaAuxiliar().getRegionId() == null){
            showError("Error", "Debe ingresar la región y comuna. Es un campo obligatorio");
            return;
        } else if(this.ot.getFichaAuxiliar().getComunaId() == null){
            showError("Error", "Debe ingresar la región y comuna. Es un campo obligatorio");
            return;
        }

        if(this.ot.getId() == null){
            this.ot.setEstado(null);
            this.ot.setEstadoId(EstadoCotizacion.EN_ESTUDIO);

            FichaAuxiliar fichaAuxiliarCreada = null;

            if(this.ot.getFichaAuxiliar().getId() == null) {
                FichaAuxiliar fichaAuxiliar = fichaAuxiliarDAO.findByRut(this.ot.getFichaAuxiliar().getRut().replace(".", ""));

                if(fichaAuxiliar != null){
                    showError("Error", "El rut del cliente ya está registrado en la base de datos. Para seleccionarlo, debe ingresar el rut y hacer click en ENTER para cargar su información");
                    return;
                } else {
                    FichaAuxiliar fichaAuxiliarToSave = this.ot.getFichaAuxiliar();
                    fichaAuxiliarToSave.setId(null);
                    fichaAuxiliarCreada = fichaAuxiliarDAO.create(fichaAuxiliarToSave);
                }
            }

            this.ot.setFichaAuxiliar(null);

            if(fichaAuxiliarCreada != null)
                this.ot.setClienteId(fichaAuxiliarCreada.getId());

            OrdenDeTrabajo ot = ordenDeTrabajoDAO.create(this.ot);

            try{
                for (ProductoOrdenTrabajo producto : productos) {
                    if(producto.getFichaProducto().getTipoFormularioId() != null && producto.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.PANELES)){
                        PanelForm panelForm = producto.getPanelForm();
                        PanelForm panelFormCreated = panelFormDAO.create(panelForm);

                        producto.setOrdenDeTrabajoId(ot.getId());
                        producto.setPanelForm(null);
                        producto.setPanelFormId(panelFormCreated.getId());
                    } else if(producto.getFichaProducto().getTipoFormularioId() != null && producto.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.MOLDURAS)){
                        MoldeduraForm moldeduraForm = producto.getMoldeduraForm();
                        MoldeduraForm molduraFormCreated = molduraFormDAO.create(moldeduraForm);

                        producto.setOrdenDeTrabajoId(ot.getId());
                        producto.setMoldeduraForm(null);
                        producto.setMoldeduraFormId(molduraFormCreated.getId());
                    } else if(producto.getFichaProducto().getTipoFormularioId() != null && producto.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.PUERTAS)){
                        PuertaForm puertaForm = producto.getPuertaForm();
                        PuertaForm puertaFormCreated = puertaFormDAO.create(puertaForm);

                        producto.setOrdenDeTrabajoId(ot.getId());
                        producto.setPuertaForm(null);
                        producto.setPuertaFormId(puertaFormCreated.getId());
                    } else if(producto.getFichaProducto().getTipoFormularioId() != null && producto.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.CAMARAS)){
                        CamaraForm camaraForm = producto.getCamaraForm();
                        CamaraForm camaraFormCreated = camaraFormDAO.create(camaraForm);

                        producto.setOrdenDeTrabajoId(ot.getId());
                        producto.setCamaraForm(null);
                        producto.setCamaraFormId(camaraFormCreated.getId());
                    } else if(producto.getFichaProducto().getTipoFormularioId() != null && producto.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.EQUIPOS_REFRIGERACION)){
                        RefrigeracionForm refrigeracionForm = producto.getRefrigeracionForm();
                        RefrigeracionForm refrigeracionFormCreated = refrigeracionFormDAO.create(refrigeracionForm);

                        producto.setOrdenDeTrabajoId(ot.getId());
                        producto.setRefrigeracionForm(null);
                        producto.setRefrigeracionFormId(refrigeracionFormCreated.getId());
                    }

                    producto.setOrdenDeTrabajoId(ot.getId());
                    productoOrdenTrabajoDAO.create(producto);
                }
            } catch (Exception e){
                showError("Error", "Hubo un error al crear los productos asociados a la OT");
                RequestContext.getCurrentInstance().scrollTo("main-content");
                return;
            }

            showInfo("Aviso", "Se creó de manera correcta la cotizacion. El código de la cotizacion es el :" + ot.getId());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            refreshPage();
        } else {
            // Editando una OT.
            List<ProductoOrdenTrabajo> nuevaListaProductos = productos;

            for (ProductoOrdenTrabajo nuevaListaProducto : nuevaListaProductos) {
                nuevaListaProducto.setId(null);
            }

            OrdenDeTrabajo ordenDeTrabajoBD = ordenDeTrabajoDAO.findById(this.ot.getId());
            List<ProductoOrdenTrabajo> list = productoOrdenTrabajoDAO.findByOrdenTrabajoId(ordenDeTrabajoBD.getId());
            if(list != null && list.size()>0)
                ordenDeTrabajoBD.setProductos(list);
            else
                ordenDeTrabajoBD.setProductos(new ArrayList<ProductoOrdenTrabajo>());

            // Se revisa si se editó le ficha auxiliar.
            if(this.ot.getClienteId() != null){
                fichaAuxiliarDAO.update(this.ot.getFichaAuxiliar());
            }

            // Se eliminan los actuales productos persistidos
            for (ProductoOrdenTrabajo productoParaEliminar : ordenDeTrabajoBD.getProductos()) {
                if(productoParaEliminar.getPanelForm() != null){
                    PanelForm panel = panelFormDAO.getById(productoParaEliminar.getPanelFormId());
                    ProductoOrdenTrabajo productoOrdenTrabajo = productoOrdenTrabajoDAO.findById(productoParaEliminar.getId());
                    productoOrdenTrabajoDAO.delete(productoOrdenTrabajo);
                    panelFormDAO.delete(panel);
                } else if(productoParaEliminar.getMoldeduraForm() != null){
                    MoldeduraForm moldura = molduraFormDAO.getById(productoParaEliminar.getMoldeduraFormId());
                    ProductoOrdenTrabajo productoOrdenTrabajo = productoOrdenTrabajoDAO.findById(productoParaEliminar.getId());
                    productoOrdenTrabajoDAO.delete(productoOrdenTrabajo);
                    molduraFormDAO.delete(moldura);
                } else if(productoParaEliminar.getCamaraForm() != null){
                    CamaraForm camara = camaraFormDAO.getById(productoParaEliminar.getCamaraFormId());
                    ProductoOrdenTrabajo productoOrdenTrabajo = productoOrdenTrabajoDAO.findById(productoParaEliminar.getId());
                    productoOrdenTrabajoDAO.delete(productoOrdenTrabajo);
                    camaraFormDAO.delete(camara);
                } else if(productoParaEliminar.getPuertaForm() != null){
                    PuertaForm puerta = puertaFormDAO.getById(productoParaEliminar.getPuertaFormId());
                    ProductoOrdenTrabajo productoOrdenTrabajo = productoOrdenTrabajoDAO.findById(productoParaEliminar.getId());
                    productoOrdenTrabajoDAO.delete(productoOrdenTrabajo);
                    puertaFormDAO.delete(puerta);
                } else if(productoParaEliminar.getRefrigeracionForm() != null){
                    RefrigeracionForm refrigeracion = refrigeracionFormDAO.getById(productoParaEliminar.getRefrigeracionFormId());
                    ProductoOrdenTrabajo productoOrdenTrabajo = productoOrdenTrabajoDAO.findById(productoParaEliminar.getId());
                    productoOrdenTrabajoDAO.delete(productoOrdenTrabajo);
                    refrigeracionFormDAO.delete(refrigeracion);
                }
            }


            // Se agregan los nuevos productos de la vista.
            for (ProductoOrdenTrabajo productoParaGuardar : nuevaListaProductos) {
                productoParaGuardar.setOrdenDeTrabajoId(this.ot.getId());

                if(productoParaGuardar.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.PANELES)){
                    PanelForm panelFormToCreate = productoParaGuardar.getPanelForm();
                    panelFormToCreate.setId(null);
                    PanelForm panelFormCreated = panelFormDAO.create(panelFormToCreate);

                    productoParaGuardar.setPanelForm(null);
                    productoParaGuardar.setPanelFormId(panelFormCreated.getId());
                    productoOrdenTrabajoDAO.create(productoParaGuardar);
                } else if(productoParaGuardar.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.MOLDURAS)){
                    MoldeduraForm molduraFormToCreate = productoParaGuardar.getMoldeduraForm();
                    molduraFormToCreate.setId(null);
                    MoldeduraForm molduraFromCreated = molduraFormDAO.create(molduraFormToCreate);

                    productoParaGuardar.setMoldeduraForm(null);
                    productoParaGuardar.setMoldeduraFormId(molduraFromCreated.getId());
                    productoOrdenTrabajoDAO.create(productoParaGuardar);
                } else if(productoParaGuardar.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.PUERTAS)){
                    PuertaForm puertaFormToCreate = productoParaGuardar.getPuertaForm();
                    puertaFormToCreate.setId(null);
                    PuertaForm puertaFormCreated = puertaFormDAO.create(puertaFormToCreate);

                    productoParaGuardar.setPuertaForm(null);
                    productoParaGuardar.setPuertaFormId(puertaFormCreated.getId());
                    productoOrdenTrabajoDAO.create(productoParaGuardar);
                } else if(productoParaGuardar.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.CAMARAS)){
                    CamaraForm camaraFormToCreate = productoParaGuardar.getCamaraForm();
                    camaraFormToCreate.setId(null);
                    CamaraForm camaraFromCreated = camaraFormDAO.create(camaraFormToCreate);

                    productoParaGuardar.setCamaraForm(null);
                    productoParaGuardar.setCamaraFormId(camaraFromCreated.getId());
                    productoOrdenTrabajoDAO.create(productoParaGuardar);
                } else if(productoParaGuardar.getFichaProducto().getTipoFormularioId().equals(TipoFormulario.EQUIPOS_REFRIGERACION)){
                    RefrigeracionForm refrigeracionFormToCreate = productoParaGuardar.getRefrigeracionForm();
                    refrigeracionFormToCreate.setId(null);
                    RefrigeracionForm refrigeracionFormCreated = refrigeracionFormDAO.create(refrigeracionFormToCreate);

                    productoParaGuardar.setRefrigeracionForm(null);
                    productoParaGuardar.setRefrigeracionFormId(refrigeracionFormCreated.getId());
                    productoOrdenTrabajoDAO.create(productoParaGuardar);
                }

            }

            ordenDeTrabajoDAO.update(this.ot);

            showInfo("Aviso", "Se editó de manera correcta la cotizacion " + ot.getId());

            // Move to top
            RequestContext.getCurrentInstance().scrollTo("main-content");
            refreshPage();
        }
    }

    private void refreshPage() {
        ot = new OrdenDeTrabajo();
        ot.setFecha(new Date());
        FichaAuxiliar nuevoFichaAuxiliar = new FichaAuxiliar();
        ot.setFichaAuxiliar(nuevoFichaAuxiliar);
        productos = new ArrayList<ProductoOrdenTrabajo>();
    }

    public void enviarParaEvaluacionComercial(){
        this.ot.setEstado(null);
        this.ot.setEstadoId(EstadoCotizacion.ESPERA_APROBACION_COMERCIAL);

        try{
            ordenDeTrabajoDAO.update(this.ot);
        } catch (Exception e){
            showError("Error", "Hubo un error al enviar la cotización a aprobación comercial.");
        }

        // Move to top
        RequestContext.getCurrentInstance().scrollTo("main-content");
        showInfo("Aviso", "Se envió la cotización a aprobación comercial correctamente.");
    }

    public void seleccionarComunasDeRegion(){
        comunas = comunaDAO.findByPadre(this.ot.getFichaAuxiliar().getRegionId());
    }

    public List<Region> getTodasLasRegiones(){
        return regionDAO.findAll();
    }

    public void agregarProducto(FichaProducto fichaProducto){
        ProductoOrdenTrabajo nuevoProducto = new ProductoOrdenTrabajo();
        nuevoProducto.setFichaProductoId(fichaProducto.getId());
        nuevoProducto.setFichaProducto(fichaProducto);

        Receta recetaBase = recetaDAO.findRecetaBaseByCodigoProducto(fichaProducto.getCodigoProducto());
        Double precioUnitarioReceta= 0D;
        if(recetaBase != null){
            nuevoProducto.setReceta(recetaBase);
            nuevoProducto.setRecetaId(recetaBase.getId());

            for (ProductoReceta productoReceta : recetaBase.getProductosAsociados()) {
                precioUnitarioReceta += productoReceta.getValor();
            }

        }

        // Cantidad inicial por defecto
        nuevoProducto.setCantidad(1D);

        // Porcentaje de descuento por defecto es 0;
        nuevoProducto.setPorcentajeDescuento(0D);

        nuevoProducto.setPrecioUnitario(precioUnitarioReceta);
        nuevoProducto.setPrecioTotal(nuevoProducto.getCantidad() * precioUnitarioReceta);

        // Se crea entidad formulario asociado al producto.
        if(fichaProducto.getTipoFormularioId() != null && fichaProducto.getTipoFormularioId().equals(TipoFormulario.CAMARAS)){
            CamaraForm camaraForm = new CamaraForm();
            nuevoProducto.setCamaraForm(camaraForm);
        }

        if(fichaProducto.getTipoFormularioId() != null && fichaProducto.getTipoFormularioId().equals(TipoFormulario.MOLDURAS)){
            MoldeduraForm moldeduraForm = new MoldeduraForm();
            nuevoProducto.setMoldeduraForm(moldeduraForm);
        }

        if(fichaProducto.getTipoFormularioId() != null && fichaProducto.getTipoFormularioId().equals(TipoFormulario.PANELES)){
            PanelForm panelForm = new PanelForm();
            nuevoProducto.setPanelForm(panelForm);
        }

        if(fichaProducto.getTipoFormularioId() != null && fichaProducto.getTipoFormularioId().equals(TipoFormulario.PUERTAS)){
            PuertaForm puertaForm = new PuertaForm();
            nuevoProducto.setPuertaForm(puertaForm);
        }

        if(fichaProducto.getTipoFormularioId() != null && fichaProducto.getTipoFormularioId().equals(TipoFormulario.EQUIPOS_REFRIGERACION)){
            RefrigeracionForm refrigeracionForm = new RefrigeracionForm();
            nuevoProducto.setRefrigeracionForm(refrigeracionForm);
        }

        productos.add(nuevoProducto);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void buscarReceta(ProductoOrdenTrabajo productoOrdenTrabajo){
        currentRecetaCodigoProducto = productoOrdenTrabajo.getFichaProducto().getCodigoProducto();
        currentProducto = productoOrdenTrabajo;
        recetasByProducto = null;
    }

    public void seleccionarReceta(Receta receta){
        productos.remove(currentProducto);
        currentProducto.setReceta(receta);
        productos.add(currentProducto);
    }

    public void seleccionarProveedor(FichaAuxiliar ficha){
        this.ot.setFichaAuxiliar(ficha);
        this.ot.setClienteId(ficha.getId());

        this.comunas = comunaDAO.findByPadre(this.ot.getFichaAuxiliar().getRegionId());

        showInfo("Aviso", "Se agregó el proveedor a la cotización de forma correcta.");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void eliminarProducto(ProductoOrdenTrabajo productoOrdenTrabajo){
        this.productos.remove(productoOrdenTrabajo);
        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void cambiarReceta(ProductoOrdenTrabajo productoOrdenTrabajo, Long recetaId){
        Receta receta = recetaDAO.getById(recetaId);

        Double total = 0D;
        if(receta.getProductosAsociados().size() > 0){
            for (ProductoReceta productoReceta : receta.getProductosAsociados()) {
                total += productoReceta.getValor() * productoReceta.getCantidad();
            }
        }

        productoOrdenTrabajo.setPrecioUnitario(total);
        productoOrdenTrabajo.setReceta(receta);
        productoOrdenTrabajo.setRecetaId(receta.getId());

        productoOrdenTrabajo.setPrecioTotal(productoOrdenTrabajo.getCantidad() * productoOrdenTrabajo.getPrecioUnitario());
    }

    public void imprimirGuiaPDF() {
        try {
            Locale locale = new Locale("es","CL");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> parametros = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            //parametros.put("centro_costo", (this.editItem.getCentroDeCosto() == null) ? "N/A" : this.editItem.getCentroDeCosto().getNombre());
            parametros.put("n_cotizacion", this.ot.getId().toString());
            parametros.put("clientes", this.ot.getFichaAuxiliar().getNombreRazonSocial());
            parametros.put("fecha", sdf.format(this.ot.getFecha()));

            parametros.put("forma_pago", "N/A");
            parametros.put("plazo_entrega", "N/A");


            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("jasper_cotizacion.jasper"));

            GuiaSalidaJasperObject cb = new GuiaSalidaJasperObject();
            List<GuiaSalidaObject> productosFormatted = new ArrayList<GuiaSalidaObject>();

            double total_neto = 0;
            if(this.productos != null && this.productos.size() > 0){
                for (ProductoOrdenTrabajo producto : this.productos) {
                    GuiaSalidaObject nuevo = new GuiaSalidaObject();
                    nuevo.setId(producto.getId().toString());
                    nuevo.setCodigo(producto.getFichaProducto().getCodigoProducto());
                    nuevo.setDescripcion(producto.getFichaProducto().getDescripcion());
                    nuevo.setCantidad(producto.getCantidad().toString());
                    nuevo.setPunitario(producto.getPrecioUnitario().toString());
                    nuevo.setTotal(producto.getPrecioTotal().toString());
                    nuevo.setUmedida(producto.getFichaProducto().getUnidadMedida().getNombre());

                    productosFormatted.add(nuevo);
                    total_neto += total_neto + producto.getPrecioTotal();
                }
            }

            parametros.put("total_neto", "$ " + total_neto);
            double iva = total_neto * 0.19;
            parametros.put("iva", "$ " + iva);
            double total = total_neto + iva;
            parametros.put("total", "$ " + total);

            cb.setProductos(productosFormatted);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(cb), false);

            byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, dataSource);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=inf_cotizacion_impr.pdf\"");
            response.setContentLength(bytes.length);
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
            outStream.close();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Receta> recetasPorCodigoProducto(ProductoOrdenTrabajo producto){
        return recetaDAO.findByCodigoProducto(producto.getFichaProducto().getCodigoProducto());
    }

    public void cambiarCantidadProducto(ProductoOrdenTrabajo productoOrdenTrabajo){
        productoOrdenTrabajo.setPrecioTotal(productoOrdenTrabajo.getCantidad() * productoOrdenTrabajo.getPrecioUnitario());
    }

    public void cambiarDescuentoProducto(ProductoOrdenTrabajo productoOrdenTrabajo){
        /* TODO */
    }

    public OrdenDeTrabajo getOt() {
        return ot;
    }

    public String getRutBusqueda() {
        return rutBusqueda;
    }

    public void setRutBusqueda(String rutBusqueda) {
        this.rutBusqueda = rutBusqueda;
    }

    public List<Comuna> getComunas() {
        return comunas;
    }

    public boolean isDeshabilitarRut() {
        return deshabilitarRut;
    }

    public String getPlaceHolderOT() {
        return placeHolderOT;
    }

    public void setPlaceHolderOT(String placeHolderOT) {
        this.placeHolderOT = placeHolderOT;
    }

    public boolean isReadOnlyOT() {
        return readOnlyOT;
    }

    public String getMensajeNuevaAntiguaOT() {
        return mensajeNuevaAntiguaOT;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public List<ProductoOrdenTrabajo> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenTrabajo> productos) {
        this.productos = productos;
    }

    public RecetasLazyDataModel getRecetasByProducto() {
        if ( recetasByProducto == null )
            recetasByProducto = new RecetasLazyDataModel(recetaDAO, currentRecetaCodigoProducto);
        return recetasByProducto;
    }

    public void setRecetasByProducto(RecetasLazyDataModel recetasByProducto) {
        this.recetasByProducto = recetasByProducto;
    }

    public String getCurrentRecetaCodigoProducto() {
        return currentRecetaCodigoProducto;
    }

    public void setCurrentRecetaCodigoProducto(String currentRecetaCodigoProducto) {
        this.currentRecetaCodigoProducto = currentRecetaCodigoProducto;
    }

    public ProductoOrdenTrabajo getCurrentProducto() {
        return currentProducto;
    }

    public void setCurrentProducto(ProductoOrdenTrabajo currentProducto) {
        this.currentProducto = currentProducto;
    }

    public ProveedoresLazyDataModel getProveedores() {
        if ( proveedores == null )
            proveedores = new ProveedoresLazyDataModel(fichaAuxiliarDAO, TipoFichaAuxiliar.PROVEEDOR);
        return proveedores;
    }

    public void setProveedores(ProveedoresLazyDataModel proveedores) {
        this.proveedores = proveedores;
    }
}
