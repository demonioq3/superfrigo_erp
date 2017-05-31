package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.EstadoOrdenCompra;
import cl.superfrigo.entity.bodega.ConceptoEntrada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class EstadoOrdenCompraDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public EstadoOrdenCompra getById(Long id) {
        return em.createQuery("select b from EstadoOrdenCompra b where b.id =:id",EstadoOrdenCompra.class).setParameter("id", id).getSingleResult();
    }

    public List<EstadoOrdenCompra> findAll() {
        return em.createQuery("select b from EstadoOrdenCompra b ORDER BY b.id asc",EstadoOrdenCompra.class).getResultList();
    }

}
