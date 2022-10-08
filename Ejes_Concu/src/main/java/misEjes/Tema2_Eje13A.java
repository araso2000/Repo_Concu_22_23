package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Tema2_Eje13A {
	
	private static final int NUM_TRENES = 5;
	
	private static SimpleSemaphore sem_A;
	private static SimpleSemaphore sem_B;
	private static SimpleSemaphore sem_C;
	
	public static void tren(int numTren) {
		sem_A.acquire();
		sleepRandom(500);
		recorrerTramoA(numTren);
		sem_A.release();

		sem_B.acquire();
		sleepRandom(500);
		recorrerTramoB(numTren);
		sem_B.release();

		sem_C.acquire();
		sleepRandom(500);
		recorrerTramoC(numTren);
		sem_C.release();
	}
	
	private static void recorrerTramoA(int numTren) {
		printlnI("Entra TA T" + numTren); 
		sleepRandom(500);
		printlnI("Sale TA T" + numTren);
	}
	
	private static void recorrerTramoB(int numTren) {
		printlnI("Entra TB T" + numTren); 
		sleepRandom(500);
		printlnI("Sale TB T" + numTren);
	}
	
	private static void recorrerTramoC(int numTren) {
		printlnI("Entra TC T" + numTren); 
		sleepRandom(500);
		printlnI("Sale TC T" + numTren);
	}

	public static void main(String[] args) {
		sem_A = new SimpleSemaphore(1);
		sem_B = new SimpleSemaphore(1);
		sem_C = new SimpleSemaphore(1);
		
		for (int i = 0; i < NUM_TRENES; i++) {
			createThread("tren", i);
		}
		startThreadsAndWait();
	}
}