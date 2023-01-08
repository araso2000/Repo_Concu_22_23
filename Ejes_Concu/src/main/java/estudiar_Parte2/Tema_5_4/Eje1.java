package estudiar_Parte2.Tema_5_4;

public class Eje1 {
	
	static volatile boolean continuar = false;
	
	public static void await() {
		while(!continuar);
	}
	
	public static void signal() {
		continuar = true;
	}

}
