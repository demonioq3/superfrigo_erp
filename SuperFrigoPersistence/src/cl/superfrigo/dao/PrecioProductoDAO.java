package cl.superfrigo.dao;


import cl.superfrigo.entity.abastecimiento.PrecioProducto;
import cl.superfrigo.entity.bodega.Bodega;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class PrecioProductoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public PrecioProducto getById(Long id) {
        return em.find(PrecioProducto.class, id);
    }

    public List<PrecioProducto> findAll() {
        return em.createQuery("select b from PrecioProducto b ORDER BY b.id asc",PrecioProducto.class).getResultList();
    }

    public boolean delete(PrecioProducto precioProducto) {

        List<PrecioProducto> lista = em.createQuery("select b from PrecioProducto b where b.id =:id",PrecioProducto.class)
                .setParameter("id", precioProducto.getId()).getResultList();

        if(lista != null){
            for (PrecioProducto producto : lista) {
                em.remove(producto);
                return true;
            }
        }

        return false;
    }

    public PrecioProducto create(PrecioProducto precioProducto) {

        em.persist(precioProducto);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return precioProducto;
    }

    public PrecioProducto update(PrecioProducto precioProducto) {
        if ( precioProducto == null )
            throw new IllegalArgumentException("PrecioProducto can't be null");

        PrecioProducto updated = em.merge(precioProducto);
        em.flush();

        return updated;
    }

    public PrecioProducto findByFichaProductoId(Long id) {
        List<PrecioProducto> precioProducto = em.createQuery("select b from PrecioProducto b where b.fichaProductoId =:id",PrecioProducto.class)
                .setParameter("id", id).getResultList();

        for (PrecioProducto producto : precioProducto) {
            return producto;
        }

        return null;
    }
}
