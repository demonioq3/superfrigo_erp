package cl.superfrigo.dao;

import cl.superfrigo.entity.Capacidad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by agustinsantiago on 8/20/16.
 */
@Stateless
public class CapacidadDAO {
    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;

    public Capacidad getById(Long id) {
        return em.find(Capacidad.class,id);
    }

    public boolean delete(Capacidad Capacidad) {

        Capacidad toDelete = em.find(Capacidad.class,Capacidad.getId());
        em.remove(toDelete);

        return true;
    }

    public Capacidad getByName(String name) {
        List<Capacidad> lista = em.createQuery("select c from Capacidad c",Capacidad.class).getResultList();

        for (Capacidad Capacidad: lista) {
            if(Capacidad.getNombre().equals(name)){
                return Capacidad;
            }
        }

        return null;
    }

    public List<Capacidad> findAll() {
        return  em.createQuery("select c from Capacidad c",Capacidad.class).getResultList();
    }

    public Capacidad create(Capacidad Capacidad) {

        em.persist(Capacidad);
        em.flush();

        return Capacidad;
    }

    public Capacidad update(Capacidad Capacidad) {
        if ( Capacidad == null )
            throw new IllegalArgumentException("Capacidad can't be null");

        Capacidad updated = em.merge(Capacidad);
        em.flush();

        return updated;
    }

    public Capacidad findByName (String nameCapacidad){
        Query query =  em.createQuery("select c from Capacidad c where lower(c.nombre)= lower(:nameCapacidad) ")
                .setParameter("nameCapacidad",nameCapacidad);

        List<Capacidad> Capacidad =  query.getResultList();
        for (Capacidad Capacidad1 : Capacidad) {
            return Capacidad1;
        }

        return null;
    }

}
