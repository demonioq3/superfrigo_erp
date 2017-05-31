package cl.superfrigo.entity.registros_hh;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "registros_hh")
public class RegistroHH extends BaseEntity implements Serializable {

    private Long id;
    private TrabajadorHH trabajadorHH;
    private Long trabajadorHHId;
    private AreaHH areaHH;
    private Long areaHHId;
    private Integer hh;
    private Long ordenDeTrabajoId;
    private Long centroDeCostoId;
    private Date fecha;
    private Double valor;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registros_hh", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "trabajador_hh_id", referencedColumnName = "id_trabajadores_hh", nullable = false, insertable = false, updatable = false)
    public TrabajadorHH getTrabajadorHH() {
        return trabajadorHH;
    }

    public void setTrabajadorHH(TrabajadorHH trabajadorHH) {
        this.trabajadorHH = trabajadorHH;
    }

    @Basic
    @Column(name = "trabajador_hh_id", nullable = false, insertable = true, updatable = true)
    public Long getTrabajadorHHId() {
        return trabajadorHHId;
    }

    public void setTrabajadorHHId(Long trabajadorHHId) {
        this.trabajadorHHId = trabajadorHHId;
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
    @Column(name = "hh", nullable = false, insertable = true, updatable = true)
    public Integer getHh() {
        return hh;
    }

    public void setHh(Integer hh) {
        this.hh = hh;
    }

    @Basic
    @Column(name = "orden_de_trabajo_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    @Basic
    @Column(name = "centro_de_costo_id", nullable = false, insertable = true, updatable = true)
    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
    }

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Transient
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
