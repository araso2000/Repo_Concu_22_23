package practica2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskProducer {

    public static final int N_TASKS = 10;
    public static final int TASK_CREATION_INTERVAL_MAX = 500;

    private final Universtrum universtrumInstance;
    private final String producerId;

    private Thread producerThread;

    private List<Future<ComplexTaskResult>> submittedTasks = new ArrayList<>();

    public TaskProducer(Universtrum universtrumInstance, String producerId) {
        this.universtrumInstance = universtrumInstance;
        this.producerId = producerId;
    }

    public void start() throws InterruptedException {
        //TODO: Crear y arrancar un hilo que implemente el siguiente comportamiento para un Productor de tareas de Universtrum:
        // hacer N_TASKS veces.
            // Generar una ComplexTask (usar ComplexTaskRandomGenerator.generateRandomComplexTask()
            // Enviar la tarea a la instancia de Universtrum. (debe imprimir un mensaje indicando la tarea que se envia).
            // el hilo se bloqueará un tiempo aleatorio entre 100 y 500 milisegundos. (se puede utilizar el método sleepRandom).
        // Al acabar de enviar las tareas, se quedará a la espera de que se resuelvan las tareas enviadas por
        // este proceso, imprimiendo el resultado cuando estén finalizadas.
        // Cuando estén todas las tareas acabadas, el productor terminará diciendo que se ha completado todas sus tareas

    	new Thread(() -> {
    		while(!universtrumInstance.getStatus().equals(Universtrum.Status.RUNNING)) {}
    		for(int ii = 0; ii < N_TASKS; ii++) {
    			ComplexTask tarea = ComplexTaskRandomGenerator.generateRandomComplexTask(producerId,Integer.toString(((int)(Math.random()*5000))));
    	    	System.out.println("Se envia la siguiente tarea para ser ejecutada: " + tarea.toString());
    	    	
    	    	Future<ComplexTaskResult> tarea1 = universtrumInstance.submit(tarea);
    	    	if(tarea1 != null) {
    	    		submittedTasks.add(tarea1);
    	    	}
    	    	
    	    	sleepRandom(500);
    		}
    		
    		for(Future<ComplexTaskResult> task : submittedTasks) {
    			try {
					task.get().toString();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
    		}
    		
    		System.out.println("Tareas terminadas");
    		
    	}, "productor").start();
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
