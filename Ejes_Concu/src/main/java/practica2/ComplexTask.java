package practica2;

/**
 * Clase que representa una tarea complea a resolver por Universtrum
 */
public class ComplexTask {

    private final String taskId;
    private final int expectedSolution;
    private final long computationCostInMillis;

    public ComplexTask(String taskId, int expectedSolution, long costInMillis) {
        this.taskId = taskId;
        this.expectedSolution = expectedSolution;
        this.computationCostInMillis = costInMillis;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getExpectedSolution() {
		return expectedSolution;
	}

	public long getComputationCostInMillis() {
		return computationCostInMillis;
	}

	//DONE
	public int solve() {
        sleep(computationCostInMillis);
    	return expectedSolution;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public String toString() {
        return "ComplexTask{" +
                "taskId='" + taskId + '\'' +
                ", expectedSolution=" + expectedSolution +
                ", computationCostInMillis=" + computationCostInMillis +
                '}';
    }
}
