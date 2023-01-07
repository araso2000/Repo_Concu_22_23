package estudiar_Parte2;

public class Tema5_3_Eje_1 {
	
	static volatile boolean producido = false;
	static volatile double producto;
	
	public static void productor() {
		producto = Math.random();
		producido = true;
	}
	
	public static void consumidor() {
		while (!producido);
		System.out.println("Producto: "+producto);
	}
	
	public void exec() {
		new Thread(() -> productor()).start();
		new Thread(() -> consumidor()).start();
	}

	public static void main(String[] args) {
		new Tema5_3_Eje_1().exec();
	}

}
