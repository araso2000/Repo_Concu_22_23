import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LectoresEscritores {

	private ReentrantLock lock = new ReentrantLock();

	// Para que los escritores esperen antes de empezar a escribir
	private Condition escritoresCond = lock.newCondition(); 

	// Para que los lectores esperen antes de empezar a leer
	private Condition lectoresCond = lock.newCondition();
	
	// Cuantos lectores hay ahora leyendo
	private int numLectoresBD = 0; 

	// Si hay un escritor ahora escribiendo (sólo puede haber un escritor
	// a la vez escribiendo, por lo tanto no es necesario contarlos como sí
	// hacemos con los lectores en "numLectoresBD"
	private boolean escritoresBD = false; 

	// Nos indica cuántos escritores desean escribir (estén o no estén
	// bloqueados en su condición)
	private int numEscritoresEsperando = 0;
	
	public void inicioLectura() {
		lock.lock();
		try {
			while (escritoresBD || numEscritoresEsperando>0) 
				lectoresCond.await();
			numLectoresBD++;	
		} catch (InterruptedException e) {}			
		finally {
			lock.unlock();
		}
	}

	public void finLectura() {
		lock.lock();
		try {
			numLectoresBD--;
			if (numLectoresBD == 0 && lock.hasWaiters(escritoresCond)) {
				escritoresCond.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	public void inicioEscritura() {
		lock.lock();
		try {
			numEscritoresEsperando++;					
			try {
				while (numLectoresBD > 0 || escritoresBD) 
					escritoresCond.await();
			} catch (InterruptedException e) {}
			escritoresBD = true;
			numEscritoresEsperando--;
		} finally {
			lock.unlock();
		}
	}

	public void finEscritura() {
		lock.lock();
		try {
			escritoresBD = false;	
			// Lo que viene a continuación se podría mejorar para no despertar
			// inútilmente a escritores si ya hay alguno despierto pero esperando
			// para adquirir la sección crítica.
			if (numEscritoresEsperando > 0) escritoresCond.signal();		
			else lectoresCond.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void lector() {
		while (true) {
			inicioLectura();
			System.out.println("Leer datos 1");
			sleep();
			System.out.println("Leer datos 2");
			finLectura();
			System.out.println("Procesar datos");
			sleep();
		}
	}

	public void escritor() {
		while (true) {
			System.out.println("Generar datos");
			inicioEscritura();
			System.out.println("Escribir datos 1");
			sleep();
			System.out.println("Escribir datos 2");
			finEscritura();
			sleep();
		}
	}

	public void exec() {
		for (int i = 0; i < 15; i++) {
			new Thread(() -> lector()).start();
		}

		for (int i = 0; i < 5; i++) {
			new Thread(() -> escritor()).start();
		}
	}

	public static void main(String[] args) {
		new LectoresEscritores().exec();
	}
	
	private void sleep() {
		try {
			Thread.sleep((long) (Math.random()*500));
		} catch (InterruptedException e) {}
	}
}