package estudiar_Parte2.Tema_5_5;

import java.util.concurrent.locks.ReentrantLock;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.println;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Eje7 {
	
	static Lock cerrojo = new ReentrantLock();
	
	private static final int N_FRAGMENTOS = 10; 
	private static final int N_HILOS = 3;
	
	private static volatile int pendiente = 0;
	
	private static volatile int[] fichero = new int[N_FRAGMENTOS];
	
	static CyclicBarrier barrera = new CyclicBarrier(N_HILOS,() -> finalizar());
	
	public static Runnable descargador() {
		while(true) {
			while(true) {
				cerrojo.lock();
				
				if(pendiente == N_FRAGMENTOS) {
					cerrojo.unlock();
					break;
				}
				
				int descarga = pendiente;
				pendiente++;
				cerrojo.unlock();
				
				println("Descargando fragmento: " + descarga);
				int descargado = 0;
				try {
					descargado = descargaDatos(descarga);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				println("Escribiendo fragmento: " + descarga);
				fichero[descarga] = descargado;
			}
			try {
				barrera.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void finalizar() {
		mostrarFichero();
		pendiente=0;
		fichero = new int[N_FRAGMENTOS];
	}
	
	private static int descargaDatos(int numFragmento) throws InterruptedException { 
		Thread.sleep(1000);
		return numFragmento * 2;
	}
	
	private static void mostrarFichero() { 
		System.out.println("--------------------------------------------------"); 
		System.out.print("File = [");
		for (int i = 0; i < N_FRAGMENTOS; i++) {
			System.out.print(fichero[i] + ","); 
		}
		System.out.println("]"); 
	}
	
	public void exec() throws InterruptedException, BrokenBarrierException {
		for(int ii=0; ii<N_HILOS; ii++) {
			new Thread(() -> {
				descargador();
			},"descargador" + ii).start();
		}
	}

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		new Eje7().exec();
	}
}
