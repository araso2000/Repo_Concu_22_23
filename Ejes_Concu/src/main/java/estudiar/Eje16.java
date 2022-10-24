package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje16 {
	
	private static final int N_FRAGMENTOS = 10; 
	private static final int N_HILOS = 3;
	private static volatile int[] fichero = new int[N_FRAGMENTOS];
	private static volatile int pendiente = 0;
	private static volatile int terminado = 0;
	private static volatile int numBarrera = 0;
	private static SimpleSemaphore sem,porDescargar,aumentar;
	
	
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
			porDescargar.acquire();
			
			if(pendiente == N_FRAGMENTOS) {
				porDescargar.release();
				break;
			}
			
			int descarga = pendiente;
			pendiente++;
			porDescargar.release();
			
			println("Descargando fragmento: " + descarga);
			int descargado = descargaDatos(descarga);
			
			println("Escribiendo fragmento: " + descarga);
			fichero[descarga] = descargado;
			
			aumentar.acquire();
			terminado++;
			aumentar.release();
			
			if(terminado==N_FRAGMENTOS) {
				mostrarFichero();
			}
			
			sincronizar();
		}
	}
	
	public static void sincronizar() {
		aumentar.acquire();
		numBarrera++;
		
		if(numBarrera < N_FRAGMENTOS) {
			
		}
	}

	public static void main(String[] args) {
		sem = new SimpleSemaphore(N_HILOS);
		
		createThreads(N_HILOS, "downloader");
		startThreadsAndWait();
		//mostrarFichero();
	}

}
