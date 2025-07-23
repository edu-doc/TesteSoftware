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
public class CadastroJornadaFalhaTest {

    @Autowired
    private WebDriver driver;

    @Autowired
    private CadastroPageObject cadastroPage;

    @Autowired
    private LoginPageObject loginPage;

    private String baseUrl = "http://localhost:3000";

    @BeforeEach
    void setup() {
        driver.get(baseUrl);
    }

    @Test
    void testFluxoDeCadastroComSucesso() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Na tela de login, clicar no botão de cadastro.
        loginPage.irParaPaginaDeCadastro(driver);

        // Primeiro, espera um elemento da tela de cadastro aparecer para garantir que a
        // navegação ocorreu.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("foto")));

        // Preenche e submete o formulário de cadastro.
        cadastroPage.realizarCadastro(driver, "admin", "admin",
                "https://github.com/BenAriel/teste-software-front/blob/master/public/benno.jpg?raw=true");

        // Agora, o teste espera que uma mensagem de erro seja exibida, pois o usuário
        // já existe.
        WebElement mensagemDeErro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(), 'Falha no registro. O nome de usuário pode já estar em uso.')]")));

        // Asserção: Confirma que a mensagem de erro está visível.
        Assertions.assertTrue(mensagemDeErro.isDisplayed(), "A mensagem de erro de cadastro não foi encontrada.");
    }
}