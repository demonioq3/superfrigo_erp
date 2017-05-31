package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.bodega.GuiaSalida;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class GuiaSalidaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public GuiaSalida getById(Long id) {
        return em.find(GuiaSalida.class, id);
    }

    public List<GuiaSalida> findAll() {
        return em.createQuery("select b from GuiaSalida b ORDER BY b.id asc",GuiaSalida.class).getResultList();
    }

    public boolean delete(Long id) {

        GuiaSalida toDelete = em.createQuery("select b from GuiaSalida b where b.id =:id",GuiaSalida.class)
                .setParameter("id", id).getSingleResult();

        em.remove(toDelete);

        return true;
    }

    public GuiaSalida create(GuiaSalida guiaSalida) {

        em.persist(guiaSalida);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return guiaSalida;
    }

    public GuiaSalida update(GuiaSalida guiaSalida) {
        if ( guiaSalida == null )
            throw new IllegalArgumentException("GuiaSalida can't be null");

        GuiaSalida updated = em.merge(guiaSalida);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM GuiaSalida d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<GuiaSalida> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<GuiaSalida> query = em.createQuery(
                "select d from GuiaSalida d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                GuiaSalida.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<GuiaSalida> res = query.getResultList();

        return res;
    }

    public GuiaSalida findById(Long id) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.id =:id",GuiaSalida.class)
                .setParameter("id", id).getResultList();

        for (GuiaSalida guiaSalida : resultList) {
            return guiaSalida;
        }

        return null;
    }

    public List<GuiaSalida> findByFechaAndConceptos(Date fechaDesde, Date fechaHasta, Long conceptoId, Long bodegaId) {
        if(conceptoId == 0L && bodegaId == 0L){
            return em.createQuery("select b from GuiaSalida b where b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaSalida.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        }

        if(!(conceptoId == 0L) && bodegaId == 0L){
            return em.createQuery("select b from GuiaSalida b where b.conceptoSalidaId=:conceptoId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaSalida.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("conceptoId", conceptoId)
                    .getResultList();
        }


        if(conceptoId == 0L && !(bodegaId == 0L)){
            return em.createQuery("select b from GuiaSalida b where b.bodegaId=:bodegaId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaSalida.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("bodegaId", bodegaId)
                    .getResultList();
        }

        if(!(conceptoId == 0L) && !(bodegaId == 0L)){
            return em.createQuery("select b from GuiaSalida b where b.bodegaId=:bodegaId and b.conceptoSalidaId=:conceptoId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaSalida.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("conceptoId", conceptoId)
                    .setParameter("bodegaId", bodegaId)
                    .getResultList();
        }

        return null;
    }

    public List<GuiaSalida> findByOrdenDeTrabajoId(Long ordenDeTrabajoId, Date fechaDesde, Date fechaHasta) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.ordenDeTrabajoId =:ordenDeTrabajoId and b.fecha between :fechaDesde and :fechaHasta",GuiaSalida.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByOrdenDeTrabajoIdBodegaId(Long ordenDeTrabajoId, Long bodegaId, Date fechaDesde, Date fechaHasta) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.ordenDeTrabajoId =:ordenDeTrabajoId and b.bodegaId=:bodegaId and b.fecha between :fechaDesde and :fechaHasta",GuiaSalida.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).setParameter("bodegaId", bodegaId).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.ordenDeTrabajoId =:ordenDeTrabajoId",GuiaSalida.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByOrdenDeTrabajoIdBodegaId(Long ordenDeTrabajoId, Long bodegaId) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.ordenDeTrabajoId =:ordenDeTrabajoId and b.bodegaId=:bodegaId",GuiaSalida.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).setParameter("bodegaId", bodegaId).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByCC(Long centroDeCostoId, Date fechaDesde, Date fechaHasta) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.centroDeCostoId =:centroDeCostoId and b.fecha between :fechaDesde and :fechaHasta",GuiaSalida.class)
                .setParameter("centroDeCostoId", centroDeCostoId).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByCCBodegaId(Long centroDeCostoId, Long bodegaId, Date fechaDesde, Date fechaHasta) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.centroDeCostoId =:centroDeCostoId and b.bodegaId=:bodegaId and b.fecha between :fechaDesde and :fechaHasta",GuiaSalida.class)
                .setParameter("centroDeCostoId", centroDeCostoId).setParameter("bodegaId", bodegaId).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByCCBodegaId(Long centroDeCostoId, Long bodegaId) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.centroDeCostoId =:centroDeCostoId and b.bodegaId=:bodegaId",GuiaSalida.class)
                .setParameter("centroDeCostoId", centroDeCostoId).setParameter("bodegaId", bodegaId).getResultList();

        return resultList;
    }

    public List<GuiaSalida> findByCC(Long centroDeCostoId) {
        List<GuiaSalida> resultList = em.createQuery("select b from GuiaSalida b where b.centroDeCostoId =:centroDeCostoId",GuiaSalida.class)
                .setParameter("centroDeCostoId", centroDeCostoId).getResultList();

        return resultList;
    }
}
