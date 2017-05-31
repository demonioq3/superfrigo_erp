package cl.superfrigo.dao;


import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.bodega.ConceptoEntrada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class TipoFichaAuxiliarDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public TipoFichaAuxiliar getById(Long id) {
        return em.find(TipoFichaAuxiliar.class, id);
    }

    public List<TipoFichaAuxiliar> findAll() {
        return em.createQuery("select b from TipoFichaAuxiliar b ORDER BY b.id asc",TipoFichaAuxiliar.class).getResultList();
    }

}
