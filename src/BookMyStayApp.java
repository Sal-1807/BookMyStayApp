
public class BookMyStayApp {
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
    }
}