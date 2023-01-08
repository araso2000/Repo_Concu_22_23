package estudiar_Parte2.Tema_5_3;

public class Eje2 {
	
	private String[] frases = {"La vida es bella...","O no...","Los pajaritos cantan...","Y molestan..."};
	
	public void mensajes() {
		for(int ii=0;ii<4;ii++) {
			System.out.println(frases[ii]);
			
			if(Thread.interrupted()) {
				System.out.println("Se acabó!");
				break;
			}
			
			try {
				Thread.sleep(2000);
			}catch(InterruptedException e) {
				System.out.println("Se acabó!");
				break;
			}
		}
	}
	
	public void exec() throws InterruptedException {
		Thread mensajero = new Thread(() -> mensajes(), "hilo_mensajero");
		mensajero.start();
		
		for(int ii=0;ii<5;ii++) {
			Thread.sleep(1000);
			System.out.println("Todavia esperando...");
		}
		
		System.out.println("Cansado de esperar!");
		mensajero.interrupt();
		mensajero.join();
		System.out.println("Por fin!");
	}

	public static void main(String[] args) throws InterruptedException {
		new Eje2().exec();
	}

}
