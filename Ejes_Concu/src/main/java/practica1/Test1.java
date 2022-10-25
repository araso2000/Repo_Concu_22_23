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

    private static int [] data,result;
    
    private static SimpleSemaphore aumentar,barrera;
    
    private static volatile int posicion,array,nivel,numBarrera;

    private static int add(int a, int b) {
        sleepRandom(1000);
        return a + b;
    }
    
    public static void proceso() {
		while(true) {
			aumentar.acquire();
			int posicionDatos = posicion;
			posicion = posicion + 2;
			int posicionGuardar = array;
			array++;
			numBarrera++;
			aumentar.release();
			
			result[posicionGuardar] = add(data[posicionDatos],data[posicionDatos++]);
			
			println("Hola soy " + getThreadName());
			
			if(numBarrera == (nivel)) {
				print("Array: [");
				for(int ii=0;ii<8;ii++) {
					print(result[ii] + ", ");
				}
				print("]");
				break;
			}
		}
		
		
		
		/*if((array < nivel) && (numBarrera < N_THREADS)) {
			
		}
		
		if(numBarrera < nivel-1) {
			aumentar.release();
			println(getThreadName() + "Me bloqueo");
			//barrera.acquire();
		}else {
			aumentar.release();
			for(int ii=0;ii<N_THREADS-1;ii++) {
				barrera.release();
			}
			print("Array nivel-8: [");
			for(int ii=0;ii<nivel;ii++) {
				print(result[ii] + ", ");
			}
			print("]");
		}		*/
		
		
	}

    private static void initDataset() {
        data = ThreadLocalRandom.current()
                .ints(DATA_SAMPLE_SIZE, 1, 10)
                .toArray();
        println("Sample dataset: " + Arrays.toString(Test1.data));
    }

    public static void main(String[] args) {
    	aumentar = new SimpleSemaphore(1);
    	barrera = new SimpleSemaphore(0);
    	
    	posicion = 0;
    	array = 0;
    	nivel = 8;
    	
    	result = new int[DATA_SAMPLE_SIZE/2];
    	
        initDataset();

        createThreads(N_THREADS,"proceso");
        LocalDateTime start = LocalDateTime.now();
        startThreadsAndWait();
        Duration time = Duration.between(start, LocalDateTime.now());
        println("ConcurrentAdder computed sum in " + Duration.between(start, LocalDateTime.now()));  
    
        int sum = 0;
        for (int i = 0; i < DATA_SAMPLE_SIZE; i++) {
            sum = add(sum, data[i]);
        }
        println("Suma secuencial : " + sum);
    }
}
