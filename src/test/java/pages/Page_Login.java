package pages;

import driver.DriverContext;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import reporter.EstadoPrueba;
import reporter.PdfReports;

import java.io.IOException;

import static Helpers.ControlledActions.*;
import static Helpers.Helpers.*;


public class Page_Login {
    private WebDriver driver;

    public int tiempo = 1;
    public int tiempoMax = 5;

    public Page_Login() {
        this.driver = DriverContext.getDriver();
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[name=rut]")
    WebElement inputUsuario;

    @FindBy(css = "input[name=password]")
    WebElement inputPass;

    @FindBy(css = "button[class=\"brand-button primary\"]")
    WebElement btnInicioSesion;

    @FindBy(xpath = "//p[contains(text(),'El RUT es inválido')]")
    WebElement lebelRut;

    @FindBy(xpath = " //h3[contains(text(),'Ingresa a tu cuenta')]")
    WebElement lebelIngresoCuenta;

    @FindBy(xpath = "//button[contains(text(),'¿Olvidaste o se bloqueó tu clave?')]")
    WebElement btnOlvidasteClave;

    @FindBy(xpath = "//div[contains(text(),'Lo sentimos')]")
    WebElement labelLoSentimo;




    @Step("Page Login: Validacion de elementos")
    public void validaElementosLogin() throws IOException {
        System.out.println("Page Login: Inicio metodo valida elementos");
        ingresoPantalla(lebelIngresoCuenta, "Home de PWA");
        validarElemento(lebelIngresoCuenta, "Ingresa a tu cuenta", tiempo);
        validarElemento(btnOlvidasteClave, "¿Olvidaste o se bloqueó tu clave?", tiempo);
        imagenAllure("Page Login: Validacion de elementos");
    }


    @Step("Page Login: Ingreso de Usuario PWA")
    public void ingresoUsuarioPWA(String usuario) throws IOException {
        System.out.println("Page Login: Inicia metodo Ingreso de Usuario");
        loadingNegado(inputUsuario, tiempoMax, "Page Login: Después de 60 segundos no se visualiza el Home de la PWA'.");

        String user = readConfig("login.properties", usuario);

        if (visualizarObjeto(inputUsuario, tiempoMax)) {
            inputUsuario.sendKeys(user);
            PdfReports.addReport("Page Login:", "Ingreso de Usuario : " + user + ":", EstadoPrueba.PASSED, false);
        } else {
            System.out.println("Page Login: No se encuentra el elemento para el ingreso de Usuario: " + user);
            PdfReports.addWebReportImage("Page Login", "Ingreso de Usuario : " + user + "", EstadoPrueba.FAILED, false);
        }
        validaLabelErrorRut();
        imagenAllure("Page Login: Ingreso de Usuario PWA");
    }

    @Step("Page Login: Ingreso de Clave PWA")
    public void ingresClavePWA(String pass) throws IOException {
        System.out.println("Page Login: Inicia metodo Ingreso de Clave PWA");
        String clave = readConfig("login.properties", pass);

        validaLabelErrorRut();
        if (visualizarObjeto(inputPass, tiempoMax)) {
            inputPass.sendKeys(clave);
            PdfReports.addReport("Page Login:", "Ingreso de Contraseña : " + clave + ":", EstadoPrueba.PASSED, false);
        } else {
            System.out.println("Page Login: No se encuentra el elemento para el ingreso de Contraseña: " + clave);
            PdfReports.addWebReportImage("Page Login", "Ingreso de Contraseña : " + clave + "", EstadoPrueba.FAILED, false);
        }
        imagenAllure("Page Login: Ingreso de Clave PWA");
    }

    @Step("Page Login: Realiza click en boton Ingresar")
    public void botonIngresarPWA() throws InterruptedException, IOException {
        System.out.println("Page Login: Inicia metodo click en boton Ingresar");
        if (visualizarObjeto(btnInicioSesion, tiempo)) {
            btnInicioSesion.click();
            sleepSeconds(tiempoMax);
            validaErrorIngreso();
            PdfReports.addReport("Page Login:", "Se realiza click en boton Ingresar", EstadoPrueba.PASSED, false);
        } else {
            PdfReports.addReport("Page Login:", "No se visualiza el boton Ingresar", EstadoPrueba.FAILED, true);
        }
        imagenAllure("Page Login: Realiza click en boton Ingresar");
    }

    @Step("Page Login: Valida error en el ingreso de Rut")
    public void validaLabelErrorRut() throws IOException {
        System.out.println("Page Login: Inicia metodo Valida error en el ingreso de Rut");
        if (visualizarObjeto(lebelRut, tiempo)) {
            PdfReports.addWebReportImage("Page Login:", "Rut Ingresado es invalido", EstadoPrueba.FAILED, true);
            imagenAllure("Page Login: Valida error en el ingreso de Rut");
        }

    }

    @Step("Page Login: Valida error en ingreso de PWA")
    public void validaErrorIngreso() throws IOException {
        System.out.println("Page Login: Inicia metodo Valida error en ingreso de PWA");
        if (visualizarObjeto(labelLoSentimo, tiempo)) {
            PdfReports.addWebReportImage("Page Login:", "Lo sentimos", EstadoPrueba.FAILED, true);
            imagenAllure("Page Login: Lo sentimos");
        }
    }


}
