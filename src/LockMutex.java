public class LockMutex {

    public static class SimpleMutex {
        private boolean locked = false;

        public synchronized void lock() throws InterruptedException {
            while (locked) {
                wait();
            }
            locked = true;
        }

        public synchronized void unlock() {
            locked = false;
            notify();
        }
    }
    private static int sharedCounter = 0;
    private static final SimpleMutex mutex = new SimpleMutex();
    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
       
        Runnable counterTask1 = () -> {
            try {
                for (int i = 0; i < ITERATIONS; i++) {
                    mutex.lock();
                    try {
                        sharedCounter++;
                    } finally {
                        mutex.unlock();
                    }
                }
                System.out.println("Contador 1 finalizou.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

       
        Runnable counterTask2 = () -> {
            try {
                for (int i = 0; i < ITERATIONS; i++) {
                    mutex.lock();
                    try {
                        sharedCounter++;
                    } finally {
                        mutex.unlock();
                    }
                }
                System.out.println("Contador 2 finalizou.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

      
        Thread t1 = new Thread(counterTask1, "Thread-Contador-1");
        Thread t2 = new Thread(counterTask2, "Thread-Contador-2");

        
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        
        System.out.printf("Valor final de sharedCounter = %d (esperado %d)%n",
                          sharedCounter, 2 * ITERATIONS);
    }
}