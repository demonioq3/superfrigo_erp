package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.Area;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AreaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Area getById(Long id) {
        return em.find(Area.class, id);
    }

    public List<Area> findAll() {
        return em.createQuery("select b from Area b ORDER BY b.id asc",Area.class).getResultList();
    }

    public boolean delete(Area area) {

        Area toDelete = em.find(Area.class,area.getId());

        em.remove(toDelete);

        return true;
    }

    public Area create(Area area) {

        em.persist(area);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return area;
    }

    public Area update(Area area) {
        if ( area == null )
            throw new IllegalArgumentException("Area can't be null");

        Area updated = em.merge(area);
        em.flush();

        return updated;
    }
}
