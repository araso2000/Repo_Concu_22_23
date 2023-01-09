package estudiar_Parte2.Tema_5_7;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Eje1 {
	
	class Tarea implements Callable<String> {
		
		private int num_tarea;
		
		public Tarea(int num_tarea) {
			this.num_tarea = num_tarea;
		}

		@Override
		public String call() throws Exception {
			String texto = null;
			
			try {
				Thread.sleep((int)(Math.random()*500));
				
				try {
					if(new Random().nextBoolean()) {
						texto = Thread.currentThread().getName() + "_Tarea " + num_tarea + " correcta.";
				    }
				}catch(RuntimeException e) {
					texto = Thread.currentThread().getName() + "_Tarea " + num_tarea + " con error.";
				}
			    
			}catch(InterruptedException e) {
				texto = Thread.currentThread().getName() + "_Error al dormir";
			}
			
			return texto;
		}
	}
	
	static final int N_TAREAS = 10;
	
	ExecutorService executorService;
	CompletionService<String> completionService;
	
	public void start() throws InterruptedException, ExecutionException {
		executorService = Executors.newFixedThreadPool(N_TAREAS);
        completionService = new ExecutorCompletionService<>(executorService);
        
        try {
        	for(int ii=0; ii< N_TAREAS*10; ii++) {
            	completionService.submit(new Tarea(ii));
            }
            
            for(int ii=0; ii< N_TAREAS*10; ii++) {
            	System.out.println(completionService.take().get());
            }
        }finally {
        	executorService.shutdown();
        }
	}    

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new Eje1().start();
	}
}