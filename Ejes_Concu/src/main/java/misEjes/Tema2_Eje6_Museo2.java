package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Tema2_Eje6_Museo2 {
	
	static volatile int personas;
	
	public static void persona(int hilo) {
		for(int ii=0;ii<4;ii++) {
			enterMutex();
			personas++;
			printlnI(hilo + ".-hola, somos " + personas + " persona/s");
			exitMutex();
			
			printlnI(hilo + ".-que bonito");
			printlnI(hilo + ".-alucinante");
			
			enterMutex();
			personas--;
			printlnI(hilo + ".-adios a los " + personas);
			exitMutex();
			
			printlnI(hilo + ".-paseo");
		}
	}

	public static void main(String[] args) {
		personas=0;
		for(int ii=0;ii<4;ii++) {
			createThread("persona",ii);
		}
		startThreadsAndWait();
	}
}