package estudiar_Parte2.Tema_5_6;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Eje4 {

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
		new Thread(() -> findDuplicates(new File("/Users/alvaroraso"))).start();
		new Thread(() -> findDuplicates(new File("/Users/alvaroraso"))).start();
	}
	
	public static void main(String[] args) {
		new Eje4().exec();
	}
}