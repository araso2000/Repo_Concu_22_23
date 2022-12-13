package Calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Controlador de la calculadora; clase que implementa
 * el método main; lee desde teclado dos numeros y una
 * operación y aplica el operador sobre los dos números
 * en tiempo de ejecución. Los operados se implementan
 * utilizando las expresiones lambda.
 */
public class LambdaCalculator {

    public static void main(String args[]){
        try {
            //Lectura de valores y operador desde teclado.
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            System.out.println("Inserta número 1:");
            String num1 = br.readLine();
            System.out.println("Inserta número 2:");
            String num2 = br.readLine();
            System.out.println("Operador (+,-)");
            String op = br.readLine();
            Operator operator=null;
            //Implementación de los operadores utilizando las expresiones lambda.
            switch (op){
                case "+": {
                    operator = (a, b) -> (a + b);
                    break;
                }
                case "-": {
                    operator = (a,b) ->(a-b);
                    break;
                }
            }

            System.out.println("Resultado:"+operator.operation(Integer.parseInt(num1), Integer.parseInt(num2)));
        }catch (Exception e){
            System.out.println("Excepcion lanzada, error:"+e.getMessage());
        }
    }
}
