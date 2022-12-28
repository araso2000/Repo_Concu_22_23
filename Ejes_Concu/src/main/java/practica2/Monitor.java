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

    //DONE
    public void startMonitor() {
        monitorThread = new Thread(() -> {
        	while (true) {
                System.out.println("\nUniverstrum status : " + universtrumInstance.getStatus()
                        + " with " + universtrumInstance.getPendingTasks().size() + "\n");
                try {
                    Thread.sleep(intervalInMillis);
                } catch (InterruptedException e) {
                	Thread.currentThread().interrupt();
                    break;
                }
            }
        },"monitorThread");
       
        monitorThread.start();
    }

    //DONE
    public void stopMonitor() {
    	monitorThread.interrupt();
    	//shutdownMonitorThread.interrupt();
    }
    
    /*public void stopMonitorFromMain() {
    	shutdownMonitorThread = new Thread(() -> {    		
    		
        	while(universtrumInstance.getIfShutdown()) {
    			try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e) {
    				Thread.currentThread().interrupt();
                    break;
    			}
    		}
        	System.out.println(Thread.currentThread().getName() + "_Apagando monitor...");
        	monitorThread.interrupt();
    		
    	},"shutdownMonitorThread");
    	
    	shutdownMonitorThread.start();
    }*/
}
