package misEjes_Parte2;

public class Eje4_5_4_2 {

	private Eje3_5_4_1 SincCond;
	static volatile double producto;
	
	public void productor() {
		producto = Math.random()*5;
		SincCond.signal();
	}
	
	public void consumidor() {
		SincCond.await();
		System.out.println("Producto: " + producto);
	}
	
	private void exec() {
		new Thread(() -> productor()).start();
		new Thread(() -> consumidor()).start();
		
		SincCond = new Eje3_5_4_1();
	}

	public static void main(String[] args) {
		new Eje4_5_4_2().exec();
	}
}
