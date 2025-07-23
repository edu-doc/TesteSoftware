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
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("local")
public class CadastroTest {

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

        // Gera dados únicos para evitar que o teste falhe por usuário já existente.
        String uniqueLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);

        // Preenche e submete o formulário de cadastro.
        cadastroPage.realizarCadastro(driver, uniqueLogin, "senhaForte123",
                "https://github.com/BenAriel/teste-software-front/blob/master/public/benno.jpg?raw=true");

        // Agora, o teste espera ser redirecionado para a pagina de simulação.
        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Simulação de Criaturas Saltitantes')]")));

        Assertions.assertTrue(pageTitle.isDisplayed(),
                "Após o cadastro, não foi redirecionado para a página de simulação.");
    }
}