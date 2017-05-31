package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.FichaAuxiliar;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "orden_de_trabajo")
public class OrdenDeTrabajo extends BaseEntity implements Serializable {

    private Long id;
    private Date fecha;
    private String gestor;
    private FichaAuxiliar fichaAuxiliar;
    private Long clienteId;
    private EstadoCotizacion estado;
    private Long estadoId;
    private Long estadoOTId;
    private String observacion;
    private List<ProductoOrdenTrabajo> productos;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fecha", nullable = true, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "gestor", nullable = true, insertable = true, updatable = true)
    public String getGestor() {
        return gestor;
    }

    public void setGestor(String gestor) {
        this.gestor = gestor;
    }

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public FichaAuxiliar getFichaAuxiliar() {
        return fichaAuxiliar;
    }

    public void setFichaAuxiliar(FichaAuxiliar fichaAuxiliar) {
        this.fichaAuxiliar = fichaAuxiliar;
    }

    @Basic
    @Column(name = "cliente_id", nullable = false, insertable = true, updatable = true)
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    @Basic
    @Column(name = "observacion", nullable = false, insertable = true, updatable = true)
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @ManyToOne
    @JoinColumn(name = "estado_cotizacion_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public EstadoCotizacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoCotizacion estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "estado_cotizacion_id", nullable = false, insertable = true, updatable = true)
    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    @Basic
    @Column(name = "estado_ot_id", nullable = false, insertable = true, updatable = true)
    public Long getEstadoOTId() {
        return estadoOTId;
    }

    public void setEstadoOTId(Long estadoOTId) {
        this.estadoOTId = estadoOTId;
    }

    @OneToMany(mappedBy = "ordenDeTrabajo", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<ProductoOrdenTrabajo> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoOrdenTrabajo> productos) {
        this.productos = productos;
    }

}
