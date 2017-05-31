package cl.superfrigo.entity.bodega;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "conceptos_salida")
public class ConceptoSalida extends BaseEntity implements Serializable {

    public final static Long DEVOLUCION_MATERIAL_A_PROVEEDOR = 1L;
    public final static Long TRANSFERENCIA_HACIA_OTRA_BODEGA = 2L;
    public final static Long CONSUMO_POR_CENTRO_DE_COSTO = 3L;
    public final static Long CONSUMO_POR_ORDEN_DE_TRABAJO = 4L;
    public final static Long SALIDA_POR_TOMA_INVENTARIO = 5L;

    private Long id;
    private String descripcion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conceptos_salida", nullable = false, insertable = true, updatable = true)
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
