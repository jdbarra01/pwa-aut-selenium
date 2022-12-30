package Helpers;


import driver.DriverContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import reporter.EstadoPrueba;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static Helpers.ControlledActions.visualizarObjeto;
import static constants.Ambientes.QA;
import static reporter.PdfReports.addReport;

public class Helpers {

    public static boolean validarTexto(String textoApp, String textoAValidar) {
        if (textoApp.trim().equals(textoAValidar.trim())) {
            addReport("Validación de texto", "Texto:" + textoApp + " validado segun diseño y es correcto", EstadoPrueba.PASSED, false);
            return true;
        } else {
            addReport("Validación de texto", "Texto desplegado en app:" + textoApp + " validado segun diseño y no es correcto, deberia desplegar:" + textoAValidar, EstadoPrueba.FAILED, false);
            return false;
        }
    }

    public static void reporteValidacionTextos(WebElement objeto, String textoEsperado) {
        try {
            boolean existeObjeto = visualizarObjeto(objeto, 5);
            if (existeObjeto == true) {
                if (objeto.getText().equals(textoEsperado)) {
                    addReport("Validación de objeto y texto", "Se visualiza el texto esperado:'" + textoEsperado + "'.", EstadoPrueba.PASSED, false);
                } else {
                    addReport("Validación de objeto y texto", "No se visualiza el texto esperado:'" + textoEsperado + "'.", EstadoPrueba.FAILED, false);
                }
            } else {
                addReport("Validación de objeto y texto", "No se visualiza el texto:‘" + textoEsperado + "’ en la vista desplegada.", EstadoPrueba.FAILED, false);
            }
        } catch (Exception e) {
            System.out.println("No se  logra ejecutar funcion reporteValidacionTextos, motivo:" + e.getMessage());
            addReport("Validación de objeto y texto", "Error en el metodo ‘reporteValidacionTextos’, motivo:" + e.getMessage(), EstadoPrueba.FAILED, false);
        }
    }

    public static boolean validarTextoSinReporte(String textoApp, String textoAValidar) {
        if (textoApp.trim().equals(textoAValidar.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEnabled(WebElement element) throws NoSuchElementException {
        System.out.println("Esta el elemento habilitado?:" + element.getAttribute("enabled"));
        return element.getAttribute("enabled").trim().equals("true");
    }

    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }


    public static String tipoAmbiente() {
        if (QA.equals("QA")) {
            return "Certificación";
        } else if (QA.equals("INT")) {
            return "Integración";
        } else {
            return "Desarrollo";
        }

    }

    public static String readConfig(String propFileName, String param) {
        Properties prop = new Properties();
        String Value = "NULL";
        try {
            InputStream input = new FileInputStream("src/test/resources/" + propFileName + "");
            prop.load(input);
            Value = prop.getProperty(param);

        } catch (Exception e) {
            e.printStackTrace();
            addReport("Read Properties", "No se pudo encontrar el archivo porperties " + propFileName, EstadoPrueba.FAILED, true);
        }
        return Value;
    }


    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // dice qué sucedió y en qué parte del código sucedió esto.
    }



    @Attachment(value = "Screenshot of {0}", type = "image/png")
    public static byte[] saveScreenshot(String name, WebDriver driver) {
        return (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void imagenAllure(String nameScreenshot) throws IOException {
        try {
            File screenShot = ((TakesScreenshot) DriverContext.getDriver()).getScreenshotAs(OutputType.FILE);
            Allure.addAttachment(nameScreenshot, FileUtils.openInputStream(screenShot));
        } catch (Exception e) {
            System.out.println("No se pudo sacar Screenshot");
            System.out.println("Error: " + e.getCause().toString() + " \n" + "StackTrace: " + e.fillInStackTrace().toString());
        }
    }

}
