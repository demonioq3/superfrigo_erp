package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "form_puerta")
public class PuertaForm extends BaseEntity implements Serializable {

    private Long id;
    private int tipo;
    private int hojasPorPuertas;
    private double medidasRangoLibreAncho;
    private double medidasRangoLibreAlto;
    private double medidasRangoLibreEspesor;
    private int revestimiento;
    private int revestimientoAbs;
    private String revestimientoAbsEspecificacion;
    private String color;
    private int sentidoApertura;
    private int tipoAjuste;
    private int cerrojo;
    private int bumper;
    private int alturaBumper;
    private int otrosAccesorios;
    private int cortinaPvcCantidad;
    private String cortinaPvcMedidas;
    private String cortinaPvcObservaciones;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_form_puerta", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tipo", nullable = false, insertable = true, updatable = true)
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "hojas_por_puertas", nullable = false, insertable = true, updatable = true)
    public int getHojasPorPuertas() {
        return hojasPorPuertas;
    }

    public void setHojasPorPuertas(int hojasPorPuertas) {
        this.hojasPorPuertas = hojasPorPuertas;
    }

    @Basic
    @Column(name = "med_rang_libre_ancho", nullable = false, insertable = true, updatable = true)
    public double getMedidasRangoLibreAncho() {
        return medidasRangoLibreAncho;
    }

    public void setMedidasRangoLibreAncho(double medidasRangoLibreAncho) {
        this.medidasRangoLibreAncho = medidasRangoLibreAncho;
    }

    @Basic
    @Column(name = "med_rang_libre_alto", nullable = false, insertable = true, updatable = true)
    public double getMedidasRangoLibreAlto() {
        return medidasRangoLibreAlto;
    }

    public void setMedidasRangoLibreAlto(double medidasRangoLibreAlto) {
        this.medidasRangoLibreAlto = medidasRangoLibreAlto;
    }

    @Basic
    @Column(name = "med_rang_libre_espesor", nullable = false, insertable = true, updatable = true)
    public double getMedidasRangoLibreEspesor() {
        return medidasRangoLibreEspesor;
    }

    public void setMedidasRangoLibreEspesor(double medidasRangoLibreEspesor) {
        this.medidasRangoLibreEspesor = medidasRangoLibreEspesor;
    }

    @Basic
    @Column(name = "revestimiento", nullable = false, insertable = true, updatable = true)
    public int getRevestimiento() {
        return revestimiento;
    }

    public void setRevestimiento(int revestimiento) {
        this.revestimiento = revestimiento;
    }

    @Basic
    @Column(name = "revestimiento_abs", nullable = false, insertable = true, updatable = true)
    public int getRevestimientoAbs() {
        return revestimientoAbs;
    }

    public void setRevestimientoAbs(int revestimientoAbs) {
        this.revestimientoAbs = revestimientoAbs;
    }

    @Basic
    @Column(name = "revestimiento_abs_esp", nullable = false, insertable = true, updatable = true)
    public String getRevestimientoAbsEspecificacion() {
        return revestimientoAbsEspecificacion;
    }

    public void setRevestimientoAbsEspecificacion(String revestimientoAbsEspecificacion) {
        this.revestimientoAbsEspecificacion = revestimientoAbsEspecificacion;
    }

    @Basic
    @Column(name = "color", nullable = false, insertable = true, updatable = true)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Basic
    @Column(name = "sentido_apertura", nullable = false, insertable = true, updatable = true)
    public int getSentidoApertura() {
        return sentidoApertura;
    }

    public void setSentidoApertura(int sentidoApertura) {
        this.sentidoApertura = sentidoApertura;
    }

    @Basic
    @Column(name = "tipo_ajuste", nullable = false, insertable = true, updatable = true)
    public int getTipoAjuste() {
        return tipoAjuste;
    }

    public void setTipoAjuste(int tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }

    @Basic
    @Column(name = "cerrojo", nullable = false, insertable = true, updatable = true)
    public int getCerrojo() {
        return cerrojo;
    }

    public void setCerrojo(int cerrojo) {
        this.cerrojo = cerrojo;
    }

    @Basic
    @Column(name = "bumper", nullable = false, insertable = true, updatable = true)
    public int getBumper() {
        return bumper;
    }

    public void setBumper(int bumper) {
        this.bumper = bumper;
    }

    @Basic
    @Column(name = "altura_bumper", nullable = false, insertable = true, updatable = true)
    public int getAlturaBumper() {
        return alturaBumper;
    }

    public void setAlturaBumper(int alturaBumper) {
        this.alturaBumper = alturaBumper;
    }

    @Basic
    @Column(name = "otros_accesorios", nullable = false, insertable = true, updatable = true)
    public int getOtrosAccesorios() {
        return otrosAccesorios;
    }

    public void setOtrosAccesorios(int otrosAccesorios) {
        this.otrosAccesorios = otrosAccesorios;
    }

    @Basic
    @Column(name = "cortina_pvc_cantidad", nullable = false, insertable = true, updatable = true)
    public int getCortinaPvcCantidad() {
        return cortinaPvcCantidad;
    }

    public void setCortinaPvcCantidad(int cortinaPvcCantidad) {
        this.cortinaPvcCantidad = cortinaPvcCantidad;
    }

    @Basic
    @Column(name = "cortina_pvc_medidas", nullable = false, insertable = true, updatable = true)
    public String getCortinaPvcMedidas() {
        return cortinaPvcMedidas;
    }

    public void setCortinaPvcMedidas(String cortinaPvcMedidas) {
        this.cortinaPvcMedidas = cortinaPvcMedidas;
    }

    @Basic
    @Column(name = "cortina_pvc_observacion", nullable = false, insertable = true, updatable = true)
    public String getCortinaPvcObservaciones() {
        return cortinaPvcObservaciones;
    }

    public void setCortinaPvcObservaciones(String cortinaPvcObservaciones) {
        this.cortinaPvcObservaciones = cortinaPvcObservaciones;
    }
}
