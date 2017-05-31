package cl.superfrigo.dao;


import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.entity.solicitud_material.SolicitudMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class SolicitudMaterialDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public SolicitudMaterial getById(Long id) {
        return em.find(SolicitudMaterial.class, id);
    }

    public List<SolicitudMaterial> findAll() {
        return em.createQuery("select b from SolicitudMaterial b ORDER BY b.id asc",SolicitudMaterial.class).getResultList();
    }

    public boolean delete(SolicitudMaterial receta) {

        SolicitudMaterial toDelete = em.find(SolicitudMaterial.class,receta.getId());

        em.remove(toDelete);

        return true;
    }

    public SolicitudMaterial create(SolicitudMaterial receta) {

        em.persist(receta);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return receta;
    }

    public SolicitudMaterial update(SolicitudMaterial receta) {
        if ( receta == null )
            throw new IllegalArgumentException("SolicitudMaterial can't be null");

        SolicitudMaterial updated = em.merge(receta);
        em.flush();

        return updated;
    }

    public SolicitudMaterial findById(Long id) {
        List<SolicitudMaterial> resultList = em.createQuery("select b from SolicitudMaterial b where b.id =:id",SolicitudMaterial.class)
                .setParameter("id", id).getResultList();

        for (SolicitudMaterial guiaEntrada : resultList) {
            return guiaEntrada;
        }

        return null;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM SolicitudMaterial d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<SolicitudMaterial> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<SolicitudMaterial> query = em.createQuery(
                "select d from SolicitudMaterial d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                SolicitudMaterial.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<SolicitudMaterial> res = query.getResultList();

        return res;
    }
}
