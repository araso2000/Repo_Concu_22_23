package estudiar_Parte2.Tema_5_5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje4 {
	
private static final int BUFFER_SIZE = 10;
	
	private static int[] datos = new int[BUFFER_SIZE];
	private static int posInser = 0;
	private static int posSacar = 0;
	private static int numProductos = 0;
	
	static Lock cerrojoGeneral = new ReentrantLock(); 
	static Condition condLleno = cerrojoGeneral.newCondition();
	static Condition condVacio = cerrojoGeneral.newCondition();
	
	public static void insertarBuffer(int dato) throws InterruptedException {		
		cerrojoGeneral.lock();		
		
		if(numProductos == BUFFER_SIZE) {
			condLleno.await();
		}
		
		datos[posInser] = dato;
		posInser = (posInser+1) % datos.length;
		numProductos++;
		
		condVacio.signal();
		
		cerrojoGeneral.unlock();
	}

	public static int sacarBuffer() throws InterruptedException {		
		cerrojoGeneral.lock();	
		
		if(numProductos == 0) {
			condVacio.await();
		}

		int dato = datos[posSacar];
		posSacar = (posSacar+1) % datos.length;
		numProductos--;
		
		condLleno.signal();
		
		cerrojoGeneral.unlock();		
		
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
