package misEjes_Parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje8_5_5_4 {
	
	private int[] array = new int[10];
	private int nextINPos = 0;
	private int nextOUTPos = 0;
	private int posDisponibles = 10;
	
	static Lock cerrojo = new ReentrantLock();
	static Condition cd1 = cerrojo.newCondition();
	static Condition cd2 = cerrojo.newCondition();
	
	public void insertar(int dato) throws InterruptedException {
		cerrojo.lock();
		
		while(posDisponibles == 0) {
			cd1.await();
		}
			
		array[nextINPos] = dato;
		nextINPos = (nextINPos+1) % array.length;
		posDisponibles--;
		
		cd2.signal();
		
		cerrojo.lock();
	}
	
	public int sacar() throws InterruptedException{
		cerrojo.lock();
		
		while(posDisponibles == 10) {
			cd2.await();
		}
		
		int returnear = array[nextOUTPos];
		nextOUTPos = (nextOUTPos+1) % array.length;
		posDisponibles++;
		
		cd1.signal();
		
		cerrojo.unlock();
		
		return returnear;
	}
	
	
	public void productor() {
		try {
			while(true) {
				int numero = (int)(Math.random()*50);
				insertar(numero);
				System.out.println(Thread.currentThread().getName() + ": Producido: " + numero);
				Thread.sleep(1000);
			}
			
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": No se ha podido producir");
		}
	}
	
	public void consumidor() {
		try {
			while(true) {
				int numero = sacar();
				Thread.sleep(1000);
				System.out.println(Thread.currentThread().getName() + ": Consumido: " + numero);
			}
			
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": No se ha podido consumir");
		}
	}
	
	public void exec() {
		for (int i = 0; i < 5; i++) {
			new Thread(()->productor(),"++Productor  " + i).start();
		}

		for (int i = 0; i < 5; i++) {
			new Thread(()->consumidor(),"--Consumidor " + i).start();
		}
	}

	public static void main(String[] args) {
		new Eje8_5_5_4().exec();
	}
}