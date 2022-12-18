package misEjes_Parte2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje6 {
	
	private final int N_HILOS = 20;
	static volatile int numero;
	private static Lock cerrojo = new ReentrantLock();
	
	public static void persona() {
		cerrojo.lock();
		try {
			numero++;
			System.out.println(Thread.currentThread().getName() + "_Hola, somos " + numero + " personas");
		}finally {
			cerrojo.unlock();
		}
		
		cerrojo.lock();
		try {
			if(numero==1) {
				System.out.println(Thread.currentThread().getName() + "_Tengo un regalo!");
			}else {
				System.out.println(Thread.currentThread().getName() + "_Pruebe otra vez...");
			}
		}finally {
			cerrojo.unlock();
		}
			
		System.out.println(Thread.currentThread().getName() + "_Que bonito!");
		System.out.println(Thread.currentThread().getName() + "_Alucinante!");
		
		cerrojo.lock();
		try {
			numero--;
			System.out.println(Thread.currentThread().getName() + "_Adios a " + numero + " personas");
		}finally {
			cerrojo.unlock();
		}
			
		System.out.println(Thread.currentThread().getName() + "_Paseo");
	}
	
	public void exec() {
		for(int ii=0;ii<N_HILOS;ii++) {
			new Thread(() -> persona(), "persona_" + ii).start();
		}
	}

	public static void main(String[] args) {
		new Eje6().exec();
	}
}
