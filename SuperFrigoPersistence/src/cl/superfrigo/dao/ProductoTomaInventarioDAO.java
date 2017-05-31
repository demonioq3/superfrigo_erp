package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.ProductoTomaInventario;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class ProductoTomaInventarioDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoTomaInventario getById(Long id) {
        return em.find(ProductoTomaInventario.class, id);
    }

    public List<ProductoTomaInventario> findAll() {
        return em.createQuery("select b from ProductoTomaInventario b ORDER BY b.id asc",ProductoTomaInventario.class).getResultList();
    }

    public boolean delete(ProductoTomaInventario ProductoTomaInventario) {

        ProductoTomaInventario toDelete = em.find(ProductoTomaInventario.class,ProductoTomaInventario.getId());

        em.remove(toDelete);

        return true;
    }

    public ProductoTomaInventario create(ProductoTomaInventario ProductoTomaInventario) {

        em.persist(ProductoTomaInventario);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return ProductoTomaInventario;
    }

    public ProductoTomaInventario update(ProductoTomaInventario ProductoTomaInventario) {
        if ( ProductoTomaInventario == null )
            throw new IllegalArgumentException("ProductoTomaInventario can't be null");

        ProductoTomaInventario updated = em.merge(ProductoTomaInventario);
        em.flush();

        return updated;
    }

    public ProductoTomaInventario findByBodegaIdAndFecha(Long bodegaId, Date fecha) {
        List<ProductoTomaInventario> list = em.createQuery("select b from ProductoTomaInventario b where b.bodegaId=:bodegaId and b.fecha=:fecha",ProductoTomaInventario.class)
                .setParameter("bodegaId", bodegaId)
                .setParameter("fecha", fecha)
                .getResultList();

        for (ProductoTomaInventario ProductoTomaInventario : list) {
            return ProductoTomaInventario;
        }

        return null;
    }
}
