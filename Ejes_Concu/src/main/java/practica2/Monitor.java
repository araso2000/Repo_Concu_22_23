package practica2;

/**
 * Clase que implementa el soporte para monitorizar el estado de una instancia de Unvierstrum
 */
public class Monitor {

    private final Universtrum universtrumInstance;
    private final long intervalInMillis;
    
    private Thread monitorThread;

    public Monitor(Universtrum instance, long intervalInMillis) {
        this.universtrumInstance = instance;
        this.intervalInMillis = intervalInMillis;
    }

    public void startMonitor() {
        monitorThread = new Thread(() -> {
            while (true) {
                System.out.println("Universtrum status : " + universtrumInstance.getStatus()
                        + " with " + universtrumInstance.getPendingTasks().size());
                try {
                    Thread.sleep(intervalInMillis);
                } catch (InterruptedException e) {
                    // TODO: Repair the sabotaged doorbell: https://www.javaspecialists.eu/archive/Issue146-The-Secrets-of-Concurrency-Part-1.html
                	Thread.currentThread().interrupt();
                    break;
                }
            }
        },"monitorThread");
        //The law of the distracted fisherman ;
       
        monitorThread.start();

        //TODO Deberá arrancar un hilo que compruebe e imprima el estado de la instancia de Universtrum
        // y cuantas tareas están pendientes de ejecución.
        // la invocación de este método debe de ser asincrona, el método debe arrancar el hilo de monitorización
        // y devolver la ejecución mientras se ejecuta el hilo de monitorización del estado.
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    public void stopMonitor() {
        // TODO Deberá parar el hilo de impresión del estado de la instancia de Universtrum.
    	monitorThread.interrupt();
    }
}
