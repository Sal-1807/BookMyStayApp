import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class BookMyStayApp {

    static class BookingService {

        Set<String> allocated=new HashSet<>();

        void allocate(Reservation r,RoomInventory inv){

            if(inv.inventory.get(r.roomType)<=0){

                System.out.println("No rooms available for "+r.guest);
                return;

            }

            String id;

            do{
                id=r.roomType.charAt(0)+""+(int)(Math.random()*100);
            }
            while(allocated.contains(id));

            allocated.add(id);

            inv.inventory.put(r.roomType,
                    inv.inventory.get(r.roomType)-1);

            System.out.println("Reservation Confirmed for "+r.guest+" Room ID:"+id);

        }

    }

    static class Reservation {

        String guest;
        String roomType;

        Reservation(String g,String r){
            guest=g;
            roomType=r;
        }

    }

    static class BookingQueue {

        Queue<Reservation> queue=new LinkedList<>();

        void addRequest(Reservation r){
            queue.offer(r);
        }

        Reservation nextRequest(){
            return queue.poll();
        }

    }

    static class RoomSearchService {

        void search(RoomInventory inv){

            System.out.println("Available Rooms:");

            for(String type:inv.inventory.keySet()){

                if(inv.inventory.get(type)>0)
                    System.out.println(type+" Available:"+inv.inventory.get(type));

            }

        }

    }

    static class RoomInventory {

        HashMap<String,Integer> inventory=new HashMap<>();

        RoomInventory(){

            inventory.put("Single Room",5);
            inventory.put("Double Room",3);
            inventory.put("Suite Room",2);

        }

        void displayInventory(){

            for(String type:inventory.keySet())
                System.out.println(type+" Available:"+inventory.get(type));

        }
    }

    abstract static class Room {

        String type;
        int beds;
        int size;
        double price;

        Room(String type,int beds,int size,double price){
            this.type=type;
            this.beds=beds;
            this.size=size;
            this.price=price;
        }

        void displayRoom(){
            System.out.println(type+" | Beds:"+beds+" | Size:"+size+"sqm | Price:$"+price);
        }
    }

    static class SingleRoom extends Room{
        SingleRoom(){
            super("Single Room",1,200,100);
        }
    }

    static class DoubleRoom extends Room{
        DoubleRoom(){
            super("Double Room",2,350,180);
        }
    }

    static class SuiteRoom extends Room{
        SuiteRoom(){
            super("Suite Room",3,500,300);
        }
    }
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable=5;
        int doubleAvailable=3;
        int suiteAvailable=2;

        single.displayRoom();
        System.out.println("Available:"+singleAvailable);

        dbl.displayRoom();
        System.out.println("Available:"+doubleAvailable);

        suite.displayRoom();
        System.out.println("Available:"+suiteAvailable);

        RoomInventory inv=new RoomInventory();
        inv.displayInventory();

        RoomSearchService search=new RoomSearchService();
        search.search(inv);

        BookingService service=new BookingService();

        Reservation req;

        BookingQueue queue = null;
        while(true) {
            assert false;
            if ((req = queue.nextRequest()) == null) break;

            service.allocate(req, inv);


        }

    }
}