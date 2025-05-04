package pt.dsi.dpi.rest.dal;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;


@ApplicationScoped
public class DeptService {
    
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public List<Dept>findAll() {
        return em.createQuery("SELECT e FROM Dept e", Dept.class)
                    .getResultList();
    }
    

    public Dept findById(Long id) {
        return em.find(Dept.class, id);
    }

    @Transactional
    public void save(Dept d) {
        em.persist(d);
    }


    @Transactional
    public void update(Long id,Dept ud) {
        Dept d = em.find(Dept.class, id);
        if (d != null) {
            em.merge(new Dept(d.getDeptno(), ud.getDname(), ud.getLoc()));
        }
    }

    @Transactional
    public void deleteById(Long deptno) {
        Dept d = em.find(Dept.class, deptno);
        if (d != null) {
            em.remove(d);
        }
    }
}
