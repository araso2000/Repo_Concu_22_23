package estudiar_Parte2.Tema_5_6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Eje5 {
	
	private static final int MAX_PIEZAS = 10;
	private static final int NUM_ROBOTS = 2;
	private static final int NUM_TIPOS_PIEZAS = 10;
	
	private List<BlockingQueue<Integer>> almacen = new ArrayList<BlockingQueue<Integer>>();
	
	public int fabricarPieza(int tipo) {
		try {
			Thread.sleep((int)(Math.random()*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return tipo;
	}
	
	public void almacenarPieza(int tipo, int pieza) {
		try {
			almacen.get(tipo).put(pieza);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public void maquina(int tipo) {
		while(true) {
			int pieza = fabricarPieza(tipo);
			System.out.println(Thread.currentThread().getName() + "_Fabrico pieza de tipo: " + pieza);
			almacenarPieza(tipo,pieza);
			System.out.println(Thread.currentThread().getName() + "_Almaceno pieza de tipo: " + pieza);
		}
	}
	
	public int recogerPieza(int tipo) throws InterruptedException {
		return(almacen.get(tipo).take());
	}
	
	public void montarPieza(int pieza) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + "_Montando pieza de tipo: " + pieza);
		Thread.sleep((int)(Math.random()*2000));
	}
	
	public void robot() throws InterruptedException {
		while(true) {
			for(int ii=0;ii<NUM_TIPOS_PIEZAS;ii++) {
				int pieza = recogerPieza(ii);
				System.out.println(Thread.currentThread().getName() + "_Cojo pieza de tipo: " + pieza);
				montarPieza(pieza);
			}
			System.out.println(Thread.currentThread().getName() + "_Producto montado!");
		}
	}
	
	public void exec() {
		for(int i=0; i<NUM_TIPOS_PIEZAS; i++){
			almacen.add(new LinkedBlockingQueue<Integer>(MAX_PIEZAS));
		}
		
		for(int ii=0; ii<NUM_TIPOS_PIEZAS; ii++) {
			int temp = ii;
			new Thread(() -> maquina(temp),"maquina" + ii).start();
		}
		
		for(int ii=0; ii<NUM_ROBOTS; ii++) {
			new Thread(() -> {
				try {
					robot();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			},"robot" + ii).start();
		}
	}

	public static void main(String[] args) {
		new Eje5().exec();
	}
}