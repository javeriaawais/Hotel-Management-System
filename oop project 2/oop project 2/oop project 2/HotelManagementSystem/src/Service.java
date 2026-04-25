public class Service {

    private final String name;
    private final double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }

    public static Service spa() {
        return new Service("Spa Treatment", 50.0);
    }
    public static Service roomService() {
        return new Service("Room Service", 30.0);
    }
    public static Service miniBar() {
        return new Service("Mini-Bar", 25.0);
    }
    public static Service laundry() {
        return new Service("Laundry", 15.0);
    }
    public static Service breakfast() {
        return new Service("Breakfast", 20.0);
    }
    public static Service airportPickup() {
        return new Service("Airport Pickup", 40.0);
    }
}
