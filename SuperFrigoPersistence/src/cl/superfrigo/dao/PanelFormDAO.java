package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.Area;
import cl.superfrigo.entity.comercial.PanelForm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PanelFormDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public PanelForm getById(Long id) {
        return em.find(PanelForm.class, id);
    }

    public List<PanelForm> findAll() {
        return em.createQuery("select b from PanelForm b ORDER BY b.id asc",PanelForm.class).getResultList();
    }

    public boolean delete(PanelForm panelForm) {

        PanelForm toDelete = em.find(PanelForm.class,panelForm.getId());

        em.remove(toDelete);

        return true;
    }

    public PanelForm create(PanelForm panelForm) {

        em.persist(panelForm);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return panelForm;
    }

    public PanelForm update(PanelForm panelForm) {
        if ( panelForm == null )
            throw new IllegalArgumentException("PanelForm can't be null");

        PanelForm updated = em.merge(panelForm);
        em.flush();

        return updated;
    }
}
