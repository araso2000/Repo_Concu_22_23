package practica2;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * Universtrum
 *
 * Sistema que modela el API de entrada al supercomputador para enviar tareas complejas para resolver.
 *
 */
public class Universtrum {

    public static enum Status {
        READY, RUNNING, SHUTTING_DOWN, STOPPED;
    }

    private int concurrencyLevel = 0;
    private volatile Status status;
        
    //DONE
    ExecutorService executorService;
    CompletionService<ComplexTaskResult> completionService;
    
    Thread shutdownThread;
    
    //DONE
    class ComplexTaskResolver implements Callable<ComplexTaskResult> {

        private final ComplexTask task;
        private final LocalDateTime received;

        ComplexTaskResolver(ComplexTask task) {
            received = LocalDateTime.now();
            this.task = task;
        }

        @Override
        public ComplexTaskResult call() throws Exception {
            LocalDateTime start = LocalDateTime.now();
            int resultValue = task.solve();
            Duration executionTime = Duration.between(start, LocalDateTime.now());
            Duration waitingTime = Duration.between(received, start);
            pendingTasks.remove(task);
            return new ComplexTaskResult(task.getTaskId(), resultValue, executionTime.toMillis(), waitingTime.toMillis());
        }
    }
    
    public Universtrum(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        status = Status.READY;
    }
    
    private Collection<ComplexTask> pendingTasks = new ConcurrentLinkedQueue<>();

    //DONE
    public Future<ComplexTaskResult> submit(ComplexTask task) {
        if(status.equals(Status.RUNNING)) {
    		Future<ComplexTaskResult> submitted = completionService.submit(new ComplexTaskResolver(task));
            pendingTasks.add(task);
            
            return submitted;
            
    	}else {
    		return null;
    	} 
    }

    public Status getStatus() {
        return status;
    }
    
    public CompletionService<ComplexTaskResult> getCompletionService(){
    	return this.completionService;
    }

    public Collection<ComplexTask> getPendingTasks() {
        return pendingTasks;
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(this.concurrencyLevel);
        completionService = new ExecutorCompletionService<>(executorService);
        
        shutdownThread = new Thread(() -> shutdown(false),"shutdown_thread");
        
        this.status=Status.RUNNING;
    }

    //DONE
    public void shutdown(boolean endTasks) {
    	while(!endTasks) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	this.status=Status.SHUTTING_DOWN;
    	System.out.println("\nRecibida orden de apagado.");
    	System.out.println("Ya no se aceptan nuevas tareas.");
    	System.out.println("Esperando a la finalización de las tareas en ejecución... Pendientes: " + pendingTasks.size() + "\n");
    	executorService.shutdown();
    	
    	while(!executorService.isTerminated()) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	this.status=Status.STOPPED;
    	System.out.println("\nAPAGADO");
    }
}