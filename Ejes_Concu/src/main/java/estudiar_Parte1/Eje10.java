package estudiar_Parte1;

import static es.urjc.etsii.code.concurrency.SimpleConcurrent.*;

import java.util.ArrayList;

public class Eje10 {
	
	static volatile boolean procC,procF,procB,procG;
	
	static ArrayList <String> array = new ArrayList<String>();
	
	public static void proceso1() {
		while(!procC);
		array.add("A");
		
		array.add("B");
		procB = true;
	}
	
	public static void proceso2() {
		array.add("C");
		procC = true;
		
		while(!procF);
		array.add("D");
		
		while(!procB);
		while(!procG);
		array.add("E");
	}
	
	public static void proceso3() {
		array.add("F");
		procF = true;
		
		array.add("G");
		procG = true;
	}

	public static void main(String[] args) {
		createThread("proceso1");
		createThread("proceso2");
		createThread("proceso3");
		
		startThreadsAndWait();
		
		println(array.toString());
	}
}
