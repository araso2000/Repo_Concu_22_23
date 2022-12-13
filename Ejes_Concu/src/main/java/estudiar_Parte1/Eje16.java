package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Eje16 {
	
	private static final int N_FRAGMENTOS = 10; 
	private static final int N_HILOS = 3;
	
	private static volatile int[] fichero = new int[N_FRAGMENTOS];
	
	private static volatile int pendiente = 0;
	private static volatile int terminado = 0;
	private static volatile int numBarrera = 0;
	
	private static SimpleSemaphore porDescargar,aumentar,barrera,desbloqueo;
	
	
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
		}
	}
	
	public static void sincronizar() {
		while(true) {
			downloader();
			aumentar.acquire();
			numBarrera++;
			
			if(numBarrera<N_HILOS) {
				aumentar.release();
								
				barrera.acquire();
				desbloqueo.release();
				
			}else {
				mostrarFichero();
				
				numBarrera = 0;
				pendiente = 0;
				
				barrera.release(N_HILOS-1);
				desbloqueo.acquire(N_HILOS-1);
				aumentar.release();
			}
		}
	}

	public static void main(String[] args) {
		barrera = new SimpleSemaphore(0);
		desbloqueo = new SimpleSemaphore(0);
		aumentar = new SimpleSemaphore(1);
		porDescargar = new SimpleSemaphore(1);
		
		createThreads(N_HILOS, "sincronizar");
		startThreadsAndWait();
	}
}
