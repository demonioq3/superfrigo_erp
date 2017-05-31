package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Grupo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class GrupoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Grupo getById(Long id) {
        return em.find(Grupo.class, id);
    }

    public List<Grupo> findAll() {
        return em.createQuery("select b from Grupo b ORDER BY b.id asc",Grupo.class).getResultList();
    }

    public boolean delete(Grupo grupo) {

        Grupo toDelete = em.find(Grupo.class,grupo.getId());

        em.remove(toDelete);

        return true;
    }

    public Grupo create(Grupo grupo) {

        em.persist(grupo);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return grupo;
    }

    public Grupo update(Grupo grupo) {
        if ( grupo == null )
            throw new IllegalArgumentException("Grupo can't be null");

        Grupo updated = em.merge(grupo);
        em.flush();

        return updated;
    }

    public boolean existe(String codigo) {
        List<Grupo> grupos =  em.createQuery("select b from Grupo b where b.codigo =:codigo",Grupo.class)
                .setParameter("codigo", codigo).getResultList();

        for (Grupo grupo : grupos) {
            return true;
        }

        return false;
    }

    public Grupo findByCodigo(String codigo) {
        List<Grupo> grupos =  em.createQuery("select b from Grupo b where b.codigo =:codigo",Grupo.class)
                .setParameter("codigo", codigo).getResultList();

        for (Grupo grupo : grupos) {
            return grupo;
        }

        return null;
    }
}
