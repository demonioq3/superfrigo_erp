package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "form_refrigeracion")
public class RefrigeracionForm extends BaseEntity implements Serializable {

    private Long id;
    private int numeroEquipo;
    private String marca;
    private String modelo;
    private String voltaje;
    private int ubicacion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_form_refrigeracion", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "numero_equipo", nullable = false, insertable = true, updatable = true)
    public int getNumeroEquipo() {
        return numeroEquipo;
    }

    public void setNumeroEquipo(int numeroEquipo) {
        this.numeroEquipo = numeroEquipo;
    }

    @Basic
    @Column(name = "marca", nullable = false, insertable = true, updatable = true)
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Basic
    @Column(name = "modelo", nullable = false, insertable = true, updatable = true)
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Basic
    @Column(name = "voltaje", nullable = false, insertable = true, updatable = true)
    public String getVoltaje() {
        return voltaje;
    }

    public void setVoltaje(String voltaje) {
        this.voltaje = voltaje;
    }

    @Basic
    @Column(name = "ubicacion", nullable = false, insertable = true, updatable = true)
    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }
}
