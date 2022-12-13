package misEjes_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Tema2_Eje4A {
	
	static volatile int numero;
	static volatile boolean enviado;
	static volatile boolean procesado;
	
	public static void cliente() {
		while(true) {
			numero=(int)(Math.random()*50);
			
			println("CLIENTE: Numero a enviar: " + numero);
			procesado=false;
			sleep(50);
			enviado=true;
			
			while(!procesado);
			
			println("CLIENTE: Numero recibido: " + numero + "\n\n");
		}
	}
	
	public static void servidor() {
		while(true) {
			while(!enviado);
			numero++;
			println("SERVIDOR: Procesando...");
			enviado = false;
			sleep(50);
			procesado=true;
		}
	}

	public static void main(String[] args) {
		procesado = false;
		enviado = false;
		
		createThread("servidor");
		createThread("cliente");
		
		startThreadsAndWait();
	}

}
