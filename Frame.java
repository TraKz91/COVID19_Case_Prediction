import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Frame extends JFrame {
    /*Instance variable*/
    private static Frame single_instance = null;

    public ArrayList<String[]> casesDeath = new ArrayList<String[]>();
    public ArrayList<String[]> casesTotal = new ArrayList<String[]>();

    public List<Integer> intCasesTotal = new ArrayList<>(); // two lists to store int values of new daily cases for testing graph
    public List<Integer> intCasesDeath = new ArrayList<>();

    public List<Integer> intSumCasesTotal = new ArrayList<>(); // two lists to store int values of total daily cases
    public List<Integer> intSumCasesDeath = new ArrayList<>();

    public JFrame frame;
    public JPanel graphPanel, btnPanel, mainPanel;
    public JButton buttonDeath, buttonCases, buttonStats, pdfButton, btnTotalDeaths, btnTotalCases, buttonData;
    public JTextArea textArea;

    public JPanel myPanel;
    public JOptionPane popOut;
    public JTextField xField;
    public JTextField yField;

    boolean provided = false;
    boolean dataProvided;

    public Graph graph = new Graph();
    public Map<Date, String> casesGraphData;
    public Map<Date, String> deathsGraphData;
    public Predictor predictor;

    private Frame() {
        frame = new JFrame();
        btnPanel = new JPanel();
        graphPanel = new JPanel();
        popOut = new JOptionPane();

        myPanel = new JPanel();
        xField = new JTextField(10);
        yField = new JTextField(10);
        myPanel.add(new JLabel("Death data:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Cases data:"));
        myPanel.add(yField);

        casesGraphData = new LinkedHashMap<>();
        deathsGraphData = new LinkedHashMap<>();
        predictor = new Predictor();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("");
        frame.setSize(1000, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        textArea = new JTextArea(30, 30);
        textArea.setEditable(false);
        Color c = new Color(255, 255, 255, 20);
        textArea.setBackground(c);

        buttonData = new JButton("Upload data for analysis");
        buttonData.addActionListener(new ButtonHandler(this));
        btnPanel.add(buttonData);

        buttonDeath = new JButton("Weekly deaths");
        buttonDeath.addActionListener(new ButtonHandler(this));
        btnPanel.add(buttonDeath);

        buttonCases = new JButton("Weekly cases");
        buttonCases.addActionListener(new ButtonHandler(this));
        btnPanel.add(buttonCases);

        btnTotalDeaths = new JButton("Total deaths");
        btnTotalDeaths.addActionListener(new ButtonHandler(this));
        btnPanel.add(btnTotalDeaths);

        btnTotalCases = new JButton("Total cases");
        btnTotalCases.addActionListener(new ButtonHandler(this));
        btnPanel.add(btnTotalCases);

        buttonStats = new JButton("Statistics");
        buttonStats.addActionListener(new ButtonHandler(this));
        btnPanel.add(buttonStats);

        pdfButton = new JButton("PDF Button");
        pdfButton.addActionListener(new ButtonHandler(this));
        btnPanel.add(pdfButton);

        initialise();

    }

    public void initialise() {
        if (mainPanel != null) {
            frame.remove(mainPanel);
            mainPanel = null;
        }
        mainPanel = new JPanel();

        mainPanel.add(btnPanel, BorderLayout.NORTH);
        mainPanel.add(graphPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);

    }

    /*A method called getScreenshot is used to get a screenshot of the graph*/
    /*Input is component and a file name*/
    /*Output is a void method that creates the document*/
    void getScreenShot(Component component, String filename) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());
        try {
            ImageIO.
                    write(image, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There is an error creating the image for the report");
        }
    }

    void readCasesAndDeaths(String filenameD, String filenameT) {
        try {
            Scanner input1 = new Scanner(new File(filenameD));
            String throwawayD = input1.nextLine();
            while (input1.hasNextLine()) {
                String line = input1.nextLine();
                String[] splitLine = line.split(",\\s*");
                String[] current = {splitLine[3], splitLine[4], splitLine[5]}; //creates a new array using the pieces of splitLine used in the old code
                deathsGraphData.put(new Date(splitLine[3]), splitLine[4]);
                casesDeath.add(current);
            }
            Collections.reverse(casesDeath);
            TreeMap<Date, String> tempMap1 = new TreeMap<>(deathsGraphData);
            deathsGraphData.clear();
            deathsGraphData.putAll(tempMap1.descendingMap());
            input1.close();

            // adding int values to int list of daily new death cases
            int d = 0;
            while (d < casesDeath.size() - 1) {
                intCasesDeath.add(Integer.parseInt(casesDeath.get(d)[1]));
                intSumCasesDeath.add(Integer.parseInt(casesDeath.get(d)[2]));
                d++;
            }

            Scanner input2 = new Scanner(new File(filenameT));
            String throwawayC = input2.nextLine();
            while (input2.hasNextLine()) {
                String line = input2.nextLine();
                String[] splitLine = line.split(",\\s*");
                String[] current = {splitLine[3], splitLine[4], splitLine[5]};//also creates a new array using the pieces of splitLine used in the old code
                casesGraphData.put(new Date(splitLine[3]), splitLine[4]);
                casesTotal.add(current);
            }
            Collections.reverse(casesTotal);
            TreeMap<Date, String> tempMap2 = new TreeMap<>(casesGraphData);
            casesGraphData.clear();
            casesGraphData.putAll(tempMap2.descendingMap());
            input2.close();

            // adding int values to int list of daily new cases
            int t = 0;
            while (t < casesTotal.size() - 1) {
                intCasesTotal.add(Integer.parseInt(casesTotal.get(t)[1]));
                intSumCasesTotal.add(Integer.parseInt(casesTotal.get(t)[2]));
                t++;
            }
            provided = true;

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            provided = false;
        }
    }

    int getDailyCaseByIndex(int index) {
        String[] data = casesTotal.get(index);
        System.out.println(data[1]);
        return Integer.parseInt(data[1]);
    }

    void clear() {
        casesDeath.clear();
        casesTotal.clear();

        intCasesTotal.clear();
        intCasesDeath.clear();

        casesGraphData.clear();
        deathsGraphData.clear();

        intSumCasesTotal.clear();
        intSumCasesDeath.clear();
    }

    String getStatisticsCases() {
        String firstDetected = "";
        String lastDetected = "";
        int totalCases = 0;
        int totalDays = 0;
        for (int i = 0; i < casesTotal.size() - 1; i++) {
            totalDays += 1;
            String[] total = casesTotal.get(i);
            lastDetected = total[0];

            if (Integer.parseInt(total[2]) > totalCases)
                totalCases = Integer.parseInt(total[2]);

            if (firstDetected.isEmpty())
                firstDetected = total[0];

        }

        int average = totalCases / totalDays;

        return firstDetected + "," + lastDetected + "," + totalCases + "," + totalDays + "," + average;
    }

    String getStatisticsDeath() {
        String firstDetected = "";
        String lastDetected = "";
        int totalDeaths = 0;
        int totalDays = 0;
        for (int i = 0; i < casesDeath.size() - 1; i++) {
            totalDays += 1;
            String[] total = casesDeath.get(i);
            lastDetected = total[0];

            if (Integer.parseInt(total[2]) > totalDeaths)
                totalDeaths = Integer.parseInt(total[2]);
            if (firstDetected.isEmpty())
                firstDetected = total[0];
        }

        int average = totalDeaths / totalDays;

        return firstDetected + "," + lastDetected + "," + totalDeaths + "," + totalDays + "," + average;
    }

    String[] statsDeath() {
        String[] totalStats = getStatisticsCases().split(",\\s*");
        String[] deathStats = getStatisticsDeath().split(",\\s*");

        String[] words = {"\n \n" + "Statistics related to corona deaths: \n\n" + "The first reported death was on " + deathStats[0] + " and as of now the last reported was " + deathStats[1] + ".\n" +
                "Through the pandemic an average of " + deathStats[4] + " people died a day with a total of " + deathStats[2] + " reported corona related deaths." + ".\n" +
                String.format("%.2f", (Double.parseDouble(deathStats[2]) / Double.parseDouble(totalStats[2])) * 100) + "% percentage of people who were infected with corona died. " + "."};

        return words;
    }

    String[] statsTotal() {
        String[] totalStats = getStatisticsCases().split(",\\s*");

        String[] words = {"Statistics related to new corona cases: \n\n " + "The first reported case was on " +
                totalStats[0] +
                " and as of now the last reported was " + totalStats[1] + ".\n" +
                "In total there were " +
                totalStats[3] + " days of corona reported with an average of " +
                totalStats[4] + " new cases a day with the total of " +
                totalStats[2] + " cases" + "."};

        return words;
    }

    /*Creating a Singleton*/
    public static Frame getInstance() {
        if (single_instance == null)
            single_instance = new Frame();

        return single_instance;
    }
}

class ButtonHandler implements ActionListener {
    Frame buttonLink;

    ButtonHandler(Frame x) {
        buttonLink = x;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == buttonLink.buttonData) {
            buttonLink.dataProvided = false;
            //buttonLink.clear(); dont know if this helps
            JOptionPane.showMessageDialog(buttonLink.frame, buttonLink.myPanel, "Mount data", JOptionPane.PLAIN_MESSAGE);
            if (!buttonLink.xField.getText().equals("") && !buttonLink.yField.getText().equals("")) {
                buttonLink.clear();
                buttonLink.readCasesAndDeaths(buttonLink.xField.getText(), buttonLink.yField.getText());
                if (buttonLink.provided) // if readCasesAndDeath successful
                {
                    buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data provided.");
                    buttonLink.predictor.predictCall(7, 7, buttonLink.intCasesDeath, buttonLink.deathsGraphData);
                    buttonLink.predictor.predictCall(7, 7, buttonLink.intCasesTotal, buttonLink.casesGraphData);
                    Collections.reverse(buttonLink.intSumCasesDeath);
                    Collections.reverse(buttonLink.intSumCasesDeath);
                    buttonLink.predictor.predictCall(7,7,buttonLink.intSumCasesTotal,buttonLink.deathsGraphData);
                    buttonLink.predictor.predictCall(7,7,buttonLink.intSumCasesDeath,buttonLink.casesGraphData);
                    buttonLink.dataProvided = true;
                    return;
                }
            } else
                buttonLink.dataProvided = false;
            buttonLink.clear();
            buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data not provided.");
        }
        if (e.getSource() == buttonLink.buttonStats) {
            if (buttonLink.dataProvided) {
                buttonLink.graphPanel.removeAll();

                String[] totalStats = buttonLink.statsTotal();
                String[] deathStats = buttonLink.statsDeath();

                buttonLink.textArea.setText("");
                Font one = buttonLink.textArea.getFont();
                Font two = new Font(one.getFontName(), one.getStyle(), 17);
                buttonLink.textArea.setFont(two);

                for (int i = 0; i < totalStats.length; i++) {
                    buttonLink.textArea.append(totalStats[i]);
                }
                for (int i = 0; i < totalStats.length; i++) {
                    buttonLink.textArea.append(deathStats[i]);
                }
                buttonLink.graphPanel.add(buttonLink.textArea);
                buttonLink.graphPanel.repaint();
                buttonLink.graphPanel.revalidate();
                buttonLink.initialise();
            } else {
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
            }

        }
        /*When the pdfButton is clicked a message displayed to user*/
        if (e.getSource() == buttonLink.pdfButton) {
            if (buttonLink.dataProvided) {
                try {
                    new PDF();
                    JOptionPane.showMessageDialog(null,
                            "A PDF File has been created! ",
                            "PDF", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("PDF has been created!");
                } catch (IOException ioException) {
                    System.out.println("Error saving a PDF FIle");
                }
            } else
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
        }
        if (e.getSource() == buttonLink.buttonCases) {
            if (buttonLink.dataProvided) {
                buttonLink.graph.dataMap = buttonLink.casesGraphData;
                buttonLink.graphPanel.removeAll();
                buttonLink.graph.setSpecification(buttonLink.casesTotal, buttonLink.intCasesTotal, 10, "daily", 20000);
                buttonLink.graphPanel.add(buttonLink.graph);
                buttonLink.initialise();
                buttonLink.getScreenShot(buttonLink.graphPanel, "CasesImage.png");
            } else
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
        }
        if (e.getSource() == buttonLink.buttonDeath) {
            if (buttonLink.dataProvided) {
                buttonLink.graph.dataMap = buttonLink.deathsGraphData;
                buttonLink.graphPanel.removeAll();
                buttonLink.graph.setSpecification(buttonLink.casesDeath, buttonLink.intCasesDeath, 10, "daily", 20000);
                buttonLink.graphPanel.add(buttonLink.graph);
                buttonLink.initialise();
                buttonLink.getScreenShot(buttonLink.graphPanel, "DeathCasesImage.png");
            } else
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
        }
        if (e.getSource() == buttonLink.btnTotalCases) {
            if (buttonLink.dataProvided) {
                buttonLink.graphPanel.removeAll();
                buttonLink.graph.setSpecification(buttonLink.casesTotal, buttonLink.intSumCasesTotal, 10, "line", 1000000);
                buttonLink.graphPanel.add(buttonLink.graph);
                buttonLink.initialise();
                buttonLink.getScreenShot(buttonLink.graphPanel, "PredictedCases.png");
            } else
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
        }
        if (e.getSource() == buttonLink.btnTotalDeaths) {
            if (buttonLink.dataProvided) {
                buttonLink.graphPanel.removeAll();
                buttonLink.graph.setSpecification(buttonLink.casesDeath, buttonLink.intSumCasesDeath, 10, "line", 1000000);
                buttonLink.graphPanel.add(buttonLink.graph);
                buttonLink.initialise();
                buttonLink.getScreenShot(buttonLink.graphPanel, "PredictedDeaths.png");
            } else
                buttonLink.popOut.showMessageDialog(buttonLink.frame, "Data has not yet been provided.");
        }
    }
}

class Main {

    public static void main(String[] args) {
        Frame view = Frame.getInstance();
        Predictor p = new Predictor();
    }
}

