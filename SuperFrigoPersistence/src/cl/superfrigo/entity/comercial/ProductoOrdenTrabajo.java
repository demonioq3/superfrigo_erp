package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.ProductoRequisicion;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.proyectos.Receta;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "producto_orden_trabajo")
public class ProductoOrdenTrabajo extends BaseEntity implements Serializable {

    private Long id;
    private FichaProducto fichaProducto;
    private Long fichaProductoId;
    private OrdenDeTrabajo ordenDeTrabajo;
    private Long ordenDeTrabajoId;
    private Receta receta;
    private Long recetaId;
    private Double cantidad;
    private Double precioUnitario;
    private Double porcentajeDescuento;
    private Double precioTotal;
    private MoldeduraForm moldeduraForm;
    private Long moldeduraFormId;
    private PanelForm panelForm;
    private Long panelFormId;
    private PuertaForm puertaForm;
    private Long puertaFormId;
    private RefrigeracionForm refrigeracionForm;
    private Long refrigeracionFormId;
    private CamaraForm camaraForm;
    private Long camaraFormId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_orden_trabajo", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ficha_producto_id", referencedColumnName = "id_ficha_producto", nullable = false, insertable = false, updatable = false)
    public FichaProducto getFichaProducto() {
        return fichaProducto;
    }

    public void setFichaProducto(FichaProducto fichaProducto) {
        this.fichaProducto = fichaProducto;
    }

    @Basic
    @Column(name = "ficha_producto_id", nullable = false, insertable = true, updatable = true)
    public Long getFichaProductoId() {
        return fichaProductoId;
    }

    public void setFichaProductoId(Long fichaProductoId) {
        this.fichaProductoId = fichaProductoId;
    }

    @ManyToOne
    @JoinColumn(name = "orden_de_trabajo_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public OrdenDeTrabajo getOrdenDeTrabajo() {
        return ordenDeTrabajo;
    }

    public void setOrdenDeTrabajo(OrdenDeTrabajo ordenDeTrabajo) {
        this.ordenDeTrabajo = ordenDeTrabajo;
    }

    @Basic
    @Column(name = "orden_de_trabajo_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    @ManyToOne
    @JoinColumn(name = "receta_id", referencedColumnName = "id_receta", nullable = false, insertable = false, updatable = false)
    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    @Basic
    @Column(name = "receta_id", nullable = false, insertable = true, updatable = true)
    public Long getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(Long recetaId) {
        this.recetaId = recetaId;
    }

    @Basic
    @Column(name = "precio_unitario", nullable = false, insertable = true, updatable = true)
    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Basic
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "porcentaje_descuento", nullable = false, insertable = true, updatable = true)
    public Double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Basic
    @Column(name = "precio_total", nullable = false, insertable = true, updatable = true)
    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    @ManyToOne
    @JoinColumn(name = "form_moldedura_id", referencedColumnName = "id_form_moldedura", nullable = false, insertable = false, updatable = false)
    public MoldeduraForm getMoldeduraForm() {
        return moldeduraForm;
    }

    public void setMoldeduraForm(MoldeduraForm moldeduraForm) {
        this.moldeduraForm = moldeduraForm;
    }

    @Basic
    @Column(name = "form_moldedura_id", nullable = false, insertable = true, updatable = true)
    public Long getMoldeduraFormId() {
        return moldeduraFormId;
    }

    public void setMoldeduraFormId(Long moldeduraFormId) {
        this.moldeduraFormId = moldeduraFormId;
    }

    @ManyToOne
    @JoinColumn(name = "form_panel_id", referencedColumnName = "id_form_panel", nullable = false, insertable = false, updatable = false)
    public PanelForm getPanelForm() {
        return panelForm;
    }

    public void setPanelForm(PanelForm panelForm) {
        this.panelForm = panelForm;
    }

    @Basic
    @Column(name = "form_panel_id", nullable = false, insertable = true, updatable = true)
    public Long getPanelFormId() {
        return panelFormId;
    }

    public void setPanelFormId(Long panelFormId) {
        this.panelFormId = panelFormId;
    }

    @ManyToOne
    @JoinColumn(name = "form_puerta_id", referencedColumnName = "id_form_puerta", nullable = false, insertable = false, updatable = false)
    public PuertaForm getPuertaForm() {
        return puertaForm;
    }

    public void setPuertaForm(PuertaForm puertaForm) {
        this.puertaForm = puertaForm;
    }

    @Basic
    @Column(name = "form_puerta_id", nullable = false, insertable = true, updatable = true)
    public Long getPuertaFormId() {
        return puertaFormId;
    }

    public void setPuertaFormId(Long puertaFormId) {
        this.puertaFormId = puertaFormId;
    }

    @ManyToOne
    @JoinColumn(name = "form_refrigeracion_id", referencedColumnName = "id_form_refrigeracion", nullable = false, insertable = false, updatable = false)
    public RefrigeracionForm getRefrigeracionForm() {
        return refrigeracionForm;
    }

    public void setRefrigeracionForm(RefrigeracionForm refrigeracionForm) {
        this.refrigeracionForm = refrigeracionForm;
    }

    @Basic
    @Column(name = "form_refrigeracion_id", nullable = false, insertable = true, updatable = true)
    public Long getRefrigeracionFormId() {
        return refrigeracionFormId;
    }

    public void setRefrigeracionFormId(Long refrigeracionFormId) {
        this.refrigeracionFormId = refrigeracionFormId;
    }

    @ManyToOne
    @JoinColumn(name = "form_camara_id", referencedColumnName = "id_form_camara", nullable = false, insertable = false, updatable = false)
    public CamaraForm getCamaraForm() {
        return camaraForm;
    }

    public void setCamaraForm(CamaraForm camaraForm) {
        this.camaraForm = camaraForm;
    }

    @Basic
    @Column(name = "form_camara_id", nullable = false, insertable = true, updatable = true)
    public Long getCamaraFormId() {
        return camaraFormId;
    }

    public void setCamaraFormId(Long camaraFormId) {
        this.camaraFormId = camaraFormId;
    }
}
