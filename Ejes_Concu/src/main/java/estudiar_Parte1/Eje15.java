package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje15 {
	
	private static final int PROCESOS = 4;
	private static volatile int numBarrera;
	
	private static SimpleSemaphore barrera, aumentar, desbloqueo;
	
	public static void proceso(int num) {
		while(true) {
			print(""+num);
			aumentar.acquire();
			numBarrera++;
			
			if(numBarrera<PROCESOS) {
				aumentar.release();
				sleepRandom(500);
				barrera.acquire();
				desbloqueo.release();
			}else {
				print("\n");
				numBarrera = 0;
				
				barrera.release(PROCESOS-1);
				desbloqueo.acquire(PROCESOS-1);
				aumentar.release();
			}
		}
	}

	public static void main(String[] args) {
		numBarrera = 0;
		barrera = new SimpleSemaphore(0);
		desbloqueo = new SimpleSemaphore(0);
		aumentar = new SimpleSemaphore(1);
		
		for(int ii=0;ii<PROCESOS;ii++) {
			createThread("proceso",ii);
		}
		
		startThreadsAndWait();
	}

}
