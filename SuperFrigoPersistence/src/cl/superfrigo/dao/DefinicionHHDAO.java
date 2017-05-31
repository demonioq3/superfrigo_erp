package cl.superfrigo.dao;


import cl.superfrigo.entity.registros_hh.AreaHH;
import cl.superfrigo.entity.registros_hh.DefinicionHH;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class DefinicionHHDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public DefinicionHH getById(Long id) {
        return em.find(DefinicionHH.class, id);
    }

    public List<DefinicionHH> findAll() {
        return em.createQuery("select b from DefinicionHH b ORDER BY b.id asc",DefinicionHH.class).getResultList();
    }

    public boolean delete(DefinicionHH definicionHH) {

        DefinicionHH toDelete = em.createQuery("select b from DefinicionHH b where b.id =:id",DefinicionHH.class)
                .setParameter("id", definicionHH.getId()).getSingleResult();

        em.remove(toDelete);

        return true;
    }

    public DefinicionHH create(DefinicionHH definicionHH) {

        em.persist(definicionHH);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return definicionHH;
    }

    public DefinicionHH update(DefinicionHH definicionHH) {
        if ( definicionHH == null )
            throw new IllegalArgumentException("DefinicionHH can't be null");

        DefinicionHH updated = em.merge(definicionHH);
        em.flush();

        return updated;
    }

    public boolean existe(String codigo) {
        List<DefinicionHH> definiciones =  em.createQuery("select b from DefinicionHH b where b.tipoHH =:codigo",DefinicionHH.class)
                .setParameter("codigo", codigo).getResultList();

        for (DefinicionHH definicion : definiciones) {
            return true;
        }

        return false;
    }

    public DefinicionHH findByAreaHHId(Long areaHHId) {
        List<DefinicionHH> definiciones = em.createQuery("select b from DefinicionHH b where b.areaHHId=:areaHHId",DefinicionHH.class)
                .setParameter("areaHHId", areaHHId).getResultList();

        for (DefinicionHH definicione : definiciones) {
            return definicione;
        }

        return null;
    }
}
