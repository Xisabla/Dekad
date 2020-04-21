package dekad.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.*;

public class Settings {

    // File
    String settingsFile;

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

    public Settings(String filename) {
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

    // Write
    public void write(String filename) {

        String data = toString();

        try {
            Files.write(Paths.get(filename), data.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Read
    static public Settings readOrGenerate(String filename, String defaultSettingsFile) {

        File f = new File(filename);

        if(!f.exists()) {
            Settings temp = new Settings(defaultSettingsFile);
            temp.write(filename);
        }

        return new Settings(filename);

    }

    private void read(File fxmlFile) {

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fxmlFile);
            document.getDocumentElement().normalize();

            Element settings = (Element) document.getElementsByTagName("settings").item(0);

            readPlot(settings);
            readFunctions(settings);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Element getLastChildNamed(Element parent, String name) {

        if (parent == null) return null;

        NodeList nodeList = parent.getElementsByTagName(name);

        return nodeList.getLength() > 0 ? (Element) nodeList.item(nodeList.getLength() - 1) : null;

    }

    private void readPlot(Element settings) {

        Element plot = getLastChildNamed(settings, "plot");

        if (plot != null) {

            readPlotXMin(plot);
            readPlotXMax(plot);
            readPlotYMin(plot);
            readPlotYMax(plot);
            readPlotOffset(plot);
            readPlotBounds(plot);

        }

    }

    private void readFunctions(Element settings) {

        Element functions = getLastChildNamed(settings, "functions");

        if (functions != null) {

            readFunctionsArguments(functions);

        }

    }

    private void readPlotXMin(Element plot) {

        Element xmin = getLastChildNamed(plot, "xmin");

        if (xmin != null) plotXMin = parseDouble(xmin.getTextContent());

    }

    private void readPlotXMax(Element plot) {

        Element xmax = getLastChildNamed(plot, "xmax");

        if (xmax != null) plotXMax = parseDouble(xmax.getTextContent());

    }

    private void readPlotYMin(Element plot) {

        Element ymin = getLastChildNamed(plot, "ymin");

        if (ymin != null) plotYMin = parseDouble(ymin.getTextContent());

    }

    private void readPlotYMax(Element plot) {

        Element ymax = getLastChildNamed(plot, "ymax");

        if (ymax != null) plotYMax = parseDouble(ymax.getTextContent());

    }

    private void readPlotOffset(Element plot) {

        Element offset = getLastChildNamed(plot, "offset");

        if (offset != null) {

            readPlotOffsetComputing(offset);
            readPlotOffsetDefault(offset);

        }

    }

    private void readPlotOffsetDefault(Element offset) {

        Element def = getLastChildNamed(offset, "default");

        if (def != null) plotOffsetDefault = parseDouble(def.getTextContent());

    }

    private void readPlotOffsetComputing(Element offset) {

        Element computing = getLastChildNamed(offset, "computing");

        if (computing != null) plotOffsetComputing = parseDouble(computing.getTextContent());

    }

    private void readPlotBounds(Element plot) {

        Element bounds = getLastChildNamed(plot, "bounds");

        if (bounds != null) {

            readPlotBoundsComputed(bounds);
            readPlotBoundsComputefactor(bounds);

        }

    }

    private void readPlotBoundsComputed(Element bounds) {

        Element computed = getLastChildNamed(bounds, "computed");

        if (computed != null) plotBoundsComputed = computed.getTextContent().toLowerCase().equals("true");

    }

    private void readPlotBoundsComputefactor(Element bounds) {

        Element computeFactor = getLastChildNamed(bounds, "computeFactor");

        if (computeFactor != null) plotBoundsComputefactor = parseDouble(computeFactor.getTextContent());

    }

    private void readFunctionsArguments(Element functions) {

        Element arguments = getLastChildNamed(functions, "arguments");

        if (arguments != null) {

            List<String> args = new ArrayList<>();

            for (int i = 0; i < arguments.getChildNodes().getLength(); i++) {
                Node argument = arguments.getChildNodes().item(i);

                if (argument.getNodeName().equals("argument") && !argument.getTextContent().trim().isEmpty()) {
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
    public void setPlotXMin(double plotXMin) {
        this.plotXMin = plotXMin;
    }

    public void setPlotXMax(double plotXMax) {
        this.plotXMax = plotXMax;
    }

    public void setPlotYMin(double plotYMin) {
        this.plotYMin = plotYMin;
    }

    public void setPlotYMax(double plotYMax) {
        this.plotYMax = plotYMax;
    }

    public void setPlotOffsetDefault(double plotOffsetDefault) {
        this.plotOffsetDefault = plotOffsetDefault;
    }

    public void setPlotOffsetComputing(double plotOffsetComputing) {
        this.plotOffsetComputing = plotOffsetComputing;
    }

    public void setPlotBoundsComputed(boolean plotBoundsComputed) {
        this.plotBoundsComputed = plotBoundsComputed;
    }

    public void setPlotBoundsComputefactor(double plotBoundsComputefactor) {
        this.plotBoundsComputefactor = plotBoundsComputefactor;
    }

    public void setFunctionsArguments(List<String> functionsArguments) {
        this.functionsArguments = functionsArguments;
    }

    public void addFunctionsArgument(String arg) {

        functionsArguments.add(arg);

    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
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

        for (String arg : functionsArguments) r.append("\t\t\t<argument>").append(arg).append("</argument>\n");

        r.append("\t\t</arguments>\n" + "\t</functions>\n" + "</settings>\n");

        return r.toString();
    }
}
