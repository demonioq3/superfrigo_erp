package cl.superfrigo.dao;


import cl.superfrigo.entity.comercial.OrdenDeTrabajo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class OrdenDeTrabajoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public OrdenDeTrabajo getById(Long id) {
        return em.find(OrdenDeTrabajo.class, id);
    }

    public List<OrdenDeTrabajo> findAll() {
        return em.createQuery("select b from OrdenDeTrabajo b ORDER BY b.id desc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotazionesEnEstudio(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 1 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotizacionesEnEsperaAprobacionComercial(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 2 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotizacionesEnEsperaAprobacionFinanciera(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 3 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotizacionesEnEsperaAprobacionCliente(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 4 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotizacionesAprobadas(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 5 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public List<OrdenDeTrabajo> cotizacionesRechazadas(){
        return em.createQuery("select b from OrdenDeTrabajo b where b.estadoId = 6 ORDER BY b.id asc",OrdenDeTrabajo.class).getResultList();
    }

    public boolean delete(OrdenDeTrabajo ot) {

        OrdenDeTrabajo toDelete = em.find(OrdenDeTrabajo.class,ot.getId());

        em.remove(toDelete);

        return true;
    }

    public OrdenDeTrabajo create(OrdenDeTrabajo ot) {

        em.persist(ot);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return ot;
    }

    public OrdenDeTrabajo update(OrdenDeTrabajo ot) {
        if ( ot == null )
            throw new IllegalArgumentException("OT can't be null");

        OrdenDeTrabajo updated = em.merge(ot);
        em.flush();

        return updated;
    }

    public OrdenDeTrabajo findById(Long id) {
        List<OrdenDeTrabajo> resultList = em.createQuery("select b from OrdenDeTrabajo b where b.id =:id",OrdenDeTrabajo.class)
                .setParameter("id", id).getResultList();

        for (OrdenDeTrabajo ot : resultList) {
            return ot;
        }

        return null;
    }


    public List<OrdenDeTrabajo> findByEstadoIdAndCotizacionId(Long estadoId, Long cotizacionId) {
        List<OrdenDeTrabajo> resultList = new ArrayList<OrdenDeTrabajo>();

        if(estadoId == 0 && cotizacionId == null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b order by b.id asc",OrdenDeTrabajo.class).getResultList();
        } else if(estadoId != 0 && cotizacionId == null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.estadoId =:estadoId order by b.id asc",OrdenDeTrabajo.class).setParameter("estadoId", estadoId).getResultList();
        } else if(estadoId == 0 && cotizacionId != null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.id =:cotizacionId",OrdenDeTrabajo.class).setParameter("cotizacionId", cotizacionId).getResultList();
        } else if(estadoId != 0 && cotizacionId != null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.id =:cotizacionId and b.estadoId =:estadoId",OrdenDeTrabajo.class)
                    .setParameter("cotizacionId", cotizacionId)
                    .setParameter("estadoId", estadoId)
                    .getResultList();
        }

        return resultList;
    }

    public List<OrdenDeTrabajo> findByEstadoIdAndCotizacionIdAndFechas(Long estadoId, Long cotizacionId, Date fechaDesde, Date fechaHasta) {
        List<OrdenDeTrabajo> resultList = new ArrayList<OrdenDeTrabajo>();

        if(estadoId == 0 && cotizacionId == null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.fecha between :desde and :hasta order by b.id asc",OrdenDeTrabajo.class)
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta).getResultList();
        } else if(estadoId != 0 && cotizacionId == null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.estadoId =:estadoId and b.fecha between :desde and :hasta order by b.id asc",OrdenDeTrabajo.class)
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta)
                    .setParameter("estadoId", estadoId).getResultList();
        } else if(estadoId == 0 && cotizacionId != null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.id =:cotizacionId and b.fecha between :desde and :hasta",OrdenDeTrabajo.class)
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta)
                    .setParameter("cotizacionId", cotizacionId).getResultList();
        } else if(estadoId != 0 && cotizacionId != null){
            resultList = em.createQuery("select b from OrdenDeTrabajo b where b.id =:cotizacionId and b.estadoId =:estadoId and b.fecha between :desde and :hasta",OrdenDeTrabajo.class)
                    .setParameter("desde", fechaDesde)
                    .setParameter("hasta", fechaHasta)
                    .setParameter("cotizacionId", cotizacionId)
                    .setParameter("estadoId", estadoId)
                    .getResultList();
        }

        return resultList;
    }

}
