package estudiar_Parte2.Tema5_5_Eje_6;

public class Main {
	
	static final int N_FIJOS = 2;
	private final int N_NORMAL = 2;
	
	static volatile DataSourceManager manager = new DataSourceManager();

	public void procesoFijo(){
		int indice = (int)(Math.random()*2);
		System.out.println(Thread.currentThread().getName() + "_Quiero el DS: " + indice);
		manager.accessDataSource(indice,Thread.currentThread().getName());
		System.out.println(Thread.currentThread().getName() + "_Uso el DS: " + indice);
		System.out.println(Thread.currentThread().getName() + "_Libero el DS: " + indice);
		manager.freeDataSource(indice,Thread.currentThread().getName());
	}
	
	public void procesoNormal() {
		int indice = manager.accessAnyDataSource(Thread.currentThread().getName());
		System.out.println(Thread.currentThread().getName() + "_Uso el DS: " + indice);
		System.out.println(Thread.currentThread().getName() + "_Libero el DS: " + indice);
		manager.freeDataSource(indice,Thread.currentThread().getName());
		System.out.println(Thread.currentThread().getName() + "_Libero el DS: " + indice);
	}
	
	public void exec() throws InterruptedException {
		for(int ii = 0; ii < N_FIJOS; ii++) {
			new Thread(() -> procesoFijo(), "  fijo_" + ii).start();
		}
		
		for(int ii = 0; ii < N_NORMAL; ii++) {
			new Thread(() -> procesoNormal(), "  normal_" + ii).start();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Main().exec();
	}

}
