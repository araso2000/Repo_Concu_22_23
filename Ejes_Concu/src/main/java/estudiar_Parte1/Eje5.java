package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje5 {
	
	public static void persona() {
		for(int ii=0;ii<4;ii++) {
			enterMutex();
			
			printlnI("Hola!");
			printlnI("Que bonito!");
			printlnI("Alucinante!");
			printlnI("Adios!");
			exitMutex();
			
			printlnI("Paseo");
		}
	}

	public static void main(String[] args) {
		createThreads(3,"persona");
		
		startThreadsAndWait();
	}

}
