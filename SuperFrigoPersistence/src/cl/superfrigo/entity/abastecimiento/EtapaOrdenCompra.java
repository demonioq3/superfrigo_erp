package cl.superfrigo.entity.abastecimiento;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "etapa_orden_compra")
public class EtapaOrdenCompra extends BaseEntity implements Serializable {

    public static final Long SIN_RECEPCION = 1L;
    public static final Long PARCIAL = 2L;
    public static final Long RECEPCION_COMPLETA = 3L;

    private Long id;
    private String descripcion;

    @Id
    @Column(name = "id_etapa_orden_compra", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion", nullable = true, insertable = true, updatable = true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}