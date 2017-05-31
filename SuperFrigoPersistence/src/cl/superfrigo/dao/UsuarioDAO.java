package cl.superfrigo.dao;


import cl.superfrigo.entity.Usuario;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class UsuarioDAO implements Serializable {

    @PersistenceContext(unitName = "SuperFrigoPersistenceUnit")
    private EntityManager em;


    public Usuario getById(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> findAll() {
        return em.createQuery("select b from Usuario b ORDER BY b.id asc",Usuario.class).getResultList();
    }

    public boolean delete(Usuario usuario) {

        Usuario toDelete = em.find(Usuario.class,usuario.getId());

        em.remove(toDelete);

        return true;
    }

    public Usuario create(Usuario usuario) {

        em.persist(usuario);

        //se debe hacer flush para garantizar la creacion del ID
        em.flush();

        return usuario;
    }

    public Usuario update(Usuario usuario) {
        if ( usuario == null )
            throw new IllegalArgumentException("Usuario can't be null");

        Usuario updated = em.merge(usuario);
        em.flush();

        return updated;
    }

    public Usuario findByEmailAndPassword(String email, String password) {
        return em.createQuery("select b from Usuario b where b.email=:email and b.password=:password",Usuario.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public List<Usuario> getUserByPerfil(Long profileId) {
        List<Usuario> lista = em.createQuery("select u from Usuario u where u.perfiles.id =:profileId")
                .setParameter("profileId", profileId)
                .getResultList();

        return lista;
    }
}
