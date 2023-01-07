package estudiar_Parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Tema5_5_Eje_5 {
	
	static ReentrantLock cerrojo = new ReentrantLock();
	static Condition condLector = cerrojo.newCondition();
	static Condition condEscritor = cerrojo.newCondition();
	
	private int numEscritoresEsperando = 0;
	private int numLectoresLeyendo = 0;
	private boolean escritoresBD = false;
	
	public void lector() throws InterruptedException {
		//Inicio la lectura
		cerrojo.lock();
		print("Leyendo...");
		sleepRandom();
		
		try {
			//Mientras haya escritores O leyendo O esperando para leer, me espero
			while(escritoresBD || numEscritoresEsperando>0) {
				condLector.await();
			}
			numLectoresLeyendo++;
		}catch(InterruptedException e) {
			
		}finally {
			cerrojo.unlock();
		}
		
		//Imprimo mensaje de consola
		print("Fin lectura");
		
		//Finalizo lectura
		cerrojo.lock();
		try {
			numLectoresLeyendo--;
			//Si ya no quedan lectores leyendo y hay escritores esperando, les avisamos para que se desbloqueen
			if(numLectoresLeyendo == 0 && cerrojo.hasWaiters(condEscritor)) {
				condEscritor.signal();
			}
		}finally {
			cerrojo.unlock();
		}
		
		//Imprimo mensaje de consola
		print("Procesar datos");
	}
	
	public void escritor() throws InterruptedException {
		//Imprimo mensaje de consola
		print("Generando datos...");
		
		//Inicio escritura
		cerrojo.lock();
		sleepRandom();
		
		try {
			numEscritoresEsperando++;
			try {
				//Mientras haya escritores escribiendo y aun lectores por leer, el escritor se espera
				while(numLectoresLeyendo>0 && escritoresBD) {
					condEscritor.await();
				}
			}catch(InterruptedException e) {
				
			}
			//Una vez la espera, marca que esta leyendo y decrece el numero de esperandos
			escritoresBD = true;
			numEscritoresEsperando--;
		}finally {
			cerrojo.unlock();
		}
		
		//Imprimo mensaje de consola
		print("Generado");
		
		//Finalizo escritura
		cerrojo.lock();
		try {
			escritoresBD = false;
			//Si hay escritores bloqueados esperndo escribir, les avisamos
			if(numEscritoresEsperando > 0) {
				condEscritor.signal();
			}else {
				//Si no damos paso a los lectores
				condLector.signal();
			}
		}finally {
			cerrojo.unlock();
		}
		
		//Imprimo mensaje de consola
		print("Escrito");
	}
	
	public void print(String texto) {
		System.out.println(Thread.currentThread().getName() + "_" + texto);
	}
	
	public void sleepRandom() throws InterruptedException {
		Thread.sleep((int)(Math.random()*2000));
	}
	
	public void exec() {
		for(int ii = 0; ii<5; ii++) {
			new Thread(() -> {
				try {
					lector();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			},"lector" + ii).start();
		}
		
		for(int ii = 0; ii<5; ii++) {
			new Thread(() -> {
				try {
					escritor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			},"escritor" + ii).start();
		}
	}

	public static void main(String[] args) {
		new Tema5_5_Eje_5().exec();
	}
}