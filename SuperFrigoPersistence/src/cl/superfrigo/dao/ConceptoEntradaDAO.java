package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.ConceptoEntrada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ConceptoEntradaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public List<ConceptoEntrada> findAll() {
        return em.createQuery("select b from ConceptoEntrada b ORDER BY b.id asc",ConceptoEntrada.class).getResultList();
    }

}
