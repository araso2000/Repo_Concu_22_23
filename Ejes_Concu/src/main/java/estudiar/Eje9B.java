package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje9B {
	
	private static final int N_FRAGMENTOS = 10; 
	private static final int N_HILOS = 3;
	private static volatile int[] fichero = new int[N_FRAGMENTOS];
	public static volatile int pendiente = 0;
	public static volatile int terminado = 0;
	
	private static int descargaDatos(int numFragmento) { 
		sleepRandom(1000);
		return numFragmento * 2;
	}
	
	private static void mostrarFichero() { 
		println("--------------------------------------------------"); 
		print("File = [");
		for (int i = 0; i < N_FRAGMENTOS; i++) {
			print(fichero[i] + ","); 
		}
		println("]"); 
	}
	
	public static void downloader() {
		while(true) {
			enterMutex();
			if(pendiente == N_FRAGMENTOS) {
				exitMutex();
				break;
			}
			
			int descarga = pendiente;
			pendiente++;
			exitMutex();
			
			println("Descargando fragmento: " + descarga);
			int descargado = descargaDatos(descarga);
			
			println("Escribiendo fragmento: " + descarga);
			fichero[descarga] = descargado;
			
			enterMutex();
			terminado++;
			exitMutex();
			
			if(terminado==N_FRAGMENTOS) {
				mostrarFichero();
			}
		}
	}

	public static void main(String[] args) {
		createThreads(N_HILOS, "downloader");
		startThreadsAndWait();
		//mostrarFichero();
	}

}
