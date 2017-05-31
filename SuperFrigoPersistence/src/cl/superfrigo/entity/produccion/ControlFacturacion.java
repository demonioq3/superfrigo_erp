package cl.superfrigo.entity.produccion;

import cl.superfrigo.entity.BaseEntity;
import cl.superfrigo.entity.FichaAuxiliar;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "control_facturacion")
public class ControlFacturacion extends BaseEntity implements Serializable {

    private Long id;
    private Long fichaAuxiliarId;
    private FichaAuxiliar fichaAuxiliar;
    private Integer numero_factura;
    private Date fecha;
    private Date fechaEmision;
    private Double montoNeto;
    private Double iva;
    private Double montoTotal;
    private Long ordenDeTrabajoId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control_facturacion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ficha_auxiliar_id", nullable = false, insertable = true, updatable = true)
    public Long getFichaAuxiliarId() {
        return fichaAuxiliarId;
    }

    public void setFichaAuxiliarId(Long fichaAuxiliarId) {
        this.fichaAuxiliarId = fichaAuxiliarId;
    }

    @ManyToOne
    @JoinColumn(name = "ficha_auxiliar_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public FichaAuxiliar getFichaAuxiliar() {
        return fichaAuxiliar;
    }

    public void setFichaAuxiliar(FichaAuxiliar fichaAuxiliar) {
        this.fichaAuxiliar = fichaAuxiliar;
    }

    @Basic
    @Column(name = "numero_factura", nullable = false, insertable = true, updatable = true)
    public Integer getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(Integer numero_factura) {
        this.numero_factura = numero_factura;
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
    @Column(name = "fecha_emision", nullable = false, insertable = true, updatable = true)
    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Basic
    @Column(name = "monto_neto", nullable = false, insertable = true, updatable = true)
    public Double getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(Double montoNeto) {
        this.montoNeto = montoNeto;
    }

    @Basic
    @Column(name = "iva", nullable = false, insertable = true, updatable = true)
    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    @Basic
    @Column(name = "monto_total", nullable = false, insertable = true, updatable = true)
    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    @Basic
    @Column(name = "orden_de_trabajo_id", nullable = false, insertable = true, updatable = true)
    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

}
