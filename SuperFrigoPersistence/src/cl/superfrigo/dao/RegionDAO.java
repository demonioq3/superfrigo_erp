package cl.superfrigo.dao;


import cl.superfrigo.entity.Region;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class RegionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public List<Region> findAll() {
        return em.createQuery("select b from Region b ORDER BY b.id asc",Region.class).getResultList();
    }

    public Region findById(long numericCellValue) {
        List<Region> list = em.createQuery("select b from Region b where b.id=:id",Region.class)
                .setParameter("id", numericCellValue).getResultList();

        for (Region region : list) {
            return region;
        }

        return null;
    }
}
