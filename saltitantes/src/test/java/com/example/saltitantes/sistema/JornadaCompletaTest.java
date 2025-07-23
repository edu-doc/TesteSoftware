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
import java.util.UUID;

@SpringBootTest
public class JornadaCompletaTest {

    @Autowired
    private WebDriver driver;

    @Autowired
    private CadastroPageObject cadastroPage;

    @Autowired
    private LoginPageObject loginPage;

    @Autowired
    private SimulationPageObject simulationPage;

    @Autowired
    private EstatisticasPageObject estatisticasPage;

    private String baseUrl = "http://localhost:3000";

    @BeforeEach
    void setup() {
        driver.get(baseUrl);
    }

    @Test
    void testFluxoCompletoCadastroESimulacao() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // --- PARTE 1: CADASTRO ---
        loginPage.irParaPaginaDeCadastro(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("foto")));
        String uniqueLogin = "user_" + UUID.randomUUID().toString().substring(0, 8);
        cadastroPage.realizarCadastro(driver, uniqueLogin, "senhaForte123", "https://github.com/BenAriel/teste-software-front/blob/master/public/benno.jpg?raw=true");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se cadastro foi bem-sucedido e redirecionou para a página de simulação
        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Simulação de Criaturas Saltitantes')]")
        ));
        Assertions.assertTrue(pageTitle.isDisplayed(), "Não foi redirecionado para a página de simulação após o cadastro.");

        // --- 2. LOGOUT ---
        simulationPage.fazerLogout(driver);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se voltou para a página de login
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("entrar")));
        Assertions.assertTrue(loginButton.isDisplayed(), "Não retornou à página de login após o logout.");

        // --- 3. LOGIN ---
        loginPage.realizarLogin(driver, uniqueLogin, "senhaForte123");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se chegou na página de simulação novamente
        pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Simulação de Criaturas Saltitantes')]")
        ));
        Assertions.assertTrue(pageTitle.isDisplayed(), "Redirecionamento para simulação após login falhou.");

        // --- 4. SIMULAÇÃO ---
        simulationPage.configurarEIniciarSimulacao(driver, "5", "10");

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se a simulação começou (procurando pelo canvas)
        WebElement canvas = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("canvas")));
        Assertions.assertTrue(canvas.isDisplayed(), "O canvas da simulação não foi renderizado.");

        // --- 5. ESTATÍSTICAS ---
        simulationPage.irParaEstatisticas(driver);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se as estatísticas foram carregadas
        WebElement statsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(), 'Estatísticas')]") // Assumindo que a página tem este título
        ));
        Assertions.assertTrue(statsTitle.isDisplayed(), "FALHA: Não navegou para a página de estatísticas.");

        // --- 6. JOGADOR ---
        estatisticasPage.buscarEstatisticasPorLogin(driver, uniqueLogin);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verifica se o resultado da busca pelo login do jogador foi exibido
        WebElement avatarDoUsuario = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("usuario-avatar")
        ));
        Assertions.assertNotNull(avatarDoUsuario, "FALHA: O avatar do usuário buscado não foi encontrado no DOM da página de estatísticas.");
    }
}