package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.ProductoRequisicion;
import cl.superfrigo.entity.bodega.EntradaProductoCantidad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ProductoRequisicionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoRequisicion getById(Long id) {
        return em.find(ProductoRequisicion.class, id);
    }

    public List<ProductoRequisicion> findAll() {
        return em.createQuery("select b from ProductoRequisicion b ORDER BY b.id asc",ProductoRequisicion.class).getResultList();
    }

    public boolean delete(ProductoRequisicion productoRequisicion) {

        ProductoRequisicion toDelete = em.find(ProductoRequisicion.class,productoRequisicion.getId());

        em.remove(toDelete);

        return true;
    }

    public ProductoRequisicion create(ProductoRequisicion productoRequisicion) {

        em.persist(productoRequisicion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return productoRequisicion;
    }

    public ProductoRequisicion update(ProductoRequisicion productoRequisicion) {
        if ( productoRequisicion == null )
            throw new IllegalArgumentException("ProductoRequisicion can't be null");

        ProductoRequisicion updated = em.merge(productoRequisicion);
        em.flush();

        return updated;
    }

    public List<ProductoRequisicion> findByRequisicionId(Long id) {
        List<ProductoRequisicion> lista = em.createQuery("select b from ProductoRequisicion b where b.requisicionId =:id",ProductoRequisicion.class)
                .setParameter("id", id).getResultList();

        return lista;
    }
}
