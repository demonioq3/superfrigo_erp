package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.Area;
import cl.superfrigo.entity.abastecimiento.CondicionPago;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class CondicionPagoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public CondicionPago getById(Long id) {
        return em.find(CondicionPago.class, id);
    }

    public List<CondicionPago> findAll() {
        return em.createQuery("select b from CondicionPago b ORDER BY b.id asc",CondicionPago.class).getResultList();
    }

    public boolean delete(CondicionPago condicionPago) {

        CondicionPago toDelete = em.find(CondicionPago.class,condicionPago.getId());

        em.remove(toDelete);

        return true;
    }

    public CondicionPago create(CondicionPago condicionPago) {

        em.persist(condicionPago);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return condicionPago;
    }

    public CondicionPago update(CondicionPago condicionPago) {
        if ( condicionPago == null )
            throw new IllegalArgumentException("CondicionPago can't be null");

        CondicionPago updated = em.merge(condicionPago);
        em.flush();

        return updated;
    }
}
