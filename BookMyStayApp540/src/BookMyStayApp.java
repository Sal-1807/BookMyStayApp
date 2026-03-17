import java.util.*;

// ---------------- BOOKING REQUEST ----------------
class BookingRequest {
    String id;
    String roomType;

    public BookingRequest(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
    }
}

// ---------------- THREAD-SAFE INVENTORY ----------------
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // synchronized critical section
    public synchronized boolean bookRoom(String type) {
        if (!inventory.containsKey(type)) {
            System.out.println("Invalid room type: " + type);
            return false;
        }

        if (inventory.get(type) <= 0) {
            System.out.println(Thread.currentThread().getName() +
                    " → No rooms available for " + type);
            return false;
        }

        // simulate delay (to expose race condition if unsynchronized)
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        inventory.put(type, inventory.get(type) - 1);

        System.out.println(Thread.currentThread().getName() +
                " → Booked " + type + " room");

        return true;
    }

    public void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// ---------------- THREAD-SAFE QUEUE ----------------
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest req) {
        queue.add(req);
    }

    public synchronized BookingRequest getRequest() {
        if (queue.isEmpty()) return null;
        return queue.poll();
    }
}

// ---------------- WORKER THREAD ----------------
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest req;

            // synchronized access to queue
            synchronized (queue) {
                req = queue.getRequest();
            }

            if (req == null) break;

            inventory.bookRoom(req.roomType);
        }
    }
}

// ---------------- MAIN ----------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple booking requests
        queue.addRequest(new BookingRequest("R1", "Single"));
        queue.addRequest(new BookingRequest("R2", "Single"));
        queue.addRequest(new BookingRequest("R3", "Single")); // should fail
        queue.addRequest(new BookingRequest("R4", "Double"));
        queue.addRequest(new BookingRequest("R5", "Suite"));
        queue.addRequest(new BookingRequest("R6", "Suite")); // should fail

        // Create multiple threads
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);
        Thread t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final inventory
        inventory.displayInventory();
    }
}