package cl.superfrigo.dao;


import cl.superfrigo.entity.bodega.FichaProducto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class FichaProductoDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public FichaProducto getById(Long id) {
        return em.find(FichaProducto.class, id);
    }

    public List<FichaProducto> findAll() {
        return em.createQuery("select b from FichaProducto b ORDER BY b.id asc",FichaProducto.class).getResultList();
    }

    public boolean delete(FichaProducto fichaProducto) {

        FichaProducto toDelete = em.find(FichaProducto.class,fichaProducto.getId());

        em.remove(toDelete);

        return true;
    }

    public FichaProducto create(FichaProducto fichaProducto) {

        em.persist(fichaProducto);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return fichaProducto;
    }

    public FichaProducto update(FichaProducto fichaProducto) {
        if ( fichaProducto == null )
            throw new IllegalArgumentException("FichaProducto can't be null");

        FichaProducto updated = em.merge(fichaProducto);
        em.flush();

        return updated;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM FichaProducto d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<FichaProducto> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<FichaProducto> query = em.createQuery(
                "select d from FichaProducto d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                FichaProducto.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<FichaProducto> res = query.getResultList();

        return res;
    }

    public FichaProducto findByCodigoProducto(String codigoProducto) {
        List<FichaProducto> list =  em.createQuery("select b from FichaProducto b where b.codigoProducto=:codigoProducto ORDER BY b.id asc",FichaProducto.class)
                .setParameter("codigoProducto", codigoProducto).getResultList();

        if(list != null){
            for (FichaProducto fichaProducto : list) {
                return fichaProducto;
            }
        }

        return null;
    }

    public List<FichaProducto> findBySubGrupoId(Long subGrupoId) {
        List<FichaProducto> list =  em.createQuery("select b from FichaProducto b where b.subGrupoId=:subGrupoId ORDER BY b.id asc",FichaProducto.class)
                .setParameter("subGrupoId", subGrupoId).getResultList();
        return list;
    }

    public List<FichaProducto> findByGrupoId(Long grupoId) {
        List<FichaProducto> list =  em.createQuery("select b from FichaProducto b where b.grupoId=:grupoId ORDER BY b.id asc",FichaProducto.class)
                .setParameter("grupoId", grupoId).getResultList();
        return list;
    }
}
