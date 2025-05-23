package pt.dsi.dpi.rest.graph;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

@Type("Hero")
@Description("A film Hero description")
public class Hero {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private Double height;
    @NonNull
    private Integer mass;
    @NonNull
    private Boolean darkSide;

    private LightSaber lightSaber;
    @NonNull
    private List<Integer> episodeIds = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getMass() {
        return mass;
    }

    public void setMass(Integer mass) {
        this.mass = mass;
    }

    public Boolean getDarkSide() {
        return darkSide;
    }

    public void setDarkSide(Boolean darkSide) {
        this.darkSide = darkSide;
    }

    public LightSaber getLightSaber() {
        return lightSaber;
    }

    public void setLightSaber(LightSaber lightSaber) {
        this.lightSaber = lightSaber;
    }

    public List<Integer> getEpisodeIds() {
        return episodeIds;
    }

    public void setEpisodeIds(List<Integer> episodeIds) {
        this.episodeIds = episodeIds;
    }
    
    @Override
    public String toString() {
        return "Hero [name=" + name + ", surname=" + surname + ", height=" + height + ", mass=" + mass + ", darkSide="
                + darkSide + ", lightSaber=" + lightSaber + ", episodeIds=" + episodeIds + "]";
    }
}