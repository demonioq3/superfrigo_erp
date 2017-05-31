package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.Area;
import cl.superfrigo.entity.proyectos.ProductoReceta;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ProductoRecetaDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoReceta getById(Long id) {
        return em.find(ProductoReceta.class, id);
    }

    public List<ProductoReceta> findAll() {
        return em.createQuery("select b from ProductoReceta b ORDER BY b.id asc",ProductoReceta.class).getResultList();
    }

    public boolean delete(ProductoReceta productoReceta) {

        ProductoReceta toDelete = em.find(ProductoReceta.class,productoReceta.getId());

        em.remove(toDelete);

        return true;
    }

    public ProductoReceta create(ProductoReceta productoReceta) {

        em.persist(productoReceta);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return productoReceta;
    }

    public ProductoReceta update(ProductoReceta productoReceta) {
        if ( productoReceta == null )
            throw new IllegalArgumentException("ProductoReceta can't be null");

        ProductoReceta updated = em.merge(productoReceta);
        em.flush();

        return updated;
    }
}
