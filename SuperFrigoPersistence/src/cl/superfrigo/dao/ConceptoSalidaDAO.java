package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.ConceptoEntrada;
import cl.superfrigo.entity.bodega.ConceptoSalida;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ConceptoSalidaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public List<ConceptoSalida> findAll() {
        return em.createQuery("select b from ConceptoSalida b ORDER BY b.id asc",ConceptoSalida.class).getResultList();
    }

}
