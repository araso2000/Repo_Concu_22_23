package misEjes_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Tema2_Eje13B {
	
	private static final int NUM_TRENES = 5;
	private static final int NUM_TRAMOS = 5;
	
	private static SimpleSemaphore[] sema;
	
	public static void tren(int numTren) {
		for(int ii=0;ii<NUM_TRAMOS;ii++) {
			sema[ii].acquire();
			sleepRandom(500);
			recorrerTramo(ii,numTren);
			if(ii!=0) {
				sema[ii-1].release();
			}
			
			if(ii==(NUM_TRAMOS-1)) {
				sema[ii].release();
			}
		}
	}
	
	private static void recorrerTramo(int numTramo,int numTren) {
		printlnI("IN/T-" + numTren + "/V-" + numTramo); 
		sleepRandom(500);
		printlnI("OUT/T-" + numTren + "/V-" + numTramo);
	}

	public static void main(String[] args) {
		sema = new SimpleSemaphore[NUM_TRAMOS];
		
		for(int ii=0;ii<NUM_TRAMOS;ii++) {
			sema[ii]=new SimpleSemaphore(1);
		}
		
		for (int i = 0; i < NUM_TRENES; i++) {
			createThread("tren", i);
		}
		startThreadsAndWait();
	}
}