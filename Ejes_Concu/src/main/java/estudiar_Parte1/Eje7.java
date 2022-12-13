package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje7 {
	
	static volatile int numero;
	
	public static void persona() {
		//for(int ii=0;ii<4;ii++) {
			enterMutex();
			numero++;
			printlnI("Hola, somos " + numero + " personas");
			exitMutex();
			
			enterMutex();
			if(numero==1) {
				printlnI("Tengo un regalo!");
			}else {
				printlnI("Pruebe otra vez...");
			}
			exitMutex();
			
			printlnI("Que bonito!");
			printlnI("Alucinante!");
			
			enterMutex();
			numero--;
			printlnI("Adios a " + numero + " personas");
			exitMutex();
			
			printlnI("Paseo");
		//}
	}

	public static void main(String[] args) {
		createThreads(5,"persona");
		
		startThreadsAndWait();
	}

}
