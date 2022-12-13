package Calculator;

/**
 * Implementación del operador resta.
 */
public class SubtractOp implements Operator {
    @Override
    public int operation(int a, int b) {
        return(a-b);
    }
}
