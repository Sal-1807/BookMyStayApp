import java.util.*;

// ---------------- ROOM CLASSES ----------------
abstract class Room {
    protected String roomType;
    protected double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
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
    private String reservationId;
    private Room room;

    public Reservation(String reservationId, Room room) {
        this.reservationId = reservationId;
        this.room = room;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Room Type: " + room.getRoomType() +
                ", Price: " + room.getPrice();
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r); // maintains order
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ---------------- REPORT SERVICE ----------------
class BookingReportService {

    public void showAllBookings(BookingHistory history) {
        System.out.println("\n---- Booking History ----");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }
    }

    public void showTotalBookings(BookingHistory history) {
        System.out.println("\nTotal Bookings: " + history.getAllReservations().size());
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate booking confirmations
        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        Reservation res1 = new Reservation("R101", r1);
        Reservation res2 = new Reservation("R102", r2);
        Reservation res3 = new Reservation("R103", r3);

        // Add to booking history (UC8 core logic)
        history.addReservation(res1);
        history.addReservation(res2);
        history.addReservation(res3);

        // Generate reports
        reportService.showAllBookings(history);
        reportService.showTotalBookings(history);
    }
}