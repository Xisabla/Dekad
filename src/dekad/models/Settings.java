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
public class Settings {

    // File
    private transient String settingsFile;

    // plot
    private double plotXMin;
    private double plotXMax;
    private double plotYMin;
    private double plotYMax;

    // plot > offset
    private double plotOffsetDefault;
    private double plotOffsetComputing;

    // plot > bounds
    private boolean plotBoundsComputed;
    private double plotBoundsComputefactor;

    // functions
    private List<String> functionsArguments;

    public Settings() {
        setDefault();
    }

    public Settings(final String filename) {
        this.settingsFile = filename;

        setDefault();
        read(new File(filename));
    }

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

    private boolean checkTrimEmpty(final String str) {

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // Write
    public void write(final String filename) {

        final String data = toString();

        try {
            Files.write(Paths.get(filename), data.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Read
    static public Settings readOrGenerate(final String filename) {

        final File f = new File(filename);

        if (!f.exists()) {
            final Settings temp = new Settings();
            temp.write(filename);
        }

        return new Settings(filename);

    }

    private void read(final File fxmlFile) {

        try {

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final Document document = documentBuilder.parse(fxmlFile);
            document.getDocumentElement().normalize();

            final Element settings = (Element) document.getElementsByTagName("settings").item(0);

            readPlot(settings);
            readFunctions(settings);

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Element getLastChildNamed(final Element parent, final String name) {

        if (parent == null) return null;

        final NodeList nodeList = parent.getElementsByTagName(name);

        return nodeList.getLength() > 0 ? (Element) nodeList.item(nodeList.getLength() - 1) : null;

    }

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

    private void readFunctions(final Element settings) {

        final Element functions = getLastChildNamed(settings, "functions");

        if (functions != null) {

            readFunctionsArguments(functions);

        }

    }

    private void readPlotXMin(final Element plot) {

        final Element xmin = getLastChildNamed(plot, "xmin");

        if (xmin != null) plotXMin = parseDouble(xmin.getTextContent());

    }

    private void readPlotXMax(final Element plot) {

        final Element xmax = getLastChildNamed(plot, "xmax");

        if (xmax != null) plotXMax = parseDouble(xmax.getTextContent());

    }

    private void readPlotYMin(final Element plot) {

        final Element ymin = getLastChildNamed(plot, "ymin");

        if (ymin != null) plotYMin = parseDouble(ymin.getTextContent());

    }

    private void readPlotYMax(final Element plot) {

        final Element ymax = getLastChildNamed(plot, "ymax");

        if (ymax != null) plotYMax = parseDouble(ymax.getTextContent());

    }

    private void readPlotOffset(final Element plot) {

        final Element offset = getLastChildNamed(plot, "offset");

        if (offset != null) {

            readPlotOffsetComputing(offset);
            readPlotOffsetDefault(offset);

        }

    }

    private void readPlotOffsetDefault(final Element offset) {

        final Element def = getLastChildNamed(offset, "default");

        if (def != null) plotOffsetDefault = parseDouble(def.getTextContent());

    }

    private void readPlotOffsetComputing(final Element offset) {

        final Element computing = getLastChildNamed(offset, "computing");

        if (computing != null) plotOffsetComputing = parseDouble(computing.getTextContent());

    }

    private void readPlotBounds(final Element plot) {

        final Element bounds = getLastChildNamed(plot, "bounds");

        if (bounds != null) {

            readPlotBoundsComputed(bounds);
            readPlotBoundsComputefactor(bounds);

        }

    }

    private void readPlotBoundsComputed(final Element bounds) {

        final Element computed = getLastChildNamed(bounds, "computed");

        if (computed != null)
            plotBoundsComputed = computed.getTextContent().toLowerCase(Locale.getDefault()).equals("true");

    }

    private void readPlotBoundsComputefactor(final Element bounds) {

        final Element computeFactor = getLastChildNamed(bounds, "computeFactor");

        if (computeFactor != null) plotBoundsComputefactor = parseDouble(computeFactor.getTextContent());

    }

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

    // Getters
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

    // Setters
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

    public void addFunctionsArgument(final String arg) {

        functionsArguments.add(arg);

    }

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
