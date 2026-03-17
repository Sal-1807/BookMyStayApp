import java.io.*;
import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String id;
    String roomType;

    public Reservation(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + id + ", Room: " + roomType;
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public void bookRoom(String type) {
        if (inventory.containsKey(type) && inventory.get(type) > 0) {
            inventory.put(type, inventory.get(type) - 1);
        }
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

// ---------------- SYSTEM STATE ----------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookings;
    RoomInventory inventory;

    public SystemState(List<Reservation> bookings, RoomInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// ---------------- PERSISTENCE SERVICE ----------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // SAVE
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    // LOAD
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("✅ System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("⚠️ No saved state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("❌ Error loading state: " + e.getMessage());
        }

        return null;
    }
}

// ---------------- MAIN ----------------
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Try loading previous state
        SystemState state = PersistenceService.load();

        List<Reservation> bookings;
        RoomInventory inventory;

        if (state == null) {
            // fresh start
            bookings = new ArrayList<>();
            inventory = new RoomInventory();
        } else {
            bookings = state.bookings;
            inventory = state.inventory;
        }

        // Simulate booking
        Reservation r1 = new Reservation("R1", "Single");
        Reservation r2 = new Reservation("R2", "Double");

        bookings.add(r1);
        bookings.add(r2);

        inventory.bookRoom("Single");
        inventory.bookRoom("Double");

        // Display current state
        System.out.println("\n--- Current Bookings ---");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        inventory.display();

        // Save state before exit
        PersistenceService.save(new SystemState(bookings, inventory));
    }
}