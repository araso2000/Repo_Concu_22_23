package estudiar_Parte2.Tema_5_5;

import java.util.concurrent.Exchanger;

public class Eje8 {
	
	static final int VECES = 100;
		
	private Exchanger<Integer> cliente2proxy = new Exchanger<Integer>();
	private Exchanger<Integer> proxy2servidor = new Exchanger<Integer>();
	private Exchanger<Integer> servidor2proxy = new Exchanger<Integer>();
	private Exchanger<Integer> proxy2cliente = new Exchanger<Integer>();
	
	public void cliente() throws InterruptedException {
		for(int ii=0; ii<VECES; ii++) {
			int numero = (int)(Math.random()*50);
			println(" CLIENTE: Numero enviado: " + numero);
			
			cliente2proxy.exchange(numero);
			
			int producto = proxy2cliente.exchange(null);
			println(" CLIENTE: Numero recibido: " + producto + "\n\n");
		}
	}
	
	public void proxy() throws InterruptedException {
		for(int ii=0; ii<VECES; ii++) {
			int numero = cliente2proxy.exchange(null);
			
			println("   PROXY: Numero recibido: " + numero);
			numero++;
			println("   PROXY: Numero enviado: " + numero);
			proxy2servidor.exchange(numero);
			
			int producto = servidor2proxy.exchange(null);
			println("   PROXY: Reenviando al cliente...");
			proxy2cliente.exchange(producto);
		}
	}
	
	public void servidor() throws InterruptedException {
		for(int ii=0; ii<VECES; ii++) {
			int numero = proxy2servidor.exchange(null);
			
			println("SERVIDOR: Numero recibido: " + numero);
			numero++;
			println("SERVIDOR: Numero enviado: " + numero);
			
			servidor2proxy.exchange(numero);
		}
	}
	
	public void println(String texto) {
		System.out.println(texto);
	}
	
	public void exec() {
		new Thread(() -> {
			try {
				cliente();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"cliente").start();
		
		new Thread(() -> {
			try {
				proxy();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"proxy").start();
		
		new Thread(() -> {
			try {
				servidor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"servidor").start();
	}

	public static void main(String[] args) {
		new Eje8().exec();
	}
}