package cl.superfrigo.entity.produccion;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "control_liberacion")
public class ControlLiberacion extends BaseEntity implements Serializable {

    private Long id;
    private Integer cantidad;
    private Date fecha;
    private Date fechaLiberacion;
    private Long ordenDeTrabajoId;
    private String nota;
    private String liberadoPor;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control_liberacion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
    @Column(name = "fecha_liberacion", nullable = false, insertable = true, updatable = true)
    public Date getFechaLiberacion() {
        return fechaLiberacion;
    }

    public void setFechaLiberacion(Date fechaLiberacion) {
        this.fechaLiberacion = fechaLiberacion;
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
    @Column(name = "nota", nullable = false, insertable = true, updatable = true)
    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Basic
    @Column(name = "liberado_por", nullable = false, insertable = true, updatable = true)
    public String getLiberadoPor() {
        return liberadoPor;
    }

    public void setLiberadoPor(String liberadoPor) {
        this.liberadoPor = liberadoPor;
    }
}
