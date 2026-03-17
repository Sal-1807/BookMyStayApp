import java.util.*;

// ---------------- CUSTOM EXCEPTION ----------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- ROOM ----------------
abstract class Room {
    protected String type;
    protected double price;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 5000);
    }
}

// ---------------- RESERVATION ----------------
class Reservation {
    private String id;
    private Room room;

    public Reservation(String id, Room room) {
        this.id = id;
        this.room = room;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + id +
                ", Room Type: " + room.getType() +
                ", Price: " + room.getPrice();
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void validateRoom(String type) throws InvalidBookingException {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void checkAvailability(String type) throws InvalidBookingException {
        if (inventory.get(type) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }
    }

    public void bookRoom(String type) throws InvalidBookingException {
        validateRoom(type);
        checkAvailability(type);

        // update safely
        inventory.put(type, inventory.get(type) - 1);
    }
}

// ---------------- VALIDATOR ----------------
class BookingValidator {

    public static void validateInput(String id, String type) throws InvalidBookingException {
        if (id == null || id.isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty");
        }

        if (type == null || type.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public Reservation book(String id, String type) throws InvalidBookingException {

        // FAIL FAST
        BookingValidator.validateInput(id, type);
        inventory.bookRoom(type);

        Room room;

        switch (type) {
            case "Single":
                room = new SingleRoom();
                break;
            case "Double":
                room = new DoubleRoom();
                break;
            case "Suite":
                room = new SuiteRoom();
                break;
            default:
                throw new InvalidBookingException("Invalid room selection");
        }

        return new Reservation(id, room);
    }
}

// ---------------- MAIN ----------------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases
        String[][] bookings = {
                {"R1", "Single"},
                {"R2", "Double"},
                {"R3", "Suite"},
                {"R4", "Suite"},     // should fail (no inventory)
                {"", "Single"},      // invalid ID
                {"R6", "Luxury"}     // invalid type
        };

        for (String[] b : bookings) {
            try {
                Reservation r = service.book(b[0], b[1]);
                System.out.println("SUCCESS: " + r);
            } catch (InvalidBookingException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        System.out.println("\nSystem continues running safely ✅");
    }
}