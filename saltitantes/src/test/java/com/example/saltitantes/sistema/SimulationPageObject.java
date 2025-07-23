package com.example.saltitantes.sistema;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SimulationPageObject {

    private final By quantidadeCriaturasField = By.id("quantidade");
    private final By iteracoesField = By.id("iteracoes");
    private final By iniciarSimulacaoButton = By.id("iniciar-simulacao");
    private final By logoutButton = By.id("logout-button");
    private final By estatisticasLink = By.linkText("Estatísticas");

    /**
     * Configura os parâmetros e inicia a simulação.
     */
    public void configurarEIniciarSimulacao(WebDriver driver, String quantidade, String iteracoes) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement quantidadeInput = wait.until(ExpectedConditions.elementToBeClickable(quantidadeCriaturasField));
        quantidadeInput.clear();
        quantidadeInput.sendKeys(quantidade);

        WebElement iteracoesInput = wait.until(ExpectedConditions.elementToBeClickable(iteracoesField));
        iteracoesInput.clear();
        iteracoesInput.sendKeys(iteracoes);

        driver.findElement(iniciarSimulacaoButton).click();
    }

    public void fazerLogout(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutBtn.click();
    }

    public void irParaEstatisticas(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(estatisticasLink)).click();
    }

}