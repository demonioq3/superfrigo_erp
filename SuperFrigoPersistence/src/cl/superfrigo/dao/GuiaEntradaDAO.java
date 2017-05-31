package cl.superfrigo.dao;

import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.GuiaEntrada;

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
public class GuiaEntradaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public GuiaEntrada getById(Long id) {
        return em.find(GuiaEntrada.class, id);
    }

    public List<GuiaEntrada> findAll() {
        return em.createQuery("select b from GuiaEntrada b ORDER BY b.id asc",GuiaEntrada.class).getResultList();
    }

    public boolean delete(Long id) {

        GuiaEntrada toDelete = em.createQuery("select b from GuiaEntrada b where b.id =:id",GuiaEntrada.class)
                .setParameter("id", id).getSingleResult();

        em.remove(toDelete);

        return true;
    }

    public GuiaEntrada create(GuiaEntrada guiaEntrada) {

        em.persist(guiaEntrada);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return guiaEntrada;
    }

    public GuiaEntrada update(GuiaEntrada guiaEntrada) {
        if ( guiaEntrada == null )
            throw new IllegalArgumentException("GuiaEntrada can't be null");

        GuiaEntrada updated = em.merge(guiaEntrada);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM GuiaEntrada d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<GuiaEntrada> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<GuiaEntrada> query = em.createQuery(
                "select d from GuiaEntrada d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                GuiaEntrada.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<GuiaEntrada> res = query.getResultList();

        return res;
    }

    public GuiaEntrada findById(Long id) {
        List<GuiaEntrada> resultList = em.createQuery("select b from GuiaEntrada b where b.id =:id",GuiaEntrada.class)
                .setParameter("id", id).getResultList();

        for (GuiaEntrada guiaEntrada : resultList) {
            return guiaEntrada;
        }

        return null;
    }

    public List<GuiaEntrada> findByFechaAndConceptos(Date fechaDesde, Date fechaHasta, Long conceptoId, Long bodegaId) {
        if(conceptoId == 0L && bodegaId == 0L){
            return em.createQuery("select b from GuiaEntrada b where b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaEntrada.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .getResultList();
        }

        if(!(conceptoId == 0L) && bodegaId == 0L){
            return em.createQuery("select b from GuiaEntrada b where b.conceptoEntradaId=:conceptoId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaEntrada.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("conceptoId", conceptoId)
                    .getResultList();
        }


        if(conceptoId == 0L && !(bodegaId == 0L)){
            return em.createQuery("select b from GuiaEntrada b where b.bodegaId=:bodegaId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaEntrada.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("bodegaId", bodegaId)
                    .getResultList();
        }

        if(!(conceptoId == 0L) && !(bodegaId == 0L)){
            return em.createQuery("select b from GuiaEntrada b where b.bodegaId=:bodegaId and b.conceptoEntradaId=:conceptoId and b.fecha between :fechaDesde and :fechaHasta ORDER BY b.id asc",GuiaEntrada.class)
                    .setParameter("fechaDesde", fechaDesde)
                    .setParameter("fechaHasta", fechaHasta)
                    .setParameter("conceptoId", conceptoId)
                    .setParameter("bodegaId", bodegaId)
                    .getResultList();
        }

        return null;
    }

    public List<GuiaEntrada> findByBodegaIdGrupoIdSubGrupoIdProductoIdAcumuladoAll(Long bodegaId, Long grupoId, Long subGrupoId, Long fichaProductoId) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProductoId);

        List<GuiaEntrada> guiasByBodega = em.createQuery("select b from GuiaEntrada b where b.bodegaId=:bodegaId",GuiaEntrada.class)
                .setParameter("bodegaId", bodegaId)
                .getResultList();

        for (GuiaEntrada guiaEntrada : guiasByBodega) {
            List<EntradaProductoCantidad> entradaPorGrupoYSubgrupo = new ArrayList<EntradaProductoCantidad>();
            for (EntradaProductoCantidad entradaProductoCantidad : guiaEntrada.getProductos()) {
                if(entradaProductoCantidad.getFichaProducto().getGrupoId().equals(grupoId)){

                }
            }
        }

        return null;
    }

    public List<GuiaEntrada> findByBodegaIdGrupoIdSubGrupoIdProductoIdAcumuladoOnlyStock(Long bodegaId, Long grupoId, Long subGrupoId, Long fichaProductoId) {
        return null;
    }

    public List<GuiaEntrada> findByBodegaIdGrupoIdSubGrupoIdProductoIdFechasAll(Long bodegaId, Long grupoId, Long subGrupoId, Long fichaProductoId, Date fechaDesde, Date fechaHasta) {
        return null;
    }

    public List<GuiaEntrada> findByBodegaIdGrupoIdSubGrupoIdProductoIdFechasOnlyStock(Long bodegaId, Long grupoId, Long subGrupoId, Long fichaProductoId, Date fechaDesde, Date fechaHasta) {
        return null;
    }

    public List<GuiaEntrada> findByProductoFicha(FichaProducto fichaProducto) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<GuiaEntrada> list = em.createQuery("select b from GuiaEntrada b where b.productos in (select c from EntradaProductoCantidad c where c.fichaProductoId =:fichaProductoId)",GuiaEntrada.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .getResultList();

        return list;
    }

    public List<GuiaEntrada> findByProductoFichaAndFechas(FichaProducto fichaProducto, Date fechaDesde, Date fechaHasta) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<GuiaEntrada> list = em.createQuery("select b from GuiaEntrada b where b.productos in (:productosId) and b.fecha between :fechaDesde and :fechaHasta",GuiaEntrada.class)
                .setParameter("productosId", productosId)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }

    public List<GuiaEntrada> findByProductoFichaAndBodegaId(FichaProducto fichaProducto, Long bodegaId) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<GuiaEntrada> list = em.createQuery("select b from GuiaEntrada b where b.productos in (:productosId) and b.bodegaId=:bodegaId",GuiaEntrada.class)
                .setParameter("productosId", productosId)
                .setParameter("bodegaId", bodegaId)
                .getResultList();

        return list;
    }

    public List<GuiaEntrada> findByProductoFichaAndBodegaIdAndFechas(FichaProducto fichaProducto, Long bodegaId, Date fechaDesde, Date fechaHasta) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<GuiaEntrada> list = em.createQuery("select b from GuiaEntrada b where b.productos in (:productosId) and b.bodegaId=:bodegaId and b.fecha between :fechaDesde and :fechaHasta",GuiaEntrada.class)
                .setParameter("productosId", productosId)
                .setParameter("bodegaId", bodegaId)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }
}
