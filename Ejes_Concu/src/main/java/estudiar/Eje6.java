package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje6 {
	
	static volatile int numero;
	
	public static void persona() {
		for(int ii=0;ii<4;ii++) {
			enterMutex();
			numero++;
			printlnI("Hola, somos " + numero + " personas");
			exitMutex();
			
			printlnI("Que bonito!");
			printlnI("Alucinante!");
			
			enterMutex();
			numero--;
			printlnI("Adios a " + numero + " personas");
			exitMutex();
			
			printlnI("Paseo");
		}
	}

	public static void main(String[] args) {
		createThreads(5,"persona");
		
		startThreadsAndWait();
	}

}
