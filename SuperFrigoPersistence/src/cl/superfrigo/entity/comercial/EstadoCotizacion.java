package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "estado_cotizacion")
public class EstadoCotizacion extends BaseEntity implements Serializable {

    public static final Long EN_ESTUDIO = 1L;
    public static final Long ESPERA_APROBACION_COMERCIAL = 2L;
    public static final Long ESPERA_APROBACION_FINANCIERA= 3L;
    public static final Long ESPERA_APROBACION_CLIENTE = 4L;
    public static final Long APROBADA = 5L;
    public static final Long RECHAZADA = 6L;

    private Long id;
    private String descripcion;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion", nullable = true, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}