package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "toma_inventario")
public class TomaInventario extends BaseEntity implements Serializable {

    private Long id;
    private Date fecha;
    private Long bodegaId;
    private Bodega bodega;
    private List<ProductoTomaInventario> productosAsociados;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_toma_inventario", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @ManyToOne
    @JoinColumn(name = "bodega_id", referencedColumnName = "id_bodega", nullable = false, insertable = false, updatable = false)
    public Bodega getBodega() {
        return bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    @Basic
    @Column(name = "bodega_id", nullable = false, insertable = true, updatable = true)
    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    @OneToMany(mappedBy = "tomaInventario", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<ProductoTomaInventario> getProductosAsociados() {
        return productosAsociados;
    }

    public void setProductosAsociados(List<ProductoTomaInventario> productosAsociados) {
        this.productosAsociados = productosAsociados;
    }
}
