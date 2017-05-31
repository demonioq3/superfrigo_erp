package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.CentroDeCosto;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "guia_salida")
public class GuiaSalida extends BaseEntity implements Serializable {

    private Long id;
    private ConceptoSalida conceptoSalida;
    private Long conceptoSalidaId;
    private Bodega bodega;
    private Long bodegaId;
    private FichaAuxiliar proveedor;
    private Long proveedorId;
    private Date fecha;
    private OrdenDeCompra ordenDeCompra;
    private Long ordenDeCompraId;
    private OrdenDeTrabajo ordenDeTrabajo;
    private Long ordenDeTrabajoId;
    private CentroDeCosto centroDeCosto;
    private Long centroDeCostoId;
    private String observaciones;
    private Integer notaCredito;
    private Bodega bodegaOrigen;
    private Long bodegaOrigenId;
    private List<SalidaProductoCantidad> productos;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guia_salida", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "concepto_salida_id", referencedColumnName = "id_conceptos_salida", nullable = false, insertable = false, updatable = false)
    public ConceptoSalida getConceptoSalida() {
        return conceptoSalida;
    }

    public void setConceptoSalida(ConceptoSalida conceptoSalida) {
        this.conceptoSalida = conceptoSalida;
    }

    @Basic
    @Column(name = "concepto_salida_id", nullable = false, insertable = true, updatable = true)
    public Long getConceptoSalidaId() {
        return conceptoSalidaId;
    }

    public void setConceptoSalidaId(Long conceptoSalidaId) {
        this.conceptoSalidaId = conceptoSalidaId;
    }

    @ManyToOne
    @JoinColumn(name = "bodega_id", referencedColumnName = "id_bodega", nullable = false, insertable = false, updatable = false)
    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    @Basic
    @Column(name = "bodega_id", nullable = false, insertable = true, updatable = true)
    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    @ManyToOne
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public FichaAuxiliar getProveedor() {
        return proveedor;
    }

    public void setProveedor(FichaAuxiliar proveedor) {
        this.proveedor = proveedor;
    }

    @Basic
    @Column(name = "proveedor_id", nullable = false, insertable = true, updatable = true)
    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    @Basic
    @Column(name = "fecha", nullable = true, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @ManyToOne
    @JoinColumn(name = "orden_trabajo_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public OrdenDeTrabajo getOrdenDeTrabajo() {
        return ordenDeTrabajo;
    }

    public void setOrdenDeTrabajo(OrdenDeTrabajo ordenDeTrabajo) {
        this.ordenDeTrabajo = ordenDeTrabajo;
    }

    @Basic
    @Column(name = "orden_trabajo_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    @ManyToOne
    @JoinColumn(name = "orden_compra_id", referencedColumnName = "id_orden_de_compra", nullable = false, insertable = false, updatable = false)
    public OrdenDeCompra getOrdenDeCompra() {
        return ordenDeCompra;
    }

    public void setOrdenDeCompra(OrdenDeCompra ordenDeCompra) {
        this.ordenDeCompra = ordenDeCompra;
    }

    @Basic
    @Column(name = "orden_compra_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeCompraId() {
        return ordenDeCompraId;
    }

    public void setOrdenDeCompraId(Long ordenDeCompraId) {
        this.ordenDeCompraId = ordenDeCompraId;
    }

    @ManyToOne
    @NotFound(
            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "centro_costo_id", referencedColumnName = "id_centro_de_costo", nullable = false, insertable = false, updatable = false)
    public CentroDeCosto getCentroDeCosto() {
        return centroDeCosto;
    }

    public void setCentroDeCosto(CentroDeCosto centroDeCosto) {
        this.centroDeCosto = centroDeCosto;
    }

    @Basic
    @Column(name = "centro_costo_id", nullable = false, insertable = true, updatable = true)
    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
    }

    @Basic
    @Column(name = "nota_credito", nullable = true, insertable = true, updatable = true)
    public Integer getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(Integer notaCredito) {
        this.notaCredito = notaCredito;
    }


    @Basic
    @Column(name = "observaciones", nullable = true, insertable = true, updatable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @ManyToOne
    @JoinColumn(name = "bodega_origen_id", referencedColumnName = "id_bodega", nullable = false, insertable = false, updatable = false)
    public Bodega getBodegaOrigen() {
        return bodegaOrigen;
    }

    public void setBodegaOrigen(Bodega bodegaOrigen) {
        this.bodegaOrigen = bodegaOrigen;
    }

    @Basic
    @Column(name = "bodega_origen_id", nullable = false, insertable = true, updatable = true)
    public Long getBodegaOrigenId() {
        return bodegaOrigenId;
    }

    public void setBodegaOrigenId(Long bodegaOrigenId) {
        this.bodegaOrigenId = bodegaOrigenId;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "guiaSalida", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SalidaProductoCantidad> getProductos() {
        return productos;
    }

    public void setProductos(List<SalidaProductoCantidad> productos) {
        this.productos = productos;
    }
}
