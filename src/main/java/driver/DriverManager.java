package driver;

import constants.Navegador;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


import java.io.File;
import java.util.Collections;

import static Helpers.Helpers.readConfig;

public class DriverManager {
    private DesiredCapabilities capabilities = new DesiredCapabilities();
    private WebDriver webDriver;
    private File root = new File("DriverNavegador");
    private String extensionDriver = "";

    protected void resolveDriver(Navegador nav, String ambURL) {
        String headless_mode = readConfig("config.properties", "headless");
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("\nSistema operativo ->" + System.getProperty("os.name").toLowerCase() + "\n");
        File driverPath;
        //windows o mac
        if (!os.contains("mac")) {
            extensionDriver = ".exe";
        }


        switch (nav) {
            case Chrome:
                System.out.println("Se selecciona Chrome");
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless_mode.equals("true")) {
                    chromeOptions.setHeadless(true);
                    chromeOptions.addArguments("--headless");
                }

                webDriver = new ChromeDriver(chromeOptions);
                capabilities.setBrowserName("Chrome");
                webDriver.manage().window().maximize();
                break;
            case Explorer:
                System.out.println("Se selecciona Explorer");
                driverPath = new File(root, "IEDriverServer" + extensionDriver);
                System.setProperty("webdriver.ie.driver", driverPath.getAbsolutePath());
                webDriver = new FirefoxDriver();
                capabilities.setBrowserName("Explorer");
                webDriver.manage().window().maximize();
                break;
            case Firefox:
                System.out.println("Se selecciona Firefox");
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxoptions = new FirefoxOptions();
                if (headless_mode.equals("true")) {
                    firefoxoptions.setHeadless(true);
                    firefoxoptions.addArguments("--headless");
                }
                webDriver = new FirefoxDriver(firefoxoptions);
                capabilities.setBrowserName("Firefox");
                webDriver.manage().window().maximize();
                break;
            case Edge:
                System.out.println("Se selecciona Edge");
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                capabilities.setBrowserName("Microsoft Edge");
                webDriver.manage().window().maximize();
                break;
            default:
                System.out.println("No es posible lanzar el navegador, no se reconoce el navegador: " + nav);

        }
        webDriver.get(ambURL);
    }

    protected WebDriver getDriver() {
        return webDriver;
    }

    protected void setDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    protected Dimension getScreenSize() {
        return webDriver.manage().window().getSize();
    }
}




