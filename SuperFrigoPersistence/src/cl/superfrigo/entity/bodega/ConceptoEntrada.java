package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "conceptos_entrada")
public class ConceptoEntrada extends BaseEntity implements Serializable {

    public final static Long COMPRA_CON_GUIA_DESPACHO = 1L;
    public final static Long COMPRA_CON_FACTURA = 2L;
    public final static Long TRANSFERENCIA_ENTRE_BODEGA = 3L;
    public final static Long DEVOLUCION_PRODUCTO_A_OT_CC = 4L;

    private Long id;
    private String descripcion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conceptos_entrada", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
