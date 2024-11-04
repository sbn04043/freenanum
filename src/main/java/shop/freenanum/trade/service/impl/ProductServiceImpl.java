package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.enumeration.ProductStatus;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.model.repository.ProductRepository;
import shop.freenanum.trade.service.ProductImageService;
import shop.freenanum.trade.service.ProductService;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductEntity> findAll() {
        return List.of();
    }

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Map<?, ?> login(UserModel model) {
        return Map.of();
    }

    @Override
    public void productCrawling() {
        WebDriver driver = null;
        try {
            Document productsDoc = Jsoup.connect("https://www.daangn.com/region/%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C/%EA%B0%95%EB%82%A8%EA%B5%AC/%EB%8F%84%EA%B3%A1%EB%8F%99").get();
            Elements elements = productsDoc.select("section.cards-wrap");
            Elements linkElements = productsDoc.select("a.card-link");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // 브라우저 UI를 보지 않고 실행
            driver = new ChromeDriver(options);

            for (Element linkElement : linkElements) {
                String link = linkElement.attr("href");
                System.out.println("https://www.daangn.com" + link);
                driver.get("https://www.daangn.com" + link);

                // 동적으로 로딩된 이미지 요소를 기다림
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                String title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("article-title"))).getText();
                String address = "서울특별시 강남구 도곡동";
                String description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("article-detail"))).getText();
                String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("article-price"))).getText();
                priceText = priceText.replaceAll("[^0-9]", "");

                long price = priceText.isEmpty() ? 0 : Long.parseLong(priceText);

                Long productId = productRepository.save(ProductEntity.builder()
                                .userId(1L + (long) (Math.random() * 10))
                                .productTitle(title)
                                .productAddress(address)
                                .productDescription(description)
                                .productStatus(ProductStatus.GOOD)
                                .price(price)
                                .views(0L)
                                .build())
                        .getId();

                List<WebElement> portraitElements = driver.findElements(By.className("portrait"));
                if (!portraitElements.isEmpty()) {
                    for (WebElement portrait : portraitElements) {
                        String url = portrait.getAttribute("src");
                        if (url.contains("dnvefa"))
                            continue;
                        productImageService.saveImage(productId, url);
                    }
                } else {
                    // landscape 요소 처리
                    List<WebElement> landscapeElements = driver.findElements(By.className("landscape"));
                    if (!landscapeElements.isEmpty()) {
                        for (WebElement landscape : landscapeElements) {
                            String url = landscape.getAttribute("src");
                            if (url.contains("dnvefa"))
                                continue;
                            productImageService.saveImage(productId, url);
                        }
                    } else {
                        System.out.println("No portrait or landscape elements found.");
                    }
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }


}
