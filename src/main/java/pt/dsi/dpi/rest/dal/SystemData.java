package pt.dsi.dpi.rest.dal;


import java.io.Serializable;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Schema(name = "SystemData",
        description = "POJO that represents a single data inventory entry.")
@Entity
@Table(name = "SystemData")
@NamedQuery(name = "SystemData.findAll", query = "SELECT e FROM SystemData e")
@NamedQuery(name = "SystemData.findSystem",
            query = "SELECT e FROM SystemData e WHERE e.hostname = :hostname")
public class SystemData implements Serializable {
    private static final long serialVersionUID = 1L;


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private int id;

    @Schema(required = true)
    @Column(name = "hostname")
    private String hostname;

    @Column(name = "osName")
    private String osName;
    @Column(name = "javaVersion")
    private String javaVersion;
    @Column(name = "heapSize")
    private Long heapSize;

    public SystemData() {
    }

    public SystemData(String hostname, String osName, String javaVer, Long heapSize) {
        this.hostname = hostname;
        this.osName = osName;
        this.javaVersion = javaVer;
        this.heapSize = heapSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Long getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(Long heapSize) {
        this.heapSize = heapSize;
    }

    @Override
    public int hashCode() {
        return hostname.hashCode();
    }

    @Override
    public boolean equals(Object host) {
        if (host instanceof SystemData) {
            return hostname.equals(((SystemData) host).getHostname());
        }
        return false;
    }
}

