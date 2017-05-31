package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "tipo_formulario")
public class TipoFormulario extends BaseEntity implements Serializable {

    public final static Long PANELES = 1L;
    public final static Long MOLDURAS = 2L;
    public final static Long PUERTAS = 3L;
    public final static Long CAMARAS = 4L;
    public final static Long SIN_TABLA = 5L;
    public final static Long EQUIPOS_REFRIGERACION = 6L;

    private Long id;
    private String descripcion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_formulario", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
