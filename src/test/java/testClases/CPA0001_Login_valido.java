package testClases;

import pages.Page_Login;

import java.io.IOException;


public class CPA0001_Login_valido {


    public void login_valido(String rut, String pass) throws InterruptedException, IOException {
        Page_Login page_login = new Page_Login();


        //Metodos de Login PWA
        page_login.validaElementosLogin();
        page_login.ingresoUsuarioPWA(rut);
        page_login.ingresClavePWA(pass);
        page_login.botonIngresarPWA();


    }

}

