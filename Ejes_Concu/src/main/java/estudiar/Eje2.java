package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje2 {
	
	static volatile boolean continuar;
	static int numero;
	
	public static void productor() {
		while(true) {
			numero = (int)(Math.random()*50);
			continuar = true;
		}
		
	}
	
	public static void consumidor() {
		while(true) {
			while(!continuar);
			println("Numero recibido: " + numero);
			continuar = false;
		}
	}

	public static void main(String[] args) {
		continuar = false;
		createThread("productor");
		createThread("consumidor");
		
		startThreadsAndWait();
	}

}
