package dekad.models;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class MathFunction {

    private Expression expression;

    private String arg;

    public MathFunction(String functionText) {

        this.arg = "x";

        if (functionText.contains("t")) arg = "t";

        Argument argument = new Argument(arg);
        this.expression = new Expression(functionText, argument);

    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public Double eval(Double value) {

        expression.setArgumentValue(arg, value);

        return expression.calculate();

    }

    public boolean isValid() {

        return expression.checkLexSyntax() && expression.checkSyntax();

    }

}
