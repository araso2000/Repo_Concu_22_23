package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Tema2_Eje13B {
	
	private static final int NUM_TRENES = 10;
	private static final int NUM_TRAMOS = 2;
	
	private static SimpleSemaphore[] sema;
	
	public static void tren(int numTren) {
		for(int ii=0;ii<NUM_TRAMOS;ii++) {
			sema[ii].acquire();
			sleepRandom(500);
			recorrerTramo(ii,numTren);
			/*if(ii!=(NUM_TRAMOS-1)) {
				sema[ii++].acquire();
			}*/
			sema[ii].release();
		}
	}
	
	private static void recorrerTramo(int numTramo,int numTren) {
		println("ENTRA el TREN " + numTren + " en el TRAMO " + numTramo); 
		sleepRandom(500);
		println("SALE  el TREN " + numTren + " del   TRAMO " + numTramo);
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