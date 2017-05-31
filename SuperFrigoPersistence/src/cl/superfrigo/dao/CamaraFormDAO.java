package cl.superfrigo.dao;


import cl.superfrigo.entity.comercial.CamaraForm;
import cl.superfrigo.entity.comercial.PuertaForm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class CamaraFormDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public CamaraForm getById(Long id) {
        return em.find(CamaraForm.class, id);
    }

    public List<CamaraForm> findAll() {
        return em.createQuery("select b from CamaraForm b ORDER BY b.id asc",CamaraForm.class).getResultList();
    }

    public boolean delete(CamaraForm camaraForm) {

        CamaraForm toDelete = em.find(CamaraForm.class,camaraForm.getId());

        em.remove(toDelete);

        return true;
    }

    public CamaraForm create(CamaraForm camaraForm) {

        em.persist(camaraForm);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return camaraForm;
    }

    public CamaraForm update(CamaraForm camaraForm) {
        if ( camaraForm == null )
            throw new IllegalArgumentException("CamaraForm can't be null");

        CamaraForm updated = em.merge(camaraForm);
        em.flush();

        return updated;
    }
}
