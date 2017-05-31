package cl.superfrigo.entity.produccion;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "control_despachos")
public class ControlDespacho extends BaseEntity implements Serializable {

    private Long id;
    private Integer cantidad;
    private Date fechaDespacho;
    private Long numeroGuia;
    private Long ordenDeTrabajoId;
    private String nota;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control_despacho", nullable = false, insertable = true, updatable = true)
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
    @Column(name = "fecha_despacho", nullable = false, insertable = true, updatable = true)
    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    @Basic
    @Column(name = "numero_guia", nullable = false, insertable = true, updatable = true)
    public Long getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(Long numeroGuia) {
        this.numeroGuia = numeroGuia;
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
