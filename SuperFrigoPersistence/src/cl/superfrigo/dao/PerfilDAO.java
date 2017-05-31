package cl.superfrigo.dao;

import cl.superfrigo.entity.Perfil;
import sun.misc.Perf;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by agustinsantiago on 8/20/16.
 */
@Stateless
public class PerfilDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;

    public Perfil getById(Long id) {

        return em.find(Perfil.class, id);
    }

    public boolean delete(Perfil Perfil) {

        Perfil toDelete = em.find(Perfil.class, Perfil.getId());
        em.remove(toDelete);

        return true;
    }

    public List<Perfil> findAll() {
        return em.createQuery("select p from Perfil p order by p.id ASC", Perfil.class).getResultList();
    }

    public Perfil create(Perfil Perfil) {

        em.persist(Perfil);
        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return Perfil;
    }

    public Perfil update(Perfil Perfil) {
        if (Perfil == null)
            throw new IllegalArgumentException("Perfil can't be null");

        Perfil updated = em.merge(Perfil);
        em.flush();

        return updated;
    }

    //Busca los pefiles que no tienen perfiles que extienden de el.
    public List<Perfil> findByPerfil(Long PerfilId) {
        Query query = em.createQuery("select p from Perfil p where p.id = :PerfilId and " +
                " p.id in (select h.id from Perfil h)")
                .setParameter("PerfilId", PerfilId);

        return query.getResultList();
    }

    public Perfil findByNamePerfil(String namePerfil) {
        Query query = em.createQuery("select p from Perfil p where lower(p.nombre) = lower(:namePerfil) ")
                .setParameter("namePerfil", namePerfil);

        List<Perfil> Perfils = query.getResultList();
        for (Perfil Perfil : Perfils) {
            return Perfil;
        }
        return null;
    }

    public List<Perfil> findPerfilByPerfilFather(Long PerfilFather) {
        List<Perfil> Perfils = em.createQuery("select p from Perfil  p where p.PerfilByPadreId = :PerfilFather")
                .setParameter("PerfilFather", PerfilFather)
                .getResultList();

        return Perfils;

    }
}