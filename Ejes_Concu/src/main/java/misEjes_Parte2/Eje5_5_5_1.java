package misEjes_Parte2;

import java.util.concurrent.Semaphore;

public class Eje5_5_5_1 {
	
	private static final int PRODUCTORES = 5;
	private static final int CONSUMIDORES = 3;
	
	private static final int BUFFER_SIZE = 10;
	
	private int[] datos = new int[BUFFER_SIZE];
	private int posInser = 0;
	private int posSacar = 0;
	
	private Semaphore nHuecos = new Semaphore(BUFFER_SIZE);
	private Semaphore nProductos = new Semaphore(0);
	private Semaphore emPosInser = new Semaphore(1);
	private Semaphore emPosSacar = new Semaphore(1);
	
	public void insertar(int dato) throws InterruptedException {		
		nHuecos.acquire();		
		
		emPosInser.acquire();
		datos[posInser] = dato;
		posInser = (posInser+1) % datos.length;
		emPosInser.release();
		
		nProductos.release();
	}

	public int sacar() throws InterruptedException {		
		nProductos.acquire();
		
		emPosSacar.acquire();
		int dato = datos[posSacar];
		posSacar = (posSacar+1) % datos.length;
		emPosSacar.release();
		
		nHuecos.release();	
		
		return dato;
	}
	
	public void productor() {
		try {
			
			while(true) {
				int numero = (int)(Math.random()*50);
				insertar(numero);
				System.out.println(Thread.currentThread().getName() + ": Producido: " + numero);
				Thread.sleep(1000);
			}
			
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": No se ha podido producir");
		}
	}
	
	public void consumidor() {
		try {
			
			while(true) {
				int numero = sacar();
				Thread.sleep(1000);
				System.out.println(Thread.currentThread().getName() + ": Consumido: " + numero);
			}
			
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": No se ha podido consumir");
		}
	}
	
	public void exec() {
		for (int i = 0; i < PRODUCTORES; i++) {
			new Thread(()->productor(),"++Productor  " + i).start();
		}

		for (int i = 0; i < CONSUMIDORES; i++) {
			new Thread(()->consumidor(),"--Consumidor " + i).start();
		}
	}

	public static void main(String[] args) {
		new Eje5_5_5_1().exec();
	}

}
