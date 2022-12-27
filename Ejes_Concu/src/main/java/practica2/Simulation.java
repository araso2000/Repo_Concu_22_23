package practica2;
public class Simulation {

    private static final int N_THREADS = 10; // Math.min(2, Runtime.getRuntime().availableProcessors() - 1);
    private static final int N_PRODUCERS = 1;
    public static final int MONITOR_INTERVAL_MS = 1_000;
    public static final int UNIVERSTRUM_EXECUTION_TIME_MS = 50_000;
    public static final int UNIVERSTRUM_SHUTDOWN_MONITOR_TIME = 5_000;

    public static void main(String[] args) throws InterruptedException {

        //Se crea la instancia de Univestrum, inicializada con N_TREADS.
        Universtrum universtrum = new Universtrum(N_THREADS);
        
        //Crear un Monitor, que monitorizará e imprimirá el estado de la instancia de Universtrum cada segundo.
        Monitor monitor = new Monitor(universtrum, MONITOR_INTERVAL_MS);
        //Arrancar el monitor de la instancia de Universtrum.
        monitor.startMonitor();
        monitor.stopMonitorFromMain();
        
        //Crear varios productores, que comenzarán a enviar tareas.
        for (int i = 0; i < N_PRODUCERS; i++) {
            new TaskProducer(universtrum, "Producer_" + i).start();
        }
        System.out.println("All producers started\n");
        
        System.out.println("\nArrancando el cluster de computación. Por favor, espere...\n");
        //Thread.sleep(3000);
        universtrum.start();
        
        sleep(UNIVERSTRUM_EXECUTION_TIME_MS);
        universtrum.shutdown(true);
        System.out.println("\nShutdown signal sent to Universtrum instance\n");

        sleep(UNIVERSTRUM_SHUTDOWN_MONITOR_TIME);
        monitor.stopMonitor();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        
        }
    }
}