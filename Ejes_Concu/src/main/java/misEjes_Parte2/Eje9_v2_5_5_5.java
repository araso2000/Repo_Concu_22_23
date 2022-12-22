package misEjes_Parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje9_v2_5_5_5 {
	
	private static final int N_ESCRITOR = 3;
	private static final int N_LECTOR = 5;
	
	static ReentrantLock cerrojo = new ReentrantLock();
	
	static Condition condLector = cerrojo.newCondition();
	static Condition condEscritor = cerrojo.newCondition();
	
	private boolean escritoresBD = false;
	private int numLectoresBD = 0;
	
	//Evita que un nuevo lector se "cuele" entre escritores
	private boolean escritorPreparado = false;
	
	public void lector() throws InterruptedException {
		while(true) {
			cerrojo.lock();
			
			System.out.println(Thread.currentThread().getName() + "_Quiero leer");
			
			//Si hay un escrito preparado O si hay un escritor escribiendo O si hay escritores esperando, el lector se bloquea
			while(escritorPreparado || escritoresBD || cerrojo.hasWaiters(condEscritor)) {
				condLector.await();
			}
			
			System.out.println(Thread.currentThread().getName() + "_Ya puedo leer");
			
			numLectoresBD++;
			
			cerrojo.unlock();
			
			Thread.sleep((long) (Math.random()*500));
			
			cerrojo.lock();
			
			numLectoresBD--;
			
			System.out.println(Thread.currentThread().getName() + "_Leido");
			
			//Si ya no hay lectores leyendo Y hay escritores esperando para escribir, les avisamos para que se desbloqueen
			if(numLectoresBD == 0 && cerrojo.hasWaiters(condEscritor)) {
				condEscritor.signal();
			}
			
			cerrojo.unlock();
			
			System.out.println(Thread.currentThread().getName() + "_Procesar datos");
			
			Thread.sleep((long) (Math.random()*500));
		}
	}
	
	public void escritor() throws InterruptedException {
		while(true) {
			System.out.println(Thread.currentThread().getName() + "_Generando");
			
			cerrojo.lock();
			System.out.println(Thread.currentThread().getName() + "_Quiero escribir");
			//Mientras haya lectores leyendo O haya algun escritor escribiendo, me bloqueo
			while(numLectoresBD > 0 || escritoresBD) {
				condEscritor.await();
				escritorPreparado = false;
			}
			
			//Salgo del bucle porque ya no hay escritores pero ahora voy a escribir yo asi que aviso al resto
			escritoresBD = true;
			System.out.println(Thread.currentThread().getName() + "_Ya puedo escribir");
			
			cerrojo.unlock();
			
			Thread.sleep((long) (Math.random()*500));
			
			cerrojo.lock();
			
			escritoresBD = false;
			//Si hay escritores esperando escribo y aviso al resto de que ya he escrito
			//Si no, aviso a los lectores de que les toca leer
			if(cerrojo.hasWaiters(condEscritor)) {
				escritorPreparado = true;
				condEscritor.signal();
			}else {
				condLector.signalAll();
			}
			
			cerrojo.unlock();
			
			System.out.println(Thread.currentThread().getName() + "_Escrito");
			
			Thread.sleep((long) (Math.random()*500));
		}
	}
	
	public void exec() {		
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
		new Eje9_v2_5_5_5().exec();
	}

}
