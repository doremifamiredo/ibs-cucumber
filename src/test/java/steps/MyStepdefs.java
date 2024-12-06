package steps;

import io.cucumber.java.bg.И;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyStepdefs {
    public static WebDriver driver;
    @FindBy(tagName = "tr")
    List<WebElement> elements;

    @FindBy(tagName = "table") WebElement table;
    @FindBy(id = "editForm") WebElement addingForm;
    @FindBy(xpath = "//button[contains(text(), 'Добавить')]")
    WebElement addBtn;
    @FindBy(xpath = "//button[@id]")
    WebElement saveBtn;

    @Step("открыть {string}")
    @И("открыть {string}")
    public void openPage(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
       // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        driver.manage().window().maximize();
    }

    @Step("проверить, что в списке продуктов нет {string}")
    @И("проверить, что в списке продуктов нет {string}")
    public void presentWordInProductList(String word) {
        assertFalse(elements.stream().anyMatch(s -> s.getText().contains(word)));
    }

    @Step("проверить, что в списке продуктов есть {string}")
    @И("проверить, что в списке продуктов есть {string}")
    public void absentWordInProductList(String word) throws InterruptedException {
        List<WebElement> products = table.findElements(By.xpath("//tr"));
        assertTrue(products.stream().anyMatch(s -> s.getText().contains(word)));
    }

    @Step("кликнуть кнопку Добавить")
    @И("кликнуть кнопку Добавить")
    public void addClick() {
        addBtn.click();
    }

    @Step("кликнуть кнопку Сохранить")
    @И("кликнуть кнопку Сохранить")
    public void saveClick() throws InterruptedException {
        addingForm.findElement(By.xpath("//button[@id]")).click();
        Thread.sleep(10);
    }

    @Step("поле Наименование заполняется значением {string}")
    @И("поле Наименование заполняется значением {string}")
    public void sendValue(String value) {
        addingForm.findElement(By.xpath("//input[@id='name']")).sendKeys(value);
    }

    @Step("в выпадающем списке {string} выбрать {string}")
    @И("в выпадающем списке {string} выбрать {string}")
    public void selectType(String label, String value) {
        String xPath = String.format("//label[contains(text(), '%s')]", label);
        String id = addingForm.findElement(By.xpath(xPath)).getAttribute("for");
        WebElement dropDown = addingForm.findElement(By.id(id));
        Select select = new Select(dropDown);
        select.selectByVisibleText(value);
    }

    @Step("очистить данные")
    @И("очистить данные")
    public void tearDown() throws InterruptedException {
        driver.findElement(By.id( "navbarDropdown")).click();
        List<WebElement> menu = driver.findElements(By.className("dropdown-item"));
        menu.get(menu.size() - 1).click();
        driver.close();
        driver.quit();
    }
}
