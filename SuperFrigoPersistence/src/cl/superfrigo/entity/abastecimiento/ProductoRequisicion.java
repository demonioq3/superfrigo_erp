package cl.superfrigo.entity.abastecimiento;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "producto_requisicion")
public class ProductoRequisicion extends BaseEntity implements Serializable {

    private Long id;
    private Requisicion requisicion;
    private Long requisicionId;
    private FichaProducto fichaProducto;
    private Long fichaProductoId;
    private Double cantidad;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_requisicion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "requisicion_id", referencedColumnName = "id_requisicion", nullable = false, insertable = false, updatable = false)
    public Requisicion getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(Requisicion requisicion) {
        this.requisicion = requisicion;
    }

    @Basic
    @Column(name = "requisicion_id", nullable = false, insertable = true, updatable = true)
    public Long getRequisicionId() {
        return requisicionId;
    }

    public void setRequisicionId(Long requisicionId) {
        this.requisicionId = requisicionId;
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

    @Basic
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
