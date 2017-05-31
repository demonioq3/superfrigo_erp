package cl.superfrigo.entity.comercial;

import cl.superfrigo.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by asgco on 02-May-16.
 */
@Entity
@Table(name = "form_panel")
public class PanelForm extends BaseEntity implements Serializable {

    private Long id;
    private int ensamble;
    private int aislacion;
    private int espesor;
    private int poliestireno;
    private int revestimiento;
    private int frisado;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_form_panel", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ensamble", nullable = false, insertable = true, updatable = true)
    public int getEnsamble() {
        return ensamble;
    }

    public void setEnsamble(int ensamble) {
        this.ensamble = ensamble;
    }

    @Basic
    @Column(name = "aislacion", nullable = false, insertable = true, updatable = true)
    public int getAislacion() {
        return aislacion;
    }

    public void setAislacion(int aislacion) {
        this.aislacion = aislacion;
    }

    @Basic
    @Column(name = "espesor", nullable = false, insertable = true, updatable = true)
    public int getEspesor() {
        return espesor;
    }

    public void setEspesor(int espesor) {
        this.espesor = espesor;
    }

    @Basic
    @Column(name = "poliestireno", nullable = false, insertable = true, updatable = true)
    public int getPoliestireno() {
        return poliestireno;
    }

    public void setPoliestireno(int poliestireno) {
        this.poliestireno = poliestireno;
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
    @Column(name = "frisado", nullable = false, insertable = true, updatable = true)
    public int getFrisado() {
        return frisado;
    }

    public void setFrisado(int frisado) {
        this.frisado = frisado;
    }
}
