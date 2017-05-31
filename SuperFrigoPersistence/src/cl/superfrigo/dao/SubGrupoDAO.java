package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Grupo;
import cl.superfrigo.entity.bodega.SubGrupo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class SubGrupoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public SubGrupo getById(Long id) {
        return em.find(SubGrupo.class, id);
    }

    public List<SubGrupo> findAll() {
        return em.createQuery("select b from SubGrupo b ORDER BY b.id asc",SubGrupo.class).getResultList();
    }

    public boolean delete(SubGrupo subGrupo) {

        List<SubGrupo> list = em.createQuery("select b from SubGrupo b where b.id =:id",SubGrupo.class).setParameter("id", subGrupo.getId()).getResultList();

        for (SubGrupo grupo : list) {
            em.remove(grupo);
            return true;
        }

        return false;
    }

    public SubGrupo create(SubGrupo subGrupo) {

        em.persist(subGrupo);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return subGrupo;
    }

    public SubGrupo update(SubGrupo subGrupo) {
        if ( subGrupo == null )
            throw new IllegalArgumentException("SubGrupo can't be null");

        SubGrupo updated = em.merge(subGrupo);
        em.flush();

        return updated;
    }

    public List<SubGrupo> findByPadreId(Long grupoId) {
        return em.createQuery("select b from SubGrupo b where b.padreId=:padreId",SubGrupo.class)
                .setParameter("padreId", grupoId)
                .getResultList();
    }

    public boolean existe(String codigo) {
        List<SubGrupo> subGrupos =  em.createQuery("select b from SubGrupo b where b.codigo =:codigo",SubGrupo.class)
                .setParameter("codigo", codigo).getResultList();

        for (SubGrupo subGrupo : subGrupos) {
            return true;
        }

        return false;
    }

    public SubGrupo findByCodigo(String codigo) {
        List<SubGrupo> subGrupos =  em.createQuery("select b from SubGrupo b where b.codigo =:codigo",SubGrupo.class)
                .setParameter("codigo", codigo).getResultList();

        for (SubGrupo subGrupo : subGrupos) {
            return subGrupo;
        }

        return null;
    }
}
