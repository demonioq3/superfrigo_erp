package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "salida_producto_cantidad")
public class SalidaProductoCantidad extends BaseEntity implements Serializable {

    private Long id;
    private FichaProducto fichaProducto;
    private Long fichaProductoId;
    private GuiaSalida guiaSalida;
    private Long guiaSalidaId;
    private Double cantidad;
    private Double precioUnitario;
    private Double precioTotal;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salida_producto_cantidad", nullable = false, insertable = true, updatable = true)
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
            @JoinColumn(name="guia_salida_id",referencedColumnName="id_guia_salida", insertable = false, updatable = false)
    })
    public GuiaSalida getGuiaSalida() {
        return guiaSalida;
    }

    public void setGuiaSalida(GuiaSalida guiaSalida) {
        this.guiaSalida = guiaSalida;
    }

    @Basic
    @Column(name = "guia_salida_id", nullable = false, insertable = true, updatable = true)
    public Long getGuiaSalidaId() {
        return guiaSalidaId;
    }

    public void setGuiaSalidaId(Long guiaSalidaId) {
        this.guiaSalidaId = guiaSalidaId;
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
