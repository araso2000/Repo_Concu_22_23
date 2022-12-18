package misEjes_Parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje8 {
	
	private int[] array = new int[10];
	private int nextINPos = 0;
	private int nextOUTPos = 0;
	private int posDisponibles = 10;
	
	Lock lockCond_1 = new ReentrantLock();
	Condition cd_1 = lockCond_1.newCondition();
	
	Lock lock1 = new ReentrantLock();
	
	public void insertar(int dato) {
		try {
			lock1.lock();
			
			while(posDisponibles == 0) {
				try {
					cd_1.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} finally{
			
		}
		
		array[nextINPos] = dato;
		nextINPos = (nextINPos+1) % array.length;
	}
	
	public int sacar() {
		int returnear = array[nextOUTPos];
		nextOUTPos = (nextOUTPos+1) % array.length;
		return returnear;
	}
	
	public void productor() {
		insertar((int)(Math.random()*50));
	}
	
	public void consumidor() {
		System.out.println("Consumido: " + sacar());
	}

	public static void main(String[] args) {

	}
}