package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.UnidadMedida;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class UnidadMedidaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public UnidadMedida getById(Long id) {
        return em.find(UnidadMedida.class, id);
    }

    public List<UnidadMedida> findAll() {
        return em.createQuery("select b from UnidadMedida b ORDER BY b.id asc",UnidadMedida.class).getResultList();
    }

    public boolean delete(UnidadMedida unidadMedida) {

        UnidadMedida toDelete = em.find(UnidadMedida.class,unidadMedida.getId());

        em.remove(toDelete);

        return true;
    }

    public UnidadMedida create(UnidadMedida unidadMedida) {

        em.persist(unidadMedida);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return unidadMedida;
    }

    public UnidadMedida update(UnidadMedida unidadMedida) {
        if ( unidadMedida == null )
            throw new IllegalArgumentException("UnidadMedida can't be null");

        UnidadMedida updated = em.merge(unidadMedida);
        em.flush();

        return updated;
    }

    public boolean existe(String codigo) {
        List<UnidadMedida> unidades =  em.createQuery("select b from UnidadMedida b where b.codigo =:codigo",UnidadMedida.class)
                .setParameter("codigo", codigo).getResultList();

        for (UnidadMedida unidadMedida : unidades) {
            return true;
        }

        return false;
    }

    public UnidadMedida findByCodigo(String codigo) {
        List<UnidadMedida> unidades =  em.createQuery("select b from UnidadMedida b where b.codigo =:codigo",UnidadMedida.class)
                .setParameter("codigo", codigo).getResultList();

        for (UnidadMedida unidadMedida : unidades) {
            return unidadMedida;
        }

        return null;
    }
}
