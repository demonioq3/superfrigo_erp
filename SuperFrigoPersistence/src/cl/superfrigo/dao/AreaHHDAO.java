package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.Area;
import cl.superfrigo.entity.registros_hh.AreaHH;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class AreaHHDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public AreaHH getById(Long id) {
        return em.find(AreaHH.class, id);
    }

    public List<AreaHH> findAll() {
        return em.createQuery("select b from AreaHH b ORDER BY b.id asc",AreaHH.class).getResultList();
    }

    public List<AreaHH> findByAreaHHId() {
        return em.createQuery("select b from AreaHH b ORDER BY b asc",AreaHH.class).getResultList();
    }

    public boolean delete(AreaHH area) {

        AreaHH toDelete = em.find(AreaHH.class,area.getId());

        em.remove(toDelete);

        return true;
    }

    public AreaHH create(AreaHH area) {

        em.persist(area);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return area;
    }

    public AreaHH update(AreaHH area) {
        if ( area == null )
            throw new IllegalArgumentException("AreaHH can't be null");

        AreaHH updated = em.merge(area);
        em.flush();

        return updated;
    }

    public boolean existe(String nombre) {
        return false;
    }
}
