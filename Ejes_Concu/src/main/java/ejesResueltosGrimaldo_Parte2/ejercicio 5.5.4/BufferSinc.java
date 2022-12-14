package ejercicio.ejer_t5_5_4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BufferSinc {

	private static final int BUFFER_SIZE = 10;

	private int[] datos = new int[BUFFER_SIZE];
	private int posInser = 0;
	private int posSacar = 0;
	private int numProductos = 0;
	static Lock procesosLock = new ReentrantLock();
	static Condition full = procesosLock.newCondition();
	static Condition empty = procesosLock.newCondition();

	public void insertar(int dato) throws InterruptedException {
		procesosLock.lock();

		while (numProductos == BUFFER_SIZE) {
			full.await();
		}

		datos[posInser] = dato;
		posInser = (posInser + 1) % datos.length;
		numProductos++;

		empty.signal();

		procesosLock.unlock();
	}

	public int sacar() throws InterruptedException {
		procesosLock.lock();

		while (numProductos == 0) {
			empty.await();
		}

		int dato = datos[posSacar];
		posSacar = (posSacar + 1) % datos.length;
		numProductos--;

		full.signal();

		procesosLock.unlock();

		return dato;
	}
}