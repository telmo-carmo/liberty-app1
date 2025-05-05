After you generate a starter project, these instructions will help you with what to do next.

The Open Liberty starter gives you a simple, quick way to get the necessary files to start building
an application on Open Liberty. There is no need to search how to find out what to add to your 
Maven build files. A simple RestApplication.java file is generated for you to start 
creating a REST based application. A server.xml configuration file is provided with the necessary 
features for the MicroProfile and Jakarta EE versions that you previously selected.

If you plan on developing and/or deploying your app in a containerized environment, the included 
Dockerfile will make it easier to create your application image on top of the Open Liberty Docker 
image.

1) Once you download the starter project, unpackage the .zip file on your machine.
2) Open a command line session, navigate to the installation directory, and run `./mvnw liberty:dev` (Linux/Mac) or `mvnw liberty:dev` (Windows). 
   This will install all required dependencies and start the default server. When complete, you will
   see the necessary features installed and the message "server is ready to run a smarter planet."

For information on developing your application in dev mode using Maven, see the 
dev mode documentation (https://openliberty.io/docs/latest/development-mode.html).

For further help on getting started actually developing your application, see some of our 
MicroProfile guides (https://openliberty.io/guides/?search=microprofile&key=tag) and Jakarta EE 
guides (https://openliberty.io/guides/?search=jakarta%20ee&key=tag).

If you have problems building the starter project, make sure the Java SE version on your 
machine matches the Java SE version you picked from the Open Liberty starter on the downloads 
page (https://openliberty.io/downloads/). You can test this with the command `java -version`.

Open Liberty performs at its best when running using Open J9 which can be obtained via IBM Semeru 
(https://developer.ibm.com/languages/java/semeru-runtimes/downloads/). For a full list of supported 
Java SE versions and where to obtain them, reference the Java SE support page 
(https://openliberty.io/docs/latest/java-se.html).

If you find any issues with the starter project or have recommendations to improve it, open an 
issue in the starter GitHub repo (https://github.com/OpenLiberty/start.openliberty.io).

---

echo "# liberty-app1" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/telmo-carmo/liberty-app1.git
git push -u origin main

---

# Create a JCEKS keystore
keytool -genseckey -alias jwtSecret -keyalg AES -keysize 256 -storetype JCEKS -keystore jwtkeystore.jceks -storepass myPassword

# (Optional) Verify the keystore contents
keytool -list -keystore jwtkeystore.jceks -storepass myPassword -v

Warning:
The JCEKS keystore uses a proprietary format. It is recommended to migrate to PKCS12 
which is an industry standard format using 
"keytool -importkeystore -srckeystore jwtkeystore.jceks -destkeystore jwtkeystore.jceks -deststoretype pkcs12".

copy jwtkeystore.jceks  to  src/main/resources/security
------------
works on Ubuntu with  
Launching defaultServer (Open Liberty 25.0.0.4/wlp-1.0.100.cl250420250407-1902) on OpenJDK 64-Bit Server VM, version 21.0.6+7-LTS (en_US)

---

mvn liberty:start    # start in background
mvn liberty:stop     # stop server

mvn liberty:dev      # start in foreground (dev mode)
mvn liberty:run      # start in foreground (non-dev mode)

----
The GraphQL UI can be accessed from http://localhost:9080/app1/graphql-ui 

Schema at http://localhost:9080/app1/graphql/schema.graphql 

GraphQL queries:

query allFilms {
  allFilms {
    title
    director
    releaseDate
    episodeID
  }
}

--
query getFilm {
  film(filmId: 1) {
    title
    director
    releaseDate
    episodeID
  }
}



--
query getFilmHeroes {
  film(filmId: 1) {
    title
    director
    releaseDate
    episodeID
    heroes {
      name
      height
      mass
      darkSide
      lightSaber
    }
  }
}

-- this gives error!!
mutation CreateHero {
  createHero(hero: {
    name: "Brian",
    surname: "Vader",
    height: 1.9,
    mass: 70,
    episodeIds: [1, 5, 6],
    darkSide: false,
    lightSaber: "BLUE"
  }) {
    name
    surname
    height
    mass
    episodeIds
    darkSide
    lightSaber
  }
}

-- this works:

mutation {
  newHero(
    name: "Javali",
    surname: "Vader",
    darkSide: false
  )
}

--
query  {
  heroesWithSurname(surname: "Vader") {
    name
    surname
    height
    mass
    episodeIds
    darkSide
    lightSaber
  }
}

--
mutation {
  deleteHero(idx: 3) 
   {
      name
      surname
      height
      mass
      episodeIds
      darkSide
      lightSaber
   }
}
-----------