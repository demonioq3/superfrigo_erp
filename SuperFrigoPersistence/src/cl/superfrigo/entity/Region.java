package cl.superfrigo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "regiones")
public class Region extends BaseEntity implements Serializable {

    private Long id;
    private String nombre;
    private int orden;
    private int activo;

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

    @Basic
    @Column(name = "orden", nullable = true, insertable = true, updatable = true)
    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @Basic
    @Column(name = "activo", nullable = true, insertable = true, updatable = true)
    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
