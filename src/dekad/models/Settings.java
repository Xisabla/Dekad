package dekad.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.*;

@SuppressWarnings("PMD.GodClass")
/**
 * GodClass that stores all the settings value and allow to export/import/generate a settings file
 */
public class Settings {

    /**
     * Not used yet. The path to the current settings file
     */
    private transient String settingsFile;

    /**
     * All under are the graph default bounds values
     */
    private double plotXMin;
    private double plotXMax;
    private double plotYMin;
    private double plotYMax;

    /**
     * Default offset value (in the x axis) between 2 points of graph
     */
    private double plotOffsetDefault;
    /**
     * Offset value used while computing graph to reduce lag (zoom/unzoom/drag)
     */
    private double plotOffsetComputing;

    /**
     * If the on true, then the Y bounds will be automatically computed
     */
    private boolean plotBoundsComputed;
    /**
     * This value allows to bigger or lower the computed grounds by a given factor
     */
    private double plotBoundsComputefactor;

    /**
     * List of all available arguments for a function
     * e.g: functionsArguments = [ "x", "t"],
     *  allowed: 2*x + 2,   e^t,   sin(x)
     *  not allowed:  3 + u,    cos(k)
     */
    private List<String> functionsArguments;

    /**
     * Generate Settings class with defautl settings
     */
    public Settings() {
        setDefault();
    }

    /**
     * Read Settings from a settings file (xml format)
     * @param filename Path to the settings fiel
     */
    public Settings(final String filename) {
        this.settingsFile = filename;

        setDefault();
        read(new File(filename));
    }

    /**
     * Restore Settings value to default
     */
    public void setDefault() {
        plotXMin = -10;
        plotXMax = 10;
        plotYMin = -10;
        plotYMax = 10;
        plotOffsetDefault = 0.01;
        plotOffsetComputing = 0.4;
        plotBoundsComputed = false;
        plotBoundsComputefactor = 1.1;

        functionsArguments = new ArrayList<>();
        functionsArguments.add("x");
        functionsArguments.add("t");
    }

    /**
     * Check if a given string, once trimmed is empty
     * @param str The string to trim and to test
     * @return true if the trimmed string is empty
     */
    private boolean checkTrimEmpty(final String str) {

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Write the Settings to a settings file
     * @param filename The path to the setting file to write
     */
    public void write(final String filename) {

        final String data = toString();

        try {
            Files.write(Paths.get(filename), data.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Read all values from a settings file
     * If the file doesn't exists, generate it with the default values
     * If any value is missing, it will be replace into the Settings object by the default value
     * @param filename The settings file path
     * @return The Settings object loaded with the values
     */
    static public Settings readOrGenerate(final String filename) {

        final File f = new File(filename);

        // If the file doesn't exists, generate it from a "blank" Settings object (default values)
        if (!f.exists()) {
            final Settings temp = new Settings();
            temp.write(filename);
        }

        return new Settings(filename);

    }

    /**
     * Import all the values from a xml settings file
     * @param xmlFile The xml settings file
     */
    private void read(final File xmlFile) {

        try {

            // Use a DocumentBuilder to parse the file into a Document Object
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();

            // Get main "settings" element (first one if several)
            final Element settings = (Element) document.getElementsByTagName("settings").item(0);

            // Read the plot settings
            readPlot(settings);
            // Read the functions settings
            readFunctions(settings);

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find the last child of an element by it's name
     * @param parent The "parent" element in which you are looking for the child
     * @param name The child element name
     * @return The last of all found elements or null if the parent is null or the child is not found
     */
    private Element getLastChildNamed(final Element parent, final String name) {

        if (parent == null) return null;

        final NodeList nodeList = parent.getElementsByTagName(name);

        return nodeList.getLength() > 0 ? (Element) nodeList.item(nodeList.getLength() - 1) : null;

    }

    /**
     * Read all the plot settings values from the settings element
     * @param settings The settings element
     */
    private void readPlot(final Element settings) {

        final Element plot = getLastChildNamed(settings, "plot");

        if (plot != null) {

            readPlotXMin(plot);
            readPlotXMax(plot);
            readPlotYMin(plot);
            readPlotYMax(plot);
            readPlotOffset(plot);
            readPlotBounds(plot);

        }

    }

    /**
     * Read all the functions values from the settings element
     * @param settings The settings element
     */
    private void readFunctions(final Element settings) {

        final Element functions = getLastChildNamed(settings, "functions");

        if (functions != null) {

            readFunctionsArguments(functions);

        }

    }

    /**
     * Read the settings > plot > xmin value from the plot element
     * @param plot The plot element
     */
    private void readPlotXMin(final Element plot) {

        final Element xmin = getLastChildNamed(plot, "xmin");

        if (xmin != null) plotXMin = parseDouble(xmin.getTextContent());

    }

    /**
     * Read the settings > plot > xmax value from the plot element
     * @param plot The plot element
     */
    private void readPlotXMax(final Element plot) {

        final Element xmax = getLastChildNamed(plot, "xmax");

        if (xmax != null) plotXMax = parseDouble(xmax.getTextContent());

    }

    /**
     * Read the settings > plot > ymin value from the plot element
     * @param plot The plot element
     */
    private void readPlotYMin(final Element plot) {

        final Element ymin = getLastChildNamed(plot, "ymin");

        if (ymin != null) plotYMin = parseDouble(ymin.getTextContent());

    }

    /**
     * Read the settings > plot > ymax value from the plot element
     * @param plot The plot element
     */
    private void readPlotYMax(final Element plot) {

        final Element ymax = getLastChildNamed(plot, "ymax");

        if (ymax != null) plotYMax = parseDouble(ymax.getTextContent());

    }

    /**
     * Read the plot offset data from the plot element
     * @param plot The plot element
     */
    private void readPlotOffset(final Element plot) {

        final Element offset = getLastChildNamed(plot, "offset");

        if (offset != null) {

            readPlotOffsetComputing(offset);
            readPlotOffsetDefault(offset);

        }

    }

    /**
     * Read the settings > plot > offset > default value from the offset element
     * @param offset The offset element
     */
    private void readPlotOffsetDefault(final Element offset) {

        final Element def = getLastChildNamed(offset, "default");

        if (def != null) plotOffsetDefault = parseDouble(def.getTextContent());

    }

    /**
     * Read the settings > plot > offset > computing value from the offset element
     * @param offset The offset element
     */
    private void readPlotOffsetComputing(final Element offset) {

        final Element computing = getLastChildNamed(offset, "computing");

        if (computing != null) plotOffsetComputing = parseDouble(computing.getTextContent());

    }

    /**
     * Read the plot bounds data from the plot element
     * @param plot The plot element
     */
    private void readPlotBounds(final Element plot) {

        final Element bounds = getLastChildNamed(plot, "bounds");

        if (bounds != null) {

            readPlotBoundsComputed(bounds);
            readPlotBoundsComputefactor(bounds);

        }

    }

    /**
     * Read the settings > plot > bounds > computed value from the bounds element
     * @param bounds The bounds element
     */
    private void readPlotBoundsComputed(final Element bounds) {

        final Element computed = getLastChildNamed(bounds, "computed");

        if (computed != null)
            plotBoundsComputed = computed.getTextContent().toLowerCase(Locale.getDefault()).equals("true");

    }

    /**
     * Read the settings > plot > bounds > computeFactor value from the bounds element
     * @param bounds The bounds element
     */
    private void readPlotBoundsComputefactor(final Element bounds) {

        final Element computeFactor = getLastChildNamed(bounds, "computeFactor");

        if (computeFactor != null) plotBoundsComputefactor = parseDouble(computeFactor.getTextContent());

    }

    /**
     * Read the settings > functions > arguments values from the functions element
     * @param functions
     */
    private void readFunctionsArguments(final Element functions) {

        final Element arguments = getLastChildNamed(functions, "arguments");

        if (arguments != null) {

            final List<String> args = new ArrayList<>();

            for (int i = 0; i < arguments.getChildNodes().getLength(); i++) {
                final Node argument = arguments.getChildNodes().item(i);

                if (argument.getNodeName().equals("argument") && !checkTrimEmpty(argument.getTextContent())) {
                    args.add(argument.getTextContent());
                }
            }

            functionsArguments = args;

        }

    }

    /*************** Getters *****************/

    public double getPlotXMin() {
        return plotXMin;
    }

    public double getPlotXMax() {
        return plotXMax;
    }

    public double getPlotYMin() {
        return plotYMin;
    }

    public double getPlotYMax() {
        return plotYMax;
    }

    public double getPlotOffsetDefault() {
        return plotOffsetDefault;
    }

    public double getPlotOffsetComputing() {
        return plotOffsetComputing;
    }

    public boolean isPlotBoundsComputed() {
        return plotBoundsComputed;
    }

    public double getPlotBoundsComputefactor() {
        return plotBoundsComputefactor;
    }

    public List<String> getFunctionsArguments() {
        return functionsArguments;
    }

    /*************** Setters *****************/

    public void setPlotXMin(final double plotXMin) {
        this.plotXMin = plotXMin;
    }

    public void setPlotXMax(final double plotXMax) {
        this.plotXMax = plotXMax;
    }

    public void setPlotYMin(final double plotYMin) {
        this.plotYMin = plotYMin;
    }

    public void setPlotYMax(final double plotYMax) {
        this.plotYMax = plotYMax;
    }

    public void setPlotOffsetDefault(final double plotOffsetDefault) {
        this.plotOffsetDefault = plotOffsetDefault;
    }

    public void setPlotOffsetComputing(final double plotOffsetComputing) {
        this.plotOffsetComputing = plotOffsetComputing;
    }

    public void setPlotBoundsComputed(final boolean plotBoundsComputed) {
        this.plotBoundsComputed = plotBoundsComputed;
    }

    public void setPlotBoundsComputefactor(final double plotBoundsComputefactor) {
        this.plotBoundsComputefactor = plotBoundsComputefactor;
    }

    public void setFunctionsArguments(final List<String> functionsArguments) {
        this.functionsArguments = functionsArguments;
    }

    /**
     * Add a function argument into the functionsArgument list
     * This allows the functions in the app to have the given argument
     * @param arg The argument
     */
    public void addFunctionsArgument(final String arg) {

        functionsArguments.add(arg);

    }

    /**
     * @return The xml exported Settings value
     */
    @Override
    public String toString() {
        final @SuppressWarnings("PMD.ConsecutiveLiteralAppends")
        StringBuilder stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"//NOPMD
                + "<settings>\n"
                + "\t<plot>\n"
                + "\t\t<xmin>" + plotXMin + "</xmin>\n"
                + "\t\t<xmax>" + plotXMax + "</xmax>\n"
                + "\t\t<ymin>" + plotYMin + "</ymin>\n"
                + "\t\t<ymax>" + plotYMax + "</ymax>\n"
                + "\t</plot>\n"
                + "\t<offset>\n"
                + "\t\t<default>" + plotOffsetDefault + "</default>\n"
                + "\t\t<computing>" + plotOffsetComputing + "</computing>\n"
                + "\t</offset>\n"
                + "\t<bounds>\n"
                + "\t\t<computed>" + plotBoundsComputed + "</computed>\n"
                + "\t\t<computeFactor>" + plotBoundsComputefactor + "</computeFactor>\n"
                + "\t</bounds>\n"
                + "\t<functions>\n"
                + "\t\t<arguments>\n");

        for (final String arg : functionsArguments) {
            stringBuilder.append("\t\t\t<argument>")
                    .append(arg)
                    .append("</argument>\n");
        }

        stringBuilder.append("\t\t</arguments>\n" + "\t</functions>\n" + "</settings>\n");

        return stringBuilder.toString();
    }
}
