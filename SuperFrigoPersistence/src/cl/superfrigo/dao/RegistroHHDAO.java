package cl.superfrigo.dao;


import cl.superfrigo.entity.registros_hh.EstadoHH;
import cl.superfrigo.entity.registros_hh.RegistroHH;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class RegistroHHDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public RegistroHH getById(Long id) {
        return em.find(RegistroHH.class, id);
    }

    public List<RegistroHH> findAll() {
        return em.createQuery("select b from RegistroHH b ORDER BY b.id asc",RegistroHH.class).getResultList();
    }

    public boolean delete(RegistroHH registroHH) {

        RegistroHH toDelete = em.find(RegistroHH.class,registroHH.getId());

        em.remove(toDelete);

        return true;
    }

    public RegistroHH create(RegistroHH registroHH) {

        em.persist(registroHH);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return registroHH;
    }

    public RegistroHH update(RegistroHH registroHH) {
        if ( registroHH == null )
            throw new IllegalArgumentException("RegistroHH can't be null");

        RegistroHH updated = em.merge(registroHH);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM RegistroHH d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<RegistroHH> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<RegistroHH> query = em.createQuery(
                "select d from RegistroHH d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                RegistroHH.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<RegistroHH> res = query.getResultList();

        return res;
    }


    public List<RegistroHH> findByOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        return em.createQuery("select b from RegistroHH b where b.ordenDeTrabajoId=:ordenDeTrabajoId",RegistroHH.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).getResultList();
    }
}
