package com.example.saltitantes.sistema;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("local")
public class LoginJornadaFalhaTest {

    @Autowired
    private WebDriver driver;

    @Autowired
    private LoginPageObject loginPage;

    private String baseUrl = "http://localhost:3000";

    @BeforeEach
    void setup() {
        driver.get(baseUrl);
    }

    @Test
    void testLoginComSucesso() {

        // Acessa a página de login.
        loginPage.realizarLogin(driver, "Seila", "Seila");

        // Verifica se a mensagem de erro é exibida após uma tentativa de login com
        // credenciais inválidas.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement mensagemDeErro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Falha no login. Verifique suas credenciais.')]")));

        // Asserção: Confirma que a mensagem de erro está visível.
        Assertions.assertTrue(mensagemDeErro.isDisplayed(), "A mensagem de erro de login não foi exibida.");
    }
}
