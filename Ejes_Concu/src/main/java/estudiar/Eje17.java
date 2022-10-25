package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import  es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje17 {
	
	public static final int N_FILOSOFOS = 5; 
	
	private static SimpleSemaphore comedor;
	private static SimpleSemaphore[] tenedores;
	
	public static void filosofo(int numFilosofo) {
		while (true) { 
			printlnI(numFilosofo + ".Pensar"); 
			
			int tIzq = numFilosofo;
			int tDer = (numFilosofo + 1) % N_FILOSOFOS;
			
			comedor.acquire();

			tenedores[tIzq].acquire();
			tenedores[tDer].acquire();
			
			printlnI(numFilosofo + ".COMER"); 
			
			sleepRandom(1000);
			
			tenedores[tIzq].release();
			tenedores[tDer].release();
			
			comedor.release();
			
			printlnI(numFilosofo + ".Terminar");
		}
	}
	
	public static void main(String[] args) {
		comedor = new SimpleSemaphore(N_FILOSOFOS-1);
		
		tenedores = new SimpleSemaphore[N_FILOSOFOS];
		
		for(int ii=0;ii<N_FILOSOFOS;ii++) {
			tenedores[ii] = new SimpleSemaphore(1);
		}
		
		for (int i = 0; i < N_FILOSOFOS; i++) { 
			createThread("filosofo", i);
		}
		startThreadsAndWait(); 
	}
}
