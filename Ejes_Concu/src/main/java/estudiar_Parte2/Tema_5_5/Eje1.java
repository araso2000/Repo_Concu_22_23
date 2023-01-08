package estudiar_Parte2.Tema_5_5;

import java.util.concurrent.Semaphore;

public class Eje1 {
	
	private static final int BUFFER_SIZE = 10;
	
	private static int[] datos = new int[BUFFER_SIZE];
	private static int posInser = 0;
	private static int posSacar = 0;
	private static Semaphore nHuecos = new Semaphore(BUFFER_SIZE);
	private static Semaphore nProductos = new Semaphore(0);
	private static Semaphore emPosInser = new Semaphore(1);
	private static Semaphore emPosSacar = new Semaphore(1);
	
	public static void insertarBuffer(int dato) throws InterruptedException {		
		nHuecos.acquire();		
		
		emPosInser.acquire();
		datos[posInser] = dato;
		posInser = (posInser+1) % datos.length;
		emPosInser.release();
		
		nProductos.release();
	}

	public static int sacarBuffer() throws InterruptedException {		
		nProductos.acquire();
		
		emPosSacar.acquire();
		int dato = datos[posSacar];
		posSacar = (posSacar+1) % datos.length;
		emPosSacar.release();
		
		nHuecos.release();	
		
		return dato;
	}

	public static void productor() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			Thread.sleep(500);
			insertarBuffer(i);
		}
	}

	public static void consumidor() throws InterruptedException {
		while (true) {
			int data = sacarBuffer();
			Thread.sleep(500);
			System.out.println(data + " ");
		}
	}
	
	public void exec() {
		new Thread(() -> {
			try {
				productor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				consumidor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		new Eje1().exec();
	}
}
