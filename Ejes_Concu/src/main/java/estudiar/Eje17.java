package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import  es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje17 {
	
	public static final int N_FILOSOFOS = 5; 
	
	//El planteamiento está bien pero te falta poder coger solo los tenedores de cada lado. Para ello accedes al comedor y más tarde coges derecha o izquierda y otra vez.
	//De esa manera no siempre comen tan rapido si no que tienen que esperar mas a comer ya que un filosofo debe esperar a cada uno de sus laterales para poder comer
	
	private static SimpleSemaphore sem;
	
	public static void filosofo(int numFilosofo) {
		while (true) { 
			printlnI(numFilosofo + ".Pensar"); 

			sem.acquire();
			printlnI(numFilosofo + ".Me falta 1 tenedor");
			sem.acquire();
			printlnI(numFilosofo + ".COMER"); 
			sleepRandom(1000);
			sem.release();
			sem.release();
			
			printlnI(numFilosofo + ".Terminar");
		}
	}
	
	public static void main(String[] args) {
		sem = new SimpleSemaphore(N_FILOSOFOS);
		
		for (int i = 0; i < N_FILOSOFOS; i++) { 
			createThread("filosofo", i);
		}
		startThreadsAndWait(); 
	}
}
