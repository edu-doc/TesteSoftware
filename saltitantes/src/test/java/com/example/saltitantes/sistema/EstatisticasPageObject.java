package com.example.saltitantes.sistema;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class EstatisticasPageObject {

    private final By loginSearchField = By.id("estatisticas-usuario-input");
    private final By buscarButton = By.id("estatisticas-usuario-button");

    public void buscarEstatisticasPorLogin(WebDriver driver, String login) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(loginSearchField)).sendKeys(login);
        wait.until(ExpectedConditions.elementToBeClickable(buscarButton)).click();
    }
}
