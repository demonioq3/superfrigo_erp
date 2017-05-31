package cl.superfrigo.dao;


import cl.superfrigo.entity.registros_hh.RegistroHH;
import cl.superfrigo.entity.registros_hh.TrabajadorHH;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class TrabajadorHHDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public TrabajadorHH getById(Long id) {
        return em.find(TrabajadorHH.class, id);
    }

    public List<TrabajadorHH> findAll() {
        return em.createQuery("select b from TrabajadorHH b ORDER BY b.id asc",TrabajadorHH.class).getResultList();
    }

    public boolean delete(TrabajadorHH trabajadorHH) {

        TrabajadorHH toDelete = em.find(TrabajadorHH.class,trabajadorHH.getId());

        em.remove(toDelete);

        return true;
    }

    public TrabajadorHH create(TrabajadorHH trabajadorHH) {

        em.persist(trabajadorHH);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return trabajadorHH;
    }

    public TrabajadorHH update(TrabajadorHH trabajadorHH) {
        if ( trabajadorHH == null )
            throw new IllegalArgumentException("TrabajadorHH can't be null");

        TrabajadorHH updated = em.merge(trabajadorHH);
        em.flush();

        return updated;
    }

    public TrabajadorHH findByRut(String rut) {
        List<TrabajadorHH> trabajador =  em.createQuery("select b from TrabajadorHH b where b.rut=:rut",TrabajadorHH.class)
                .setParameter("rut", rut)
                .getResultList();

        if(trabajador != null){
            for (TrabajadorHH trabajadorLista : trabajador) {
                return trabajadorLista;
            }
        }

        return null;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM TrabajadorHH d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<TrabajadorHH> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<TrabajadorHH> query = em.createQuery(
                "select d from TrabajadorHH d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                TrabajadorHH.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<TrabajadorHH> res = query.getResultList();

        return res;
    }
}
