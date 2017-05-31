package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "tipo_ficha_producto")
public class TipoFichaProducto extends BaseEntity implements Serializable {

    public final static Long INVENTARIABLE = 1L;
    public final static Long NO_INVENTARIABLE = 2L;

    private Long id;
    private String nombre;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_ficha_producto", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre", nullable = false, insertable = true, updatable = true)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
