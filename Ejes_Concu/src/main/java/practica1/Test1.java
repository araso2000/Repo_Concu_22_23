package practica1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Test1 {

    public static final int N_THREADS = 5;
    public static final int DATA_SAMPLE_SIZE = 16;
    public static final int TOT_NIVELES = (int) (Math.ceil(log(DATA_SAMPLE_SIZE, 2)));

    private static int [] data,resultado;
    
    public static SimpleSemaphore modVars,modBarrera,barrera,desbloqueo;
    
    public static volatile int posArray,numHilosBarrera,numNivel,nivel;

    private static Double log(int num, int base) {
        return (Math.log10(num) / Math.log10(base));
     }
    
    private static int add(int a, int b) {
        sleepRandom(1000);
        return a + b;
    }
    
    public static void proceso() {
		for(int ii=0;ii<TOT_NIVELES;ii++) {
			while(true) {
				modVars.acquire();
				int posArrayHilo = posArray;
				posArray = posArray + 2;
				
				if((posArrayHilo + 1) <= numNivel) {
					modVars.release();
					
					println(getThreadName() + "-Me toca seguir sumando");
					
					int datoA = data[posArrayHilo];
					int datoB = data[posArrayHilo + 1];
					int guardar = posArrayHilo / 2;
					resultado[guardar] = add(datoA,datoB);
				
				}else {
					println(getThreadName() + "-Ya no hay más que sumar en este nivel");
					modVars.release();
					break;
				}
			}
			
			modBarrera.acquire();
			numHilosBarrera++;
			
			if(numHilosBarrera < N_THREADS) {
				modBarrera.release();
				println(getThreadName() + "-Me bloqueo");
				barrera.acquire();
				desbloqueo.release();
			}else {
				println(getThreadName() + "-Soy el ultimo del nivel");
				
				data = new int[numNivel];
				System.arraycopy(resultado, 0, data, 0, (numNivel/2));
			
				numNivel = numNivel / 2;
				
				resultado = new int[numNivel];
				
				if(numNivel != 1) {
					println("\nFin nivel " + nivel + " que era de " + (numNivel*2) + "\n");
				}else {
					println("\nFin nivel " + nivel + " que era de " + (numNivel*2) + "\n");
					println("\nFin de la suma: " + data[0]);
				}
				
				nivel++;
				posArray = 0;
				numHilosBarrera = 0;
				
				barrera.release(N_THREADS-1);
				desbloqueo.acquire(N_THREADS-1);
				modBarrera.release();
			}
			
		}
	}

    private static void initDataset() {
        data = ThreadLocalRandom.current()
                .ints(DATA_SAMPLE_SIZE, 1, 10)
                .toArray();
        println("Sample dataset: " + Arrays.toString(Test1.data));
    }

    public static void main(String[] args) {
    	initDataset();
    	
    	int sum = 0;
        for (int i = 0; i < DATA_SAMPLE_SIZE; i++) {
            sum = add(sum, data[i]);
        }
        
    	
    	resultado = new int[DATA_SAMPLE_SIZE / 2];
    	println("tam array resultado " + resultado.length);
    	println("Niveles " + TOT_NIVELES);
    	
    	modVars = new SimpleSemaphore(1);
    	modBarrera = new SimpleSemaphore(1);
    	barrera = new SimpleSemaphore(0);
    	desbloqueo = new SimpleSemaphore(0);
    	
    	posArray = 0;
    	numHilosBarrera = 0;
    	numNivel = DATA_SAMPLE_SIZE;
    	nivel = 1;

        createThreads(N_THREADS,"proceso");
        LocalDateTime start = LocalDateTime.now();
        startThreadsAndWait();
        Duration time = Duration.between(start, LocalDateTime.now());
        println("Suma secuencial : " + sum);
        println("ConcurrentAdder computed sum in " + Duration.between(start, LocalDateTime.now()));  
    }
}
