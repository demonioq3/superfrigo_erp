package cl.superfrigo.entity;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.Comuna;
import cl.superfrigo.entity.Region;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@Entity
@Table(name = "ficha_auxiliar")
public class FichaAuxiliar extends BaseEntity implements Serializable {

    private Long id;
    private String nombreRazonSocial;
    private String rut;
    private String direccionEmpresa;
    private String calle;
    private String numero_calle;
    private String sitio;
    private String camino;
    private String piso;
    private String localidad;
    private Comuna comuna;
    private Long comunaId;
    private Region region;
    private Long regionId;
    private Date horarioAtencionDesde;
    private Date horarioAtencionHasta;
    private String contacto;
    private String contactoObra;
    private String cargo;
    private String telefonoMovil;
    private String telefonoFijo;
    private List<TipoFichaAuxiliar> tipos;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre_razon_social", nullable = true, insertable = true, updatable = true)
    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }


    @Basic
    @Column(name = "rut", nullable = true, insertable = true, updatable = true)
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Basic
    @Column(name = "direccion_empresa", nullable = true, insertable = true, updatable = true)
    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    @Basic
    @Column(name = "calle", nullable = true, insertable = true, updatable = true)
    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    @Basic
    @Column(name = "numero_calle", nullable = true, insertable = true, updatable = true)
    public String getNumero_calle() {
        return numero_calle;
    }

    public void setNumero_calle(String numero_calle) {
        this.numero_calle = numero_calle;
    }

    @Basic
    @Column(name = "sitio", nullable = true, insertable = true, updatable = true)
    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    @Basic
    @Column(name = "camino", nullable = true, insertable = true, updatable = true)
    public String getCamino() {
        return camino;
    }

    public void setCamino(String camino) {
        this.camino = camino;
    }

    @Basic
    @Column(name = "piso", nullable = true, insertable = true, updatable = true)
    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    @Basic
    @Column(name = "localidad", nullable = true, insertable = true, updatable = true)
    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    @ManyToOne
    @JoinColumn(name = "comuna_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    @Basic
    @Column(name = "comuna_id", nullable = true, insertable = true, updatable = true)
    public Long getComunaId() {
        return comunaId;
    }

    public void setComunaId(Long comunaId) {
        this.comunaId = comunaId;
    }

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Basic
    @Column(name = "region_id", nullable = true, insertable = true, updatable = true)
    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Basic
    @Column(name = "horario_atencion_desde", nullable = true, insertable = true, updatable = true)
    public Date getHorarioAtencionDesde() {
        return horarioAtencionDesde;
    }

    public void setHorarioAtencionDesde(Date horarioAtencionDesde) {
        this.horarioAtencionDesde = horarioAtencionDesde;
    }

    @Basic
    @Column(name = "horario_atencion_hasta", nullable = true, insertable = true, updatable = true)
    public Date getHorarioAtencionHasta() {
        return horarioAtencionHasta;
    }

    public void setHorarioAtencionHasta(Date horarioAtencionHasta) {
        this.horarioAtencionHasta = horarioAtencionHasta;
    }

    @Basic
    @Column(name = "contacto", nullable = true, insertable = true, updatable = true)
    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Basic
    @Column(name = "contacto_obra", nullable = true, insertable = true, updatable = true)
    public String getContactoObra() {
        return contactoObra;
    }

    public void setContactoObra(String contactoObra) {
        this.contactoObra = contactoObra;
    }

    @Basic
    @Column(name = "cargo", nullable = true, insertable = true, updatable = true)
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Basic
    @Column(name = "telefono_movil", nullable = true, insertable = true, updatable = true)
    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    @Basic
    @Column(name = "telefono_fijo", nullable = true, insertable = true, updatable = true)
    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="rel_ficha_tipo",
            joinColumns={@JoinColumn(name="ficha_auxiliar_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="tipo_ficha_auxiliar_id", referencedColumnName="id_tipo_ficha_auxiliar")})
    public List<TipoFichaAuxiliar> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoFichaAuxiliar> tipos) {
        this.tipos = tipos;
    }
}
