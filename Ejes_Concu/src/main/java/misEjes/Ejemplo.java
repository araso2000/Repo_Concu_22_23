package misEjes;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

public class Ejemplo {

	public static void repeat(String text) {
		enterMutex();
		for(int i=0; i<5; i++){
			println(text);
		}
		exitMutex();
	}
		
	public static void printText() {
		enterMutex();
		printlnI("B1");
		printlnI("B2");
		printlnI("B3");
		exitMutex();
	}
		
	public static void main(String[] args) {
		createThread("repeat","XXXXX");
		createThread("repeat","-----");
		createThread("printText");
		startThreadsAndWait();
	}
}
