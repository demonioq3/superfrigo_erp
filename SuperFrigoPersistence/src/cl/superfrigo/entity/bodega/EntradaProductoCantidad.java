package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "entrada_producto_cantidad")
public class EntradaProductoCantidad extends BaseEntity implements Serializable {

    private Long id;
    private FichaProducto fichaProducto;
    private Long fichaProductoId;
    private GuiaEntrada guiaEntrada;
    private Long guiaEntradaId;
    private Double cantidad;
    private Double cantidadUtilizada;
    private Double precioUnitario;
    private Double precioTotal;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada_producto_cantidad", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "ficha_producto_id", referencedColumnName = "id_ficha_producto", nullable = false, insertable = false, updatable = false)
    public FichaProducto getFichaProducto() {
        return fichaProducto;
    }

    public void setFichaProducto(FichaProducto fichaProducto) {
        this.fichaProducto = fichaProducto;
    }

    @Basic
    @Column(name = "ficha_producto_id", nullable = false, insertable = true, updatable = true)
    public Long getFichaProductoId() {
        return fichaProductoId;
    }

    public void setFichaProductoId(Long fichaProductoId) {
        this.fichaProductoId = fichaProductoId;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="guia_entrada_id",referencedColumnName="id_guia_entrada", insertable = false, updatable = false)
    })
    public GuiaEntrada getGuiaEntrada() {
        return guiaEntrada;
    }

    public void setGuiaEntrada(GuiaEntrada guiaEntrada) {
        this.guiaEntrada = guiaEntrada;
    }

    @Basic
    @Column(name = "guia_entrada_id", nullable = false, insertable = true, updatable = true)
    public Long getGuiaEntradaId() {
        return guiaEntradaId;
    }

    public void setGuiaEntradaId(Long guiaEntradaId) {
        this.guiaEntradaId = guiaEntradaId;
    }

    @Basic
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "cantidad_utilizada", nullable = false, insertable = true, updatable = true)
    public Double getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(Double cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
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
    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
