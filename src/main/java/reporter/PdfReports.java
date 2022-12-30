package reporter;

import driver.DriverContext;
import Helpers.Helpers;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static Helpers.Helpers.readConfig;
import static com.lowagie.text.FontFactory.getFont;

public class PdfReports {

    private static final Font titleFont = getFont("Courier", 26.0F, 1);
    private static final Font normalFont = getFont("Courier", 12.0F, 0);
    private static final Font fontPASSED = getFont("Courier", 12.0F, 1);
    private static final Font fontFAILED = getFont("Courier", 12.0F, 1);
    private static final Font fontWARNING = getFont("Courier", 12.0F, 1);
    private static final Font normalBloodFont = getFont("Courier", 12.0F, 1);


    private static com.lowagie.text.Document document;
    private static Date startDate;
    private static boolean estadoFinalPrueba;
    private static String testName;
    private static String randomName;
    private static PdfWriter writePDF;

    public PdfReports() {
    }

    public static String getFullTestName() {
        return testName + " - " + getFinalStatusTest() + ".pdf";
    }

    public static String getFinalStatusTest() {
        if (estadoFinalPrueba) {
            return "Passed";
        } else {
            return "Failed";
        }

    }

    public static String getTestName() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String testName = "";

        for (int i = 0; i < ste.length - 1; ++i) {
            if (ste[i].getMethodName().indexOf("PA00") > 0) {
                testName = ste[i].getMethodName();
            }
        }

        return testName;
    }

    public static Paragraph setTestName() {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(normalBloodFont);
        paragraph.add("\nNombre de Prueba: ");
        paragraph.setFont(normalFont);
        paragraph.add(getTestName());
        paragraph.setAlignment(0);
        paragraph.setSpacingAfter(5.0F);
        return paragraph;
    }

    public static void createTitlePage() {
        System.out.println("[PdfImedReport] createTitlePage");

        try {
            document.newPage();
            Image image = Image.getInstance(readConfig("config.properties", "urlPDF"));
            //Image image = Image.getInstance("https://upload.wikimedia.org/wikipedia/commons/2/27/Logo_Ripley_banco_2.png");
            image.setAlignment(1);
            image.scaleAbsolute(400.0F, 180.0F);
            Paragraph fixImagePosition = new Paragraph();
            fixImagePosition.add("\n\n");
            fixImagePosition.setSpacingAfter(50.0F);
            document.add(fixImagePosition);
            document.add(image);
            Paragraph parrafo = new Paragraph();
            parrafo.setFont(titleFont);
            parrafo.add("\n\n\nPruebas Automatizadas");
            parrafo.setAlignment(1);
            parrafo.setSpacingAfter(30.0F);
            document.add(parrafo);
            SimpleDateFormat formatterDay = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
            parrafo = new Paragraph();
            parrafo.setFont(normalFont);
            parrafo.add("\n Fecha Ejecución: " + formatterDay.format(startDate));
            parrafo.setAlignment(2);
            parrafo.setSpacingAfter(5.0F);
            document.add(parrafo);
            parrafo = new Paragraph();
            parrafo.setFont(normalFont);
            parrafo.add("Hora Inicio: " + formatterTime.format(startDate));
            parrafo.setAlignment(2);
            parrafo.setSpacingAfter(5.0F);
            document.add(parrafo);
            parrafo = new Paragraph();
            parrafo.setFont(normalFont);
            parrafo.add("Hora Termino: " + formatterTime.format(new Date()));
            parrafo.setAlignment(2);
            parrafo.setSpacingAfter(10.0F);
            document.add(parrafo);
            document.add(setTestName());
            parrafo = new Paragraph();
            parrafo.setFont(normalBloodFont);
            parrafo.add("browser: ");
            parrafo.setFont(normalFont);
            // parrafo.add(Constants.browser);
            parrafo.add(readConfig("config.properties", "BROWSER"));
            parrafo.setSpacingAfter(5.0F);
            document.add(parrafo);

            parrafo = new Paragraph();
            parrafo.setFont(normalBloodFont);
            parrafo.add(readConfig("config.properties", "ambientePrueba"));

            //parrafo.add("Ambiente: ");
            parrafo.setFont(normalFont);
            parrafo.add(Helpers.tipoAmbiente());
            parrafo.setFont(normalBloodFont);
            parrafo.add(" Status: ");
            if (estadoFinalPrueba) {
                parrafo.setFont(fontPASSED);
                parrafo.add("PASSED");
            } else {
                parrafo.setFont(fontFAILED);
                parrafo.add("FAILED");
            }

            parrafo.setAlignment(8);
            parrafo.setSpacingAfter(5.0F);
            document.add(parrafo);
            /*
            document.add(setRepositoryEmail());
            document.add(setRepositoryName());
            document.add(setRepositoryBranch());
            */
            // document.addAuthor("Maximiliano Guerra");
        } catch (BadElementException var5) {
            System.out.println("Image BadElementException" + var5);
        } catch (IOException var6) {
            System.out.println("Image IOException " + var6);
        } catch (DocumentException var7) {
            var7.printStackTrace();
        }
    }

    public static void createPDF() {
        System.out.println("[PdfImedReport] createPDF");
        document = new Document();
        Color MyColor = new Color(0, 128, 0);
        fontPASSED.setColor(MyColor);
        fontFAILED.setColor(Color.RED);
        MyColor = new Color(255, 235, 59);
        fontWARNING.setColor(MyColor);
        startDate = new Date();
        estadoFinalPrueba = true;
        try {
            randomName = randomAlphaNumeric(15);
            writePDF = PdfWriter.getInstance(document, new FileOutputStream(new File("tmp/" + randomName)));
            document.open();
        } catch (DocumentException | FileNotFoundException var2) {
            var2.printStackTrace();
        }

    }

    public static void closePDF() {
        System.out.println("[PdfImedReport] closePDF");
        testName = getTestName();
        createTitlePage();
        document.close();
        try {
            PdfReader reader = new PdfReader("tmp/" + randomName);
            int n = reader.getNumberOfPages();
            reader.selectPages(String.format("%d, 1-%d", n, n - 1));
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("tmp/" + getFullTestName()));
            stamper.close();
            Thread.sleep(3000);
            File file = new File("tmp/" + randomName);
            if (file.delete()) {
                System.out.println("Temporal File: " + randomName + " Deleted-");
            } else {
                System.out.println("File: " + randomName + " Not found");
            }
        } catch (IOException | DocumentException var3) {
            var3.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addTextValidate(String nombrePaso, String textoEsperado, String textoObtenido, boolean fatal) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(100.0F);
        float[] widths = new float[]{24.0F, 60.0F};

        try {
            table.setWidths(widths);
        } catch (DocumentException var10) {
            var10.printStackTrace();
        }

        String textStatus = "PASSED";
        if (!textoEsperado.equals(textoObtenido)) {
            textStatus = "FAILED";
            estadoFinalPrueba = false;
        }

        Paragraph parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Name: ");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(nombrePaso);
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Status");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        if (textStatus.equals("PASSED")) {
            parrafo.setFont(fontPASSED);
        } else {
            parrafo.setFont(fontFAILED);
        }

        parrafo.add(textStatus);
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Texto esperado");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(textoEsperado);
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Texto obtenido");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(textoObtenido);
        parrafo.setAlignment(0);
        table.addCell(parrafo);

        try {
            if (textoEsperado.length() > 100) {
                if (writePDF.getVerticalPosition(true) < ((table.calculateHeights(true) - textoEsperado.length()) + 100)) {
                    document.newPage();
                }
            } else if (writePDF.getVerticalPosition(true) < 164.54) {
                document.newPage();
            }
            document.add(table);
            document.add(new Paragraph("\n"));
            if (fatal) {
                closePDF();
                // Assert.fail("Error al comparar textos");
            }
        } catch (DocumentException var9) {
            var9.printStackTrace();
        }

    }

    public static void addWebReportImage(String nombrePaso, String descripcion, EstadoPrueba estadoPrueba, boolean fatal) {

        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(100.0F);

        float[] widths = new float[]{24.0F, 60.0F};


        try {
            table.setWidths(widths);
        } catch (DocumentException var12) {
            var12.printStackTrace();
        }

        Paragraph parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Name ");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(nombrePaso);
        parrafo.setAlignment(0);
        table.addCell(parrafo);

        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Status");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        switch (estadoPrueba) {
            case FAILED:
                parrafo.setFont(fontFAILED);
                parrafo.add("FAILED");
                estadoFinalPrueba = false;
                break;
            case PASSED:
                parrafo.setFont(fontPASSED);
                parrafo.add("PASSED");
                break;
            case WARNING:
                parrafo.setFont(fontWARNING);
                parrafo.add("WARNING");
                break;
            default:
                parrafo.setFont(fontPASSED);
                parrafo.add("PASSED");
        }

        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Description");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(descripcion);
        parrafo.setAlignment(0);
        table.addCell(parrafo);

        try {
            Image image = Image.getInstance(getScreenshotWeb());
            image.setAlignment(0);
            PdfPCell pCell = new PdfPCell();
            pCell.setHorizontalAlignment(1);
            pCell.setRowspan(1);
            pCell.setColspan(2);
            pCell.addElement(image);
            table.addCell(pCell);

            //if (writePDF.getVerticalPosition(true) < 428.54){
            if ((writePDF.getVerticalPosition(true) < table.calculateHeights(true)) || (writePDF.getVerticalPosition(true) < 428.54)) {
                document.newPage();
            }
            document.add(table);
            document.add(new Paragraph("\n"));
        } catch (BadElementException var10) {
            System.out.println("Image BadElementException" + var10);
        } catch (IOException var11) {
            System.out.println("Image IOException " + var11);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addReport(String textoNombrePaso, String descripcion, EstadoPrueba estadoPrueba, boolean fatal) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(100.0F);
        float[] widths = new float[]{24.0F, 60.0F};

        try {
            table.setWidths(widths);
        } catch (DocumentException var9) {
            var9.printStackTrace();
        }

        Paragraph parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Name: ");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(textoNombrePaso);
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Status");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        switch (estadoPrueba) {
            case FAILED:
                parrafo.setFont(fontFAILED);
                parrafo.add("FAILED");
                estadoFinalPrueba = false;
                break;
            case PASSED:
                parrafo.setFont(fontPASSED);
                parrafo.add("PASSED");
                break;
            case WARNING:
                parrafo.setFont(fontWARNING);
                parrafo.add("WARNING");
                break;
            default:
                parrafo.setFont(fontPASSED);
                parrafo.add("PASSED");
        }

        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalBloodFont);
        parrafo.add("Step Description");
        parrafo.setAlignment(0);
        table.addCell(parrafo);
        parrafo = new Paragraph();
        parrafo.setFont(normalFont);
        parrafo.add(descripcion);
        parrafo.setAlignment(0);
        table.addCell(parrafo);

        try {

            if (descripcion.length() > 100) {
                if (writePDF.getVerticalPosition(true) < ((table.calculateHeights(true) - descripcion.length()) + 100)) {
                    document.newPage();
                }
            } else if (writePDF.getVerticalPosition(true) < 164.54) {
                document.newPage();
            }
            document.add(table);
            document.add(new Paragraph("\n"));
            if (fatal) {
                closePDF();
                //       Assert.fail("Error al continuar con el flujo");
            }
        } catch (DocumentException var8) {
            var8.printStackTrace();
        }

    }

    private static byte[] getScreenshotWeb() {
        return ((TakesScreenshot) DriverContext.getDriver()).getScreenshotAs(OutputType.BYTES);
    }


    /**
     * Método para generar un randomString
     *
     * @param count parametro de tipo int, define un limite de caracteres
     * @return un valor de tipo String
     */
    public static String randomAlphaNumeric(int count) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}



