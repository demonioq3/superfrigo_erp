package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.CentroDeCosto;
import cl.superfrigo.entity.bodega.ConceptoEntrada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class CentroDeCostoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public CentroDeCosto getById(Long id) {
        return em.find(CentroDeCosto.class, id);
    }

    public List<CentroDeCosto> findAll() {
        return em.createQuery("select b from CentroDeCosto b ORDER BY b.id asc",CentroDeCosto.class).getResultList();
    }

    public boolean delete(CentroDeCosto centroDeCosto) {

        CentroDeCosto toDelete = em.find(CentroDeCosto.class,centroDeCosto.getId());

        em.remove(toDelete);

        return true;
    }

    public CentroDeCosto create(CentroDeCosto centroDeCosto) {

        em.persist(centroDeCosto);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return centroDeCosto;
    }

    public CentroDeCosto update(CentroDeCosto centroDeCosto) {
        if ( centroDeCosto == null )
            throw new IllegalArgumentException("CentroDeCosto can't be null");

        CentroDeCosto updated = em.merge(centroDeCosto);
        em.flush();

        return updated;
    }

    public boolean existe(String codigo) {
        List<CentroDeCosto> centros =  em.createQuery("select b from CentroDeCosto b where b.codigo =:codigo",CentroDeCosto.class)
                .setParameter("codigo", codigo).getResultList();

        for (CentroDeCosto centro : centros) {
            return true;
        }

        return false;
    }

}
