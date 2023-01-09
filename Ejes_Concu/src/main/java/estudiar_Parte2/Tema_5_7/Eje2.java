package estudiar_Parte2.Tema_5_7;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Eje2 {
	
	ExecutorService executorService;
	
	static final int N_TAREAS = 3000;
	
	private ConcurrentMap<String, String> duplicates = new ConcurrentHashMap<>();

	public void findDuplicates(File root) {
		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
					findDuplicates(file);
				} else {

					String path = duplicates.putIfAbsent(file.getName(), file.getAbsolutePath());

					if (path != null) {
						System.out.println("Found duplicate file: " + file.getName());
						System.out.println("  " + path);
						System.out.println("  " + file.getAbsolutePath());
					}
				}
			}
		}
	}
	
	public void exec() {
		executorService = Executors.newFixedThreadPool(N_TAREAS);
		
		for(int ii=0; ii<N_TAREAS; ii++) {
			executorService.submit(() -> findDuplicates(new File("/Users/alvaroraso")));
		}
		
		executorService.shutdown();
	}

	public static void main(String[] args) {
		new Eje2().exec();
	}

}
