package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.abastecimiento.ProductoRequisicion;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ProductoOrdenCompraDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoOrdenCompra getById(Long id) {
        return em.find(ProductoOrdenCompra.class, id);
    }

    public List<ProductoOrdenCompra> findAll() {
        return em.createQuery("select b from ProductoOrdenCompra b ORDER BY b.id asc",ProductoOrdenCompra.class).getResultList();
    }

    public boolean delete(ProductoOrdenCompra productoOrdenCompra) {

        ProductoOrdenCompra toDelete = em.find(ProductoOrdenCompra.class,productoOrdenCompra.getId());

        em.remove(toDelete);

        return true;
    }

    public ProductoOrdenCompra create(ProductoOrdenCompra productoOrdenCompra) {

        em.persist(productoOrdenCompra);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return productoOrdenCompra;
    }

    public ProductoOrdenCompra update(ProductoOrdenCompra productoOrdenCompra) {
        if ( productoOrdenCompra == null )
            throw new IllegalArgumentException("ProductoOrdenCompra can't be null");

        ProductoOrdenCompra updated = em.merge(productoOrdenCompra);
        em.flush();

        return updated;
    }

    public List<ProductoOrdenCompra> findByOrdenCompraId(Long id) {
        List<ProductoOrdenCompra> lista = em.createQuery("select b from ProductoOrdenCompra b where b.ordenDeCompraId =:id",ProductoOrdenCompra.class)
                .setParameter("id", id).getResultList();

        return lista;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM ProductoOrdenCompra d" + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<ProductoOrdenCompra> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<ProductoOrdenCompra> query = em.createQuery(
                "select d from ProductoOrdenCompra d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                ProductoOrdenCompra.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<ProductoOrdenCompra> res = query.getResultList();

        return res;
    }

    public List<ProductoOrdenCompra> findByCodigoProducto(String codigoProducto) {
        return em.createQuery("select b from ProductoOrdenCompra b where b.productoRequisicion.fichaProducto.codigoProducto =:codigo ORDER BY b.id asc",ProductoOrdenCompra.class)
                .setParameter("codigo", codigoProducto).getResultList();
    }
}
