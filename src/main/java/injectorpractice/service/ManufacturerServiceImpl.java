package injectorpractice.service;

import injectorpractice.dao.ManufacturerDao;
import injectorpractice.lib.Inject;
import injectorpractice.lib.Service;
import injectorpractice.model.Manufacturer;
import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Inject
    ManufacturerDao manufacturerDao;

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        manufacturerDao.create(manufacturer);
        return manufacturer;
    }

    @Override
    public Manufacturer get(Long id) {
        return manufacturerDao.get(id).get();
    }

    @Override
    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        manufacturerDao.update(manufacturer);
        return manufacturer;
    }

    @Override
    public boolean delete(Long id) {
        return manufacturerDao.delete(id);
    }
}
