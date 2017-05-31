package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "grupo")
public class Grupo extends BaseEntity implements Serializable {

    private Long id;
    private String codigo;
    private String nombreGrupo;
    private List<SubGrupo> subgrupos;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo", nullable = false, insertable = true, updatable = true)
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
    @Column(name = "nombre_grupo", nullable = false, insertable = true, updatable = true)
    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    @OneToMany(mappedBy="padre", fetch = FetchType.EAGER)
    public List<SubGrupo> getSubgrupos() {
        return subgrupos;
    }

    public void setSubgrupos(List<SubGrupo> subgrupos) {
        this.subgrupos = subgrupos;
    }
}
