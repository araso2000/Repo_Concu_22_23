package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Tema2_Eje5_Museo {
	
	public static void persona() {
		for(int ii=0;ii<4;ii++) {
			enterMutex();
			
			printlnI("hola");
			printlnI("que bonito");
			printlnI("alucinante");
			
			printlnI("adios");
			
			exitMutex();
			
			printlnI("paseo");
		}
	}

	public static void main(String[] args) {
		createThreads(3,"persona");
		startThreadsAndWait();
	}
}