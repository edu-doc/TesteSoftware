package com.example.saltitantes.sistema;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class LoginPageObject {

    private final By usernameField = By.id("username");

    private final By passwordField = By.id("password");

    private final By loginButton = By.id("entrar");

    private final By irParaCadastroButton = By.id("cadastro");


    public void realizarLogin(WebDriver driver, String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /**
     * Clica no botão que navega da tela de login para a tela de cadastro.
     * @param driver A instância do WebDriver.
     */
    public void irParaPaginaDeCadastro(WebDriver driver) {
        driver.findElement(irParaCadastroButton).click();
    }
}