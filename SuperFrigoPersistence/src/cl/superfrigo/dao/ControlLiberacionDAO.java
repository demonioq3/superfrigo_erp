package cl.superfrigo.dao;


import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.produccion.ControlLiberacion;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ControlLiberacionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ControlLiberacion getById(Long id) {
        return em.find(ControlLiberacion.class, id);
    }

    public List<ControlLiberacion> findAll() {
        return em.createQuery("select b from ControlLiberacion b ORDER BY b.id asc",ControlLiberacion.class).getResultList();
    }

    public boolean delete(ControlLiberacion controlLiberacion) {

        ControlLiberacion toDelete = em.find(ControlLiberacion.class,controlLiberacion.getId());

        em.remove(toDelete);

        return true;
    }

    public ControlLiberacion create(ControlLiberacion controlLiberacion) {

        em.persist(controlLiberacion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return controlLiberacion;
    }

    public ControlLiberacion update(ControlLiberacion controlLiberacion) {
        if ( controlLiberacion == null )
            throw new IllegalArgumentException("ControlLiberacion can't be null");

        ControlLiberacion updated = em.merge(controlLiberacion);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM ControlLiberacion d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<ControlLiberacion> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<ControlLiberacion> query = em.createQuery(
                "select d from ControlLiberacion d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                ControlLiberacion.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<ControlLiberacion> res = query.getResultList();

        return res;
    }

}
