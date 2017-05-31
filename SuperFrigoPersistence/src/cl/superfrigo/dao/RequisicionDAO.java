package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.EstadoRequisicion;
import cl.superfrigo.entity.abastecimiento.Requisicion;
import cl.superfrigo.entity.bodega.Bodega;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class RequisicionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Requisicion getById(Long id) {
        return em.createQuery("select b from Requisicion b where b.id =:id",Requisicion.class).setParameter("id", id).getSingleResult();
    }

    public List<Requisicion> findAll() {
        return em.createQuery("select b from Requisicion b ORDER BY b.id asc",Requisicion.class).getResultList();
    }

    public boolean delete(Requisicion requisicion) {

        Requisicion toDelete = em.find(Requisicion.class,requisicion.getId());

        em.remove(toDelete);

        return true;
    }

    public Requisicion create(Requisicion requisicion) {

        em.persist(requisicion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return requisicion;
    }

    public Requisicion update(Requisicion requisicion) {
        if ( requisicion == null )
            throw new IllegalArgumentException("Requisicion can't be null");

        Requisicion updated = em.merge(requisicion);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM Requisicion d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<Requisicion> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<Requisicion> query = em.createQuery(
                "select d from Requisicion d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                Requisicion.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<Requisicion> res = query.getResultList();

        return res;
    }

    public List<Requisicion> findRequisicionesPendientes() {
        return em.createQuery("select b from Requisicion b where b.estadoRequisicionId=:estadoId",Requisicion.class)
                .setParameter("estadoId", EstadoRequisicion.PENDIENTE).getResultList();
    }

    public List<Requisicion> findByFechasAndAreaIdAndEstadoId(Date fechaDesde, Date fechaHasta, Long areaId, Long estadoId) {
        if(areaId == 0L && estadoId == 0L){
            return em.createQuery("select b from Requisicion b where b.fechaEmision between :fechaDesde and :fechaHasta ORDER BY b.id asc",Requisicion.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        }

        if(!(areaId == 0L) && estadoId == 0L){
            return em.createQuery("select b from Requisicion b where b.areaId=:areaId and b.fechaEmision between :fechaDesde and :fechaHasta ORDER BY b.id asc",Requisicion.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("areaId", areaId)
                    .getResultList();
        }


        if(areaId == 0L && !(estadoId == 0L)){
            return em.createQuery("select b from Requisicion b where b.estadoRequisicionId=:estadoId and b.fechaEmision between :fechaDesde and :fechaHasta ORDER BY b.id asc",Requisicion.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("estadoId", estadoId)
                    .getResultList();
        }

        if(!(areaId == 0L) && !(estadoId == 0L)){
            return em.createQuery("select b from Requisicion b where b.areaId=:areaId and b.estadoRequisicionId=:estadoId and b.fechaEmision between :fechaDesde and :fechaHasta ORDER BY b.id asc",Requisicion.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("areaId", areaId)
                    .setParameter("estadoId", estadoId)
                    .getResultList();
        }

        return null;
    }
}
