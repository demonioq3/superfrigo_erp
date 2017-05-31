package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.EstadoOrdenCompra;
import cl.superfrigo.entity.abastecimiento.EtapaOrdenCompra;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class EtapaOrdenCompraDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public EtapaOrdenCompra getById(Long id) {
        return em.find(EtapaOrdenCompra.class, id);
    }

    public List<EtapaOrdenCompra> findAll() {
        return em.createQuery("select b from EtapaOrdenCompra b ORDER BY b.id asc",EtapaOrdenCompra.class).getResultList();
    }

}
