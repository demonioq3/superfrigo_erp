package cl.superfrigo.dao;


import cl.superfrigo.entity.Producto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class ProductoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Producto getById(Long id) {
        return em.find(Producto.class, id);
    }

}
