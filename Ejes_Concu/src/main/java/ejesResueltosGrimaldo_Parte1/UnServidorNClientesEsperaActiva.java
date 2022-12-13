package ejesResueltosGrimaldo_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import java.util.Random;

public class UnServidorNClientesEsperaActiva {

	public static final int N = 2; // Numero de clientes
	public static final int M = 3; // Numero de peticiones por cliente
	public static volatile Random random = new Random();

	// Variables de sincronización
	public static volatile boolean datoEnviadoAlServidor = false;
	public static volatile boolean datoEnviadoAlCliente = false;

	// Variables para intercambiar información entre servidor y clientes
	public static volatile int idClientePeticionario = -1;
	public static volatile int idClienteAdmitido = -1;
	public static volatile int dato;

	public static void cliente(int id) {
		for (int i = 0; i < M; i++) {

			// Generamos el aleatorio
			int datoLocal = random.nextInt(); // Genera un dato

			// Solicita que el servidor le permita enviar el dato
			do {
				idClientePeticionario = id; // Solicita el acceso al servidor
			} while (idClienteAdmitido != id); // Comprueba si el servidor se lo ha concedido

			// Una vez que el servidor nos ha concedido acceso, ya podemos enviarle el dato
			dato = datoLocal;
			datoEnviadoAlServidor = true;

			// Recibimos el resultado del servidor
			while (!datoEnviadoAlCliente)
				;
			datoLocal = dato;
			datoEnviadoAlCliente = false;

			// Imprimimos
			printlnI("Cliente " + id + " ha recibido en la iteracion " + i + " el dato: " + datoLocal);
		}
	}

	public static void servidor() {
		for (int i = 0; i < M * N; i++) {

			// Espera a que haya alguna petición
			while (idClientePeticionario == -1)
				;

			// Captura una petición y se lo notifica al cliente adecuado
			idClienteAdmitido = idClientePeticionario;

			// Espera a que el cliente le haya enviado el dato
			while (!datoEnviadoAlServidor)
				;
			datoEnviadoAlServidor = false;
			idClientePeticionario = -1;
			idClienteAdmitido = -1;

			// Transforma el dato recibido y le notifica al cliente que esta listo
			dato++;
			sleepRandom(100);
			datoEnviadoAlCliente = true;

			// Espera a que el cliente lo recoja antes de gestionar la siguiente petición
			while (datoEnviadoAlCliente)
				;
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < N; i++)
			createThread("cliente", i);
		createThread("servidor");
		startThreadsAndWait();
	}
}
