package estudiar_Parte2.Tema_5_5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Eje2 {

	static volatile int numero;
	private static Lock cerrojo = new ReentrantLock();
	
	public static void persona() throws InterruptedException {
		for(int ii=0;ii<4;ii++) {
			cerrojo.lock();
			numero++;
			System.out.println(Thread.currentThread().getName() + "_Hola, somos " + numero + " personas");
			cerrojo.unlock();
			
			sleepRandom();
			
			cerrojo.lock();
			if(numero==1) {
				System.out.println(Thread.currentThread().getName() + "_Tengo un regalo!");
			}else {
				System.out.println(Thread.currentThread().getName() + "_Pruebe otra vez...");
			}
			cerrojo.unlock();
			
			sleepRandom();
			
			System.out.println(Thread.currentThread().getName() + "_Que bonito!");
			System.out.println(Thread.currentThread().getName() + "_Alucinante!");
			
			cerrojo.lock();
			numero--;
			System.out.println(Thread.currentThread().getName() + "_Adios a " + numero + " personas");
			cerrojo.unlock();
			
			System.out.println(Thread.currentThread().getName() + "_Paseo");
			
			sleepRandom();
		}
	}
	
	public void exec() {
		for(int ii=0;ii<5;ii++) {
			new Thread(() -> {
				try {
					persona();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	public static void sleepRandom() throws InterruptedException {
		Thread.sleep((int)(Math.random()*2000));
	}

	public static void main(String[] args) {
		new Eje2().exec();
	}
}

