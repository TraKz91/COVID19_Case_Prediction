import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Arrays;

public class PDF {
    /*Creates the name of the pdf file*/
    private final static String path = "COVID19 Report.pdf";
    /*Initialise instance of a variable*/
    private final Frame frame = Frame.getInstance();
    /*Link to the image banner*/
    private final static String BANNER_LINK = "images/NHS-banner.png";
    private final static String PROTECT_NHS = "images/protectNHs.jpg";
    private final static String CASES_IMAGES = "CasesImage.png";
    private final static String LINEAR_CASES = "PredictedCases.png";
    private final static String LINEAR_DEATHS = "PredictedDeaths.png";
    private final static String DEATH_IMAGES = "DeathCasesImage.png";

    public PDF() throws IOException {
        Image bannerImage = createBanner();
        PdfFont generalFont = createFont();

        /*Writing to the path*/
        /*PDf writer writing to the default file*/
        PdfWriter pdfWriter = new PdfWriter(path);

        //Opens document in writing mode
        /*PDF Document*/
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        //This adds to the pdf document
        /*Essentially we write to this document
         * and then put it in the pdfdocument*/
        Document document = new Document(pdfDocument);

        Paragraph headingParagraphs = headingParagraph(generalFont);
        Paragraph weeklyPara = summaryHeading(generalFont);
        /*Add paragraphs or images or anything to document*/
        Paragraph graphSection = graphSection(generalFont);
        document.add(bannerImage);
        document.add(headingParagraphs);
        document.add(weeklyPara);
        document.add(graphSection);
        document.add(predictionSection(generalFont));
        document.add(footer(generalFont));

        /*Close both the document and the writer*/
        document.close();
        pdfWriter.close();

    }

    /*Creates a banner for report!*/
    private Image createBanner() throws MalformedURLException {
        /*Create a link to the the bannerLink and then add the data to create an image*/
        ImageData data = ImageDataFactory.create(BANNER_LINK);
        Image bannerImage = new Image(data);
        bannerImage.setWidth(400).setHeight(100);
        return bannerImage;
    }

    /*Creates a Font!*/
    private PdfFont createFont() throws IOException {
        /*This creates a font
         * Multiple can be created depending on what you aim to do
         * */
        return PdfFontFactory
                .createFont(FontConstants.TIMES_ROMAN);
    }

    /*Return String Array containing statistic from the frame class*/
    private String[] getCasesStatistics() {
        return frame.statsTotal();
    }

    /*Return String Array containing statistic from the frame class*/
    private String[] getDeathStatistics() {
        return frame.statsDeath();
    }

    /*This crates a headingParagraphs
     * You can have many paragraphs instances
     * Just add them to the document*/
    private Paragraph headingParagraph(PdfFont font) {

        Text coronaVirusDisease = new Text("Coronavirus disease")
                .setFont(font)
                .setBold()
                .setFontSize(22);
         /*This creates an individual text
        you can also specify various properties
         * */

        Text coronaShortForm = new Text("\n(COVID-19)")
                .setFont(font)
                .setBold()
                .setFontSize(15);
        Text situationReport = new Text("\n Situation Report - 2021")
                .setFont(font)
                .setBold()
                .setFontSize(18);
        /*Creates a date object which gets the current date
         * */
        LocalDate localDate = LocalDate.now();
        Text validityOfReport = new Text("\n The information in this report is valid as of " +
                " " + localDate)
                .setFont(font)
                .setItalic()
                .setFontSize(15);
        return new Paragraph()
                .add(coronaVirusDisease)
                .add(coronaShortForm)
                .add(situationReport)
                .add(validityOfReport);
    }

    /*This crates a summary Paragraphs
     * You can have many paragraphs instances
     * Just add them to the document*/
    private Paragraph summaryHeading(PdfFont font) {
        Text highLights = new Text(" Highlights:")
                .setBold()
                .setFont(font)
                .setUnderline()
                .setFontSize(18);
        String output = Arrays.toString(getDeathStatistics()).replaceAll("(^\\[|\\]$)", "");
        String output2 = Arrays.toString(getCasesStatistics()).replaceAll("(^\\[|\\]$)", "");
        Text deathReport = new Text("\n" + output).setFont(font).setFontSize(14);
        Text casesReport = new Text("\n" + output2).setFont(font).setFontSize(14);
        return new Paragraph().add(highLights).add(casesReport).add(deathReport);
    }

    /*Creates the graph section for report!*/
    private Paragraph graphSection(PdfFont font) throws MalformedURLException {
        Text graphHeading = new Text("Visualisation of COVID-19 Cases")
                .setBold()
                .setFont(font)
                .setFontSize(18)
                .setUnderline();
        Text graphHeadingTwo = new Text("\nVisualisation of COVID-19 Deaths")
                .setBold()
                .setFont(font)
                .setFontSize(18)
                .setUnderline();
        //An Image is the representation of a graphic element (JPEG, PNG or GIF)
        // that has to be inserted into the document
        ImageData imageDataCase = ImageDataFactory.create(CASES_IMAGES);
        Image casesImage = new Image(imageDataCase).setWidth(500);

        ImageData imageDataDeath = ImageDataFactory.create(DEATH_IMAGES);
        Image deathImage = new Image(imageDataDeath).
                setWidth(500);


        Text figure1Cases = new Text("\nFigure 1. Number of Confirmed* " +
                "COVID-19 cases reported in the last year 30 January 2020 " +
                "23 February 2020")
                .setFont(font)
                .setFontSize(14)
                .setItalic();

        Text figure1Death = new Text("\nFigure 2. Number of Confirmed* " +
                "COVID-19 Deaths reported in the last year 30 January 2020 " +
                "23 February 2020")
                .setFont(font)
                .setFontSize(14)
                .setItalic();
        Text generalNote = new Text("Note** The X-Coordinate represents " +
                "the time specified above and Y-Coordinate represents the number of cases")
                .setFont(font)
                .setFontSize(10)
                .setItalic()
                .setFontColor(Color.RED);
        return new Paragraph()
                .add(graphHeading)
                .add(figure1Cases)
                .add(casesImage)
                .add(generalNote)
                .add(graphHeadingTwo)
                .add(figure1Death)
                .add(deathImage)
                .add(generalNote);
    }

    /*Creates the prediction section for report!*/
    private Paragraph predictionSection(PdfFont font) throws MalformedURLException {
        ImageData imageData = ImageDataFactory.create(LINEAR_CASES);
        Image casesImages = new Image(imageData).
                setWidth(400);

        ImageData imageDataDeathV2 = ImageDataFactory.create(LINEAR_DEATHS);
        Image deathImages = new Image(imageDataDeathV2).
                setWidth(400);
        Text generalNoteCases = new Text("Note: The Graph shows " +
                "there is an expected overall positive increase in cases as shown")
                .setFont(font)
                .setFontSize(10)
                .setItalic().setUnderline();

        Text generalNoteDeath = new Text("\nNote: The Graph shows " +
                "there is an expected overall positive increase in death as shown")
                .setFont(font)
                .setFontSize(14)
                .setItalic().setUnderline();
        Text figure3Cases = new Text("\n\nFigure 3. Predicted " +
                "COVID-19 cases for two or more weeks")
                .setFont(font)
                .setFontSize(14)
                .setItalic();

        Text figure4Deaths = new Text("\nFigure 4. Predicted " +
                "COVID-19 deaths for two or more weeks")
                .setFont(font)
                .setFontSize(14)
                .setItalic();

        Text estimateHeading = new Text("\nEstimation/Prediction of Cases and Deaths")
                .setBold()
                .setFont(font)
                .setFontSize(16)
                .setUnderline();

        Text descriptionOfPrediction = new Text(" \nWe have developed an algorithm, " +
                "that uses the current information" +
                " on COVID-19, in addition to linear regression techniques " +
                "to predict how the number of case and deaths would change" +
                " over the next two weeks" +
                " or more." +
                "\nIt is important to know the current pandemic is very unpredictable and" +
                " therefore this prediction should be used" +
                " in accordance with medical expertise")
                .setFont(font)
                .setFontSize(14);
        return new Paragraph()
                .add(estimateHeading)
                .add(descriptionOfPrediction)
                .add(figure3Cases)
                .add(casesImages)
                .add(generalNoteCases)
                .add(figure4Deaths)
                .add(deathImages)
                .add(generalNoteDeath);

    }

    /*Creates the footer for report!*/
    private Paragraph footer(PdfFont font) throws MalformedURLException {
        ImageData imageDataDeath = ImageDataFactory.create(PROTECT_NHS);
        Image footerImage = new Image(imageDataDeath).
                setWidth(500);

        Text designed = new Text("\nDesigned by Team10")
                .setFont(font)
                .setFontSize(13).setItalic();
        Text copyright = new Text("\nCopyright \u00a9 2021 All Rights Reserved")
                .setFont(font)
                .setFontSize(13).setItalic();
        Text license = new Text(" \nContains public sector information licensed under the Open Government Licence v3.0.")
                .setFont(font)
                .setFontSize(13).setItalic();
        return new Paragraph()
                .add(designed)
                .add(copyright)
                .add(license)
                .add(footerImage);
    }

}
