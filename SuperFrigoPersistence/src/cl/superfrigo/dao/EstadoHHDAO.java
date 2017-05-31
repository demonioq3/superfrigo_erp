package cl.superfrigo.dao;


import cl.superfrigo.entity.registros_hh.DefinicionHH;
import cl.superfrigo.entity.registros_hh.EstadoHH;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class EstadoHHDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public EstadoHH getById(Long id) {
        return em.find(EstadoHH.class, id);
    }

    public List<EstadoHH> findAll() {
        return em.createQuery("select b from EstadoHH b ORDER BY b.id asc",EstadoHH.class).getResultList();
    }

    public boolean delete(EstadoHH estadoHH) {

        EstadoHH toDelete = em.find(EstadoHH.class,estadoHH.getId());

        em.remove(toDelete);

        return true;
    }

    public EstadoHH create(EstadoHH estadoHH) {

        em.persist(estadoHH);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return estadoHH;
    }

    public EstadoHH update(EstadoHH estadoHH) {
        if ( estadoHH == null )
            throw new IllegalArgumentException("EstadoHH can't be null");

        EstadoHH updated = em.merge(estadoHH);
        em.flush();

        return updated;
    }
}
