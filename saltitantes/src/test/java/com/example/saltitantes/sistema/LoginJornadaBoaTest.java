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

import java.time.Duration;

@SpringBootTest
public class LoginJornadaBoaTest {

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
        loginPage.realizarLogin(driver, "adminn", "adminn");

        // Verifica se o usuário foi redirecionado para a página de simulação após o login.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Simulação de Criaturas Saltitantes')]")
        ));

        Assertions.assertTrue(pageTitle.isDisplayed(), "O título da página de simulação não foi encontrado.");
    }
}