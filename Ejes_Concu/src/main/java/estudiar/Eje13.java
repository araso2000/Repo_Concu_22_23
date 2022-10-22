package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje13 {
	
	static SimpleSemaphore semA,semB,semC;
	
	public static void tren(int numTren) {
		sleepRandom(500); 
		semA.acquire();
		recorrerTramoA(numTren);

		sleepRandom(500);
		semB.acquire();
		semA.release();
		recorrerTramoB(numTren);

		sleepRandom(500); 
		semC.acquire();
		semB.release();
		recorrerTramoC(numTren);
		semC.release();
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
		semA = new SimpleSemaphore(1);
		semB = new SimpleSemaphore(1);
		semC = new SimpleSemaphore(1);
		
		for(int ii=0;ii<3;ii++) {
			createThread("tren",ii);
		}
		
		startThreadsAndWait();
	}

}
