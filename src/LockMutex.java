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
}

    public static void main(String[] args) throws InterruptedException {
        // Define a primeira tarefa: incrementa sharedCounter ITERATIONS vezes
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

        // Define a segunda tarefa: faz o mesmo trabalho
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

        // Cria as threads
        Thread t1 = new Thread(counterTask1, "Thread-Contador-1");
        Thread t2 = new Thread(counterTask2, "Thread-Contador-2");

        // Inicia as duas threads
        t1.start();
        t2.start();

        // Aguarda ambas terminarem
        t1.join();
        t2.join();

        // Exibe o valor final; deve ser 2 * ITERATIONS
        System.out.printf("Valor final de sharedCounter = %d (esperado %d)%n",
                          sharedCounter, 2 * ITERATIONS);
    }
