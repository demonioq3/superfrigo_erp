package cl.superfrigo.dao;


import cl.superfrigo.entity.comercial.MoldeduraForm;
import cl.superfrigo.entity.comercial.PanelForm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class MolduraFormDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public MoldeduraForm getById(Long id) {
        return em.find(MoldeduraForm.class, id);
    }

    public List<MoldeduraForm> findAll() {
        return em.createQuery("select b from MoldeduraForm b ORDER BY b.id asc",MoldeduraForm.class).getResultList();
    }

    public boolean delete(MoldeduraForm moldeduraForm) {

        MoldeduraForm toDelete = em.find(MoldeduraForm.class,moldeduraForm.getId());

        em.remove(toDelete);

        return true;
    }

    public MoldeduraForm create(MoldeduraForm moldeduraForm) {

        em.persist(moldeduraForm);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return moldeduraForm;
    }

    public MoldeduraForm update(MoldeduraForm moldeduraForm) {
        if ( moldeduraForm == null )
            throw new IllegalArgumentException("MoldeduraForm can't be null");

        MoldeduraForm updated = em.merge(moldeduraForm);
        em.flush();

        return updated;
    }
}
