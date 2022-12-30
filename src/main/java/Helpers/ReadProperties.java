package Helpers;

import reporter.EstadoPrueba;
import reporter.PdfReports;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    public static Properties readFromConfig(String nameFile) {
        String propFileName = nameFile;
        Properties properties = new Properties();
        InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                PdfReports.addReport("Read Properties", "No se pudo encontrar el archivo properties " + propFileName, EstadoPrueba.FAILED, true);
            }
        } else {
            PdfReports.addReport("Read Properties", "No se pudo encontrar el archivo properties " + propFileName, EstadoPrueba.FAILED, true);
        }
        return properties;
    }
}
