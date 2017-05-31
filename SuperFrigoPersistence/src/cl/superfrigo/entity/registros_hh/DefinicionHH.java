package cl.superfrigo.entity.registros_hh;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "definiciones_hh")
public class DefinicionHH extends BaseEntity implements Serializable {

    private Long id;
    private String tipoHH;
    private AreaHH areaHH;
    private Long areaHHId;
    private Date fecha;
    private Long valor;
    private EstadoHH estadoHH;
    private Long estadoHHId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_definicion_hh", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tipo_hh", nullable = false, insertable = true, updatable = true)
    public String getTipoHH() {
        return tipoHH;
    }

    public void setTipoHH(String tipoHH) {
        this.tipoHH = tipoHH;
    }

    @ManyToOne
    @JoinColumn(name = "area_hh_id", referencedColumnName = "id_areas_hh", nullable = false, insertable = false, updatable = false)
    public AreaHH getAreaHH() {
        return areaHH;
    }

    public void setAreaHH(AreaHH areaHH) {
        this.areaHH = areaHH;
    }

    @Basic
    @Column(name = "area_hh_id", nullable = false, insertable = true, updatable = true)
    public Long getAreaHHId() {
        return areaHHId;
    }

    public void setAreaHHId(Long areaHHId) {
        this.areaHHId = areaHHId;
    }

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "valor", nullable = false, insertable = true, updatable = true)
    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    @ManyToOne
    @JoinColumn(name = "estado_hh_id", referencedColumnName = "id_estado_hh", nullable = false, insertable = false, updatable = false)
    public EstadoHH getEstadoHH() {
        return estadoHH;
    }

    public void setEstadoHH(EstadoHH estadoHH) {
        this.estadoHH = estadoHH;
    }

    @Basic
    @Column(name = "estado_hh_id", nullable = false, insertable = true, updatable = true)
    public Long getEstadoHHId() {
        return estadoHHId;
    }

    public void setEstadoHHId(Long estadoHHId) {
        this.estadoHHId = estadoHHId;
    }
}
