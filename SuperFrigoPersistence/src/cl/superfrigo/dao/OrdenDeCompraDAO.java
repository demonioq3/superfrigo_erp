package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.EstadoOrdenCompra;
import cl.superfrigo.entity.abastecimiento.EtapaOrdenCompra;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.Requisicion;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class OrdenDeCompraDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public OrdenDeCompra getById(Long id) {
        return em.createQuery("select b from OrdenDeCompra b where b.id =:id",OrdenDeCompra.class).setParameter("id", id).getSingleResult();
    }

    public List<OrdenDeCompra> findAll() {
        return em.createQuery("select b from OrdenDeCompra b ORDER BY b.id asc",OrdenDeCompra.class).getResultList();
    }

    public boolean delete(OrdenDeCompra ot) {

        OrdenDeCompra toDelete = em.find(OrdenDeCompra.class,ot.getId());

        em.remove(toDelete);

        return true;
    }

    public OrdenDeCompra create(OrdenDeCompra ot) {

        em.persist(ot);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return ot;
    }

    public OrdenDeCompra update(OrdenDeCompra ot) {
        if ( ot == null )
            throw new IllegalArgumentException("OrdenDeCompra can't be null");

        OrdenDeCompra updated = em.merge(ot);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM OrdenDeCompra d" + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<OrdenDeCompra> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<OrdenDeCompra> query = em.createQuery(
                "select d from OrdenDeCompra d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                OrdenDeCompra.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<OrdenDeCompra> res = query.getResultList();

        return res;
    }

    public List<OrdenDeCompra> findByFichaProducto(String codigoProducto) {
        return em.createQuery("select b from OrdenDeCompra b where b.productos in (select c from ProductoOrdenCompra c where c.productoRequisicion.fichaProducto.codigoProducto=:codigo)",OrdenDeCompra.class).setParameter("codigo", codigoProducto).getResultList();
    }

    public OrdenDeCompra findById(Long id) {
        List<OrdenDeCompra> resultList = em.createQuery("select b from OrdenDeCompra b where b.id =:id",OrdenDeCompra.class)
                .setParameter("id", id).getResultList();

        for (OrdenDeCompra ordenDeCompra : resultList) {
            return ordenDeCompra;
        }

        return null;
    }

    public List<OrdenDeCompra> queryList1o1(Date fechaDesde, Date fechaHasta, Long centroDeCostoId) {
        List<OrdenDeCompra> resultList = new ArrayList<OrdenDeCompra>();

        if(centroDeCostoId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where b.estadoOrdenCompraId = " + EstadoOrdenCompra.APROBADA + " and b.etapaOrdenCompraId = " + EtapaOrdenCompra.PARCIAL +
                    " and b.fecha between :fechaDesde and :fechaHasta order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();

        } else {
            resultList = em.createQuery("select b from OrdenDeCompra b where b.estadoOrdenCompraId = " + EstadoOrdenCompra.APROBADA + " and b.etapaOrdenCompraId = " + EtapaOrdenCompra.PARCIAL +
                    " and b.centroDeCostoId =:centroDeCostoId and b.fecha between :fechaDesde and :fechaHasta order by b.id asc",OrdenDeCompra.class)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        }

        return resultList;
    }

    public List<OrdenDeCompra> queryList1o2(Date fechaDesde, Date fechaHasta, Long centroDeCostoId, Long ordenDeCompraId) {
        List<OrdenDeCompra> resultList = new ArrayList<OrdenDeCompra>();

        if(centroDeCostoId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where b.estadoOrdenCompraId = " + EstadoOrdenCompra.APROBADA + " and b.etapaOrdenCompraId = " + EtapaOrdenCompra.PARCIAL +
                    " and b.fecha between :fechaDesde and :fechaHasta and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else {
            resultList = em.createQuery("select b from OrdenDeCompra b where b.estadoOrdenCompraId = " + EstadoOrdenCompra.APROBADA + " and b.etapaOrdenCompraId = " + EtapaOrdenCompra.PARCIAL +
                    " and b.centroDeCostoId =:centroDeCostoId and b.fecha between :fechaDesde and :fechaHasta and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        }

        return resultList;
    }

    public List<OrdenDeCompra> queryList2o1(Date fechaDesde, Date fechaHasta, Long centroDeCostoId, Long estadoId, Long etapaId) {
        List<OrdenDeCompra> resultList = new ArrayList<OrdenDeCompra>();

        if(centroDeCostoId == 0 && estadoId == 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where b.fecha between :fechaDesde and :fechaHasta order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId == 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId != 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.estadoOrdenCompraId =:estadoId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("estadoId", estadoId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId == 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.etapaOrdenCompraId =:etapaId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("etapaId", etapaId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId != 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.estadoOrdenCompraId =:estadoId and b.etapaOrdenCompraId =:etapaId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("estadoId", estadoId)
                    .setParameter("etapaId", etapaId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId != 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.estadoOrdenCompraId =:estadoId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("estadoId", estadoId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId == 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.etapaOrdenCompraId =:etapaId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("etapaId", etapaId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId != 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.estadoOrdenCompraId =:estadoId and b.etapaOrdenCompraId =:etapaId order by b.id asc",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("estadoId", estadoId)
                    .setParameter("etapaId", etapaId)
                    .getResultList();

        }

        return resultList;
    }

    public List<OrdenDeCompra> queryList2o2(Date fechaDesde, Date fechaHasta, Long centroDeCostoId, Long estadoId, Long etapaId, Long ordenDeCompraId) {
        List<OrdenDeCompra> resultList = new ArrayList<OrdenDeCompra>();

        if(centroDeCostoId == 0 && estadoId == 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId == 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId != 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.estadoOrdenCompraId =:estadoId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("estadoId", estadoId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId == 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.etapaOrdenCompraId =:etapaId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("etapaId", etapaId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId != 0 && estadoId != 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta and b.centroDeCostoId =:centroDeCostoId" +
                    " and b.estadoOrdenCompraId =:estadoId and b.etapaOrdenCompraId =:etapaId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("centroDeCostoId", centroDeCostoId)
                    .setParameter("estadoId", estadoId)
                    .setParameter("etapaId", etapaId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId != 0 && etapaId == 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.estadoOrdenCompraId =:estadoId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("estadoId", estadoId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId == 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.etapaOrdenCompraId =:etapaId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("etapaId", etapaId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();

        } else if(centroDeCostoId == 0 && estadoId != 0 && etapaId != 0){
            resultList = em.createQuery("select b from OrdenDeCompra b where  b.fecha between :fechaDesde and :fechaHasta " +
                    " and b.estadoOrdenCompraId =:estadoId and b.etapaOrdenCompraId =:etapaId and b.id =:ordenDeCompraId",OrdenDeCompra.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("estadoId", estadoId)
                    .setParameter("etapaId", etapaId)
                    .setParameter("ordenDeCompraId", ordenDeCompraId)
                    .getResultList();
        }

        return resultList;
    }
}
