package estudiar_Parte2;

public class Tema5_4_Eje_2 {
	
	static volatile boolean producido = false;
	static volatile double producto;
	
	
	public static void productor() {
		producto = Math.random();
		Tema5_4_Eje_1.signal();
	}
	
	public static void consumidor() {
		Tema5_4_Eje_1.await();
		System.out.println("Producto: "+producto);
	}
	
	public void exec() {
		new Thread(()-> productor()).start();
		new Thread(()-> consumidor()).start();
	}
	 
	public static void main(String[] args){
		 new Tema5_4_Eje_2().exec();
	}
}
