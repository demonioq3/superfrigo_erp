package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.GuiaEntrada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class EntradaProductoCantidadDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public EntradaProductoCantidad getById(Long id) {
        return em.find(EntradaProductoCantidad.class, id);
    }

    public List<EntradaProductoCantidad> findAll() {
        return em.createQuery("select b from EntradaProductoCantidad b ORDER BY b.id asc",EntradaProductoCantidad.class).getResultList();
    }

    public boolean delete(EntradaProductoCantidad entradaProductoCantidad) {

        EntradaProductoCantidad toDelete = em.find(EntradaProductoCantidad.class,entradaProductoCantidad.getId());

        em.remove(toDelete);

        return true;
    }

    public EntradaProductoCantidad create(EntradaProductoCantidad entradaProductoCantidad) {

        em.persist(entradaProductoCantidad);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return entradaProductoCantidad;
    }

    public EntradaProductoCantidad update(EntradaProductoCantidad entradaProductoCantidad) {
        if ( entradaProductoCantidad == null )
            throw new IllegalArgumentException("EntradaProductoCantidad can't be null");

        EntradaProductoCantidad updated = em.merge(entradaProductoCantidad);
        em.flush();

        return updated;
    }

    public List<EntradaProductoCantidad> findAllByBodegaIdAndProductoId(Long bodegaId, Long fichaProductoId) {
        return em.createQuery("select b from EntradaProductoCantidad b where b.guiaEntrada.bodegaId=:bodegaId and b.fichaProductoId=:fichaProductoId and " +
                "b.cantidad != b.cantidadUtilizada ORDER BY b.id asc",EntradaProductoCantidad.class)
                .setParameter("bodegaId", bodegaId).setParameter("fichaProductoId", fichaProductoId).getResultList();
    }

    public List<EntradaProductoCantidad> findByProductoFicha(FichaProducto fichaProducto) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<EntradaProductoCantidad> list = em.createQuery("select b from EntradaProductoCantidad b where b.fichaProductoId =:fichaProductoId",EntradaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .getResultList();

        return list;
    }

    public List<EntradaProductoCantidad> findByProductoFichaAndFechas(FichaProducto fichaProducto, Date fechaDesde, Date fechaHasta) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<EntradaProductoCantidad> list = em.createQuery("select b from EntradaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaEntrada.fecha between :fechaDesde and :fechaHasta",EntradaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }

    public List<EntradaProductoCantidad> findByProductoFichaAndBodegaId(FichaProducto fichaProducto, Long bodegaId) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<EntradaProductoCantidad> list = em.createQuery("select b from EntradaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaEntrada.bodegaId =:bodegaId",EntradaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("bodegaId", bodegaId)
                .getResultList();

        return list;
    }

    public List<EntradaProductoCantidad> findByProductoFichaAndBodegaIdAndFechas(FichaProducto fichaProducto, Long bodegaId, Date fechaDesde, Date fechaHasta) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<EntradaProductoCantidad> list = em.createQuery("select b from EntradaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaEntrada.bodegaId =:bodegaId and b.guiaEntrada.fecha between :fechaDesde and :fechaHasta",EntradaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("bodegaId", bodegaId)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }

    public List<EntradaProductoCantidad> findByGuiaEntradaId(Long id) {
        return em.createQuery("select b from EntradaProductoCantidad b where b.guiaEntradaId=:id",EntradaProductoCantidad.class)
                .setParameter("id", id).getResultList();
    }
}
