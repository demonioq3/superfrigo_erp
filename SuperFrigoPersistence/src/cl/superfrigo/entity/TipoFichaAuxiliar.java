package cl.superfrigo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "tipo_ficha_auxiliar")
public class TipoFichaAuxiliar extends BaseEntity implements Serializable {

    public final static Long CLIENTE = 1L;
    public final static Long PROVEEDOR = 2L;

    private Long id;
    private String descripcion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_ficha_auxiliar", nullable = false, insertable = true, updatable = true)
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
