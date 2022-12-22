package misEjes_Parte2;

public class Eje2_5_3_2 {
		
	public void mensajes() {
		
		int indice = 0;
		String[] mensajes = new String[]{"La vida es bella","O no...","Los pajaritos cantan","Y molestan..."};
		
		while(true) {
			System.out.println(Thread.currentThread().getName() + ": " + mensajes[indice]);
			
			if(indice == mensajes.length-1) {
				indice=0;
			}else {
				indice++;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName() + ": Se acabó!");
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void exec() throws InterruptedException {
		Thread hiloMensajes = new Thread(() -> mensajes());
		hiloMensajes.start();
		
		long inicio = System.currentTimeMillis();
		
		while(hiloMensajes.isAlive()) {
			System.out.println(Thread.currentThread().getName() + ": Todavía esperando...");
			
			hiloMensajes.join(1000);
			
			long fin = System.currentTimeMillis() - inicio;
			
			if(fin >= 5000 && hiloMensajes.isAlive()) {
				System.out.println(Thread.currentThread().getName() + ": Me he cansado de esperar...");
				
				hiloMensajes.interrupt();
				
				hiloMensajes.join();
			}
		}
		
		System.out.println(Thread.currentThread().getName() + ": Por fin terminamos...");
	}

	public static void main(String[] args) throws InterruptedException {
		new Eje2_5_3_2().exec();
	}
}