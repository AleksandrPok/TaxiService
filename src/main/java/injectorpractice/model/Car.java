package injectorpractice.model;

import java.util.List;

public class Car {
    private long id;
    private String model;
    private Manufacturer manufacturer;
    private List<Driver> drivers;

    public Car(long id, String model, Manufacturer manufacturer, List<Driver> drivers) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.drivers = drivers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }
}
