package cl.superfrigo.entity.proyectos;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "recetas")
public class Receta extends BaseEntity implements Serializable {

    private Long id;
    private Long productoId;
    private String codigo;
    private String descripcion;
    private Boolean base;
    private Double cantidadBase;
    private FichaProducto producto;
    private List<ProductoReceta> productosAsociados;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "ficha_producto_id", referencedColumnName = "id_ficha_producto", nullable = false, insertable = false, updatable = false)
    public FichaProducto getProducto() {
        return producto;
    }

    public void setProducto(FichaProducto producto) {
        this.producto = producto;
    }

    @Basic
    @Column(name = "ficha_producto_id", nullable = false, insertable = true, updatable = true)
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
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
    @Column(name = "descripcion", nullable = false, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "base", nullable = false, insertable = true, updatable = true)
    public Boolean getBase() {
        return base;
    }

    public void setBase(Boolean base) {
        this.base = base;
    }

    @Basic
    @Column(name = "cantidad_base", nullable = false, insertable = true, updatable = true)
    public Double getCantidadBase() {
        return cantidadBase;
    }

    public void setCantidadBase(Double cantidadBase) {
        this.cantidadBase = cantidadBase;
    }

    @OneToMany(mappedBy = "receta", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<ProductoReceta> getProductosAsociados() {
        return productosAsociados;
    }

    public void setProductosAsociados(List<ProductoReceta> productosAsociados) {
        this.productosAsociados = productosAsociados;
    }
}
