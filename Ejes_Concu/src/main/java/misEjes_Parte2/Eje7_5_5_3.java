package misEjes_Parte2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Eje7_5_5_3 {
	
	private final int N_LECTORES = 5;
	private final int N_ESCRITORES = 5;
	
	public void lector(Lock lockLector) {
		lockLector.lock();
		System.out.println(Thread.currentThread().getName() + "_Leer datos...");
		lockLector.unlock();
		System.out.println(Thread.currentThread().getName() + "_Procesar datos...");
	}
	
	public void escritor(Lock lockEscritor) {
		System.out.println(Thread.currentThread().getName() + "_Generar datos...");
		lockEscritor.lock();
		System.out.println(Thread.currentThread().getName() + "_Escribir datos...");
		lockEscritor.unlock();
	}

	public void exec() {
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		Lock readLock = readWriteLock.readLock();
		Lock writeLock = readWriteLock.writeLock();
		
		for(int ii=0;ii<N_LECTORES;ii++) {
			new Thread(() -> lector(readLock),"lector_" + ii).start();
		}
		
		for(int ii=0;ii<N_ESCRITORES;ii++) {
			new Thread(() -> escritor(writeLock),"escritor_" + ii).start();
		}
		
	}
	
	public static void main(String[] args) {
		new Eje7_5_5_3().exec();
	}
}
