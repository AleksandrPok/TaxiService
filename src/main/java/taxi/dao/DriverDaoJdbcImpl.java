package taxi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import taxi.exception.DataProcessingException;
import taxi.lib.Dao;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;

@Dao
public class DriverDaoJdbcImpl implements DriverDao {
    @Override
    public Driver create(Driver driver) {
        String create = "INSERT INTO drivers (name, license_number, login, password) "
                + "VALUE (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(create,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenceNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                driver.setId(resultSet.getLong(1));
            }
            return driver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add driver into database: " + driver, e);
        }
    }

    @Override
    public Optional<Driver> get(Long id) {
        String select = "SELECT * FROM drivers WHERE id = ? AND deleted = false";
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
            return Optional.ofNullable(driver);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get driver by id:" + id, e);
        }
    }

    @Override
    public List<Driver> getAll() {
        String selectAll = "SELECT * FROM drivers WHERE deleted = false";
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectAll)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                drivers.add(getDriver(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get drivers from database", e);
        }
        return drivers;
    }

    @Override
    public Driver update(Driver driver) {
        String update = "UPDATE drivers SET name = ?, license_number = ?, login = ?, password = ? "
                + " WHERE id = ? AND deleted = false";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(update)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenceNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.setLong(5, driver.getId());
            statement.executeUpdate();
            return driver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update driver: " + driver, e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String delete = "UPDATE drivers SET deleted = true WHERE id = ?";
        int result;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);
            result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete driver from db, id: " + id, e);
        }
    }

    @Override
    public Optional<Driver> findByLogin(String login) {
        String find = "SELECT * FROM drivers WHERE login = ? AND deleted = false";
        Driver driver = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(find)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find driver by login: " + login, e);
        }
        return Optional.ofNullable(driver);
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String licenseNumber = resultSet.getString("license_number");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        Driver driver = new Driver(name, licenseNumber, login, password);
        driver.setId(id);
        return driver;
    }
}
