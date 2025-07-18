package pt.dsi.dpi.rest.graph;

import java.util.Arrays;

/*
 Before you make any requests, see the http://localhost:9082/graphql/schema.graphql 
 or at http://localhost:9080/app1/graphql/schema.graphql
 This URL returns the schema that describes the GraphQL service.

To access the GraphQL service, GraphiQL has already been set up and included for you. 
Access GraphiQL at  http://localhost:9080/app1/graphql-ui 

 */


import java.util.List;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;


import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;

import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;



import java.util.logging.Logger;

@GraphQLApi
public class GraphQLService {
    private static final Logger logger = Logger.getLogger(GraphQLService.class.getName());
    
    @Inject
    GalaxyService service;


    @Query("allFilms")
    @NonNull
    @Description("Get all Films from a galaxy far far away")
    public List<Film> getAllFilms() {
        logger.info("Getting all films");
        return service.getAllFilms();
    }

    @Query
    @Description("Get a Film for filmId N")
    public Film getFilm(@Name("filmId") int id) {
        logger.info("Getting film with id " + id);
        return service.getFilm(id);
    }

    public List<Hero> heroes(@Source Film film) {
        logger.info("Getting heroes for film " + film);
        return service.getHeroesByFilm(film);
    }

    @Mutation
    @Description("Create a hero")       // not Working, gives exception !!??
    public Hero createHero(
                @Name("name") @NonNull      String name,
                @Name("surname") @NonNull   String surname,
                @Name("height")             double height,
                @Name("mass")               int mass,
                @Name("lightSaber")         LightSaber lightSaber,
                @Name("darkSide")           boolean darkSide,
                @Name("episodeIds")         List<Integer> episodeIds
                ) { 
        Hero hero = new Hero();
        hero.setName(name);
        hero.setSurname(surname);
        hero.setHeight(height);
        hero.setMass(mass);
        hero.setLightSaber(lightSaber);
        hero.setDarkSide(darkSide);
        hero.getEpisodeIds().addAll(episodeIds);

        logger.info("Creating hero " + hero);
        int i = service.addHero(hero);
        logger.warning("Created hero with index " + i);
        return hero;
    }

    @Mutation
    @Description("Create a new mini Hero, returns the new hero index")
    public int newHero(
                @Name("name") @NonNull      String name,
                @Name("surname") @NonNull   String surname,
                @Name("mass") @NonNull      int mass,
                @Name("darkSide")           boolean darkSide) {
        Hero hero = new Hero();
        hero.setName(name);
        hero.setSurname(surname);
        hero.setHeight(1.5);
        hero.setMass(mass);
        hero.setDarkSide(darkSide);
        hero.getEpisodeIds().addAll(Arrays.asList(4));
        int idx = service.addHero(hero);
        logger.info("Creating newHero " + hero + " with index " + idx);
        return idx;
    }

    @Mutation
    @Description("Delete a hero by index")
    public Hero deleteHero(int idx) {
        logger.info("Deleting hero with idx " + idx);
        return service.deleteHero(idx);
    }

    @Query
    public List<Hero> getHeroesWithSurname(@DefaultValue("Skywalker") String surname) {
        List<Hero> l = service.getHeroesBySurname(surname);
        logger.info("Found " + l.size() + " heroes with surname " + surname);
        return l;
    }


}

