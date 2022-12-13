package ejesResueltosGrimaldo_Parte1;

//Biblioteca SimpleConcurrent
import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class NClientesConMutexConEsperaActiva {

	static volatile boolean pedido;
	static volatile boolean respondido;
	static volatile int peticion;
	static volatile int respuesta;
	static volatile int idClientePeticionario;
	
	// Nos dormimos durante un tiempo aleatorio entre 0 y max milisegundos
	public static void sleepAleatorio(int max) {
		int tiempo = (int) (Math.random()*max);
		sleep(tiempo);
	}
	
	public static void server() {
		while (true) {
			sleepAleatorio(5000);
			if (pedido) {
				respuesta = peticion + 1;
				pedido=false;
				respondido=true;
			}
		}
	}

	public static void client(int idCliente) {
		
		while (true) {
			
			int miRespuesta;
			int miPeticion = (int) (Math.random()*100);
			printlnI("P"+idCliente+"=" + miPeticion);
			sleepAleatorio(5000);
			
			// El servidor es un recurso compartido, por lo tanto metemos su ejecución
			// en una sección crítica y esperamos que nos responda. Es poco elegante
			// usar la espera activa dentro de una sección crítica, pues hay que intentar
			// que éstas no duren mucho
			enterMutex();
				peticion=miPeticion;
				pedido = true;
				while (!respondido); // Esperamos hasta que el servidor responda
				miRespuesta=respuesta;
				respondido=false;	
			exitMutex();
			
			printlnI("R"+idCliente+"=" + miRespuesta);			
			sleepAleatorio(5000); // Nos dormimos un tiempo aleatorio hasta 5 segundos
		}
	}

	public static void main(String[] args) {
		int numeroClientes = 9;
		pedido = false;
		respondido = false;
		for (int i=0; i<numeroClientes; i++) createThread("client", i);
		createThread("server");
		startThreadsAndWait();
	}

}

