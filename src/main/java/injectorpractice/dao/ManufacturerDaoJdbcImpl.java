package injectorpractice.dao;

import injectorpractice.exceptions.DataProcessingException;
import injectorpractice.lib.Dao;
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
public class ManufacturerDaoJdbcImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String create = "INSERT INTO manufacturer (manufacturer_name, manufacturer_country)"
                + " VALUES(?, ?);";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(create,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, manufacturer.getName());
            statement.setString(2, manufacturer.getCountry());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                manufacturer.setId(resultSet.getLong(1));
            }
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create manufacturer to database "
                    + manufacturer, e);
        }
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        String select = "SELECT * FROM manufacturer WHERE manufacturer_id = ? AND "
                + "manufacturer_deleted = false;";
        Manufacturer manufacturer = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(select)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                manufacturer = getManufacturer(resultSet);
            }
            return Optional.ofNullable(manufacturer);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find manufacturer by id " + id, e);
        }
    }

    @Override
    public List<Manufacturer> getAll() {
        String selectAll = "SELECT * FROM manufacturer WHERE manufacturer_deleted = false;";
        List<Manufacturer> manufacturers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectAll)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                manufacturers.add(getManufacturer(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get manufactorers from database", e);
        }
        return manufacturers;
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String update = "UPDATE manufacturer SET manufacturer_name = ?, manufacturer_country = ? "
                + "WHERE manufacturer_id = ? AND manufacturer_deleted = false;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(update)) {
            statement.setString(1, manufacturer.getName());
            statement.setString(2, manufacturer.getCountry());
            statement.setLong(3, manufacturer.getId());
            statement.executeUpdate();
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update manufacturer: " + manufacturer, e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String delete = "UPDATE manufacturer SET manufacturer_deleted = true WHERE "
                + "manufacturer_id = ?;";
        int result;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);
            result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete manufacturer, id: " + id, e);
        }
    }

    private Manufacturer getManufacturer(ResultSet resultSet) {
        try {
            Long manufacturerId = resultSet.getObject("manufacturer_id", Long.class);
            String manufacturerName = resultSet.getString("manufacturer_name");
            String manufacturerCountry = resultSet.getString("manufacturer_country");
            Manufacturer manufacturer = new Manufacturer(manufacturerName, manufacturerCountry);
            manufacturer.setId(manufacturerId);
            return manufacturer;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create manufacturer from resultSet", e);
        }
    }
}
