package cl.superfrigo.entity.solicitud_material;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "solicitud_material")
public class SolicitudMaterial extends BaseEntity implements Serializable {

    private Long id;
    private int numeroOT;
    private String codigoReceta;
    private String codigoProducto;
    private String solicitante;
    private Date fecha;
    private Date fechaRequerida;
    private Double cantidad;
    private String descripcion;
    private String observaciones;
    private List<ProductoSolicitudMaterial> productosAsociados;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_material", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "numero_ot", nullable = false, insertable = true, updatable = true)
    public int getNumeroOT() {
        return numeroOT;
    }

    public void setNumeroOT(int numeroOT) {
        this.numeroOT = numeroOT;
    }

    @Basic
    @Column(name = "codigo_receta", nullable = false, insertable = true, updatable = true)
    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    @Basic
    @Column(name = "codigo_producto", nullable = false, insertable = true, updatable = true)
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
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
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "fecha_requerida", nullable = false, insertable = true, updatable = true)
    public Date getFechaRequerida() {
        return fechaRequerida;
    }

    public void setFechaRequerida(Date fechaRequerida) {
        this.fechaRequerida = fechaRequerida;
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
    @Column(name = "descripcion", nullable = false, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "observaciones", nullable = false, insertable = true, updatable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @OneToMany(mappedBy = "solicitudMaterial", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<ProductoSolicitudMaterial> getProductosAsociados() {
        return productosAsociados;
    }

    public void setProductosAsociados(List<ProductoSolicitudMaterial> productosAsociados) {
        this.productosAsociados = productosAsociados;
    }
}
