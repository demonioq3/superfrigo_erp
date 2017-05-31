package cl.superfrigo.dao;


import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.solicitud_material.ProductoSolicitudMaterial;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ProductoSolicitudMaterialDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public ProductoSolicitudMaterial getById(Long id) {
        return em.find(ProductoSolicitudMaterial.class, id);
    }

    public List<ProductoSolicitudMaterial> findAll() {
        return em.createQuery("select b from ProductoSolicitudMaterial b ORDER BY b.id asc",ProductoSolicitudMaterial.class).getResultList();
    }

    public boolean delete(ProductoSolicitudMaterial productoSolicitudMaterial) {

        ProductoSolicitudMaterial toDelete = em.find(ProductoSolicitudMaterial.class,productoSolicitudMaterial.getId());

        em.remove(toDelete);

        return true;
    }

    public ProductoSolicitudMaterial create(ProductoSolicitudMaterial productoSolicitudMaterial) {

        em.persist(productoSolicitudMaterial);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return productoSolicitudMaterial;
    }

    public ProductoSolicitudMaterial update(ProductoSolicitudMaterial productoSolicitudMaterial) {
        if ( productoSolicitudMaterial == null )
            throw new IllegalArgumentException("ProductoSolicitudMaterial can't be null");

        ProductoSolicitudMaterial updated = em.merge(productoSolicitudMaterial);
        em.flush();

        return updated;
    }
}
