package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.proyectos.Receta;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class RecetaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Receta getById(Long id) {
        return em.find(Receta.class, id);
    }

    public List<Receta> findAll() {
        return em.createQuery("select b from Receta b ORDER BY b.id asc",Receta.class).getResultList();
    }

    public boolean delete(Receta receta) {

        Receta toDelete = em.find(Receta.class,receta.getId());

        em.remove(toDelete);

        return true;
    }

    public Receta create(Receta receta) {

        em.persist(receta);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return receta;
    }

    public Receta update(Receta receta) {
        if ( receta == null )
            throw new IllegalArgumentException("Receta can't be null");

        Receta updated = em.merge(receta);
        em.flush();

        return updated;
    }

    public List<Receta> getByFichaProductoId(Long idProducto) {
        return em.createQuery("select b from Receta b where b.productoId =:idProducto ORDER BY b.id asc",Receta.class)
                .setParameter("idProducto", idProducto).getResultList();
    }

    public List<Receta> findByCodigoProducto(String codigoProducto) {
        return em.createQuery("select b from Receta b where b.producto.codigoProducto =:codigoProducto ORDER BY b.id asc",Receta.class)
                .setParameter("codigoProducto", codigoProducto).getResultList();
    }

    public Receta getByCodigoReceta(String codigo) {
        List<Receta> resultList = em.createQuery("select b from Receta b where b.codigo =:codigo",Receta.class)
                .setParameter("codigo", codigo).getResultList();

        for (Receta receta : resultList) {
            return receta;
        }

        return null;
    }

    public void marcarTodosBaseFalse() {
        Query q = em.createQuery("update Receta b set b.base = false");
        q.executeUpdate();
    }

    public Receta findRecetaBaseByCodigoProducto(String codigoProducto) {
        List<Receta> resultList = em.createQuery("select b from Receta b where b.producto.codigoProducto =:codigo and b.base is true",Receta.class)
                .setParameter("codigo", codigoProducto).getResultList();

        for (Receta receta : resultList) {
            return receta;
        }

        return null;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM Receta d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<Receta> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<Receta> query = em.createQuery(
                "select d from Receta d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                Receta.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<Receta> res = query.getResultList();

        return res;
    }

    public Receta findById(Long recetaId) {
        List<Receta> resultList = em.createQuery("select b from Receta b where b.id =:recetaId",Receta.class)
                .setParameter("recetaId", recetaId).getResultList();

        for (Receta receta : resultList) {
            return receta;
        }

        return null;
    }
}
