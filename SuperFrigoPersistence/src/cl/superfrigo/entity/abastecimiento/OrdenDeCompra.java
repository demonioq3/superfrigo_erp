package cl.superfrigo.entity.abastecimiento;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "orden_de_compra")
public class OrdenDeCompra extends BaseEntity implements Serializable {

    private Long id;
    private FichaAuxiliar proveedor;
    private Long proveedorId;
    private CondicionPago condicionPago;
    private Long condicionPagoId;
    private Requisicion requisicion;
    private Long requisicionId;
    private EstadoOrdenCompra estadoOrdenCompra;
    private Long estadoOrdenCompraId;
    private EtapaOrdenCompra etapaOrdenCompra;
    private Long etapaOrdenCompraId;
    private CentroDeCosto centroDeCosto;
    private Long centroDeCostoId;
    private OrdenDeTrabajo ordenDeTrabajo;
    private Long ordenDeTrabajoId;
    private Date fecha;
    private String observaciones;
    private Date entregaFinal;
    private List<ProductoOrdenCompra> productos;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_de_compra", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @ManyToOne
    @JoinColumn(name = "condicion_pago_id", referencedColumnName = "id_condicion_pago", nullable = false, insertable = false, updatable = false)
    public CondicionPago getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(CondicionPago condicionPago) {
        this.condicionPago = condicionPago;
    }

    @Basic
    @Column(name = "condicion_pago_id", nullable = false, insertable = true, updatable = true)
    public Long getCondicionPagoId() {
        return condicionPagoId;
    }

    public void setCondicionPagoId(Long condicionPagoId) {
        this.condicionPagoId = condicionPagoId;
    }

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "requisicion_id", referencedColumnName = "id_requisicion", nullable = false, insertable = false, updatable = false)
    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        this.requisicion = requisicion;
    }

    @Basic
    @Column(name = "requisicion_id", nullable = false, insertable = true, updatable = true)
    public Long getRequisicionId() {
        return requisicionId;
    }

    public void setRequisicionId(Long requisicionId) {
        this.requisicionId = requisicionId;
    }

    @ManyToOne
    @JoinColumn(name = "estado_id", referencedColumnName = "id_estado_orden_compra", nullable = false, insertable = false, updatable = false)
    public EstadoOrdenCompra getEstadoOrdenCompra() {
        return estadoOrdenCompra;
    }

    public void setEstadoOrdenCompra(EstadoOrdenCompra estadoOrdenCompra) {
        this.estadoOrdenCompra = estadoOrdenCompra;
    }

    @Basic
    @Column(name = "estado_id", nullable = false, insertable = true, updatable = true)
    public Long getEstadoOrdenCompraId() {
        return estadoOrdenCompraId;
    }

    public void setEstadoOrdenCompraId(Long estadoOrdenCompraId) {
        this.estadoOrdenCompraId = estadoOrdenCompraId;
    }

    @ManyToOne
    @JoinColumn(name = "etapa_id", referencedColumnName = "id_etapa_orden_compra", nullable = false, insertable = false, updatable = false)
    public EtapaOrdenCompra getEtapaOrdenCompra() {
        return etapaOrdenCompra;
    }

    public void setEtapaOrdenCompra(EtapaOrdenCompra etapaOrdenCompra) {
        this.etapaOrdenCompra = etapaOrdenCompra;
    }

    @Basic
    @Column(name = "etapa_id", nullable = false, insertable = true, updatable = true)
    public Long getEtapaOrdenCompraId() {
        return etapaOrdenCompraId;
    }

    public void setEtapaOrdenCompraId(Long etapaOrdenCompraId) {
        this.etapaOrdenCompraId = etapaOrdenCompraId;
    }

    @ManyToOne
    @JoinColumn(name = "centro_de_Costo_id", referencedColumnName = "id_centro_de_costo", nullable = false, insertable = false, updatable = false)
    public CentroDeCosto getCentroDeCosto() {
        return centroDeCosto;
    }

    public void setCentroDeCosto(CentroDeCosto centroDeCosto) {
        this.centroDeCosto = centroDeCosto;
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

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "observaciones", nullable = false, insertable = true, updatable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Basic
    @Column(name = "entrega_final", nullable = false, insertable = true, updatable = true)
    public Date getEntregaFinal() {
        return entregaFinal;
    }

    public void setEntregaFinal(Date entregaFinal) {
        this.entregaFinal = entregaFinal;
    }

    @Basic
    @Column(name = "centro_de_Costo_id", nullable = false, insertable = true, updatable = true)
    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "ordenDeCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ProductoOrdenCompra> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenCompra> productos) {
        this.productos = productos;
    }
}
