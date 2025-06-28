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
