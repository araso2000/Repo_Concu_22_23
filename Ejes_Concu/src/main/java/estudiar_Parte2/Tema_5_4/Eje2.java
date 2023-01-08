package estudiar_Parte2.Tema_5_4;

public class Eje2 {
	
	static volatile boolean producido = false;
	static volatile double producto;
	
	
	public static void productor() {
		producto = Math.random();
		Eje1.signal();
	}
	
	public static void consumidor() {
		Eje1.await();
		System.out.println("Producto: "+producto);
	}
	
	public void exec() {
		new Thread(()-> productor()).start();
		new Thread(()-> consumidor()).start();
	}
	 
	public static void main(String[] args){
		 new Eje2().exec();
	}
}
