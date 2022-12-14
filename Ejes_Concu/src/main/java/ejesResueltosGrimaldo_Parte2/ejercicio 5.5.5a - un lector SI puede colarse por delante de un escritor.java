package ejercicio.ejer_t5_5_5;

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

	// Posible intercalación problemática por la cual esta solución es errónea:
	// Sólo hay un escritor bloqueado, esperando a que todos los lectores actuales terminen.
	// Cuando el último lector termina, desbloquea a dicho escritor. Ése escritor tiene ahora
	// que competir para adquirir el cerrojo. Compite con posibles lectores que estén esperando
	// bajo su condición (en inicioLectura) o con posibles lectores que estén esperando para
	// entrar en la sección crítica en inicioLectura, o con posibles escritores que estén
	// esperando para entrar en la sección crítica en inicioEscritura. Cualquiera de ellos puede
	// entrar en la sección crítica, no tiene por qué ser el escritor que acabamos de desbloquear.
	// Supongamos que entra un lector que estaba esperando al principio de la sección crítica.
	// Dicho lector llegará al while de inicioLectura y no se bloqueará, pues ambas condiciones
	// del while están a falso (¡ya no hay ningún escritor bloqueado en su condición, pues el que 
	// había está despierto y esperando para entrar!), por lo tanto el lector no se bloquea y
	// consigue acceder a la base de datos, colándose por delante del escritor despertado y por
	// lo tanto violando el principio del esquema lectores-escritores. Otra cosa que podría pasar
	// es que, en vez de adquirir el cerrojo el escritor al que acabamos de despertar, adquiere
	// el cerrojo un nuevo escritor que estaba esperando para entrar al principio de la sección
	// crítica, en inicioEscritura. Dicho escritor no se bloqueará en su condición y por lo tanto
	// adquirirá directamente la base de datos, colándose por delante del escritor que hemos
	// despertado. Esto último (que un nuevo escritor se adelante a otro que estaba antes) no
	// supone ningún problema de cara a los requisitos, pero no está mal saberlo.
	
	public void inicioLectura() {
		lock.lock();
		try {
			try {
				while (escritoresBD || lock.hasWaiters(escritoresCond)) {
					lectoresCond.await();
				}
			} catch (InterruptedException e) {
			}
			numLectoresBD++;
		} finally {
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
			try {
				while (numLectoresBD > 0 || escritoresBD) {
					escritoresCond.await();
				}
			} catch (InterruptedException e) {
			}
			escritoresBD = true;
		} finally {
			lock.unlock();
		}
	}

	public void finEscritura() {
		lock.lock();
		try {
			escritoresBD = false;
			if (lock.hasWaiters(escritoresCond)) {
				escritoresCond.signal();
			} else {
				lectoresCond.signalAll();
			}
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
		for (int i = 0; i < 5; i++) {
			new Thread(() -> lector()).start();
		}

		for (int i = 0; i < 3; i++) {
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