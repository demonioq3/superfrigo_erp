package cl.superfrigo.dao;


import cl.superfrigo.entity.produccion.ControlProgramacion;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ControlProgramacionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ControlProgramacion getById(Long id) {
        return em.find(ControlProgramacion.class, id);
    }

    public List<ControlProgramacion> findAll() {
        return em.createQuery("select b from ControlProgramacion b ORDER BY b.id asc",ControlProgramacion.class).getResultList();
    }

    public boolean delete(ControlProgramacion controlProgramacion) {

        ControlProgramacion toDelete = em.find(ControlProgramacion.class,controlProgramacion.getId());

        em.remove(toDelete);

        return true;
    }

    public ControlProgramacion create(ControlProgramacion controlProgramacion) {

        em.persist(controlProgramacion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return controlProgramacion;
    }

    public ControlProgramacion update(ControlProgramacion controlProgramacion) {
        if ( controlProgramacion == null )
            throw new IllegalArgumentException("ControlProgramacion can't be null");

        ControlProgramacion updated = em.merge(controlProgramacion);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM ControlProgramacion d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<ControlProgramacion> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<ControlProgramacion> query = em.createQuery(
                "select d from ControlProgramacion d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                ControlProgramacion.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<ControlProgramacion> res = query.getResultList();

        return res;
    }

}
