package dekad.models;

import dekad.core.DekadApp;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;

/**
 * Used to be an kind of Interface Class of Expression from mXparser
 * Now a modified Function (from mXparser) that stores the functions, computes values, find the argument and checks the syntax
 */
public class MathFunction {

    /**
     * The name of the function, to be recognized by other functions
     */
    private final String name;

    /**
     * The current string expression of the function
     */
    private final String expression;

    /**
     * mXparser Function Object
     */
    private final Function function;

    /**
     * Detected argument String
     * All available listed in Settings.functionsArguments
     */
    private transient String arg;

    /**
     * Instantiate a new MathFunction from it's expression and the Function id
     * @param app The DekadApp app instance, used to access to the settings
     * @param expression The String expression of the function
     * @param id The id of the container Dekad.Function object
     */
    public MathFunction(final DekadApp app, final String expression, final int id) {

        this.name = "f" + id;
        this.expression = expression;
        this.arg = app.settings().getFunctionsArguments().get(0);

        // Find the argument
        for(final String possibleArg : app.settings().getFunctionsArguments()) {

            if(expression.contains(possibleArg)) {
                this.arg = possibleArg;
            }

        }

        this.function = new Function(name, expression, arg);

    }

    /**
     * @return The name of the Function
     */
    public String getName() {
        return name;
    }

    /**
     * @return The expression of the function
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @return The argument of the function
     */
    public String getArg() {
        return arg;
    }

    /**
     * @return The mXparser.Function object related to the Function
     */
    public Function getMxFunction() {

        return function;

    }

    /**
     * Clears all the args and add the given one, use it to change the parameter of the function
     * e.g: f(x) becomes f(t) after f.setArg("t")
     * @param arg The argument string
     */
    public void setArg(final String arg) {


        this.arg = arg;
        function.removeAllArguments();
        function.addArguments(new Argument(arg));

    }

    /**
     * Evaluate the Y value from a given X value
     * @param value X value
     * @return the computed Y value
     */
    public Double eval(final Double value) {

        return function.calculate(value);
    }

    /**
     * Checks for the mXparser syntax
     * @return true if the syntax is valid (see: org.mariuszgromada.math.mxparser.Function.checkSyntax())
     */
    public boolean isValid() {

        return function.checkSyntax();

    }

}
