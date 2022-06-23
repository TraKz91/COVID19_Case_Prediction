import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;


public class Graph extends JComponent {
    public int padding = 30;
    public int labelPadding = 30;

    private Color dailyLineColour = new Color(11, 119, 132);
    private Color totalLineColour = new Color(58, 127, 61, 255);
    private Color lineGridColour = new Color(179, 173, 173);
    private Color detailsBoxColour = new Color(255, 255, 255);
    private Color lineColour = new Color(165, 31, 31);


    public List<Integer> cases;
    public List<Point> graphPoints;

    public ArrayList<String[]> array = new ArrayList<>();
    public Map<Date, String> dataMap;

    private int yDiv;
    private int threshold;

    public int index;

    public int xm;
    public int ym;

    private String version;
    private int maximum;

    public ArrayList<Date> dateData;
    public ArrayList<String> caseData;

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public Graph() {
        getPreferredSize();
        addMouseMotionListener(new MouseHandler(this));

    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(950, 500);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (cases.size());
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxValue(cases, maximum) - minValue());

        // create new list of points x and y
        graphPoints = new ArrayList<>();
        for (int i = 0; i < cases.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((maxValue(cases, maximum) - cases.get(i)) * yScale + padding);
            Point point = new Point(x1,y1);
            graphPoints.add(point);
        }

        caseData = new ArrayList<>(dataMap.values());
        dateData = new ArrayList<>(dataMap.keySet());

        // FOR DAILY
        if (version.equals("daily")) {
            // create hatch marks, grid lines and labels for y axis
            for (int i = 0; i <= yDiv; i++) {
                int x = padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / yDiv + padding + labelPadding);
                int y1 = y0;
                if (cases.size() > 0) {
                    g2.setColor(lineGridColour);
                    g2.drawLine(padding + labelPadding + 1, y0, getWidth() - padding, y1);

                    g2.setColor(Color.BLACK);
                    String yLabel = ((int) ((minValue() + (maxValue(cases, maximum) - minValue()) * ((i * 1.0) / yDiv)) * 100)) / 100 + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2.drawString(yLabel, x - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
            }
            for (int i = 0; i < graphPoints.size(); i++) {
                if (graphPoints.size() > 1) {
                    int x = graphPoints.get(i).x;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = graphPoints.get(i).y;
                    if (i < array.size()){
                        g2.setColor(dailyLineColour);
                    } else{
                        g2.setColor(new Color(252, 74, 74));
                    }
                    g2.drawLine(x, y0, x, y1);
                }
            }
        }
        // FOR TOTAL
        if (version.equals("line")) {
            for (int i = 0; i <= yDiv; i++) {
                int x = padding + labelPadding;
                int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / yDiv + padding + labelPadding);
                int y1 = y0;
                g2.setColor(lineGridColour);
                g2.drawLine(padding + labelPadding + 1, y0, getWidth() - padding, y1);

                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((minValue() + (maxValue(cases, maximum) - minValue()) * ((i * 1.0) / yDiv)) * 100)) / 100 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);

            }
            for (int j = 0; j < array.size(); j++){
                if (dateData.get(j).day == 1){
                    g2.setColor(lineGridColour);
                    g2.drawLine(graphPoints.get(j).x, getHeight() - padding - labelPadding, graphPoints.get(j).x, padding);

                    g2.setColor(Color.black);
                    String xLabel = months[dateData.get(j).month - 1] + " " + dateData.get(j).year;
                    FontMetrics metricsx = g2.getFontMetrics();
                    drawRotate(g2, graphPoints.get(j).x, getHeight() - padding - labelPadding + metricsx.getHeight(), xLabel);
                }
            }

            for (int i = 0; i < graphPoints.size(); i++) {
                if (i < array.size()){
                    g2.setColor(totalLineColour);
                } else{
                    g2.setColor(new Color(252, 74, 74));
                }
                g2.drawLine(graphPoints.get(i).x, graphPoints.get(i).y, graphPoints.get(i).x, getHeight() - padding - labelPadding);

            }
        }

        if (index != -1) {
            if (xm > getWidth() - padding * 8) {
                g2.setColor(detailsBoxColour);
                g2.fillRect(xm - 200, ym - 20, 175, 50);

                g2.setColor(Color.black);
                g2.drawString("Date: " +    array.get(index)[0], xm - 190, ym);
                g2.drawString("Total cases:  " + cases.get(index), xm - 190, ym + 15);

            } else {
                g2.setColor(detailsBoxColour);
                g2.fillRect(xm + 15, ym - 20, 175, 50);

                g2.setColor(Color.black);
                g2.drawString("Date: " + array.get(index)[0], xm + 20, ym);
                g2.drawString("Total cases:  " + cases.get(index), xm + 20, ym + 15);
            }

            g2.setColor(lineColour);
            g2.drawLine(graphPoints.get(index).x, getHeight() - padding - labelPadding, graphPoints.get(index).x, padding);
        }

        // create x and y black axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        repaint();
        revalidate();
    }
    public static void drawRotate(Graphics2D g2d, int x, int y, String text) {
        g2d.translate(x,y);
        g2d.rotate(Math.toRadians(30));
        g2d.drawString(text,0,0);
        g2d.rotate(-Math.toRadians(30));
        g2d.translate(-x,-y);
    }
    private double minValue() {
        return 0;
    }
    private double maxValue(List<Integer> cases, int max) {
        double maxScore = Double.MIN_VALUE;

        for (int score : cases) {
            maxScore = Math.max(maxScore, score);
        }

        if (maxScore > max){
            threshold = 100000;
        } else {
            threshold = 500;
        }

        if (maxScore % threshold != 0){
            maxScore = (maxScore - maxScore % threshold) + threshold;
        }
        return maxScore;
    }
    public void setSpecification(ArrayList<String[]> array, List<Integer> cases, int div, String version, int max) {
        dataMap = new LinkedHashMap<>();
        for(String[] data: array){
            if (version.equals("daily")){
                this.dataMap.put(new Date(data[0]), data[1]);
            }
            if (version.equals("line")){
                this.dataMap.put(new Date(data[0]), data[2]);
            }
        }
        this.array = array;
        this.cases = cases;
        this.yDiv = div;
        this.version = version;
        this.index = -1;
        this.maximum = max;
    }
    public void setIndex(int index){
        this.index = index;
    }

}


class MouseHandler implements MouseMotionListener {
    public Graph g;

    MouseHandler(Graph g){
        this.g = g;
    }
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        g.xm = g.getMousePosition().x;
        g.ym = g.getMousePosition().y;
        if (g.xm > g.labelPadding + g.padding - 2 && g.xm < g.getWidth() - g.padding && g.ym < g.getHeight() - (g.padding + g.labelPadding) && g.ym > g.padding) {
            for (Point point: g.graphPoints) {
                if (point.x == g.xm){
                    g.setIndex(g.graphPoints.indexOf(point));
                }
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}