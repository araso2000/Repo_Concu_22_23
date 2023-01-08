package estudiar_Parte2.Tema_5_3;

public class Eje1 {
	
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
		new Eje1().exec();
	}

}
