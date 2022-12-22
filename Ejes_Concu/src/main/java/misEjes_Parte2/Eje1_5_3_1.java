package misEjes_Parte2;

public class Eje1_5_3_1 {
	
	static volatile boolean producido = false;
	static volatile double producto;
	
	public void productor() {
		producto = Math.random();
		producido = true;
	}
	
	public void consumidor() {
		while(!producido);
		System.out.println("Producto: " + producto);
	}
	
	private void exec() {
		new Thread(() -> productor()).start();
		new Thread(() -> consumidor()).start();
	}

	public static void main(String[] args) {
		new Eje1_5_3_1().exec();
	}

}
