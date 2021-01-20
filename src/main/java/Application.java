import taxi.lib.Injector;
import taxi.model.Car;
import taxi.model.Driver;
import taxi.model.Manufacturer;
import taxi.service.CarService;
import taxi.service.DriverService;
import taxi.service.ManufacturerService;

public class Application {
    private static final Injector injector = Injector.getInstance("taxi");

    public static void main(String[] args) {
        ManufacturerService manufacturerService = (ManufacturerService)
                injector.getInstance(ManufacturerService.class);
        Manufacturer firstManufacturer = new Manufacturer("Ferrari", "Italy");
        Manufacturer secondManufacturer = new Manufacturer("Toyota", "Japan");
        Manufacturer thirdManufacturer = new Manufacturer("BMW", "Germany");
        manufacturerService.create(firstManufacturer);
        manufacturerService.create(secondManufacturer);
        manufacturerService.create(thirdManufacturer);
        System.out.println("ManufacturersList : " + manufacturerService.getAll());
        System.out.println("Second manufacturer : " + manufacturerService.get(2L));
        Manufacturer updateManufacturer = new Manufacturer("Peugeot", "France");
        updateManufacturer.setId(1L);
        manufacturerService.update(updateManufacturer);
        System.out.println("Updated ManufacturersList : " + manufacturerService.getAll());

        DriverService driverService = (DriverService) injector.getInstance(DriverService.class);
        Driver firstDriver = new Driver("Nathan", "IMD0277213");
        Driver secondDriver = new Driver("Alan", "IMD0876138");
        Driver thirdDriver = new Driver("Adam", "IMD0000284");
        driverService.create(firstDriver);
        driverService.create(secondDriver);
        driverService.create(thirdDriver);
        System.out.println("DriversList : " + driverService.getAll());
        System.out.println("Second driver : " + driverService.get(3L));
        Driver updateDriver = new Driver("Morena", "IMD1072555");
        updateDriver.setId(3L);
        driverService.update(updateDriver);
        System.out.println(driverService.getAll());

        CarService carService = (CarService) injector.getInstance(CarService.class);
        Car firstCar = new Car("406", firstManufacturer);
        Car secondCar = new Car("Corolla", secondManufacturer);
        Car thirdCar = new Car("E38", thirdManufacturer);
        carService.create(firstCar);
        carService.create(secondCar);
        carService.create(thirdCar);
        System.out.println("CarsList : " + carService.getAll());
        Car updateCar = new Car("Supra", manufacturerService.get(2L));
        updateCar.setId(1L);
        carService.update(updateCar);
        System.out.println(carService.getAll());
        carService.addDriverToCar(driverService.get(1L), carService.get(1L));
        carService.addDriverToCar(driverService.get(2L), carService.get(1L));
        carService.addDriverToCar(driverService.get(3L), carService.get(1L));
        carService.addDriverToCar(driverService.get(1L), carService.get(2L));
        carService.addDriverToCar(driverService.get(2L), carService.get(2L));
        carService.addDriverToCar(driverService.get(1L), carService.get(3L));
        carService.addDriverToCar(driverService.get(3L), carService.get(3L));
        System.out.println(carService.getAll());
        carService.removeDriverFromCar(driverService.get(2L), carService.get(3L));
        System.out.println(carService.getAll());
        System.out.println(carService.getAllByDriver(3L));
        manufacturerService.delete(1L);
        carService.delete(2L);
        driverService.delete(2L);
        System.out.println(manufacturerService.getAll());
        System.out.println(carService.getAll());
        System.out.println(driverService.getAll());
    }
}
