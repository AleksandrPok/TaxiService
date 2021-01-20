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
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.util.ConnectionUtil;

@Dao
public class CarDaoJdbcImpl implements CarDao {
    @Override
    public Car create(Car car) {
        String create = "INSERT INTO cars (manufacturer_id, model) VALUES(?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(create,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, car.getManufacturer().getId());
            statement.setString(2, car.getModel());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                car.setId(resultSet.getObject(1, Long.class));
            }
            return car;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add car into database : " + car, e);
        }
    }

    @Override
    public Optional<Car> get(Long id) {
        String select = "SELECT c.id AS car_id, model, c.manufacturer_id,"
                + " m.name AS manufacturer_name, m.country AS manufacturer_country "
                + "FROM cars c INNER JOIN manufacturer m ON m.id = c.manufacturer_id "
                + "WHERE c.id = ? AND c.deleted = false";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                car = getCar(resultSet, connection);
            }
            return Optional.ofNullable(car);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car by id: " + id, e);
        }
    }

    @Override
    public List<Car> getAll() {
        String select = "SELECT c.id AS car_id, c.manufacturer_id, model, m.name "
                + "AS manufacturer_name, m.country AS manufacturer_country FROM cars c "
                + "INNER JOIN manufacturer m ON c.manufacturer_id = m.id "
                + "WHERE c.deleted = false";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(getCar(resultSet, connection));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get cars", e);
        }
    }

    @Override
    public Car update(Car car) {
        String updateCar = "UPDATE cars SET manufacturer_id = ?, model = ? WHERE id = ? "
                + "AND deleted = false";
        String deleteDrivers = "DELETE FROM cars_drivers WHERE car_id = ?";
        String insertDrivers = "INSERT INTO  cars_drivers (driver_id, car_id) VALUE(?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateStatement = connection.prepareStatement(updateCar);
                PreparedStatement deleteStatement = connection.prepareStatement(deleteDrivers);
                PreparedStatement insertStatement = connection.prepareStatement(insertDrivers)) {
            updateStatement.setLong(1, car.getManufacturer().getId());
            updateStatement.setString(2, car.getModel());
            updateStatement.setLong(3, car.getId());
            updateStatement.executeUpdate();
            deleteStatement.setLong(1, car.getId());
            deleteStatement.executeUpdate();
            insertStatement.setLong(2, car.getId());
            for (Driver driver : car.getDrivers()) {
                insertStatement.setLong(1, driver.getId());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update car, id: " + car.getId(), e);
        }
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String delete = "UPDATE cars SET deleted = true WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete car with id: " + id, e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String getAll = "SELECT cd.car_id, model, c.manufacturer_id, m.name AS manufacturer_name, "
                + "m.country AS manufacturer_country FROM cars_drivers cd "
                + "INNER JOIN cars c ON cd.car_id = c.id "
                + "INNER JOIN manufacturer m ON c.manufacturer_id = m.id "
                + "INNER JOIN drivers d ON d.id = cd.driver_id "
                + "WHERE cd.driver_id = ? AND d.deleted = false AND c.deleted = false";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getAll)) {
            statement.setLong(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(getCar(resultSet, connection));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get cars from database", e);
        }
    }

    private Car getCar(ResultSet resultSet, Connection connection) throws SQLException {
        Long id = resultSet.getObject("car_id", Long.class);
        String model = resultSet.getString("model");
        Manufacturer manufacturer = getManufacturer(resultSet);
        Car car = new Car(model, manufacturer);
        car.setId(id);
        car.setDrivers(getDrivers(connection, id));
        return car;
    }

    private Manufacturer getManufacturer(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("manufacturer_id", Long.class);
        String name = resultSet.getString("manufacturer_name");
        String country = resultSet.getString("manufacturer_country");
        Manufacturer manufacturer = new Manufacturer(name, country);
        manufacturer.setId(id);
        return manufacturer;
    }

    private List<Driver> getDrivers(Connection connection, Long carId) {
        String select = "SELECT cd.driver_id, name AS driver_name, license_number "
                + "FROM cars_drivers cd INNER JOIN drivers d ON d.id = cd.driver_id "
                + "WHERE cd.car_id = ? AND d.deleted = FALSE";
        try (PreparedStatement statement = connection.prepareStatement(select)) {
            statement.setLong(1, carId);
            ResultSet resultSet = statement.executeQuery();
            List<Driver> drivers = new ArrayList<>();
            while (resultSet.next()) {
                drivers.add(getDriver(resultSet));
            }
            return drivers;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get drivers for car " + carId, e);
        }
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getObject("driver_id", Long.class);
        String name = resultSet.getString("driver_name");
        String licenseNumber = resultSet.getString("license_number");
        Driver driver = new Driver(name, licenseNumber);
        driver.setId(id);
        return driver;
    }
}
