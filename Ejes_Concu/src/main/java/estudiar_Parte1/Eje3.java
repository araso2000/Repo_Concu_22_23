package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Eje3 {
	
	static int numero;
	static volatile boolean consumido;
	static volatile boolean producido;
	
	public static void cliente() {
		numero = (int)(Math.random()*50);
		printlnI("Numero enviado: " + numero);
		producido = true;
		while(!consumido);
		printlnI("Numero recibido: " + numero);
	}
	
	public static void servidor() {
		while(!producido);
		numero++;
		consumido =  true;
	}

	public static void main(String[] args) {
		consumido = false;
		producido = false;
		
		createThread("cliente");
		createThread("servidor");
		
		startThreadsAndWait();
	}

}
