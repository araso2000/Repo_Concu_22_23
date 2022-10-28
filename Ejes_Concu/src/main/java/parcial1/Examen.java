package parcial1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;
import es.urjc.etsii.code.concurrency.SimpleSemaphore;

public class Examen {
	
	public static final int N_HILOS = 100;
	public static final int N_USERS = 50;
	public static final int M = 10009990;
	
	public static volatile int userPass,finAnteriorRango,nBarrera,passEncontrada,rangoGeneral,hilos;
	public static volatile boolean encontrada;
	
	public static SimpleSemaphore modVars,modEncontrada,modBarrera,barrera,desbloqueo;
	
	public static void generarPass() {
		userPass = (int)(Math.random()*M);
	}
	
	public static boolean passCorrecta(int pass) {
		//sleepRandom(1000);
		if(pass == userPass) {
			return true;
		}else {
			return false;
		}	
	}
	
	public static void proceso(int rango) {
		for(int ii=0;ii<N_USERS;ii++) {
			modVars.acquire();
			
			println(getThreadName() + " - Buscando Pass");
			
			int anterior = 0;
			
			if(rango != rangoGeneral) {
				//println(getThreadName() + "-Probando rango: [" + (M-rango) + "," + M + ")");
				anterior = (M-rango);
			}else {
				anterior = finAnteriorRango;
				finAnteriorRango = finAnteriorRango + rango;
				//println(getThreadName() + "-Probando rango: [" + anterior + "," + finAnteriorRango + ")");
			}
							
			modVars.release();
			
			int maximo = anterior + rango;
			for(int jj=anterior;jj<maximo+1;jj++) {
				if(passCorrecta(jj)) {
					modEncontrada.acquire();
					encontrada = true;
					passEncontrada = jj;
					modEncontrada.release();
					break;
				}else if(encontrada) {
					println(getThreadName() + " - FOR_Pass ya encontrada");
					break;
				}
			}
				
			modBarrera.acquire();
			nBarrera++;
			
			if(nBarrera < hilos) {
				modBarrera.release();
				sleepRandom(500);
				barrera.acquire();
				desbloqueo.release();
			}else {
				nBarrera = 0;
				encontrada = false;
				finAnteriorRango = 0;
				
				println("\n" + getThreadName() + " - Password encontrada: " + passEncontrada + " vs. Password original: " + userPass + "\n");
				
				generarPass();
				
				barrera.release(hilos-1);
				desbloqueo.acquire(hilos-1);
				modBarrera.release();
			}
		}
	}

	public static void main(String[] args) {
		modVars = new SimpleSemaphore(1);
		modEncontrada = new SimpleSemaphore(1);
		modBarrera = new SimpleSemaphore(1);
		barrera = new SimpleSemaphore(0);
		desbloqueo = new SimpleSemaphore(0);
		
		encontrada = false;
		
		userPass = 0;
		finAnteriorRango = 0;
		nBarrera = 0;
		passEncontrada = 0;
		
		hilos = N_HILOS;
		
		generarPass();
		
		rangoGeneral = (int) Math.floor(M/N_HILOS);
		int resto = (M - (rangoGeneral*N_HILOS));
		
		int ultimoRango = 0;
		
		for(int ii=0;ii<N_HILOS;ii++) {
			ultimoRango = ultimoRango+rangoGeneral;
			createThread("proceso",ultimoRango);
		}
		
		if(resto != 0) {
			createThread("proceso",resto);
			hilos++;
		}
		
		startThreadsAndWait();
	}
}