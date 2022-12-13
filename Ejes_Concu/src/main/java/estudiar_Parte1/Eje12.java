package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje12 {
	
	static volatile int numero;
	static volatile boolean cliente,proxy,proxy2,servidor;
	
	static int PETICIONES = 10;
	
	public static void cliente() {
		for(int ii=0;ii<PETICIONES;ii++) {
			numero = (int)(Math.random()*50);
			println("CLIENTE: Numero enviado: " + numero);
			cliente = true;
			
			while(!proxy2);
			proxy2 = false;
			println("CLIENTE: Numero recibido: " + numero + "\n\n");
		}
	}
	
	public static void proxy() {
		for(int ii=0;ii<PETICIONES;ii++) {
			while(!cliente);
			cliente = false;
			println("PROXY: Numero recibido: " + numero);
			numero++;
			println("PROXY: Numero enviado: " + numero);
			proxy = true;
			
			while(!servidor);
			servidor = false;
			println("PROXY: Reenviando al cliente...");
			proxy2 = true;
		}
	}
	
	public static void servidor() {
		for(int ii=0;ii<PETICIONES;ii++) {
			while(!proxy);
			proxy = false;
			println("SERVIDOR: Numero recibido: " + numero);
			numero++;
			println("SERVIDOR: Numero enviado: " + numero);
			servidor = true;
		}
	}

	public static void main(String[] args) {
		cliente = false;
		proxy = false;
		servidor = false;
		proxy2 = false;
		
		createThread("cliente");
		createThread("proxy");
		createThread("servidor");
		
		startThreadsAndWait();
	}

}
