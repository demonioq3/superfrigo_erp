package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.SalidaProductoCantidad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class SalidaProductoCantidadDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public SalidaProductoCantidad getById(Long id) {
        return em.find(SalidaProductoCantidad.class, id);
    }

    public List<SalidaProductoCantidad> findAll() {
        return em.createQuery("select b from SalidaProductoCantidad b ORDER BY b.id asc",SalidaProductoCantidad.class).getResultList();
    }

    public boolean delete(SalidaProductoCantidad salidaProductoCantidad) {

        SalidaProductoCantidad toDelete = em.find(SalidaProductoCantidad.class,salidaProductoCantidad.getId());

        em.remove(toDelete);

        return true;
    }

    public SalidaProductoCantidad create(SalidaProductoCantidad salidaProductoCantidad) {

        em.persist(salidaProductoCantidad);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return salidaProductoCantidad;
    }

    public SalidaProductoCantidad update(SalidaProductoCantidad salidaProductoCantidad) {
        if ( salidaProductoCantidad == null )
            throw new IllegalArgumentException("SalidaProductoCantidad can't be null");

        SalidaProductoCantidad updated = em.merge(salidaProductoCantidad);
        em.flush();

        return updated;
    }

    public List<SalidaProductoCantidad> findByProductoFicha(FichaProducto fichaProducto) {
        List<SalidaProductoCantidad> list = em.createQuery("select b from SalidaProductoCantidad b where b.fichaProductoId =:fichaProductoId",SalidaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .getResultList();

        return list;
    }

    public List<SalidaProductoCantidad> findByProductoFichaAndFechas(FichaProducto fichaProducto, Date fechaDesde, Date fechaHasta) {
        List<SalidaProductoCantidad> list = em.createQuery("select b from SalidaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaSalida.fecha between :fechaDesde and :fechaHasta",SalidaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }

    public List<SalidaProductoCantidad> findByProductoFichaAndBodegaId(FichaProducto fichaProducto, Long bodegaId) {

        List<SalidaProductoCantidad> list = em.createQuery("select b from SalidaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaSalida.bodegaId =:bodegaId",SalidaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("bodegaId", bodegaId)
                .getResultList();

        return list;
    }

    public List<SalidaProductoCantidad> findByProductoFichaAndBodegaIdAndFechas(FichaProducto fichaProducto, Long bodegaId, Date fechaDesde, Date fechaHasta) {
        List<Long> productosId = new ArrayList<Long>();
        productosId.add(fichaProducto.getId());

        List<SalidaProductoCantidad> list = em.createQuery("select b from SalidaProductoCantidad b where b.fichaProductoId =:fichaProductoId and b.guiaSalida.bodegaId =:bodegaId and b.guiaSalida.fecha between :fechaDesde and :fechaHasta",SalidaProductoCantidad.class)
                .setParameter("fichaProductoId", fichaProducto.getId())
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("bodegaId", bodegaId)
                .setParameter("fechaHasta", fechaHasta)
                .getResultList();

        return list;
    }

    public List<SalidaProductoCantidad> findByGuiaSalidaId(Long id) {
        return em.createQuery("select b from SalidaProductoCantidad b where b.guiaSalidaId=:id",SalidaProductoCantidad.class)
                .setParameter("id", id).getResultList();
    }

    public List<SalidaProductoCantidad> findByOrdenDeTrabajoIdAndConceptoConsumoOt(Long ordenDeTrabajoId) {
        List<SalidaProductoCantidad> list = em.createQuery("select b from SalidaProductoCantidad b where b.guiaSalida.ordenDeTrabajoId =:ordenDeTrabajoId and b.guiaSalida.conceptoSalidaId = 4",SalidaProductoCantidad.class)
                .setParameter("ordenDeTrabajoId", ordenDeTrabajoId)
                .getResultList();

        return list;
    }
}
