package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.bodega.Grupo;
import cl.superfrigo.entity.bodega.SubGrupo;
import cl.superfrigo.entity.bodega.TipoFichaProducto;
import cl.superfrigo.entity.bodega.UnidadMedida;
import cl.superfrigo.entity.proyectos.Receta;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "ficha_producto")
public class FichaProducto extends BaseEntity implements Serializable {

    private Long id;
    private String codigoProducto;
    private String descripcion;
    private String descripcion2;
    private Long stockMinimo;
    private Long stockMaximo;
    private Long stockReposicion;
    private UnidadMedida unidadMedida;
    private Long unidadMedidaId;
    private Grupo grupo;
    private Long grupoId;
    private SubGrupo subGrupo;
    private Long subGrupoId;
    private TipoFichaProducto tipoFichaProducto;
    private Long tipoFichaProductoId;
    private boolean inventariable;
    private String venta;
    private boolean productoVenta;
    private String compra;
    private boolean productoCompra;
    private String manejaImpuesto;
    private TipoFormulario tipoFormulario;
    private Long tipoFormularioId;
    private List<Receta> recetas;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha_producto", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codigo_producto", nullable = false, insertable = true, updatable = true)
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
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
    @Column(name = "descripcion2", nullable = false, insertable = true, updatable = true)
    public String getDescripcion2() {
        return descripcion2;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    @Basic
    @Column(name = "stock_minimo", nullable = false, insertable = true, updatable = true)
    public Long getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Long stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    @Basic
    @Column(name = "stock_maximo", nullable = false, insertable = true, updatable = true)
    public Long getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(Long stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    @Basic
    @Column(name = "stock_reposicion", nullable = false, insertable = true, updatable = true)
    public Long getStockReposicion() {
        return stockReposicion;
    }

    public void setStockReposicion(Long stockReposicion) {
        this.stockReposicion = stockReposicion;
    }

    @ManyToOne
    @JoinColumn(name = "id_unidad_medida", referencedColumnName = "id_unidad_medida", nullable = false, insertable = false, updatable = false)
    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @Basic
    @Column(name = "id_unidad_medida", nullable = false, insertable = true, updatable = true)
    public Long getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(Long unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    @ManyToOne
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false, insertable = false, updatable = false)
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Basic
    @Column(name = "id_grupo", nullable = false, insertable = true, updatable = true)
    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    @ManyToOne
    @JoinColumn(name = "id_subgrupo", referencedColumnName = "id_subgrupo", nullable = false, insertable = false, updatable = false)
    public SubGrupo getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(SubGrupo subGrupo) {
        this.subGrupo = subGrupo;
    }

    @Basic
    @Column(name = "id_subgrupo", nullable = false, insertable = true, updatable = true)
    public Long getSubGrupoId() {
        return subGrupoId;
    }

    public void setSubGrupoId(Long subGrupoId) {
        this.subGrupoId = subGrupoId;
    }

    @ManyToOne
    @JoinColumn(name = "id_tipo_ficha_producto", referencedColumnName = "id_tipo_ficha_producto", nullable = false, insertable = false, updatable = false)
    public TipoFichaProducto getTipoFichaProducto() {
        return tipoFichaProducto;
    }

    public void setTipoFichaProducto(TipoFichaProducto tipoFichaProducto) {
        this.tipoFichaProducto = tipoFichaProducto;
    }

    @Basic
    @Column(name = "id_tipo_ficha_producto", nullable = false, insertable = true, updatable = true)
    public Long getTipoFichaProductoId() {
        return tipoFichaProductoId;
    }

    public void setTipoFichaProductoId(Long tipoFichaProductoId) {
        this.tipoFichaProductoId = tipoFichaProductoId;
    }

    @ManyToOne
    @JoinColumn(name = "tipo_formulario_id", referencedColumnName = "id_tipo_formulario", nullable = false, insertable = false, updatable = false)
    public TipoFormulario getTipoFormulario() {
        return tipoFormulario;
    }

    public void setTipoFormulario(TipoFormulario tipoFormulario) {
        this.tipoFormulario = tipoFormulario;
    }

    @Basic
    @Column(name = "tipo_formulario_id", nullable = false, insertable = true, updatable = true)
    public Long getTipoFormularioId() {
        return tipoFormularioId;
    }

    public void setTipoFormularioId(Long tipoFormularioId) {
        this.tipoFormularioId = tipoFormularioId;
    }

    @Transient
    public boolean isInventariable() {
        if(tipoFichaProductoId.equals(TipoFichaProducto.INVENTARIABLE)){
            return true;
        }

        return false;
    }

    public void setInventariable(boolean inventariable) {
        this.inventariable = inventariable;
    }

    @Basic
    @Column(name = "venta", nullable = false, insertable = true, updatable = true)
    public String getVenta() {
        return venta;
    }

    public void setVenta(String venta) {
        this.venta = venta;
    }

    @Basic
    @Column(name = "compra", nullable = false, insertable = true, updatable = true)
    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    @Basic
    @Column(name = "maneja_impuesto", nullable = false, insertable = true, updatable = true)
    public String getManejaImpuesto() {
        return manejaImpuesto;
    }

    public void setManejaImpuesto(String manejaImpuesto) {
        this.manejaImpuesto = manejaImpuesto;
    }

    @Transient
    public boolean isProductoVenta() {
        if(venta.equals("SI")){
            return true;
        }

        return false;
    }

    public void setProductoVenta(boolean productoVenta) {
        this.productoVenta = productoVenta;
    }

    @Transient
    public boolean isProductoCompra() {
        if(compra.equals("NO")){
            return true;
        }

        return false;
    }

    public void setProductoCompra(boolean productoCompra) {
        this.productoCompra = productoCompra;
    }

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}
