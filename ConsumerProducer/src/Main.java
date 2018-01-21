import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;

class Producer extends Thread {

    static final int MAXQUEUE = 6;
    private Vector messages = new Vector();
    private static String[] theFood = new String[]{ "Turkey", "Chicken", "Peanit Butter", "Cheese", "Milk", "Burgs"};

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println( this.getName() + ": Adding messages to queue .. maybe ..");
                putMessage();
                sleep(5000);
            }
        } catch (InterruptedException e) {
        }
    }

    private synchronized void putMessage() throws InterruptedException {
        if ( messages.size() == MAXQUEUE) {
            System.out.println( this.getName() + ": Message queue filled with " + MAXQUEUE + " objects.");
            sleep(1000);
            while (messages.size() == MAXQUEUE) {
                System.out.println( this.getName() + ": The queue is filled up so I'll wait for it to empty before filling it up ... here I go ..." );
                wait();
            }
        } else {
            System.out.println( this.getName() + ": Message less than " + MAXQUEUE + " objects ( " + messages.size() + " )");
            sleep(1000);
        }
        //messages.addElement(new java.util.Date().toString());
        for ( int i=0; i<theFood.length; i++ ){
            System.out.println( this.getName() + ": ( Putting " + theFood[i] + " in the fridge )");
            messages.addElement(theFood[i]);
            sleep(1000);
        }
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    // Called by Consumer
    public synchronized String getMessage() throws InterruptedException {
        // notify any waiting threads
        System.out.println( this.getName() + ": I am the consumer thread; I'll notify the producer that it should put some messages in case there are none" );
        notify();
        // oops there is a bug here
        while (messages.size() != 0) {
            System.out.println( this.getName() + ": >>> HELLO HELLO! There's no food in the fridge, or is there? <<<");
            sleep(1000);
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }

        String message = null;
        System.out.println( this.getName() + ": I am the consumer; I'll check for messages .. ");
        if ( messages.size() != 0 ) {
            message = (String) messages.firstElement();
            messages.removeElement(message);
        } else {
            message = "nothing";
        }
        return message;
    }
}

class Consumer extends Thread {

    Producer producer;

    Consumer(Producer p) {
        producer = p;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = producer.getMessage();
                System.out.println(this.getName() + ": Hyello, there's " + message + " in the fridge.");
                sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

 /*   public static void main(String args[]) {
        Producer producer = new Producer();
        producer.start();
        // Consumer has the producer instance which is running, and than Consumer starts
        new Consumer(producer).start();
    }*/
}

class queue {
    private Queue q;
    public queue( int size) {

    }


}



public static void main(String args[]) {
    Queue queue = new Queue();
    Producer producer = new Producer(queue);
    Consumer consumer = new Consumer(queue);
    producer.start();
    consumer.start();
}