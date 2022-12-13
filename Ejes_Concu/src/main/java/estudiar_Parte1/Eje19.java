package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import  es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje19 {
	
	private static final int N_LECTOR = 5;
	private static final int N_ESCRITOR = 3;
	private static SimpleSemaphore escritor,lectores,aumentar,barrera;
	
	private static volatile int numBarreraLector,numBarreraEscritor;

	public static void inicioLectura() { 
		lectores.acquire();
		println("Leyendo...");
		sleepRandom(2000);
		
		aumentar.acquire();
		numBarreraLector++;
	}

	public static void finLectura() { 
		println("Leido");
		if(numBarreraLector < N_LECTOR) {
			aumentar.release();
			barrera.acquire();
			escritor.acquire();
		}else {
			aumentar.release();
			for(int ii=0;ii<N_LECTOR-1;ii++) {
				barrera.release();
			}
			numBarreraLector=0;
		}
		escritor.release();
		lectores.release();
	}

	public static void inicioEscritura() {
		escritor.acquire();
		println("Generando...");
		sleepRandom(2000);
		
		aumentar.acquire();
		numBarreraEscritor++;
	}

	public static void finEscritura() {
		println("Generado");
		if(numBarreraEscritor < N_ESCRITOR) {
			aumentar.release();
			barrera.acquire();
		}else {
			aumentar.release();
			for(int ii=0;ii<N_ESCRITOR-1;ii++) {
				barrera.release();
			}
			numBarreraEscritor=0;
		}
		escritor.release();
	}

	public static void lector() {
		while(true){
			inicioLectura();
			println("Leer datos");
			finLectura();		
			println("Procesar datos");
		}
	}

	public static void escritor() {
		while (true) {
			println("Generar datos");
			inicioEscritura();
			println("Escribir datos");
			finEscritura();			
		}
	}

	public static void main(String[] args) {	
		lectores = new SimpleSemaphore(N_LECTOR);
		escritor = new SimpleSemaphore(1);
		aumentar = new SimpleSemaphore(1);
		barrera = new SimpleSemaphore(0);
		
		numBarreraLector = 0;
		numBarreraEscritor = 0;
		
		createThreads(N_LECTOR, "lector");
		createThreads(N_ESCRITOR, "escritor");
		startThreadsAndWait();
	}
}
