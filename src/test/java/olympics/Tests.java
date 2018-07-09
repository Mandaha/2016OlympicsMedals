package olympics;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Tests {
  WebDriver driver;

  @BeforeClass
  public void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();

  }

  @Test
  public void SortTest() {
    String url = "https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table";
    driver.navigate().to(url);
    List<WebElement> firstColumn = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/td[1]"));
    List<Integer> actualRanking = new ArrayList();

    for (int i = 0; i < firstColumn.size() - 1; i++) {
      actualRanking.add(Integer.parseInt(firstColumn.get(i).getText()));
    }

    List<Integer> expectedRanking = new ArrayList<Integer>(actualRanking);
    Collections.sort(expectedRanking);

    assertEquals(actualRanking, expectedRanking);

    driver.findElement(By.xpath("(//table/thead/tr/th)[8]")).click();

    List<WebElement> nOC = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/th"));
    List<String> actualNOCRanking = new ArrayList<String>();

    for (WebElement webElement : nOC) {
      actualNOCRanking.add(webElement.getText());
    }

    List<String> expectedNOCRanking = new ArrayList<String>(actualNOCRanking);
    Collections.sort(expectedNOCRanking);

    assertEquals(actualNOCRanking, expectedNOCRanking);

    firstColumn = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/td[1]"));
    actualRanking = new ArrayList();

    for (int i = 0; i < firstColumn.size() - 1; i++) {
      actualRanking.add(Integer.parseInt(firstColumn.get(i).getText()));
    }

    expectedRanking = new ArrayList<Integer>(actualRanking);
    Collections.sort(expectedRanking);

    assertNotEquals(actualRanking, expectedRanking);
  }

  @Test
  public void TheMost() throws InterruptedException {
    String url = "https://en.wikipedia.org/wiki/2016_Summer_Olympics";
    driver.get(url);
    Thread.sleep(1000);

    List<WebElement> goldMedals = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[2]"));

    List<WebElement> silverMedals = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[3]"));

    List<WebElement> bronzeMedals = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[4]"));

    List<WebElement> mostMedalsOverall = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[5]"));

    assertEquals(highestScore(goldMedals), " United States (USA)");
    assertEquals(highestScore(silverMedals), " United States (USA)");
    assertEquals(highestScore(bronzeMedals), " United States (USA)");
    assertEquals(highestScore(mostMedalsOverall), " United States (USA)");
  }

  @Test
  public void COUNTRY_BY_MEDAL() throws InterruptedException {
    driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics");

    List<String> expected = Arrays.asList(" China (CHN)", " France (FRA)");
    List<String> acttual = silverEqual18();
    assertEquals(acttual, expected);

  }

  @Test
  public void GET_INDEX() throws InterruptedException {
    String url = "https://en.wikipedia.org/wiki/2016_Summer_Olympics";
    driver.get(url);
    String actual = returnRowCol(" Japan (JPN)");
    String expected = "(6,2)";
    assertEquals(actual, expected);
  }

  @Test
  public void GET_SUM() throws InterruptedException {
    driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics");
    
    List<String> expected = Arrays.asList(" Italy (ITA)-- Australia (AUS)", " Australia (AUS)-- Italy (ITA)");
    List<String> actual=bronzeSum18();
    assertEquals(actual, expected);
  }

  @AfterClass
  public void tearDown() {
    driver.close();
  }

  public String highestScore(List<WebElement> actualData) throws InterruptedException {

    Thread.sleep(1000);
    List<WebElement> names = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/th"));

    int max = 0;
    int maxi = 0;

    if (!actualData.isEmpty())
      max = Integer.parseInt(actualData.get(0).getText());
    else
      return null;

    for (int i = 0; i < actualData.size() - 1; i++) {
      int each = Integer.parseInt(actualData.get(i).getText());

      if (each > max) {
        max = each;
        maxi = i;
      }
    }

    return names.get(maxi).getText();

  }

  public List<String> silverEqual18() throws InterruptedException {
    Thread.sleep(1000);

    List<WebElement> actualGold = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[3]"));
    List<WebElement> names = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/th"));
    List<String> result = new ArrayList<String>();

    WebElement max;

    for (int i = 0; i < actualGold.size() - 1; i++) {
      WebElement each = actualGold.get(i);

      if (Integer.parseInt(each.getText().trim()) == 18) {
        result.add(names.get(actualGold.indexOf(each)).getText());
      }
    }

    return result;

  }

  public String returnRowCol(String temp) throws InterruptedException {
    Thread.sleep(1000);
    List<WebElement> list = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/th)"));

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getText().equals(temp))
        return "(" + (i + 1) + ",2)";
    }
    return null;
  }

  public List<String> bronzeSum18() throws InterruptedException {
    Thread.sleep(1000);

    List<WebElement> actualGold = driver.findElements(
        By.xpath("(//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr)/td[4]"));
    List<WebElement> names = driver.findElements(
        By.xpath("//table[@class='wikitable sortable plainrowheaders jquery-tablesorter']/tbody/tr/th"));
    List<String> result = new ArrayList<String>();


    for (int i = 0; i < actualGold.size() - 1; i++) {
      WebElement each = actualGold.get(i);

      for (int j = 1; j < actualGold.size() - 1; j++) {
        WebElement each2 = actualGold.get(j);
        if (Integer.parseInt(each.getText().trim()) + Integer.parseInt(each2.getText().trim()) == 18 & i != j) {
          result.add(names.get(actualGold.indexOf(each)).getText() + "--"
              + names.get(actualGold.indexOf(each2)).getText());
        }
      }
    }

    
    return result;
  }

}
