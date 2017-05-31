package cl.superfrigo.dao;


import cl.superfrigo.entity.FichaAuxiliar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class FichaAuxiliarDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public FichaAuxiliar getById(Long id) {
        return em.find(FichaAuxiliar.class, id);
    }

    public List<FichaAuxiliar> findAll() {
        return em.createQuery("select b from FichaAuxiliar b ORDER BY b.id asc",FichaAuxiliar.class).getResultList();
    }

    public boolean delete(FichaAuxiliar fichaAuxiliar) {

        FichaAuxiliar toDelete = em.find(FichaAuxiliar.class, fichaAuxiliar.getId());

        em.remove(toDelete);

        return true;
    }

    public FichaAuxiliar create(FichaAuxiliar fichaAuxiliar) {

        em.persist(fichaAuxiliar);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return fichaAuxiliar;
    }

    public FichaAuxiliar update(FichaAuxiliar fichaAuxiliar) {
        if ( fichaAuxiliar == null )
            throw new IllegalArgumentException("FichaAuxiliar can't be null");

        FichaAuxiliar updated = em.merge(fichaAuxiliar);
        em.flush();

        return updated;
    }

    public FichaAuxiliar findByRut(String rutBusqueda) {
        List<FichaAuxiliar> client =  em.createQuery("select b from FichaAuxiliar b where b.rut=:rut",FichaAuxiliar.class)
                .setParameter("rut", rutBusqueda)
                .getResultList();

        if(client != null){
            for (FichaAuxiliar fichaAuxiliar : client) {
                return fichaAuxiliar;
            }
        }

        return null;

    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM FichaAuxiliar d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<FichaAuxiliar> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<FichaAuxiliar> query = em.createQuery(
                "select d from FichaAuxiliar d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                FichaAuxiliar.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<FichaAuxiliar> res = query.getResultList();

        return res;
    }

}
