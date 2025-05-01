package pt.dsi.dpi.rest.dal;


import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class DataInventory {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public List<SystemData> getSystems() {
        return em.createNamedQuery("SystemData.findAll", SystemData.class)
                 .getResultList();
    }

    public SystemData getSystem(String hostname) {
        List<SystemData> systems =
            em.createNamedQuery("SystemData.findSystem", SystemData.class)
              .setParameter("hostname", hostname)
              .getResultList();
        return systems == null || systems.isEmpty() ? null : systems.get(0);
    }

    public void add(String hostname, String osName, String javaVersion, Long heapSize) {
        em.persist(new SystemData(hostname, osName, javaVersion, heapSize));
    }

    public void update(SystemData s) {
        em.merge(s);
    }

    public void removeSystem(SystemData s) {
        em.remove(s);
    }

}
