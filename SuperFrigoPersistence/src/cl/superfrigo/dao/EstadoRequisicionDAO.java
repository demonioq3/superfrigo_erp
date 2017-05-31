package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.EstadoRequisicion;
import cl.superfrigo.entity.bodega.Bodega;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class EstadoRequisicionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public EstadoRequisicion getById(Long id) {
        return em.createQuery("select b from EstadoRequisicion b where b.id =:id",EstadoRequisicion.class).setParameter("id", id).getSingleResult();
    }

    public List<EstadoRequisicion> findAll() {
        return em.createQuery("select b from EstadoRequisicion b ORDER BY b.id asc",EstadoRequisicion.class).getResultList();
    }

}
