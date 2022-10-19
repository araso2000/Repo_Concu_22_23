package estudiar;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje11 {
	
	static volatile int numero;
	static volatile boolean cliente,proxy,proxy2,servidor;
	
	public static void cliente() {
		numero = (int)(Math.random()*50);
		println("CLIENTE: Numero enviado: " + numero);
		cliente = true;
		
		while(!proxy2);
		println("CLIENTE: Numero recibido: " + numero);
	}
	
	public static void proxy() {
		while(!cliente);
		println("PROXY: Numero recibido: " + numero);
		numero++;
		println("PROXY: Numero enviado: " + numero);
		proxy = true;
		
		while(!servidor);
		println("PROXY: Reenviando al cliente...");
		proxy2 = true;
	}
	
	public static void servidor() {
		while(!proxy);
		println("SERVIDOR: Numero recibido: " + numero);
		numero++;
		println("SERVIDOR: Numero enviado: " + numero);
		servidor = true;
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
