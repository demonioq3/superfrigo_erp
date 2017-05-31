package cl.superfrigo.dao;


import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.proyectos.PlanoFabricacion;
import cl.superfrigo.entity.proyectos.Receta;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class PlanoFabricacionDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public PlanoFabricacion getById(Long id) {
        return em.find(PlanoFabricacion.class, id);
    }

    public List<PlanoFabricacion> findAll() {
        return em.createQuery("select b from PlanoFabricacion b ORDER BY b.id asc",PlanoFabricacion.class).getResultList();
    }

    public boolean delete(PlanoFabricacion planoFabricacion) {

        PlanoFabricacion toDelete = em.find(PlanoFabricacion.class,planoFabricacion.getId());

        em.remove(toDelete);

        return true;
    }

    public PlanoFabricacion create(PlanoFabricacion planoFabricacion) {

        em.persist(planoFabricacion);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return planoFabricacion;
    }

    public PlanoFabricacion update(PlanoFabricacion planoFabricacion) {
        if ( planoFabricacion == null )
            throw new IllegalArgumentException("PlanoFabricacion can't be null");

        PlanoFabricacion updated = em.merge(planoFabricacion);
        em.flush();

        return updated;
    }

    public PlanoFabricacion findByCodigo(String codigo) {
        List<PlanoFabricacion> lista = em.createQuery("select b from PlanoFabricacion b where b.codigo=:codigo",PlanoFabricacion.class).setParameter("codigo", codigo).getResultList();

        for (PlanoFabricacion planoFabricacion : lista) {
            return planoFabricacion;
        }

        return null;
    }

    public Long countFiltered(Map<String, Object> filters) {
        // TODO filters ... Criteria API..
        String filteredQuery = "SELECT count(d.id) FROM PlanoFabricacion d " + Utils.buildWhereClauses(filters);
        TypedQuery<Long> query = em.createQuery(filteredQuery, Long.class);// + Utils.buildWhereClauses(filters), Long.class);
        Utils.setFilterParams(filters, query);

        return query.getSingleResult();
    }

    public List<PlanoFabricacion> findFiltered(int first, int pageSize, String sortField, Boolean sortOrder, Map<String, Object> filters) {
        StringBuilder orderClause = new StringBuilder();

        if ( sortField != null && !"".equals(sortField) )
            orderClause.append(" ORDER BY d." + sortField + (sortOrder != null && sortOrder ? " ASC" : " DESC"));
        else
            orderClause.append(" ORDER BY d.id DESC");

        TypedQuery<PlanoFabricacion> query = em.createQuery(
                "select d from PlanoFabricacion d"
                        + Utils.buildWhereClauses(filters)
                        + orderClause.toString(),
                PlanoFabricacion.class
        );

        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        Utils.setFilterParams(filters, query);

        List<PlanoFabricacion> res = query.getResultList();

        return res;
    }
}
