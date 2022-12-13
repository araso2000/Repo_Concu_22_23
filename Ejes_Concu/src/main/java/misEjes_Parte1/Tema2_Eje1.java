package misEjes_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Tema2_Eje1 {
	
	static volatile boolean sincro;
	static int num;
	
	public static void productor() {
		sincro=false;
		num=(int)(Math.random()*50 + 1);
		sincro=true;
	}
	
	public static void consumidor() {
		while(!sincro);
		println("Número: " + num);
	}
	
	public static void main(String[] args) {
		createThread("productor");
		createThread("consumidor");
		
		startThreadsAndWait();
	}

}
