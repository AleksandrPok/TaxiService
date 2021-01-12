package injectorpractice.dao;

import injectorpractice.db.Storage;
import injectorpractice.lib.Dao;
import injectorpractice.model.Manufacturer;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {
    public Manufacturer create(Manufacturer manufacturer) {
        Storage.addManufacturer(manufacturer);
        return manufacturer;
    }

    public Optional<Manufacturer> get(Long id) {
        return Storage.manufacturers.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public List<Manufacturer> getAll() {
        return Storage.manufacturers;
    }

    public Manufacturer update(Manufacturer manufacturer) {
        IntStream.range(0, Storage.manufacturers.size())
                .filter(i -> (Storage.manufacturers.get(i).getId().equals(manufacturer.getId())))
                .findFirst()
                .ifPresent(i -> Storage.manufacturers.set(i, manufacturer));
        return manufacturer;
    }

    public boolean delete(Long id) {
        return Storage.manufacturers.removeIf(m -> (m.getId().equals(id)));
    }
}
