package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Bodega;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class BodegaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Bodega getById(Long id) {
        return em.find(Bodega.class, id);
    }

    public Bodega findById(Long id) {
        List<Bodega> lista = em.createQuery("select b from Bodega b where b.id=:id",Bodega.class).setParameter("id", id).getResultList();

        for (Bodega bodega : lista) {
            return bodega;
        }

        return null;
    }

    public List<Bodega> findAll() {
        return em.createQuery("select b from Bodega b ORDER BY b.id asc",Bodega.class).getResultList();
    }

    public boolean delete(Bodega bodega) {

        Bodega toDelete = em.find(Bodega.class,bodega.getId());

        em.remove(toDelete);

        return true;
    }

    public Bodega create(Bodega bodega) {

        em.persist(bodega);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return bodega;
    }

    public Bodega update(Bodega bodega) {
        if ( bodega == null )
            throw new IllegalArgumentException("Bodega can't be null");

        Bodega updated = em.merge(bodega);
        em.flush();

        return updated;
    }

    public boolean existe(String codigo) {
        List<Bodega> bodegas =  em.createQuery("select b from Bodega b where b.codigo =:codigo",Bodega.class)
                .setParameter("codigo", codigo).getResultList();

        for (Bodega bodega : bodegas) {
            return true;
        }

        return false;
    }
}
