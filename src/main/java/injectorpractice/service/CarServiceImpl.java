package injectorpractice.service;

import injectorpractice.dao.CarDao;
import injectorpractice.dao.DriverDao;
import injectorpractice.lib.Inject;
import injectorpractice.lib.Service;
import injectorpractice.model.Car;
import injectorpractice.model.Driver;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    @Inject
    private CarDao carDao;
    @Inject
    DriverDao driverDao;

    @Override
    public Car create(Car car) {
        return carDao.create(car);
    }

    @Override
    public Car get(Long id) {
        return carDao.get(id).get();
    }

    @Override
    public List<Car> getAll() {
        return carDao.getAll();
    }

    @Override
    public Car update(Car car) {
        return carDao.update(car);
    }

    @Override
    public boolean delete(Long id) {
        return carDao.delete(id);
    }

    @Override
    public void addDriverToCar(Driver driver, Car car) {
        if (driverDao.get(driver.getId()).isEmpty()) {
            driverDao.create(driver);
        }
        car.getDrivers().add(driver);
        if (carDao.get(car.getId()).isEmpty()) {
            carDao.create(car);
        } else {
            carDao.update(car);
        }
    }

    @Override
    public void removeDriverFromCar(Driver driver, Car car) {
        carDao.get(car.getId()).get().getDrivers().removeIf(d -> d.equals(driver));
    }

    @Override
    public List<Car> getAllByDriver(Long driverId) {
        return carDao.getAll().stream()
                .filter(c -> c.getDrivers().stream()
                        .anyMatch(d -> d.getId().equals(driverId)))
                .collect(Collectors.toList());
    }
}
