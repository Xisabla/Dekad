package dekad.models;

import dekad.controllers.App;
import dekad.core.DekadApp;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class MathFunction {

    private String name;

    private String expression;

    private Function function;

    private String arg;

    public MathFunction(DekadApp app, final String expression, final int id) {

        this.name = "f" + id;
        this.expression = expression;
        this.arg = app.settings().getFunctionsArguments().get(0);

        for(String possibleArg : app.settings().getFunctionsArguments()) {

            if(expression.contains(possibleArg)) {
                this.arg = possibleArg;
            }

        }

        this.function = new Function(name, expression, arg);

    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public String getArg() {
        return arg;
    }

    public Function getFunction() {

        return function;

    }

    public void setArg(final String arg) {


        this.arg = arg;
        function.removeAllArguments();
        function.addArguments(new Argument(arg));

    }

    public Double eval(final Double value) {

        return function.calculate(value);
    }

    public boolean isValid() {

        return function.checkSyntax();

    }

}
