package pt.dsi.dpi.rest.dal;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Dept",
        description = "POJO that represents a single department entry.")
@Entity
@Table(name = "Dept")
public class Dept implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long deptno;
    private String dname;
    private String loc; 

    public Dept() {
        this(0L, null,null); 
    }

    public Dept(Long deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }


    public Long getDeptno() {
        return deptno;
    }

    public void setDeptno(Long deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String toString() {
        return "Dept [deptno=" + deptno + ", dname=" + dname + ", loc=" + loc + "]";
    }
    
}