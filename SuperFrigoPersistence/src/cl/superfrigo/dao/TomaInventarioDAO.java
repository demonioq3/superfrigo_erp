package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.TomaInventario;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class TomaInventarioDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public TomaInventario getById(Long id) {
        return em.find(TomaInventario.class, id);
    }

    public List<TomaInventario> findAll() {
        return em.createQuery("select b from TomaInventario b ORDER BY b.id asc",TomaInventario.class).getResultList();
    }

    public boolean delete(TomaInventario tomaInventario) {

        TomaInventario toDelete = em.find(TomaInventario.class,tomaInventario.getId());

        em.remove(toDelete);

        return true;
    }

    public TomaInventario create(TomaInventario tomaInventario) {

        em.persist(tomaInventario);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return tomaInventario;
    }

    public TomaInventario update(TomaInventario tomaInventario) {
        if ( tomaInventario == null )
            throw new IllegalArgumentException("TomaInventario can't be null");

        TomaInventario updated = em.merge(tomaInventario);
        em.flush();

        return updated;
    }

    public TomaInventario findByBodegaIdAndFecha(Long bodegaId, Date fecha) {
        List<TomaInventario> list = em.createQuery("select b from TomaInventario b where b.bodegaId=:bodegaId and b.fecha=:fecha",TomaInventario.class)
                .setParameter("bodegaId", bodegaId)
                .setParameter("fecha", fecha)
                .getResultList();

        for (TomaInventario tomaInventario : list) {
            return tomaInventario;
        }

        return null;
    }
}
