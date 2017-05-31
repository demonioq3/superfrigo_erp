package cl.superfrigo.dao;


import cl.superfrigo.entity.produccion.ControlDespacho;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ControlDespachoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ControlDespacho getById(Long id) {
        return em.find(ControlDespacho.class, id);
    }

    public List<ControlDespacho> findAll() {
        return em.createQuery("select b from ControlDespacho b ORDER BY b.id asc",ControlDespacho.class).getResultList();
    }

    public boolean delete(ControlDespacho controlDespacho) {

        ControlDespacho toDelete = em.find(ControlDespacho.class,controlDespacho.getId());

        em.remove(toDelete);

        return true;
    }

    public ControlDespacho create(ControlDespacho controlDespacho) {

        em.persist(controlDespacho);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return controlDespacho;
    }

    public ControlDespacho update(ControlDespacho controlDespacho) {
        if ( controlDespacho == null )
            throw new IllegalArgumentException("ControlDespacho can't be null");

        ControlDespacho updated = em.merge(controlDespacho);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM ControlDespacho d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<ControlDespacho> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<ControlDespacho> query = em.createQuery(
                "select d from ControlDespacho d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                ControlDespacho.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<ControlDespacho> res = query.getResultList();

        return res;
    }

    public Double findValorOTByOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        List<ControlDespacho> list = em.createQuery("select b from ControlDespacho b where b.ordenDeTrabajoId=:ordenDeTrabajoId",ControlDespacho.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).getResultList();

        return null;
    }
}
