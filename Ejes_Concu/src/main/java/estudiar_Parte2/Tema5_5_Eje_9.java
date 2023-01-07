package estudiar_Parte2;

import java.util.concurrent.CountDownLatch;

public class Tema5_5_Eje_9 {
	
	private CountDownLatch goD = new CountDownLatch(2);
	private CountDownLatch goB = new CountDownLatch(1);
	private CountDownLatch goG = new CountDownLatch(1);
	private CountDownLatch goH = new CountDownLatch(1);
	private CountDownLatch goE = new CountDownLatch(1);
	private CountDownLatch goC = new CountDownLatch(1);
	
	public void proceso1() throws InterruptedException {
		System.out.println("A");
		goD.countDown();
		
		goB.await();
		System.out.println("B");
		goE.countDown();
		goH.countDown();
		
		goC.await();
		System.out.println("C");
		
	}
	
	public void proceso2() throws InterruptedException {
		goD.await();
		System.out.println("D");
		goB.countDown();
		goG.countDown();
		
		goE.await();
		System.out.println("E");
		goC.countDown();
	}
	
	public void proceso3() throws InterruptedException {
		System.out.println("F");
		goD.countDown();
		
		goG.await();
		System.out.println("G");
		
		goH.await();
		System.out.println("H");
	}
	
	public void exec() {
		new Thread(() -> {
			try {
				proceso1();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				proceso2();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		
		new Thread(() -> {
			try {
				proceso3();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		new Tema5_5_Eje_9().exec();
	}
}