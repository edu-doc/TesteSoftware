package com.example.saltitantes.sistema;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
public class CadastroPageObject {

    private final By loginField = By.id("username");

    private final By senhaField = By.id("password");

    private final By urlFotoField = By.id("foto");

    private final By cadastrarButton = By.id("entrar");

    /**
     * Preenche todos os campos do formulário de cadastro e o submete.
     * @param driver A instância do WebDriver.
     * @param login O login do novo usuário.
     * @param senha A senha do novo usuário.
     * @param urlFoto A URL da foto de perfil.
     */
    public void realizarCadastro(WebDriver driver, String login, String senha, String urlFoto) {
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(senhaField).sendKeys(senha);
        driver.findElement(urlFotoField).sendKeys(urlFoto);
        driver.findElement(cadastrarButton).click();
    }
}