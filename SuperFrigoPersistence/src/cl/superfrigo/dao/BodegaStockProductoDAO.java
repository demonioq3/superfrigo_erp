package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.BodegaStockProducto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Stateless
public class BodegaStockProductoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public BodegaStockProducto getById(Long id) {
        return em.find(BodegaStockProducto.class, id);
    }

    public List<BodegaStockProducto> findAll() {
        return em.createQuery("select b from BodegaStockProducto b ORDER BY b.id asc",BodegaStockProducto.class).getResultList();
    }

    public boolean delete(BodegaStockProducto bodegaStockProducto) {

        BodegaStockProducto toDelete = em.find(BodegaStockProducto.class,bodegaStockProducto.getId());

        em.remove(toDelete);

        return true;
    }

    public BodegaStockProducto create(BodegaStockProducto bodegaStockProducto) {

        em.persist(bodegaStockProducto);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return bodegaStockProducto;
    }

    public BodegaStockProducto update(BodegaStockProducto bodegaStockProducto) {
        if ( bodegaStockProducto == null )
            throw new IllegalArgumentException("BodegaStockProducto can't be null");

        BodegaStockProducto updated = em.merge(bodegaStockProducto);
        em.flush();

        return updated;
    }

    public List<BodegaStockProducto> findByFichaProductoId(Long fichaProductoId) {
        return em.createQuery("select b from BodegaStockProducto b where b.fichaProductoId =:id ORDER BY b.id asc",BodegaStockProducto.class)
                .setParameter("id", fichaProductoId).getResultList();
    }

    public BodegaStockProducto findByFichaProductoIdAndBodegaId(Long fichaProductoId, Long bodegaId) {
        List<BodegaStockProducto> lista =  em.createQuery("select b from BodegaStockProducto b where b.fichaProductoId =:id and b.bodegaId =:bodegaId ORDER BY b.id asc",BodegaStockProducto.class)
                .setParameter("id", fichaProductoId)
                .setParameter("bodegaId", bodegaId)
                .getResultList();

        for (BodegaStockProducto bodegaStockProducto : lista) {
            return bodegaStockProducto;
        }

        return null;
    }

    public List<BodegaStockProducto> finByCodigoProducto(String codigoParaConsultaStock) {
        return em.createQuery("select b from BodegaStockProducto b where b.fichaProducto.codigoProducto =:codigo",BodegaStockProducto.class)
                .setParameter("codigo", codigoParaConsultaStock).getResultList();
    }

    public List<BodegaStockProducto> findByBodegaId(Long bodegaId) {
        return em.createQuery("select b from BodegaStockProducto b where b.bodegaId =:bodegaId ORDER BY b.id asc",BodegaStockProducto.class)
                .setParameter("bodegaId", bodegaId).getResultList();
    }

    public List<BodegaStockProducto> findByDescripcion(String decripcionParaConsultaStock) {
        return em.createQuery("select b from BodegaStockProducto b where b.fichaProducto.descripcion =:decripcionParaConsultaStock ORDER BY b.id asc",BodegaStockProducto.class)
                .setParameter("decripcionParaConsultaStock", "%" + decripcionParaConsultaStock + "%").getResultList();
    }

    public List<BodegaStockProducto> findByProductoId(Long id) {
        return em.createQuery("select b from BodegaStockProducto b where b.fichaProductoId =:codigo ",BodegaStockProducto.class)
                .setParameter("codigo", id).getResultList();
    }
}
