package misEjes_Parte2.Eje10_5_6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DataSourceManager {
	
	static boolean[] data;
	
	private final int N_DATA = 10;
	
	private static int n_accedidos;
	
	static ReentrantLock lock;
	
	static Condition[] cdFijo;
	static Condition cdNormal;

	public DataSourceManager() {
		data = new boolean[N_DATA];
		lock = new ReentrantLock();
		
		n_accedidos = 0;
		
		cdFijo = new Condition[N_DATA];
		for(int ii = 0; ii < N_DATA; ii++) {
			cdFijo[ii] = lock.newCondition();
		}
		
		cdNormal = lock.newCondition();
		
		//TRUE = DISPONIBLE / FALSE = OCUPADO
		for(int ii = 0; ii < N_DATA; ii++) {
			data[ii] = true;
		}
	}
	
	public void accessDataSource(int dataSource)  {
		lock.lock();
		
		try {
			System.out.println("DataManager -> Intentando acceder: " + dataSource);
			while(data[dataSource] == false) {
				cdFijo[dataSource].await();
			}
			
			data[dataSource] = false;
			n_accedidos++;
			
			System.out.println("DataManager -> Accedido: " + dataSource);
			
			//Thread.sleep((long) (Math.random()*5000));
		}catch(InterruptedException e) {
			
		}finally{
			lock.unlock();
		}
		
	}
	
	public int accessAnyDataSource() {
		int indice = 0;
		
		lock.lock();
		
		try {
			System.out.println("DataManager -> Intentando accessAnyDataSource");
			
			while(n_accedidos == 10) {
				cdNormal.await();
			}
			
			boolean stop = false;
			
			for(int ii = 0; ii < N_DATA && !stop; ii++) {
				if(data[ii] == true) {
					stop = true;
					indice = ii;
				}
			}
			
			System.out.println("DataManager -> Accedido: " + indice);
			
			data[indice] = false;
			n_accedidos++;
			
		}catch(InterruptedException e) {
			
		}finally {
			lock.unlock();
		}
		
		return indice;
	}
	
	public void freeDataSource(int dataSource) {
		lock.lock();
		
		try {
			System.out.println("DataManager -> Liberando: " + dataSource);
			
			data[dataSource] = true;
			n_accedidos--;
			
			if(lock.hasWaiters(cdFijo[dataSource])){
				cdFijo[dataSource].signal();
			}else if(lock.hasWaiters(cdNormal)) {
				cdNormal.signal();
			}
			
		}finally {
			lock.unlock();
		}
	}
}
