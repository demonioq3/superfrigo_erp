package cl.superfrigo.entity.registros_hh;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "trabajadores_hh")
public class TrabajadorHH extends BaseEntity implements Serializable {

    private Long id;
    private String nombres;
    private String apellidos;
    private String rut;
    private Date fechaAnulacion;
    private EstadoHH estadoHH;
    private Long estadoHHId;
    private AreaHH areaHH;
    private Long areaHHId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajadores_hh", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombres", nullable = false, insertable = true, updatable = true)
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Basic
    @Column(name = "apellidos", nullable = false, insertable = true, updatable = true)
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Basic
    @Column(name = "rut", nullable = false, insertable = true, updatable = true)
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Basic
    @Column(name = "fecha_anulacion", nullable = false, insertable = true, updatable = true)
    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
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
}
