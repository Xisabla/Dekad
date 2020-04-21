package dekad.models;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class MathFunction {

    // TODO: Add a color field

    private Expression expression;

    private String arg;

    public MathFunction(final String functionText) {

        this.arg = "x";

        if (functionText.contains("t")) arg = "t";

        final Argument argument = new Argument(arg);
        this.expression = new Expression(functionText, argument);

    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(final String arg) {
        this.arg = arg;
    }

    public Double eval(final Double value) {

        expression.setArgumentValue(arg, value);

        return expression.calculate();

    }

    public boolean isValid() {

        return expression.checkLexSyntax() && expression.checkSyntax();

    }

}
