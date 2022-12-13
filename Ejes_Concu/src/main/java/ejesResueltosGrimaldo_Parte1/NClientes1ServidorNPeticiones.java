package ejesResueltosGrimaldo_Parte1;

// Biblioteca SimpleConcurrent
import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

// Unica clase con todo static, para no liarnos con la programacion OO
public class NClientes1ServidorNPeticiones {

	// Arrays que indican el estado, peticiones y respuestas de cada cliente. Cada array tiene tantos 
	// elementos como clientes. Por tanto, el cliente "i" solo usa la posición "i" de cada array. 
	// Dicha posicion se comparte con el servidor, pero no con el resto de clientes
	static volatile boolean pedido[]; // Indica si cada cliente ha hecho una petición
	static volatile boolean respondido[]; // Indica si cada cliente ya tiene respuesta a su peticion
	static volatile boolean terminado[]; // Indica si cada cliente ha terminado
	static volatile int peticion[]; // El valor de la petición de cada cliente
	static volatile int respuesta[]; // El valor de la respuesta de cada cliente
	
	// Numero total de clientes que vamos a tener. Lo comparten el main y el servidor, pero no los clientes
	static volatile int numeroClientes;

	// Función, usada unicamente por el servidor, que comprueba si todos los clientes ya han terminado
	// Devuelve: true si todos han terminado, false si aun queda alguno vivo
	public static boolean estanTodosClientesTerminados() {
		boolean resultado=true;
		for (int idCliente=0; idCliente<numeroClientes; idCliente++)
			if (!terminado[idCliente]) resultado=false;
		return(resultado);
	}
	
	// Servidor. Habra un unico servidor. Va comprobando, hasta que todos los clientes han terminado,
	// si existe alguna peticion de algun cliente. Para ello, va recorriendo de forma circular el 
	// array "pedido". Si un cliente tiene una peticion, la procesa y pasa a comprobar el siguiente 
	// elemento del array "pedido". Si un cliente no tiene petición, directamente pasa al siguiente sin hacer
	// nada.
	public static void server() {

		int idClienteProcesandose=0;
		while(!estanTodosClientesTerminados()) { // Itera hasta que todos los clientes han finalizado
			
			// Si un cliente ha hecho un pedido, lo procesa
			if(pedido[idClienteProcesandose]) {
				respuesta[idClienteProcesandose] = peticion[idClienteProcesandose] + 1;
				pedido[idClienteProcesandose] = false;
				respondido[idClienteProcesandose] = true;
			}
			
			// Pasamos al siguiente cliente. Como el array es circular, usamos el módulo
			idClienteProcesandose = (idClienteProcesandose+1)%numeroClientes;
		}
	}

	// Método para el cliente. Habrá muchos clientes fabricados con este método. Realiza varias
	// peticiones al servidor. Por cada petición: la imprime, espera la respuesta del servidor, e imprime
	// la respuesta. Entonces volvemos a empezar generando otra petición
	// Parámetro idCliente: identificador del cliente. Cada proceso cliente tendrá un identificador único. Dicho
	//                      identificador le sirve al cliente para saber cuál es su propia posición en los arrays
	// Parámetro numeroPeticiones: numero total de peticiones que va a hacer el cliente. -1 para infinitas
	public static void client(int idCliente, int numeroPeticiones) {

		for (int i=0; i<numeroPeticiones || numeroPeticiones==-1 ; i++) { 
			
			peticion[idCliente] = (int) (Math.random()*100); // Genera la petición
			// sleep(1000); // Simula un tiempo de producción
			printlnI("P"+idCliente+"="+peticion[idCliente]);
			pedido[idCliente] = true;
			while (!respondido[idCliente]); // Espera a que el servidor gestione la petición
			respondido[idCliente] = false;
			printlnI("R"+idCliente+"="+respuesta[idCliente]);
		}
		
		terminado[idCliente]=true; // Marcamos al cliente como terminado, para que se entere el servidor
	}

	public static void main(String[] args) {

		// Número de clientes que vamos a tener
		numeroClientes=5;
		
		// Número de peticiones que va a hacer cada cliente. -1 para infinitas
		int numeroPeticionesPorCliente = -1;
		
		// Reservamos espacio para los arrays de estado
		pedido = new boolean[numeroClientes];
		respondido = new boolean [numeroClientes];
		terminado = new boolean [numeroClientes];
		
		// Inicializamos los arrays de estado a falso
		for (int i=0; i<numeroClientes; i++) {
			pedido[i]=false;
			respondido[i]=false;
			terminado[i]=false;
		}
		
		// Creamos los arrays para contener las peticiones y las respuestas
		peticion = new int[numeroClientes];
		respuesta = new int[numeroClientes];
		
		// Creamos los procesos clientes y el proceso servidor
		for (int idCliente=0; idCliente<numeroClientes; idCliente++) 
			createThread("client", idCliente, numeroPeticionesPorCliente);
		createThread("server");

		// Esperamos a que terminen todos. El servidor sólo acabará cuando todos los clientes
		// hayan acabado.
		startThreadsAndWait();
		println("Fin del programa");
	}
}
