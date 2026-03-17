import java.util.*;

// ---------------- CUSTOM EXCEPTION ----------------
class BookingException extends Exception {
    public BookingException(String message) {
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
    String id;
    Room room;
    boolean isCancelled;

    public Reservation(String id, Room room) {
        this.id = id;
        this.room = room;
        this.isCancelled = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + id +
                ", Room: " + room.getType() +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
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

    public void bookRoom(String type) throws BookingException {
        if (!inventory.containsKey(type)) {
            throw new BookingException("Invalid room type");
        }

        if (inventory.get(type) <= 0) {
            throw new BookingException("No rooms available for " + type);
        }

        inventory.put(type, inventory.get(type) - 1);
    }

    public void releaseRoom(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {
    private RoomInventory inventory;
    private Map<String, Reservation> reservations;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservations = new HashMap<>();
    }

    public Reservation book(String id, String type) throws BookingException {

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
                throw new BookingException("Invalid room type");
        }

        Reservation r = new Reservation(id, room);
        reservations.put(id, r);

        return r;
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// ---------------- CANCELLATION SERVICE ----------------
class CancellationService {
    private RoomInventory inventory;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory) {
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    public void cancel(Reservation r) throws BookingException {

        if (r == null) {
            throw new BookingException("Reservation does not exist");
        }

        if (r.isCancelled) {
            throw new BookingException("Reservation already cancelled");
        }

        // Step 1: push to stack (LIFO rollback)
        rollbackStack.push(r.room.getType());

        // Step 2: restore inventory
        inventory.releaseRoom(r.room.getType());

        // Step 3: mark cancelled
        r.isCancelled = true;

        System.out.println("Cancelled: " + r.id);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

// ---------------- MAIN ----------------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);
        CancellationService cancelService = new CancellationService(inventory);

        try {
            // Book rooms
            Reservation r1 = bookingService.book("R1", "Single");
            Reservation r2 = bookingService.book("R2", "Double");

            System.out.println(r1);
            System.out.println(r2);

            inventory.displayInventory();

            // Cancel booking
            cancelService.cancel(r1);

            inventory.displayInventory();

            // Try invalid cancel
            cancelService.cancel(r1); // should fail

        } catch (BookingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        cancelService.showRollbackStack();
    }
}