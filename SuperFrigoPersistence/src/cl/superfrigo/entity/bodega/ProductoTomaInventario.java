package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.proyectos.Receta;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "producto_toma_inventario")
public class ProductoTomaInventario extends BaseEntity implements Serializable {

    private Long id;
    private Long productoId;
    private FichaProducto producto;
    private Long tomaInventarioId;
    private TomaInventario tomaInventario;
    private double cantidadContada;
    private Double cantidadSistema;
    private Double resto;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_toma_inventario", nullable = false, insertable = true, updatable = true)
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
    @Column(name = "toma_inventario_id", nullable = false, insertable = true, updatable = true)
    public Long getTomaInventarioId() {
        return tomaInventarioId;
    }

    public void setTomaInventarioId(Long tomaInventarioId) {
        this.tomaInventarioId = tomaInventarioId;
    }

    @ManyToOne
    @JoinColumn(name = "toma_inventario_id", referencedColumnName = "id_toma_inventario", nullable = false, insertable = false, updatable = false)
    public TomaInventario getTomaInventario() {
        return tomaInventario;
    }

    public void setTomaInventario(TomaInventario tomaInventario) {
        this.tomaInventario = tomaInventario;
    }

    @Basic
    @Column(name = "cantidad_contada", nullable = false, insertable = true, updatable = true)
    public double getCantidadContada() {
        return cantidadContada;
    }

    public void setCantidadContada(double cantidadContada) {
        this.cantidadContada = cantidadContada;
    }

    @Basic
    @Column(name = "cantidad_sistema", nullable = false, insertable = true, updatable = true)
    public Double getCantidadSistema() {
        return cantidadSistema;
    }

    public void setCantidadSistema(Double cantidadSistema) {
        this.cantidadSistema = cantidadSistema;
    }

    @Basic
    @Column(name = "resto", nullable = false, insertable = true, updatable = true)
    public Double getResto() {
        return resto;
    }

    public void setResto(Double resto) {
        this.resto = resto;
    }
}
