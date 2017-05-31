package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "subgrupo")
public class SubGrupo extends BaseEntity implements Serializable {

    private Long id;
    private String codigo;
    private String nombreSubGrupo;
    private Grupo padre;
    private Long padreId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subgrupo", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codigo", nullable = false, insertable = true, updatable = true)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "nombre_subgrupo", nullable = false, insertable = true, updatable = true)
    public String getNombreSubGrupo() {
        return nombreSubGrupo;
    }

    public void setNombreSubGrupo(String nombreSubGrupo) {
        this.nombreSubGrupo = nombreSubGrupo;
    }

    @ManyToOne
    @JoinColumn(name = "id_padre", referencedColumnName = "id_grupo", nullable = true, insertable = false, updatable = false)
    public Grupo getPadre() {
        return padre;
    }

    public void setPadre(Grupo padre) {
        this.padre = padre;
    }

    @Basic
    @Column(name = "id_padre", nullable = true, insertable = true, updatable = true)
    public Long getPadreId() {
        return padreId;
    }

    public void setPadreId(Long padreId) {
        this.padreId = padreId;
    }

}
