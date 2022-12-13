package Calculator;

/**
 * Implementación del operador suma.
 */
public class SumOp implements Operator {
    @Override
    public int operation(int a, int b) {
        return(a+b);
    }
}
