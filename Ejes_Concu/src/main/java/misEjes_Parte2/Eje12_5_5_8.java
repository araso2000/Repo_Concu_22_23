package misEjes_Parte2;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.println;

import java.util.concurrent.Exchanger;

public class Eje12_5_5_8 {
	
	static Exchanger<Integer> exchanger1 = new Exchanger<Integer>();
	static Exchanger<Integer> exchanger2 = new Exchanger<Integer>();
	static Exchanger<Integer> exchanger3 = new Exchanger<Integer>();
	static Exchanger<Integer> exchanger4 = new Exchanger<Integer>();
	
	static int PETICIONES = 10;
	
	public static void cliente() throws InterruptedException{
		for(int ii=0;ii<PETICIONES;ii++) {
			int numero = (int)(Math.random()*50);
			println("CLIENTE: Numero enviado: " + numero);
			exchanger1.exchange(numero);

			int numero2 = exchanger4.exchange(null);
			println("CLIENTE: Numero recibido: " + numero2 + "\n\n");
		}
	}
	
	public static void proxy() throws InterruptedException {
		for(int ii=0;ii<PETICIONES;ii++) {
			int numero = exchanger1.exchange(null);
			println("PROXY: Numero recibido: " + numero);
			
			numero++;
			
			println("PROXY: Numero enviado: " + numero);
			exchanger2.exchange(numero);
			
			int numero2 = exchanger3.exchange(null);
			println("PROXY: Reenviando al cliente...");
			exchanger4.exchange(numero2);
		}
	}
	
	public static void servidor() throws InterruptedException {
		for(int ii=0;ii<PETICIONES;ii++) {
			int numero = exchanger2.exchange(null);
			println("SERVIDOR: Numero recibido: " + numero);
			
			numero++;
			
			println("SERVIDOR: Numero enviado: " + numero);
			exchanger3.exchange(numero);
		}
	}
	
	public void exec() throws InterruptedException{
		new Thread(() -> {
			try {
				proxy();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}, "proxy").start();
		new Thread(() -> {
			try {
				cliente();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "proxy").start();
		new Thread(() -> {
			try {
				servidor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "proxy").start();
	}

	public static void main(String[] args) throws InterruptedException {
		new Eje12_5_5_8().exec();
	}
}
