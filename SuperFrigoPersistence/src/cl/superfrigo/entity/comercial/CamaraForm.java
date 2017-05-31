package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "form_camara")
public class CamaraForm extends BaseEntity implements Serializable {

    private Long id;
    private int numeroCamara;
    private int tipoCamara;
    private String tipoCamaraEspecificacion;
    private int tipoAislacionPiso;
    private int cubiertaPisoModular;
    private int revestimientoPisoModular;
    private String revestimientoPisoModularEspecificacion;
    private double medIntAncho;
    private double medIntLargo;
    private double medIntAlto;
    private double medExtAncho;
    private double medExtLargo;
    private double medExtAlto;
    private int puertaCantidad;
    private int puertaTipo;
    private int cortina;
    private String refrigeracionModelo;
    private String refrigeracionMarca;
    private String refrigeracionVoltaje;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_form_camara", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "numero_camara", nullable = false, insertable = true, updatable = true)
    public int getNumeroCamara() {
        return numeroCamara;
    }

    public void setNumeroCamara(int numeroCamara) {
        this.numeroCamara = numeroCamara;
    }

    @Basic
    @Column(name = "tipo_camara", nullable = false, insertable = true, updatable = true)
    public int getTipoCamara() {
        return tipoCamara;
    }

    public void setTipoCamara(int tipoCamara) {
        this.tipoCamara = tipoCamara;
    }

    @Basic
    @Column(name = "tipo_camara_esp", nullable = false, insertable = true, updatable = true)
    public String getTipoCamaraEspecificacion() {
        return tipoCamaraEspecificacion;
    }

    public void setTipoCamaraEspecificacion(String tipoCamaraEspecificacion) {
        this.tipoCamaraEspecificacion = tipoCamaraEspecificacion;
    }

    @Basic
    @Column(name = "tipo_aislacion_piso", nullable = false, insertable = true, updatable = true)
    public int getTipoAislacionPiso() {
        return tipoAislacionPiso;
    }

    public void setTipoAislacionPiso(int tipoAislacionPiso) {
        this.tipoAislacionPiso = tipoAislacionPiso;
    }

    @Basic
    @Column(name = "cubierta_piso_modular", nullable = false, insertable = true, updatable = true)
    public int getCubiertaPisoModular() {
        return cubiertaPisoModular;
    }

    public void setCubiertaPisoModular(int cubiertaPisoModular) {
        this.cubiertaPisoModular = cubiertaPisoModular;
    }

    @Basic
    @Column(name = "revestimiento_piso_modular", nullable = false, insertable = true, updatable = true)
    public int getRevestimientoPisoModular() {
        return revestimientoPisoModular;
    }

    public void setRevestimientoPisoModular(int revestimientoPisoModular) {
        this.revestimientoPisoModular = revestimientoPisoModular;
    }

    @Basic
    @Column(name = "revestimiento_piso_modular_esp", nullable = false, insertable = true, updatable = true)
    public String getRevestimientoPisoModularEspecificacion() {
        return revestimientoPisoModularEspecificacion;
    }

    public void setRevestimientoPisoModularEspecificacion(String revestimientoPisoModularEspecificacion) {
        this.revestimientoPisoModularEspecificacion = revestimientoPisoModularEspecificacion;
    }

    @Basic
    @Column(name = "med_int_ancho", nullable = false, insertable = true, updatable = true)
    public double getMedIntAncho() {
        return medIntAncho;
    }

    public void setMedIntAncho(double medIntAncho) {
        this.medIntAncho = medIntAncho;
    }

    @Basic
    @Column(name = "med_int_largo", nullable = false, insertable = true, updatable = true)
    public double getMedIntLargo() {
        return medIntLargo;
    }

    public void setMedIntLargo(double medIntLargo) {
        this.medIntLargo = medIntLargo;
    }

    @Basic
    @Column(name = "med_int_alto", nullable = false, insertable = true, updatable = true)
    public double getMedIntAlto() {
        return medIntAlto;
    }

    public void setMedIntAlto(double medIntAlto) {
        this.medIntAlto = medIntAlto;
    }

    @Basic
    @Column(name = "med_ext_ancho", nullable = false, insertable = true, updatable = true)
    public double getMedExtAncho() {
        return medExtAncho;
    }

    public void setMedExtAncho(double medExtAncho) {
        this.medExtAncho = medExtAncho;
    }

    @Basic
    @Column(name = "med_ext_largo", nullable = false, insertable = true, updatable = true)
    public double getMedExtLargo() {
        return medExtLargo;
    }

    public void setMedExtLargo(double medExtLargo) {
        this.medExtLargo = medExtLargo;
    }

    @Basic
    @Column(name = "med_ext_alto", nullable = false, insertable = true, updatable = true)
    public double getMedExtAlto() {
        return medExtAlto;
    }

    public void setMedExtAlto(double medExtAlto) {
        this.medExtAlto = medExtAlto;
    }

    @Basic
    @Column(name = "puerta_cantidad", nullable = false, insertable = true, updatable = true)
    public int getPuertaCantidad() {
        return puertaCantidad;
    }

    public void setPuertaCantidad(int puertaCantidad) {
        this.puertaCantidad = puertaCantidad;
    }

    @Basic
    @Column(name = "puerta_tipo", nullable = false, insertable = true, updatable = true)
    public int getPuertaTipo() {
        return puertaTipo;
    }

    public void setPuertaTipo(int puertaTipo) {
        this.puertaTipo = puertaTipo;
    }

    @Basic
    @Column(name = "cortina", nullable = false, insertable = true, updatable = true)
    public int getCortina() {
        return cortina;
    }

    public void setCortina(int cortina) {
        this.cortina = cortina;
    }

    @Basic
    @Column(name = "refrigeracion_modelo", nullable = false, insertable = true, updatable = true)
    public String getRefrigeracionModelo() {
        return refrigeracionModelo;
    }

    public void setRefrigeracionModelo(String refrigeracionModelo) {
        this.refrigeracionModelo = refrigeracionModelo;
    }

    @Basic
    @Column(name = "refrigeracion_marca", nullable = false, insertable = true, updatable = true)
    public String getRefrigeracionMarca() {
        return refrigeracionMarca;
    }

    public void setRefrigeracionMarca(String refrigeracionMarca) {
        this.refrigeracionMarca = refrigeracionMarca;
    }

    @Basic
    @Column(name = "refrigeracion_voltaje", nullable = false, insertable = true, updatable = true)
    public String getRefrigeracionVoltaje() {
        return refrigeracionVoltaje;
    }

    public void setRefrigeracionVoltaje(String refrigeracionVoltaje) {
        this.refrigeracionVoltaje = refrigeracionVoltaje;
    }
}
