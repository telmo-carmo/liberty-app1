
package  pt.dsi.dpi.rest.dal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Schema(name = "Bonus",
        description = "POJO that represents the Employee Bonus contents.")
@Entity
@Table(name = "Bonus")
public class Bonus {
    @Id
    private String   ename;
    private String   job;
    private int      sal;
    private int      comm;
    
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getSal() {
        return sal;
    }

    public void setSal(int sal) {
        this.sal = sal;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }

    public Bonus() {
    }
    
    public Bonus(String e, String j, int s, int c) {
        ename = e; job = j; sal = s; comm = c;
    }
    
    @Override
    public String toString() {
        return "Bonus [ename=" + ename + ", job=" + job + ", sal=" + sal + ", comm=" + comm + "]";
    }
}
