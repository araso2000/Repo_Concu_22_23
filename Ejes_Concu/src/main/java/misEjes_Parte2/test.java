package misEjes_Parte2;

public class test {

	public static void main(String[] args) {
		
		Thread t = new Thread(()->
			System.out.println("Soy un hilo"));
		
		t.start();
		
	}

}
