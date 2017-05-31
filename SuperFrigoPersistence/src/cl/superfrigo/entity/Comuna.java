package cl.superfrigo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "comunas")
public class Comuna extends BaseEntity implements Serializable {

    private Long id;
    private String nombre;
    private Region padre;
    private Long padreId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre", nullable = true, insertable = true, updatable = true)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToOne
    @JoinColumn(name = "padre", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Region getPadre() {
        return padre;
    }

    public void setPadre(Region padre) {
        this.padre = padre;
    }

    @Basic
    @Column(name = "padre", nullable = false, insertable = true, updatable = true)
    public Long getPadreId() {
        return padreId;
    }

    public void setPadreId(Long padreId) {
        this.padreId = padreId;
    }
}
