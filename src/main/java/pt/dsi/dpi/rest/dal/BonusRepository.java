package pt.dsi.dpi.rest.dal;

/*
 -- For SQLITE local db:

    <library id="sqlite-library">
        <fileset dir="${shared.resource.dir}/" includes="sqlite*.jar" />
    </library>

    <dataSource id="DefaultDataSource" jndiName="jdbc/appDB1">
        <jdbcDriver libraryRef="sqlite-library" />
        <properties>
            <property name="URL"          value="jdbc:sqlite:${db.path}"/>
            <property name="databaseName" value="${db.path}"/>
        </properties>

    </dataSource>

    // also add dependency in pom.xml for artifactId sqlite-jdbc
---
FOR Postgres DB:

<library id="postgresql-library">
    <fileset dir="${shared.resource.dir}/" includes="*.jar" />
 </library>

 <dataSource id="DefaultDataSource" jndiName="jdbc/appDB1">
    <jdbcDriver libraryRef="postgresql-library" />
    <properties.postgresql  databaseName="scottdb"
                            serverName="localhost"
                            portNumber="5432"
                            user="scott"
                            password="tiger" />
</dataSource>

 */
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BonusRepository implements IBonusRepository {

    private static final Logger logger = LoggerFactory.getLogger(BonusRepository.class);

    //Resource(lookup = "java:comp/env/jdbc/appDB1")
    @Resource(lookup = "jdbc/appDB2")
    DataSource dataSource;

    // org.sqlite.javax.SQLiteConnectionPoolDataSource.
    //
    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM Bonus";
        logger.debug("Executing count query: " + query);

        logger.warn("DataSource Class: " + dataSource.getClass().getName());

        try (Connection connection = dataSource.getConnection()) {
            var meta = connection.getMetaData();
            logger.warn("DataSource Conn Class: " + meta.getClass().getName());
            logger.warn("DataSource Conn Driver: " + meta.getDriverName());
            logger.warn("DataSource Conn User: " + meta.getUserName());
            logger.warn("DataSource Conn DB: " + meta.getDatabaseProductName());
            logger.warn("DataSource Conn DB Version: " + meta.getDatabaseProductVersion());
            logger.warn("DataSource Conn URL: " + meta.getURL());

            try (
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    logger.info("Count Bonus query result: " + count);
                    return count;
                }
            } catch (SQLException e) {
                logger.error("Error executing count query", e);
            }

        } catch (SQLException e) {
            logger.error("Error getting connection from DataSource", e);
        }
        return 0;
    }

    @Override
    public List<Bonus> findAll() {
        List<Bonus> bonuses = new ArrayList<>();
        String query = "SELECT * FROM Bonus";
        logger.debug("Executing findAll query: " + query);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bonuses.add(mapRowToBonus(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Error executing findAll query", e);
        }
        logger.info("Number of bonuses retrieved: {}", bonuses.size());
        return bonuses;
    }

    @Override
    public Bonus findOne(String ename) {
        String query = "SELECT * FROM Bonus WHERE ename = ?";
        logger.warn("Executing findOne query for Bonus ename: {}", ename);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ename);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    logger.info("Bonus found for ename: {}", ename);
                    return mapRowToBonus(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing findOne query for ename:" + ename, e);
        }
        logger.warn("No bonus found for ename: {}", ename);
        return null;
    }

    @Override
    public Bonus save(Bonus bn) {
        String query = "INSERT INTO Bonus (ename,job,sal,comm) VALUES (?,?,?,?)";
        logger.debug("Executing save query for bonus: "+ bn);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bn.getEname());
            statement.setString(2, bn.getJob());
            statement.setInt(3, bn.getSal());
            statement.setInt(4, bn.getComm());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Bonus saved successfully: " + bn);
                return bn;
            } else {
                logger.warn("No rows affected while saving bonus");
            }
        } catch (SQLException e) {
            logger.error("Error executing save query", e);
        }
        return null;
    }

    @Override
    public boolean update(Bonus b) {
        String query = "UPDATE Bonus SET job = ?, sal = ? , comm = ?  WHERE ename = ?";
        logger.debug("Executing update query for bonus: " + b);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(4, b.getEname());
            statement.setString(1, b.getJob());
            statement.setInt(2, b.getSal());
            statement.setInt(3, b.getComm());
            boolean updated = statement.executeUpdate() > 0;
            if (updated) {
                logger.info("Bonus updated successfully: " + b);
            } else {
                logger.warn("No rows affected while updating bonus: " + b);
            }
            return updated;
        } catch (SQLException e) {
            logger.error("Error executing update query for bonus: "+ b, e);
        }
        return false;
    }

    @Override
    public boolean delete(String ename) {
        String query = "DELETE FROM Bonus WHERE ename = ?";
        logger.debug("Executing delete query for ename: " + ename);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ename);
            boolean deleted = statement.executeUpdate() > 0;
            if (deleted) {
                logger.info("Bonus deleted successfully for ename: " +ename);
            } else {
                logger.warn("No rows affected while deleting bonus for ename: " + ename);
            }
            return deleted;
        } catch (SQLException e) {
            logger.error("Error executing delete query for ename: " +ename, e);
        }
        return false;
    }

    @Override
    public List<Bonus> findRange(int from, int to) {
        List<Bonus> bonuses = new ArrayList<>();
        String query = "SELECT * FROM Bonus LIMIT ? OFFSET ?";
        logger.debug("Executing findRange query from {} to {}", from, to);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, to - from + 1);
            statement.setInt(2, from);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bonuses.add(mapRowToBonus(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing findRange query from {} to {}", from, to, e);
        }
        logger.info("Number of bonuses retrieved in range: " + bonuses.size());
        return bonuses;
    }

    private Bonus mapRowToBonus(ResultSet resultSet) throws SQLException {
        Bonus bonus = new Bonus();
        bonus.setEname(resultSet.getString("ename"));
        bonus.setJob(resultSet.getString("job"));
        bonus.setSal(resultSet.getInt("sal"));
        bonus.setComm(resultSet.getInt("comm"));
        logger.debug("Mapped row to bonus: {}", bonus);
        return bonus;
    }
}