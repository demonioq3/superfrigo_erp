package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "estado_ot")
public class EstadoOrdenTrabajo extends BaseEntity implements Serializable {

    public static final Long VIGENTE = 1L;
    public static final Long CERRADA = 2L;
    public static final Long NULA= 3L;
    private Long id;
    private String descripcion;

    @Id
    @Column(name = "id_estado_ot", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre", nullable = true, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}