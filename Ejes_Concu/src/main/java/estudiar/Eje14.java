package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje14 {
	
	private static volatile int numBarrera;
	private static final int NUM_PROCESOS = 5;
	
	private static SimpleSemaphore barrera; 
	private static SimpleSemaphore aumentar;
	
	public static void proceso() {
		printlnI("A");
		aumentar.acquire();
		numBarrera++;
		
		if(numBarrera < NUM_PROCESOS) {
			aumentar.release();
			barrera.acquire();
		}else {
			aumentar.release();
			for(int ii=0;ii<NUM_PROCESOS-1;ii++) {
				barrera.release();
			}
		}		
		printlnI("B");
	}

	public static void main(String[] args) {
		numBarrera = 0;
		barrera = new SimpleSemaphore(0);
		aumentar = new SimpleSemaphore(1);
		
		createThreads(NUM_PROCESOS,"proceso");
		
		startThreadsAndWait();
	}

}
