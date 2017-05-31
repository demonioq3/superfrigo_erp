package cl.superfrigo.entity.produccion;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "control_programacion")
public class ControlProgramacion extends BaseEntity implements Serializable {

    private Long id;
    private double cantidad;
    private Date fecha;
    private Date fechaEntrega;
    private Date fechaReal;
    private Integer porcentajeAvance;
    private String nota;
    private Long ordenDeTrabajoId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control_programacion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
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
    @Column(name = "fecha_entrega", nullable = false, insertable = true, updatable = true)
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    @Basic
    @Column(name = "fecha_real", nullable = false, insertable = true, updatable = true)
    public Date getFechaReal() {
        return fechaReal;
    }

    public void setFechaReal(Date fechaReal) {
        this.fechaReal = fechaReal;
    }

    @Basic
    @Column(name = "porcentaje_avance", nullable = false, insertable = true, updatable = true)
    public Integer getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(Integer porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
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
}
