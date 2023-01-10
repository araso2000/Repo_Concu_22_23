package estudiar_Parte2.Tema_7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Eje1 {
	
	static final int N = 500;
	
	double[] data = new double[N];

	public class SumArrayTask extends RecursiveTask<Integer> {

		private int start;
		private int end;

		public SumArrayTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int length = end - start;
			if (length < 5) {
				return computeDirectly();
			} else {
				return computeInSubtasks();
			}
		}

		private Integer computeInSubtasks() {
			int mid = (end - start) / 2;

			SumArrayTask left = new SumArrayTask(start, start + mid);
			left.fork();

			SumArrayTask right = new SumArrayTask(start + mid, end);
			right.fork();

			return right.join() + left.join();
		}

		private Integer computeDirectly() {
			int sum = 0;
			for (int i = start; i < end; i++) {
				sum += data[i];
			}
			return sum;
		}
	}

	public double parallelForSum(int size) throws InterruptedException {

		// submit the task to the pool
		ForkJoinPool pool = new ForkJoinPool(4);
		SumArrayTask task = new SumArrayTask(0, size);
		return pool.invoke(task);
	}

	public void exec() throws InterruptedException {
		for(int ii=0;ii<N;ii++) {
			data[ii] = (double)((Math.random()*9999));
		}
		
		double sum = parallelForSum(data.length);
		System.out.println("Suma: " + sum);
	}

	public static void main(String[] args) throws InterruptedException {
		new Eje1().exec();
	}

}
