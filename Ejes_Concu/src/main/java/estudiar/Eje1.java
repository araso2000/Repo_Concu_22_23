package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje1 {
	
	static volatile boolean continuar;
	static int numero;
	
	public static void productor() {
		numero = (int)(Math.random()*50);
		continuar = true;
	}
	
	public static void consumidor() {
		while(!continuar);
		println("Numero recibido: " + numero);
	}

	public static void main(String[] args) {
		continuar = false;
		createThread("productor");
		createThread("consumidor");
		
		startThreadsAndWait();
	}

}
