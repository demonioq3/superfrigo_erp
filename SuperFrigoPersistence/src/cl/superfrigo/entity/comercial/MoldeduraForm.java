package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "form_moldedura")
public class MoldeduraForm extends BaseEntity implements Serializable {

    private Long id;
    private int material;
    private int moldurasc;
    private int consideraZocalos;
    private int medidasZocalos;
    private int instalacion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_form_moldedura", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "material", nullable = false, insertable = true, updatable = true)
    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    @Basic
    @Column(name = "moldurasu", nullable = false, insertable = true, updatable = true)
    public int getMoldurasc() {
        return moldurasc;
    }

    public void setMoldurasc(int moldurasc) {
        this.moldurasc = moldurasc;
    }

    @Basic
    @Column(name = "considera_zocalos", nullable = false, insertable = true, updatable = true)
    public int getConsideraZocalos() {
        return consideraZocalos;
    }

    public void setConsideraZocalos(int consideraZocalos) {
        this.consideraZocalos = consideraZocalos;
    }

    @Basic
    @Column(name = "medidas_zocalos", nullable = false, insertable = true, updatable = true)
    public int getMedidasZocalos() {
        return medidasZocalos;
    }

    public void setMedidasZocalos(int medidasZocalos) {
        this.medidasZocalos = medidasZocalos;
    }

    @Basic
    @Column(name = "instalacion", nullable = false, insertable = true, updatable = true)
    public int getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(int instalacion) {
        this.instalacion = instalacion;
    }
}
