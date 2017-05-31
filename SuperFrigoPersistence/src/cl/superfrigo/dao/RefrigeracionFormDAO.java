package cl.superfrigo.dao;


import cl.superfrigo.entity.comercial.PuertaForm;
import cl.superfrigo.entity.comercial.RefrigeracionForm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class RefrigeracionFormDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public RefrigeracionForm getById(Long id) {
        return em.find(RefrigeracionForm.class, id);
    }

    public List<RefrigeracionForm> findAll() {
        return em.createQuery("select b from RefrigeracionForm b ORDER BY b.id asc",RefrigeracionForm.class).getResultList();
    }

    public boolean delete(RefrigeracionForm refrigeracionForm) {

        RefrigeracionForm toDelete = em.find(RefrigeracionForm.class,refrigeracionForm.getId());

        em.remove(toDelete);

        return true;
    }

    public RefrigeracionForm create(RefrigeracionForm refrigeracionForm) {

        em.persist(refrigeracionForm);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return refrigeracionForm;
    }

    public RefrigeracionForm update(RefrigeracionForm refrigeracionForm) {
        if ( refrigeracionForm == null )
            throw new IllegalArgumentException("RefrigeracionForm can't be null");

        RefrigeracionForm updated = em.merge(refrigeracionForm);
        em.flush();

        return updated;
    }
}
