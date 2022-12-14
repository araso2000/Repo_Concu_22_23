

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

	// Nos indica que hay un escritor que se quedó esperando en su condición,
	// fue despertado y ahora está esperando para entrar en su sección crítica.
	// Debería evitar que un lector que esté esperando para entrar en la sección
	// crítica de "inicioLectura" se cuele por delante de un escritor que estaba
	// bloqueado en su condición y fue despertado. Esto ocurre porque alguien 
	// que haya sido despertado de su bloqueo en una condición no tiene más
	// prioridad para entrar en la sección crítica que otro proceso que esté
	// esperando para entrar en la misma sección crítica (ya sea en otra 
	// condición o en el método lock()). Sin embargo, como veremos en un
	// comentario en el método inicioEscritura, esta solución aún es incorrecta
	// porque aún un lector puede seguir colándose por delante de un escritor
	private boolean hayEscritorPreparado = false;
	
	public void inicioLectura() {
		lock.lock();
		try {
			while (escritoresBD || lock.hasWaiters(escritoresCond) || hayEscritorPreparado) 
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
			hayEscritorPreparado=true;					
			try {
				while (numLectoresBD > 0 || escritoresBD) escritoresCond.await();
			} catch (InterruptedException e) {}
			escritoresBD = true;

			/* Supongamos que el último escritor que estaba bloqueado en su condición
			 * fue desbloqueado pero no accedió a la sección crítica. Si un lector
			 * bloqueado a la entrada de inicioLectura toma la sección crítica, se 
			 * bloqueará en su condición debido a la nueva variable "hayEscritorPreparado",
			 * o sea que eso no es problema. El problema es que, en vez de entrar el 
			 * escritor desbloqueado, entre a la sección crítica un nuevo escritor que
			 * estaba esperando al inicio de inicioEscritura. Dicho escritor no se bloqueará
			 * en la condición, y llegará hasta aquí. Y, como vemos en la siguiente
			 * instrucción, pondrá "hayEscritorPreparado" a falso, pues ya no hay ningún
			 * escritor bloqueado en la condición (el que estaba bloqueado, fue desbloqueado
			 * y actualmente está esperando para entrar en la sección crítica). Cuando este
			 * nuevo escritor termine, un lector nuevo podrá acceder a la sección crítica,
			 * pues "hayEscritorPreparado" estará a false, colándose por delante de nuestro
			 * primer escritor, que aún sigue desbloqueado en su condición y esperando para
			 * acceder a la sección crítica. Por lo tanto, esta solución sigue siendo errónea */
			if (!lock.hasWaiters(escritoresCond)) hayEscritorPreparado=false;
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