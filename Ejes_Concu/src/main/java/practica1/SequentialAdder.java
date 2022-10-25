package practica1;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class SequentialAdder {

    public static final int N_THREADS = 5;
    public static final int DATA_SAMPLE_SIZE = 16;

    private static int [] data,result;
    
    private static volatile int pendiente,nivelesRestantes,posResultado,proxSuma;
    
    private static SimpleSemaphore aumentar,barrera;
    
    public static void proceso() {
    	
    	int posicionSuma = 0;
    	int posicionArray = 0;
    	
    	while(true) {
    		
    		println(getThreadName() + "-Entrando");
    		
    		aumentar.acquire();
    		
        	if((pendiente+1) < (nivelesRestantes) && pendiente < (N_THREADS-1)) {
        		posicionSuma = proxSuma;
        		posicionArray = posResultado;
        		
        		posResultado++;
        	    pendiente++;
        	    proxSuma = proxSuma + 2;
        	    
        	    aumentar.release();
        	    
        	    result[posicionArray] = add(data[posicionSuma],data[posicionSuma++]);
        	    println(getThreadName() + "-Me bloqueo");
        	    barrera.acquire();
        	}else {
        		for(int ii=0;ii<nivelesRestantes;ii++) {
    	    		data[ii] = result[ii];
    	    	}
        		
        		pendiente = 0;
        		nivelesRestantes = nivelesRestantes / 2;
        		
        		println(getThreadName() + "-Termino yo");
        		
        		if(nivelesRestantes == 1) {
        			println("Resultado de la suma concurrente: " + data[0]);
        			aumentar.release();
        			break;
        		}
        		aumentar.release();
        		
        		for(int ii=0;ii<N_THREADS-1;ii++) {
        			barrera.release();
        		}
        	} 
    	}
    }

    private static int add(int a, int b) {
        sleepRandom(1000);
        return a + b;
    }

    private static void initDataset() {
        data = ThreadLocalRandom.current()
                .ints(DATA_SAMPLE_SIZE, 1, 10)
                .toArray();
        println("Sample dataset: " + Arrays.toString(SequentialAdder.data));
    }

    public static void main(String[] args) {
    	pendiente = 0;
    	nivelesRestantes = DATA_SAMPLE_SIZE/2;
    	
    	aumentar = new SimpleSemaphore(1);
    	barrera = new SimpleSemaphore(0);
    	
    	result = new int[DATA_SAMPLE_SIZE/2];
    	
        initDataset();

        /*LocalDateTime start = LocalDateTime.now();
        int sum = 0;
        for (int i = 0; i < DATA_SAMPLE_SIZE; i++) {
            sum = add(sum, data[i]);
        }
        Duration time = Duration.between(start, LocalDateTime.now());
        println("Total sum : " + sum);

        println("SequentialAdder computed sum in " + Duration.between(start, LocalDateTime.now()));
        */
        
        createThreads(N_THREADS,"proceso");
        LocalDateTime start = LocalDateTime.now();
        startThreadsAndWait();
        Duration time = Duration.between(start, LocalDateTime.now());
        println("ConcurrentAdder computed sum in " + Duration.between(start, LocalDateTime.now()));  
    }
}
