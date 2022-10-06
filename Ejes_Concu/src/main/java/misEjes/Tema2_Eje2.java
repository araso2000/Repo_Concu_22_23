package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Tema2_Eje2 {
	
	static volatile boolean producido;
	static volatile boolean consumido;
	static volatile double num;
	
	public static void productor() {
		double temp=0;
		for(int ii=0;ii<5;ii++) {
			num = temp++;
			consumido=false;
			sleep(50);
			producido = true;
			while(!consumido);
		}
	}
	
	public static void consumidor() {
		for(int ii=0;ii<5;ii++) {
			while(!producido);
			println("Número: " + num);
			producido=false;
			consumido=true;
		}
	}
	
	public static void main(String[] args) {
		
		producido = false;
		consumido = false;
		
		createThread("productor");
		createThread("consumidor");
		
		startThreadsAndWait();
	}

}
