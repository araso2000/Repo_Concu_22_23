package misEjes_Parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje9_v1 {
	
	private static final int N_ESCRITOR = 3;
	private static final int N_LECTOR = 5;
	
	static Lock cerrojo = new ReentrantLock();
	
	static Condition condLector = cerrojo.newCondition();
	static Condition condEscritor = cerrojo.newCondition();
	
	private static volatile int numBarreraLector,numBarreraEscritor;
	
	
	public void lector() throws InterruptedException {
		while(true) {
			cerrojo.lock();
			
			System.out.println(Thread.currentThread().getName() + "_Leyendo...");
			Thread.sleep(2000);
			
			numBarreraLector++;
			
			System.out.println(Thread.currentThread().getName() + "_Leido");
			
			if(numBarreraLector < N_LECTOR) {
				while(numBarreraLector < N_LECTOR) {
					condLector.await();
				}
			}else {
				condLector.signalAll();
				numBarreraLector = 0;
			}
			
			cerrojo.unlock();
			
			System.out.println(Thread.currentThread().getName() + "_Procesar datos");
		}
	}
	
	public void escritor() throws InterruptedException {
		while(true) {
			System.out.println(Thread.currentThread().getName() + "_Generando...");
			
			cerrojo.lock();
			
			Thread.sleep(2000);
			
			numBarreraEscritor++;
			
			System.out.println(Thread.currentThread().getName() + "_Generado->Escribir datos");
			
			if(numBarreraEscritor < N_ESCRITOR) {
				while(numBarreraEscritor < N_ESCRITOR) {
					condEscritor.await();
				}
			}else {
				condEscritor.signalAll();
				numBarreraEscritor = 0;
			}
			
			cerrojo.unlock();
			
			System.out.println(Thread.currentThread().getName() + "_Escrito");
		}
	}
	
	public void exec() {
		numBarreraLector = 0;
		numBarreraEscritor = 0;
		
		for(int ii=0;ii<N_LECTOR;ii++) {
			new Thread(() -> {
				try {
					lector();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}, "lector" + ii).start();
		}
		
		for(int ii=0;ii<N_ESCRITOR;ii++) {
			new Thread(() -> {
				try {
					escritor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}, "escritor" + ii).start();
		}
	}

	public static void main(String[] args) {
		new Eje9_v1().exec();
	}

}
