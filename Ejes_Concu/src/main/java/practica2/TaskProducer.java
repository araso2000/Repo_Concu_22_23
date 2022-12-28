package practica2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskProducer {

    public static final int N_TASKS = 10;
    public static final int TASK_CREATION_INTERVAL_MAX = 500;

    private final Universtrum universtrumInstance;
    private final String producerId;

    private List<Future<ComplexTaskResult>> submittedTasks = new ArrayList<>();

    public TaskProducer(Universtrum universtrumInstance, String producerId) {
        this.universtrumInstance = universtrumInstance;
        this.producerId = producerId;
    }

    //DONE
    public void start() throws InterruptedException {
        new Thread(() -> {
    		while(!universtrumInstance.getStatus().equals(Universtrum.Status.RUNNING)) {
    			System.out.println("\nTaskProducer: No se pueden mandar tareas debido a: Estado inválido de Universtrum: " + universtrumInstance.getStatus() + "\n");
    			sleep(500);
    		}
    		for(int ii = 0; ii < N_TASKS && universtrumInstance.getStatus().equals(Universtrum.Status.RUNNING) ; ii++) {
    			ComplexTask tarea = ComplexTaskRandomGenerator.generateRandomComplexTask(producerId,Integer.toString(ii));
    	    	System.out.println("Se envia la siguiente tarea para ser ejecutada: " + tarea.toString());
    	    	
    	    	Future<ComplexTaskResult> tarea1 = universtrumInstance.submit(tarea);
    	    	if(tarea1 != null) {
    	    		submittedTasks.add(tarea1);
    	    	}
    	    	    	    	
    	    	sleepRandom(500);
    		}
    		
    		checkFinishedTask();
    		
    		if(universtrumInstance.getStatus().equals(Universtrum.Status.RUNNING)) {
    			System.out.println("\nTareas terminadas. Enviando señal de apagado...\n");
        		universtrumInstance.shutdown(true);
    		}
    		
    	}, "productor_" + this.producerId).start();
    }

    //DONE
    public void checkFinishedTask() {
    	for(int ii = 0 ; ii < N_TASKS && !universtrumInstance.getStatus().equals(Universtrum.Status.STOPPED) ; ii++) {
			try { 
				
				System.out.println(universtrumInstance.getCompletionService().take().get().toString());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
    }

    public static void sleepRandom(long millis) {
        sleep((long) (Math.random() * millis));
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
