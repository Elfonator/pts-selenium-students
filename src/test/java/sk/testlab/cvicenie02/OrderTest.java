package sk.testlab.cvicenie02;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.testlab.cvicenie02.support.BrowserFactory;
import sk.testlab.cvicenie02.support.TestConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private TestConfig config;

    @BeforeEach
    void setUp() {

        // TODO:
        // 1. nacitaj konfiguraciu
        // 2. vytvor WebDriver
        // 3. vytvor WebDriverWait

    }

    @AfterEach
    void tearDown() {

        // TODO:
        // zatvor browser

    }

    @Test
    void createOrder() {

        // =========================
        // ARRANGE
        // =========================

        // TODO:
        // otvor aplikaciu

        // TODO:
        // prihlas pouzivatela


        // =========================
        // ACT
        // =========================

        // TODO:
        // pridaj KB-001 2x

        // TODO:
        // pridaj MS-001 1x

        // TODO:
        // otvor kosik

        // TODO:
        // nastav dopravu courier

        // TODO:
        // aplikuj kupon STUDENT10

        // TODO:
        // prejdi na checkout

        // TODO:
        // vypln zakaznicke udaje

        // TODO:
        // vytvor objednavku


        // =========================
        // ASSERT
        // =========================

        // TODO:
        // najdi vyslednu cenu

        // TODO:
        // over, ze cena je 8563 centov

    }
}