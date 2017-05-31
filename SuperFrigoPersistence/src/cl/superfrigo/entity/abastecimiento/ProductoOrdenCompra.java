package cl.superfrigo.entity.abastecimiento;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "producto_orden_compra")
public class ProductoOrdenCompra extends BaseEntity implements Serializable {

    private Long id;
    private ProductoRequisicion productoRequisicion;
    private Long productoRequisicionId;
    private OrdenDeCompra ordenDeCompra;
    private Long ordenDeCompraId;
    private Double recibido;
    private Double saldo;
    private Double precioUnitario;
    private Double total;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_orden_compra", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "producto_requisicion_id", referencedColumnName = "id_producto_requisicion", nullable = false, insertable = false, updatable = false)
    public ProductoRequisicion getProductoRequisicion() {
        return productoRequisicion;
    }

    public void setProductoRequisicion(ProductoRequisicion productoRequisicion) {
        this.productoRequisicion = productoRequisicion;
    }

    @Basic
    @Column(name = "producto_requisicion_id", nullable = false, insertable = true, updatable = true)
    public Long getProductoRequisicionId() {
        return productoRequisicionId;
    }

    public void setProductoRequisicionId(Long productoRequisicionId) {
        this.productoRequisicionId = productoRequisicionId;
    }

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "orden_compra_id", referencedColumnName = "id_orden_de_compra", nullable = false, insertable = false, updatable = false)
    public OrdenDeCompra getOrdenDeCompra() {
        return ordenDeCompra;
    }

    public void setOrdenDeCompra(OrdenDeCompra ordenDeCompra) {
        this.ordenDeCompra = ordenDeCompra;
    }

    @Basic
    @Column(name = "orden_compra_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeCompraId() {
        return ordenDeCompraId;
    }

    public void setOrdenDeCompraId(Long ordenDeCompraId) {
        this.ordenDeCompraId = ordenDeCompraId;
    }

    @Basic
    @Column(name = "recibido", nullable = false, insertable = true, updatable = true)
    public Double getRecibido() {
        return recibido;
    }

    public void setRecibido(Double recibido) {
        this.recibido = recibido;
    }

    @Basic
    @Column(name = "saldo", nullable = false, insertable = true, updatable = true)
    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    @Basic
    @Column(name = "precio_unitario", nullable = false, insertable = true, updatable = true)
    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Basic
    @Column(name = "total", nullable = false, insertable = true, updatable = true)
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
