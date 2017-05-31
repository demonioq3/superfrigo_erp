package cl.superfrigo.entity.solicitud_material;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.FichaProducto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "producto_solicitud_material")
public class ProductoSolicitudMaterial extends BaseEntity implements Serializable {

    private Long id;
    private Long productoId;
    private FichaProducto producto;
    private Double cantidad;
    private Long solicitudMaterialId;
    private SolicitudMaterial solicitudMaterial;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_solicitud_material", nullable = false, insertable = true, updatable = true)
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
    @Column(name = "cantidad", nullable = false, insertable = true, updatable = true)
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Basic
    @Column(name = "solicitud_material_id", nullable = false, insertable = true, updatable = true)
    public Long getSolicitudMaterialId() {
        return solicitudMaterialId;
    }

    public void setSolicitudMaterialId(Long solicitudMaterialId) {
        this.solicitudMaterialId = solicitudMaterialId;
    }

    @ManyToOne
    @JoinColumn(name = "solicitud_material_id", referencedColumnName = "id_solicitud_material", nullable = false, insertable = false, updatable = false)
    public SolicitudMaterial getSolicitudMaterial() {
        return solicitudMaterial;
    }

    public void setSolicitudMaterial(SolicitudMaterial solicitudMaterial) {
        this.solicitudMaterial = solicitudMaterial;
    }

}
