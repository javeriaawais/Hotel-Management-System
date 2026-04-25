import java.util.ArrayList;
import java.util.List;

public class Room {

    private final int roomNumber;
    private final RoomType type;
    private final double rate;
    private String guestName;
    private int nights;
    private RoomStatus status;
    private final List<Service> services = new ArrayList<>();

    public Room(int roomNumber, RoomType type, double rate) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.rate = rate;
        this.status = RoomStatus.AVAILABLE;
    }

    public double getBasePrice() {
        return rate;
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public double getRate() { return rate; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public int getNights() { return nights; }
    public void setNights(int nights) { this.nights = nights; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    public List<Service> getServices() { return services; }

    public double getRoomTotal() {
        return rate * nights;
    }

    public double getServicesTotal() {
        double total = 0.0;
        for (int i = 0; i < services.size(); i++) {
            total += services.get(i).getPrice();
        }
        return total;
    }

    public double getGrandTotal() {
        return getRoomTotal() + getServicesTotal();
    }

    public void resetAfterCheckout() {
        guestName = null;
        nights = 0;
        services.clear();
        status = RoomStatus.NEEDS_CLEANING;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber;
    }
}