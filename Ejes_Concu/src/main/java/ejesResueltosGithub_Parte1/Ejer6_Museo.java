package ejesResueltosGithub_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Ejer6_Museo {

	static volatile int personas;
	
	public static void persona() {

		while (true) {
			
			enterMutex();
			personas++;
			printlnI("hola, somos "+personas);
			exitMutex();						
			
			printlnI("qué bonito!");
			printlnI("alucinante!");
			
			enterMutex();
			personas--;
			printlnI("adiós a los "+personas);
			exitMutex();					
			
			printlnI("paseo");
		}
	}

	public static void main(String[] args) {
		personas = 0;
		createThreads(3, "persona");
		startThreadsAndWait();
	}
}
