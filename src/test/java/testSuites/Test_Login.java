package testSuites;

import driver.DriverContext;
import io.qameta.allure.Description;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reporter.PdfReports;
import testClases.CPA0001_Login_valido;

import java.io.IOException;

import static Helpers.Helpers.readConfig;
import static constants.Navegador.Chrome;

public class Test_Login {

    @BeforeMethod
    public void setUp() {
        PdfReports.createPDF();
        DriverContext.setUp(Chrome, readConfig("config.properties", "AMBIENTE_PP"));
    }

    @AfterMethod
    public void tearDown() {
        DriverContext.quitDriver();
    }

    @Test(priority = 1)
    @Description("Ingreso de Usuario y Contraseña Validos")
    public void CPA0001_Login_Valido() throws InterruptedException, IOException {
        CPA0001_Login_valido cpa0001_login_valido = new CPA0001_Login_valido();
        cpa0001_login_valido.login_valido("userDefault1_RUT", "userDefault1_PASS");
        PdfReports.closePDF();


    }


}


