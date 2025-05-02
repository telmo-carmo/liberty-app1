package pt.dsi.dpi.rest.dal;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
    @Resource(lookup = "jdbc/appDB1")
    private DataSource dataSource;


    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM Bonus";
        logger.debug("Executing count query: " + query);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                logger.info("Count query result: " + count);
                return count;
            }
        } catch (SQLException e) {
            logger.error("Error executing count query", e);
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
        logger.debug("Executing findOne query for ename: {}", ename);
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