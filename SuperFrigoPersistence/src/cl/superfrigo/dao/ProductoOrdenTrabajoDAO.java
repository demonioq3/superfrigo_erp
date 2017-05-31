package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.comercial.ProductoOrdenTrabajo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class ProductoOrdenTrabajoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoOrdenTrabajo getById(Long id) {
        return em.find(ProductoOrdenTrabajo.class, id);
    }

    public ProductoOrdenTrabajo findById(Long id) {
        List<ProductoOrdenTrabajo> list = em.createQuery("select b from ProductoOrdenTrabajo b where b.id =:id",ProductoOrdenTrabajo.class).setParameter("id", id).getResultList();

        for (ProductoOrdenTrabajo productoOrdenTrabajo : list) {
            return productoOrdenTrabajo;
        }

        return null;
    }

    public List<ProductoOrdenTrabajo> findAll() {
        return em.createQuery("select b from ProductoOrdenTrabajo b ORDER BY b.id asc",ProductoOrdenTrabajo.class).getResultList();
    }

    public boolean delete(ProductoOrdenTrabajo productoOrdenTrabajo) {

        ProductoOrdenTrabajo toDelete = em.createQuery("select b from ProductoOrdenTrabajo b where b.id =:id",ProductoOrdenTrabajo.class)
                .setParameter("id", productoOrdenTrabajo.getId()).getSingleResult();

        em.remove(toDelete);

        return true;
    }

    public boolean deleteDirectly(ProductoOrdenTrabajo productoOrdenTrabajo) {
        em.remove(productoOrdenTrabajo);
        return true;
    }

    public ProductoOrdenTrabajo create(ProductoOrdenTrabajo productoOrdenTrabajo) {

        em.persist(productoOrdenTrabajo);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return productoOrdenTrabajo;
    }

    public ProductoOrdenTrabajo update(ProductoOrdenTrabajo productoOrdenTrabajo) {
        if ( productoOrdenTrabajo == null )
            throw new IllegalArgumentException("ProductoOrdenTrabajo can't be null");

        ProductoOrdenTrabajo updated = em.merge(productoOrdenTrabajo);
        em.flush();

        return updated;
    }

    public List<ProductoOrdenTrabajo> findByOrdenTrabajoId(Long id) {
        List<ProductoOrdenTrabajo> lista = em.createQuery("select b from ProductoOrdenTrabajo b where b.ordenDeTrabajoId =:id",ProductoOrdenTrabajo.class)
                .setParameter("id", id).getResultList();

        return lista;
    }

    public Double findValorOTByOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        List<ProductoOrdenTrabajo> lista = em.createQuery("select b from ProductoOrdenTrabajo b where b.ordenDeTrabajoId =:ordenDeTrabajoId",ProductoOrdenTrabajo.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId).getResultList();

        Double valorTotal = 0D;
        for (ProductoOrdenTrabajo productoOrdenTrabajo : lista) {
            valorTotal += productoOrdenTrabajo.getPrecioTotal();
        }

        return valorTotal;
    }
}
