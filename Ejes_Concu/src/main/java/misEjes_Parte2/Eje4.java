package misEjes_Parte2;

public class Eje4 {

	private Eje3 SincCond;
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
		
		SincCond = new Eje3();
	}

	public static void main(String[] args) {
		new Eje4().exec();
	}
}
