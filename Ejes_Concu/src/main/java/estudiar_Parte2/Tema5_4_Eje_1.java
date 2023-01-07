package estudiar_Parte2;

public class Tema5_4_Eje_1 {
	
	static volatile boolean continuar = false;
	
	public static void await() {
		while(!continuar);
	}
	
	public static void signal() {
		continuar = true;
	}

}
