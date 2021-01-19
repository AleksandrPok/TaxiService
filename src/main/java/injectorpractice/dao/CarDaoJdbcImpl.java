package injectorpractice.dao;

import injectorpractice.exceptions.DataProcessingException;
import injectorpractice.lib.Dao;
import injectorpractice.model.Car;
import injectorpractice.model.Driver;
import injectorpractice.model.Manufacturer;
import injectorpractice.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        String select = "SELECT c.id AS car_id, c.model, m.id AS manufacturer_id, m.name "
                + "AS manufacturer_name, m.country AS manufacturer_country, d.id AS driver_id, "
                + "d.name AS driver_name, d.license_number FROM cars c "
                + "LEFT JOIN manufacturer m ON m.id = c.manufacturer_id "
                + "LEFT JOIN cars_drivers cd ON cd.car_id = c.id "
                + "LEFT JOIN drivers d ON d.id = cd.car_id "
                + "WHERE c.id = ? AND c.deleted = false AND "
                + "(d.deleted = false OR d.deleted IS NULL)";
        Car car = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                car = getCar(resultSet);
            }
            return Optional.ofNullable(car);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get car by id: " + id, e);
        }
    }

    @Override
    public List<Car> getAll() {
        String select = "SELECT c.id AS car_id, c.model, m.id AS manufacturer_id, "
                + "m.name AS manufacturer_name, m.country AS manufacturer_country, "
                + "d.id AS driver_id, d.name AS driver_name, d.license_number "
                + "FROM cars c LEFT JOIN manufacturer m ON m.id = c.manufacturer_id "
                + "LEFT JOIN cars_drivers cd ON cd.car_id = c.id "
                + "LEFT JOIN drivers d ON d.id = cd.driver_id WHERE c.deleted = false "
                + "AND (d.deleted = false OR d.deleted IS NULL) ORDER BY c.id";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(getCar(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find cars", e);
        }
    }

    @Override
    public Car update(Car car) {
        String updateCar = "UPDATE cars SET manufacturer_id = ?, model = ? WHERE id = ? "
                + "AND deleted = false";
        String deleteDrivers = "DELETE FROM cars_drivers WHERE car_id = ?";
        String insertDrivers = "INSERT INTO  cars_drivers (driver_id, car_id) VALUE(?, ?)";
        Long id = car.getId();
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
            for (Driver driver : car.getDrivers()) {
                insertStatement.setLong(1, driver.getId());
                insertStatement.setLong(2, car.getId());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update car: " + car, e);
        }
        return car;
    }

    @Override
    public boolean delete(Long id) {
        String delete = "UPDATE cars SET deleted = true WHERE id = ?";
        int result;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);
            result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete car with id: " + id, e);
        }
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        String getAll = "SELECT c.id AS car_id, c.model, m.id AS manufacturer_id, "
                + "m.name AS manufacturer_name, m.country AS manufacturer_country, "
                + "d.id AS driver_id, d.name AS driver_name, d.license_number "
                + "FROM cars c LEFT JOIN manufacturer m on m.id = c.manufacturer_id "
                + "LEFT JOIN cars_drivers cd on c.id = cd.car_id "
                + "LEFT JOIN drivers d on d.id = cd.driver_id "
                + "WHERE  c.deleted = false AND d.id = ? AND d.deleted = false";
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(getAll,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(getCar(resultSet));
            }
            return cars;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get cars from database", e);
        }
    }

    private Car getCar(ResultSet resultSet) {
        try {
            Long id = resultSet.getObject("car_id", Long.class);
            String model = resultSet.getString("model");
            Manufacturer manufacturer = getManufacturer(resultSet);
            List<Driver> drivers = getDrivers(resultSet, id);
            Car car = new Car(model, manufacturer);
            car.setId(id);
            car.setDrivers(drivers);
            return car;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create car from resultSet", e);
        }
    }

    private Manufacturer getManufacturer(ResultSet resultSet) {
        try {
            Long id = resultSet.getObject("manufacturer_id", Long.class);
            String name = resultSet.getString("manufacturer_name");
            String country = resultSet.getString("manufacturer_country");
            Manufacturer manufacturer = new Manufacturer(name, country);
            manufacturer.setId(id);
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create manufacturer from resultSet", e);
        }
    }

    private List<Driver> getDrivers(ResultSet resultSet, Long carId) {
        List<Driver> drivers = new ArrayList<>();
        try {
            while (resultSet.next() && resultSet.getObject("car_id", Long.class).equals(carId)) {
                Driver driver = getDriver(resultSet);
                if (driver.getId() != null) {
                    drivers.add(driver);
                }
            }
            resultSet.previous();
            return drivers;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create drivers list from resultSet", e);
        }
    }

    private Driver getDriver(ResultSet resultSet) {
        try {
            Long id = resultSet.getObject("driver_id", Long.class);
            String name = resultSet.getString("driver_name");
            String licenseNumber = resultSet.getString("license_number");
            Driver driver = new Driver(name, licenseNumber);
            driver.setId(id);
            return driver;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create driver from resultSet", e);
        }
    }
}
