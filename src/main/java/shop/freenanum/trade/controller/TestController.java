package shop.freenanum.trade.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.entity.UserEntity;
import shop.freenanum.trade.model.enumeration.ProductStatus;
import shop.freenanum.trade.model.repository.ProductRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.impl.ProductImageServiceImpl;
import shop.freenanum.trade.service.impl.ProductServiceImpl;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductImageServiceImpl productImageServiceImpl;
    private final ProductServiceImpl productServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/userCrawling")
    public String userCrawling(Model model) throws IOException {
        String url = "https://randomuser.me/api/?results=100";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode resultNode = jsonNode.get("results");

        for (JsonNode user : resultNode) {

            userRepository.save(UserEntity.builder()
                    .username(user.get("email").asText())
                    .password(passwordEncoder.encode(user.get("login").get("password").asText()))
                    .name(user.path("name").path("first").asText() + " " + user.path("name").path("last").asText())
                    .nickname(user.path("login").get("username").asText())
                    .phone(user.path("phone").asText())
                    .sellCount(0L)
                    .buyCount(0L)
                    .userAddress(user.path("location").path("country").asText() + " "
                            + user.path("location").path("state").asText() + " "
                            + user.path("location").path("city").asText() + " "
                            + user.path("location").path("street").path("name").asText() + " "
                            + user.path("location").path("street").path("number").asText())
                    .score(0.0)
                    .gender(user.path("gender").asText())
                    .build());
        }

        model.addAttribute("user", userRepository.findAll());
        return "/test/index";
    }

    @GetMapping("/productCrawling")
    public String productCrawling(Model model) throws IOException {
        productServiceImpl.productCrawling();


        return "/products/searchList";
    }
}

