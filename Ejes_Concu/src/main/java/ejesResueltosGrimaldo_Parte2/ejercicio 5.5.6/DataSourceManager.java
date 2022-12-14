package ejercicio.ejer_t5_5_6;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DataSourceManager {

	private static final int NUM_DS = 2;

	private boolean[] busyDataSources = new boolean[NUM_DS];

	private ReentrantLock lock = new ReentrantLock(true);
	private Condition[] waitForDataSource = new Condition[NUM_DS];
	private Condition waitForAnyDataSource = lock.newCondition();
	
	public DataSourceManager() {
		for (int i = 0; i < NUM_DS; i++) {
			waitForDataSource[i] = lock.newCondition();
		}
		Arrays.fill(busyDataSources, false);
	}

	public int accessAnyDataSource() throws InterruptedException {
		lock.lock();

		try {

			while (busyDataSources[0] && busyDataSources[1]) {
				waitForAnyDataSource.await();
			}

			if (!busyDataSources[0]) {
				busyDataSources[0] = true;
				return 0;
			} else {
				busyDataSources[1] = true;
				return 1;
			}

		} finally {
			lock.unlock();
		}
	}

	public void accessDataSource(int dataSource) throws InterruptedException {

		lock.lock();

		try {

			while (busyDataSources[dataSource]) {
				waitForDataSource[dataSource].await();
			}
			busyDataSources[dataSource] = true;

		} finally {
			lock.unlock();
		}
	}

	public void freeDataSource(int dataSource) {

		lock.lock();
		try {
			
			busyDataSources[dataSource] = false;
			
			// Aquí va a existir el mismo problema que con los
			// lectores-escritores: si desbloqueamos un hilo
			// que esté esperando específicamente por un datasource
			// concreto, se nos puede "colar" otro hilo al que le
			// sirva cualquier datasource. Se deja como ejercicio
			// mejorar esto, y también hacer que el programa sea 
			// válido para cualquier número de datasources

			if (lock.hasWaiters(waitForDataSource[dataSource])){
				waitForDataSource[dataSource].signal();
			} else if (lock.hasWaiters(waitForAnyDataSource)) {
				waitForAnyDataSource.signal();
			}

		} finally {
			lock.unlock();
		}
	}
}