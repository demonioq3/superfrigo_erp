package cl.superfrigo.dao;


import cl.superfrigo.entity.Comuna;
import cl.superfrigo.entity.Region;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ComunaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public List<Comuna> findAll() {
        return em.createQuery("select b from Comuna b ORDER BY b.id asc",Comuna.class).getResultList();
    }

    public List<Comuna> findByPadre(Long regionId) {
        return em.createQuery("select b from Comuna b WHERE b.padreId =:regionId ORDER BY b.id asc",Comuna.class)
                .setParameter("regionId", regionId)
                .getResultList();
    }

    public Comuna findById(long id) {
        List<Comuna> list = em.createQuery("select b from Comuna b WHERE b.id =:id ",Comuna.class)
                .setParameter("id", id)
                .getResultList();

        for (Comuna comuna : list) {
            return comuna;
        }

        return null;
    }
}
