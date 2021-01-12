import injectorpractice.lib.Injector;
import injectorpractice.model.Manufacturer;
import injectorpractice.service.ManufacturerService;

public class Application {
    private static final Injector injector = Injector.getInstance("injectorpractice");

    public static void main(String[] args) {
        ManufacturerService manufacturerService = (ManufacturerService)
                injector.getInstance(ManufacturerService.class);
        Manufacturer firstManufacturer = new Manufacturer("Ferrari", "Italy");
        Manufacturer secondManufacturer = new Manufacturer("Toyota", "Japan");
        manufacturerService.create(firstManufacturer);
        manufacturerService.create(secondManufacturer);
        System.out.println("ManufacturersList : " + manufacturerService.getAll());
        System.out.println("Second manufacturer : " + manufacturerService.get(2L));
        Manufacturer updateManufacturer = new Manufacturer("Peugeot", "France");
        updateManufacturer.setId(2L);
        manufacturerService.update(updateManufacturer);
        System.out.println("Updated ManufacturersList : " + manufacturerService.getAll());
        manufacturerService.delete(1L);
        System.out.println("ManufacturersList after deleting 1L : " + manufacturerService.getAll());
    }
}
