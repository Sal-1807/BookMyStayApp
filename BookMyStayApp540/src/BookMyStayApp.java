import java.util.*;

// MAIN CLASS
public class BookMyStayApp {


    abstract static class Room {
        String type;
        int beds;
        int size;
        double price;

        Room(String type, int beds, int size, double price) {
            this.type = type;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        void displayRoom() {
            System.out.println(type + " | Beds:" + beds + " | Size:" + size + "sqm | Price:$" + price);
        }
    }

    static class SingleRoom extends Room {
        SingleRoom() {
            super("Single Room", 1, 200, 100);
        }
    }

    static class DoubleRoom extends Room {
        DoubleRoom() {
            super("Double Room", 2, 350, 180);
        }
    }

    static class SuiteRoom extends Room {
        SuiteRoom() {
            super("Suite Room", 3, 500, 300);
        }
    }

    // ---------------- UC3: INVENTORY ----------------
    static class RoomInventory {
        HashMap<String, Integer> inventory = new HashMap<>();

        RoomInventory() {
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
        }

        void displayInventory() {
            System.out.println("\nInventory:");
            for (String type : inventory.keySet()) {
                System.out.println(type + " Available: " + inventory.get(type));
            }
        }
    }

    // ---------------- UC4: SEARCH ----------------
    static class RoomSearchService {
        void search(RoomInventory inv) {
            System.out.println("\nAvailable Rooms:");
            for (String type : inv.inventory.keySet()) {
                if (inv.inventory.get(type) > 0) {
                    System.out.println(type + " Available: " + inv.inventory.get(type));
                }
            }
        }
    }

    // ---------------- UC5: BOOKING QUEUE ----------------
    static class Reservation {
        String guest;
        String roomType;

        Reservation(String g, String r) {
            guest = g;
            roomType = r;
        }
    }

    static class BookingQueue {
        Queue<Reservation> queue = new LinkedList<>();

        void addRequest(Reservation r) {
            queue.offer(r);
        }

        Reservation nextRequest() {
            return queue.poll();
        }
    }

    // ---------------- UC6: ALLOCATION ----------------
    static class BookingService {
        Set<String> allocated = new HashSet<>();

        String allocate(Reservation r, RoomInventory inv) {

            if (inv.inventory.get(r.roomType) <= 0) {
                System.out.println("No rooms available for " + r.guest);
                return null;
            }

            String id;

            do {
                id = r.roomType.charAt(0) + "" + (int) (Math.random() * 100);
            } while (allocated.contains(id));

            allocated.add(id);

            inv.inventory.put(r.roomType,
                    inv.inventory.get(r.roomType) - 1);

            System.out.println("Reservation Confirmed for "
                    + r.guest + " | Room ID: " + id);

            return id;
        }
    }

    // ---------------- UC7: ADD-ON SERVICES ----------------
    static class AddOnService {
        String name;
        double price;

        AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    static class AddOnServiceManager {
        Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        void addService(String reservationId, AddOnService service) {
            serviceMap
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);

            System.out.println("Added " + service.name + " to Reservation " + reservationId);
        }

        double calculateTotal(String reservationId) {
            double total = 0;
            List<AddOnService> services = serviceMap.get(reservationId);

            if (services != null) {
                for (AddOnService s : services) {
                    total += s.price;
                }
            }
            return total;
        }

        void displayServices(String reservationId) {
            List<AddOnService> services = serviceMap.get(reservationId);

            if (services == null) {
                System.out.println("No add-on services.");
                return;
            }

            System.out.println("\nServices for Reservation " + reservationId + ":");

            for (AddOnService s : services) {
                System.out.println(s.name + " - $" + s.price);
            }

            System.out.println("Total Add-on Cost: $" + calculateTotal(reservationId));
        }
    }

    // ---------------- MAIN (UC1 → UC7 FLOW) ----------------
    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay");

        // UC2: Room creation
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        single.displayRoom();
        dbl.displayRoom();
        suite.displayRoom();

        // UC3: Inventory
        RoomInventory inv = new RoomInventory();
        inv.displayInventory();

        // UC4: Search
        RoomSearchService search = new RoomSearchService();
        search.search(inv);

        // UC5: Queue
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));

        // UC6: Allocation
        BookingService service = new BookingService();

        Reservation req;
        while ((req = queue.nextRequest()) != null) {
            service.allocate(req, inv);
        }

        // UC7: Add-on services
        AddOnServiceManager manager = new AddOnServiceManager();

        AddOnService wifi = new AddOnService("WiFi", 10);
        AddOnService breakfast = new AddOnService("Breakfast", 20);
        AddOnService spa = new AddOnService("Spa", 50);

        String res1 = service.allocate(new Reservation("David", "Single Room"), inv);

        if (res1 != null) {
            manager.addService(res1, wifi);
            manager.addService(res1, breakfast);
        }

        manager.displayServices(res1);
    }
}