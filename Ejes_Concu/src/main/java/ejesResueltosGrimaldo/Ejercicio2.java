package ejesResueltosGrimaldo;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Ejercicio2 {

	// Variables de sincronización
	static volatile boolean producido;
	static volatile boolean consumido;

	// Variable para intercambiar información
	static volatile double producto;

	public static void productor() {

		for (int i = 0; i < 5; i++) {
			while (!consumido);
			producto = Math.random();			
			consumido=false;				
			producido = true;			
		}
	}

	public static void consumidor() {

		for (int i = 0; i < 5; i++) {
			while (!producido);
			println("Producto: " + producto);
			producido=false;
			consumido = true;
		}
	}

	public static void main(String[] args) {

		producido = false;
		consumido = true;

		createThread("productor");
		createThread("consumidor");

		startThreadsAndWait();
	}
}
