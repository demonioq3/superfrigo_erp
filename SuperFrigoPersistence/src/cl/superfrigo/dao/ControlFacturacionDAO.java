package cl.superfrigo.dao;


import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.produccion.ControlFacturacion;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ControlFacturacionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ControlFacturacion getById(Long id) {
        return em.find(ControlFacturacion.class, id);
    }

    public List<ControlFacturacion> findAll() {
        return em.createQuery("select b from ControlFacturacion b ORDER BY b.id asc",ControlFacturacion.class).getResultList();
    }

    public boolean delete(ControlFacturacion controlFacturacion) {

        ControlFacturacion toDelete = em.find(ControlFacturacion.class,controlFacturacion.getId());

        em.remove(toDelete);

        return true;
    }

    public ControlFacturacion create(ControlFacturacion controlFacturacion) {

        em.persist(controlFacturacion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return controlFacturacion;
    }

    public ControlFacturacion update(ControlFacturacion controlFacturacion) {
        if ( controlFacturacion == null )
            throw new IllegalArgumentException("ControlFacturacion can't be null");

        ControlFacturacion updated = em.merge(controlFacturacion);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM ControlFacturacion d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<ControlFacturacion> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<ControlFacturacion> query = em.createQuery(
                "select d from ControlFacturacion d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                ControlFacturacion.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<ControlFacturacion> res = query.getResultList();

        return res;
    }

    public Double findValorFacturacionByOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        List<ControlFacturacion> lista = em.createQuery("select b from ControlFacturacion b where b.ordenDeTrabajoId=:ordenDeTrabajoId",ControlFacturacion.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).getResultList();

        Double valorTotal = 0D;
        for (ControlFacturacion controlFacturacion : lista) {
            valorTotal += controlFacturacion.getMontoNeto();
        }

        return valorTotal;
    }
}
