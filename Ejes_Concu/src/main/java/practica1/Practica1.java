package practica1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Practica1 {

	//Constantes
    public static final int N_THREADS = 5;
    public static final int DATA_SAMPLE_SIZE = 16;
    
    //Constante que automaticamente calcula y guarda el numero de niveles de sumas segun el tamaño del array de entrada
    public static final int TOT_NIVELES = (int) (Math.ceil(log(DATA_SAMPLE_SIZE, 2)));
    
    //Arrays globales
    private static int [] data,resultado;
    
    //Semaforos globales
    public static SimpleSemaphore modVars,modBarrera,barrera,desbloqueo;
    
    //Variables que ayudan a la sincronización entre hilos
    public static volatile int posArray,numHilosBarrera,numSumasPorNivel,nivelActual;

    //Función que calcula el logaritmo de un numero en una base concreta. Usado para calcular el numero de niveles
    private static Double log(int num, int base) {
        return (Math.log10(num) / Math.log10(base));
     }
    
    //Funcion que simula un sumador
    private static int add(int a, int b) {
        sleepRandom(1000);
        return a + b;
    }
    
    //Funcion principal a ejecutar por cada hilo
    public static void proceso() {
    	//Se itera tantas veces como niveles haya
		for(int ii=0;ii<TOT_NIVELES;ii++) {
			//Bucle infinito de suma dentro del mismo nivel
			while(true) {
				//Cogemos 1 permiso del semaforo encargado de sincronizar la lectura/escritura de las variables compartidas
				modVars.acquire();
				
				//Guardamos la posicion que nos toca leer del array para sumar los datos y la aumentamos en 2
				int posArrayHilo = posArray;
				posArray = posArray + 2;
				
				//Si la posicion que nos toca sumar es menor o igual que el numero total de sumas que hay en un nivel
				if((posArrayHilo + 1) <= numSumasPorNivel) {
					modVars.release();
					
					//Extraemos los datos a sumar del array data y calculamos la posicion de guardado del array resultado
					int datoA = data[posArrayHilo];
					int datoB = data[posArrayHilo + 1];
					int guardar = posArrayHilo / 2;
					
					println(getThreadName() + ": Se inicia la suma de data[" + posArrayHilo + "]=" + datoA + " y " + "data[" + (posArrayHilo+1) + "]=" + datoB);
					
					resultado[guardar] = add(datoA,datoB);
					
					println(getThreadName() + ": Se guarda la suma en results[" + guardar + "]=" + resultado[guardar]);
					
				}else {
					//Si ya se han realizado todas las sumas del nivel
					modVars.release();
					break;
				}
			}
			
			//Cogemos permiso para modificar las variables de la barrera
			modBarrera.acquire();
			//Proceso nuevo que llega a la barrera
			numHilosBarrera++;
			
			//Si aun quedan hilos por llegar a la barrera
			if(numHilosBarrera < N_THREADS) {
				println(getThreadName() + ": Esperando a los demas procesos. Han terminado " + numHilosBarrera + " procesos");
				
				modBarrera.release();
				
				//Nos bloqueamos en la barrera
				barrera.acquire();
				//Una vez desbloqueados de la barrera, desbloqueará cada hilo 1 vez al ultimo proceso
				desbloqueo.release();
			}else {
				//Si soy el ultimo proceso
				
				//Divido entre dos el numero de sumas por nivel actual
				numSumasPorNivel = numSumasPorNivel / 2;
				
				//Redimensiono el array de datos
				data = new int[numSumasPorNivel];
				
				//Copio el contenido del array resultado al array de datos en preparacion para las sumas del siguiente nivel
				System.arraycopy(resultado, 0, data, 0, numSumasPorNivel);
				
				println(getThreadName() + ": Actualiza el array de datos a " + Arrays.toString(data));
				
				//Redimensiono el array de resultados a la mitad del tamaño del numero de sumas del nivel siguiente
				resultado = new int[numSumasPorNivel/2];
				
				//Si aun quedan niveles por sumar se imprime un mensaje de finalizacion del nivel
				if(numSumasPorNivel != 1) {
					println(getThreadName() + ": Finalizado el nivel " + nivelActual);
					println("--------------------------------------------------");
				}else {
					//Si ya no quedan mas niveles por sumar, se imprime el resultado de la suma concurrente
					println(getThreadName() + ": Finalizado el nivel " + nivelActual);
					println("--------------------------------------------------");
					println("Resultado de la suma concurrente: " + data[0]);
				}
				
				//Se actualizan las variables en preparacion al siguiente nivel
				nivelActual++;
				posArray = 0;
				numHilosBarrera = 0;
				
				//Se liberan los procesos de la barrera
				barrera.release(N_THREADS-1);
				//Se bloquea el ultimo proceso, que será desbloqueado por los procesos que se desbloquean de la barrera
				desbloqueo.acquire(N_THREADS-1);
				modBarrera.release();
			}
		}
	}

    //Funcion dada en el codigo original que inicializa el array de datos con valores random
    private static void initDataset() {
        data = ThreadLocalRandom.current()
                .ints(DATA_SAMPLE_SIZE, 1, 10)
                .toArray();
        println("Los datos a sumar son: " + Arrays.toString(Practica1.data));
    }

    public static void main(String[] args) {
    	initDataset();
    	
    	//Inicialización de todas las variables y semáforos usados en el código
    	resultado = new int[DATA_SAMPLE_SIZE / 2];
    	
    	modVars = new SimpleSemaphore(1);
    	modBarrera = new SimpleSemaphore(1);
    	barrera = new SimpleSemaphore(0);
    	desbloqueo = new SimpleSemaphore(0);
    	
    	posArray = 0;
    	numHilosBarrera = 0;
    	numSumasPorNivel = DATA_SAMPLE_SIZE;
    	nivelActual = 1;
    	
    	//Codigo que ejecuta la suma secuencial
    	int sumaSecuencial = 0;
    	LocalDateTime startSecuencial = LocalDateTime.now();
        /*for (int i = 0; i < DATA_SAMPLE_SIZE; i++) {
        	sumaSecuencial = add(sumaSecuencial, data[i]);
        }*/
        Duration time = Duration.between(startSecuencial, LocalDateTime.now());
        
        //Creamos los hilos y ejecutamos el codigo concurrente
    	createThreads(N_THREADS,"proceso");
        LocalDateTime startConcurrente = LocalDateTime.now();
        startThreadsAndWait();
        println("Tiempo de ejecución algoritmo concurrente: " + Duration.between(startConcurrente, LocalDateTime.now()));
        
        println("Suma secuencial: " + sumaSecuencial);
        println("Tiempo de ejecución algoritmo secuencial: " + time);
    }
}