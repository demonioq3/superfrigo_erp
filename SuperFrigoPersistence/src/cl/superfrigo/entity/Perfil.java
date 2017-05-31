package cl.superfrigo.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by agustinsantiago on 8/20/16.
 */
@Entity
@Table(name = "perfil")

public class Perfil extends BaseEntity implements Serializable {

    final static public String PERFIL_ADMNISTRADOR = "Administrador";
    private Long id;
    private String nombre;
    private List<Capacidad> capacities;


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
    @Column(name = "nombre", nullable = false, insertable = true, updatable = true)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "rel_perfil_capacidad",
            joinColumns = {@JoinColumn(name = "perfil_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "capacidad_id", referencedColumnName = "id")})
    public List<Capacidad> getCapacities() {
        return capacities;
    }

    public void setCapacities(List<Capacidad> capacities) {
        this.capacities = capacities;
    }

}
