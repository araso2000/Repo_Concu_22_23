package misEjes_Parte2;

public class Eje3 {
	
	private volatile boolean signal = false;

	public void signal() {
		signal = true;		
	}

	public void await() {
		while (!signal);
	}
}
