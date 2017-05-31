package cl.superfrigo.dao;


import cl.superfrigo.entity.comercial.MoldeduraForm;
import cl.superfrigo.entity.comercial.PuertaForm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PuertaFormDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public PuertaForm getById(Long id) {
        return em.find(PuertaForm.class, id);
    }

    public List<PuertaForm> findAll() {
        return em.createQuery("select b from PuertaForm b ORDER BY b.id asc",PuertaForm.class).getResultList();
    }

    public boolean delete(PuertaForm puertaForm) {

        PuertaForm toDelete = em.find(PuertaForm.class,puertaForm.getId());

        em.remove(toDelete);

        return true;
    }

    public PuertaForm create(PuertaForm puertaForm) {

        em.persist(puertaForm);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return puertaForm;
    }

    public PuertaForm update(PuertaForm puertaForm) {
        if ( puertaForm == null )
            throw new IllegalArgumentException("PuertaForm can't be null");

        PuertaForm updated = em.merge(puertaForm);
        em.flush();

        return updated;
    }
}
