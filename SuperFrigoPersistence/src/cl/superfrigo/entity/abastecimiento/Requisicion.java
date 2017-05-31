package cl.superfrigo.entity.abastecimiento;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "requisiciones")
public class Requisicion extends BaseEntity implements Serializable {

    private Long id;
    private String solicitante;
    private Date fechaEmision;
    private Date fechaRequerida;
    private EstadoRequisicion estadoRequisicion;
    private Long estadoRequisicionId;
    private Area area;
    private Long areaId;
    private CentroDeCosto centroDeCosto;
    private Long centroDeCostoId;
    private OrdenDeTrabajo ordenDeTrabajo;
    private Long ordenDeTrabajoId;
    private String observaciones;
    private List<ProductoRequisicion> productos;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisicion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "solicitante", nullable = false, insertable = true, updatable = true)
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    @Basic
    @Column(name = "fecha_emision", nullable = false, insertable = true, updatable = true)
    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Basic
    @Column(name = "fecha_requerida", nullable = false, insertable = true, updatable = true)
    public Date getFechaRequerida() {
        return fechaRequerida;
    }

    public void setFechaRequerida(Date fechaRequerida) {
        this.fechaRequerida = fechaRequerida;
    }

    @ManyToOne
    @JoinColumn(name = "estado_requisicion_id", referencedColumnName = "id_estado_requisicion", nullable = false, insertable = false, updatable = false)
    public EstadoRequisicion getEstadoRequisicion() {
        return estadoRequisicion;
    }

    public void setEstadoRequisicion(EstadoRequisicion estadoRequisicion) {
        this.estadoRequisicion = estadoRequisicion;
    }

    @Basic
    @Column(name = "estado_requisicion_id", nullable = false, insertable = true, updatable = true)
    public Long getEstadoRequisicionId() {
        return estadoRequisicionId;
    }

    public void setEstadoRequisicionId(Long estadoRequisicionId) {
        this.estadoRequisicionId = estadoRequisicionId;
    }

    @ManyToOne
    @JoinColumn(name = "area_id", referencedColumnName = "id_area", nullable = false, insertable = false, updatable = false)
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Basic
    @Column(name = "area_id", nullable = false, insertable = true, updatable = true)
    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @ManyToOne
    @JoinColumn(name = "centro_de_costo_id", referencedColumnName = "id_centro_de_costo", nullable = false, insertable = false, updatable = false)
    public CentroDeCosto getCentroDeCosto() {
        return centroDeCosto;
    }

    public void setCentroDeCosto(CentroDeCosto centroDeCosto) {
        this.centroDeCosto = centroDeCosto;
    }

    @Basic
    @Column(name = "centro_de_costo_id", nullable = false, insertable = true, updatable = true)
    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
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
    @Column(name = "observaciones", nullable = false, insertable = true, updatable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "requisicion")
    public List<ProductoRequisicion> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoRequisicion> productos) {
        this.productos = productos;
    }
}
