package pt.dsi.dpi.rest;


import java.util.List;

public class UserInfo {
    private Integer id;
    private String username;
    private String password;
    private List<String> roles;

    public UserInfo(Integer id, String username, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasRole(String... ra) {
        if (roles == null)
            return false;
        for (String r : ra)
            if (roles.contains(r))
                return true;
        return false;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> v) {
        roles = v;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles.toString() +
                '}';
    }
}
