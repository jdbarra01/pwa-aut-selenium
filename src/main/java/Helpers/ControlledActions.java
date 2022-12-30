package Helpers;

import driver.DriverContext;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import reporter.EstadoPrueba;
import reporter.PdfReports;

import java.time.Duration;

import static Helpers.Helpers.sleepSeconds;

public class ControlledActions {

    public static boolean exists(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException ex) {
            return false;
        } catch (StaleElementReferenceException ex) {
            return false;
        }
    }

    @Step("Valida Elementos Visibles")
    public static boolean isElementPresentWait(WebElement elementName, int timeout) {
        try {
            System.out.println("Valida si Es visible el elemnto a validar.");
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), timeout);
            wait.until(ExpectedConditions.visibilityOf(elementName));
            System.out.println("Es visible el elemnto a validar.");
            return true;
        } catch (Exception e) {
            System.out.println("No es visible el elemento a validar.");
            return false;
        }
    }

    @Step("Carga de maximo de 60 segundos")
    public static void loading(WebElement element, int tiempo, String descripcion) {
        try {
            long tiempoStart = System.currentTimeMillis();
            long tiempoEnd = tiempoStart + 60 * 1000; // 60 seconds * 1000 ms/sec
            do {
                if (System.currentTimeMillis() >= tiempoEnd) {
                    PdfReports.addWebReportImage("loading", descripcion, EstadoPrueba.FAILED, true);
                }
                Duration.ofMillis(100);
            } while (isElementPresentWait(element, tiempo));

        } catch (NoSuchElementException ex) {
            PdfReports.addWebReportImage("loading", "[ControlledActions] Error: No se pudo encontrar elemento " + ex.getMessage(), EstadoPrueba.FAILED, true);
        }
    }

    @Step("Carga mientras un elemento NO exista")
    public static void loadingNegado(WebElement element, int tiempo, String descripcion) {
        try {
            long tiempoStart = System.currentTimeMillis();
            long tiempoEnd = tiempoStart + 60 * 1000; // 60 seconds * 1000 ms/sec
            do {
                if (System.currentTimeMillis() >= tiempoEnd) {
                    PdfReports.addWebReportImage("Loading", descripcion, EstadoPrueba.FAILED, true);
                }
                Duration.ofMillis(100);
            } while (!isElementPresentWait(element, tiempo));

        } catch (NoSuchElementException ex) {
            PdfReports.addWebReportImage("Loading", "[ControlledActions] Error: No se pudo encontrar elemento " + ex.getMessage(), EstadoPrueba.FAILED, true);
        }
    }

    @Step("Se Visualiza Objetos")
    public static boolean visualizarObjeto(WebElement objeto, int segundos) {
        try {
            System.out.println("Buscamos el objeto:" + objeto + ", esperamos " + segundos + " segundos, hasta que aparezca.");
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), segundos);
            wait.until(ExpectedConditions.visibilityOf(objeto));
            System.out.println("Se encontró objeto:" + objeto + ", se retorna true.");
            return true;
        } catch (Exception e) {
            System.out.println("No se encontró objeto:" + objeto + ", se retorna false.");
            return false;
        }
    }

    public static boolean urlContains(String UrlAEsperar, int segundos) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), segundos);
        if (wait.until(ExpectedConditions.urlContains(UrlAEsperar))) {
            return true;
        } else {
            return false;
        }
    }


    /************************************************************
     *  Metodo que valida si el elemento ingresado por parametro*
     *  existe. Devuelve un boolean dependiendo si se encuentra *
     *  o no el elemento.                                       *
     ************************************************************/
    public static boolean existeElemento(WebElement objeto, int segundos) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), (long) segundos);
            wait.until(ExpectedConditions.visibilityOf(objeto));
            return true;
        } catch (Exception var3) {
            return false;
        }
    }


    /**********************************************************
     *  Metodo que hace una comparativa entre el texto        *
     *  que se despliega en la APP contra el texto que        *
     *  se espera que se muestre. La prueba falla si          *
     *  los textos no coinciden                               *
     **********************************************************/
    public static void validarElemento(WebElement elemento, String textoAValidar, int segundos) {
        try {
            boolean existe = existeElemento(elemento, segundos);
            if (existe) {
                //    PdfReports.addReport("Elemento encontrado", "Se encuentra el elemento " + elemento, EstadoPrueba.PASSED, false);
                System.out.println("Se encuentra el elemnto" + elemento);
                if (elemento.getText().trim().equals(textoAValidar.trim())) {
                    PdfReports.addReport("Validación texto", "Se visualiza el texto esperado: " + textoAValidar, EstadoPrueba.PASSED, false);
                } else {
                    PdfReports.addReport("Validación texto", "NO se visualiza el texto esperado: " + textoAValidar + " se visualiza el texto: " + elemento.getText(), EstadoPrueba.FAILED, false);
                }
            } else {
                PdfReports.addWebReportImage("Elemento NO encontrado", "No se ha podido encontrar el elemento " + elemento, EstadoPrueba.FAILED, true);
                System.out.println("No se encuentra el elemento" + elemento);
            }
        } catch (Exception var3) {
            PdfReports.addReport("Validación de objeto", "Error en el metodo 'validarElemento', motivo: " + var3.getMessage(), EstadoPrueba.FAILED, true);
        }
    }

    /***************************************************
     * Metodo que valida si se ha cargado la pantalla. *
     ***************************************************/
    public static void ingresoPantalla(WebElement objeto, String tituloPantalla) {
        boolean ingreso = existeElemento(objeto, 60);
        if (ingreso == true) {
            PdfReports.addWebReportImage("Ingreso a Pantalla", "Se ingresa correctamente a la pantalla " + tituloPantalla, EstadoPrueba.PASSED, false);
        } else {
            PdfReports.addWebReportImage("Ingreso a Pantalla", "No ingresa a la pantalla esperada: " + tituloPantalla, EstadoPrueba.FAILED, true);
        }
    }


    /**
     * recibe un elemento y un tiempo en segundos, para biuscar el elemento, al encontrarlos valida si el objeto esta
     * con la propiedad "isEnabled" en true, retorna true o false
     *
     * @param objeto
     * @param segundos
     * @return
     */
    public static boolean validarEnable(WebElement objeto, int segundos) {
        System.out.println("Se validará que el objeto:" + objeto + " se encuentre enabled en " + segundos + " segundos.");
        int milisegundos = segundos * 1000;
        boolean res = false;
        for (int i = 0; i < 3; i++) {
            if (isEnabled(objeto)) {
                System.out.println("El objeto:" + objeto + " se encuentra enabled.");
                res = true;
                break;
            } else if (i == 3) {
                System.out.println("El objeto:" + objeto + " después de " + segundos + " segundos no se encuentra enabled.");
                res = false;
            } else {
                try {
                    Thread.sleep(milisegundos);
                } catch (InterruptedException e) {
                    PdfReports.addWebReportImage("validarEnable", "El Sleep del metodo validarEnable falló, el motivo:" + e.getMessage(), EstadoPrueba.FAILED, true);
                }
            }
        }
        return res;
    }

    public static boolean isEnabled(WebElement element) throws NoSuchElementException {
        System.out.println("Esta el elemento habilitado?:" + element.getAttribute("enabled"));
        return element.getAttribute("enabled").trim().equals("true");
    }

    public static boolean encontrarObjeto(WebElement elemento,String namePage ,String detalle) {
        boolean existe;
        int intento = 0;
        existe = existeElemento(elemento, 1);
        while (!existe) {
            existe = existeElemento(elemento, 1);
            intento++;
            if (intento == 5) {
                PdfReports.addWebReportImage(namePage + "Encontrar Objeto", "No se visualiza " + detalle, EstadoPrueba.FAILED, false);
            }
        }
        PdfReports.addWebReportImage(namePage + "Encontrar Objeto", "Se visualiza " + detalle, EstadoPrueba.PASSED, false);
        return existe;
    }


    public static void webScrollToElement(WebElement elemento) {
        JavascriptExecutor js = (JavascriptExecutor) DriverContext.getDriver();
        WebElement Element = DriverContext.getDriver().findElement((By) elemento);
        js.executeScript("arguments[0].scrollIntoView();", Element);
        sleepSeconds(5);
    }

}