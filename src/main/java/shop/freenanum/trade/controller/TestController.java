package shop.freenanum.trade.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

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
                    .email(user.get("email").asText())
                    .password(user.get("login").get("password").asText())
                    .userName(user.path("name").path("first").asText() + " " + user.path("name").path("last").asText())
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
        try {
            Document productsDoc = Jsoup.connect("https://www.daangn.com/hot_articles").get();
            Elements elements = productsDoc.select("section.cards-wrap");
            Elements linkElements = productsDoc.select("a.card-link");

            Iterator<Element> titles = elements.select("h2").iterator();
            Iterator<Element> addresses = elements.select("div.card-region-name").iterator();

            while (!linkElements.isEmpty()) {
                String link = linkElements.first().attr("href");
                Document productDoc = Jsoup.connect("https://www.daangn.com" + link).get();
                Elements descElements = productDoc.select("div#article-detail").select("p");
                Elements imgElements = productDoc.select("img.portrait");

                System.out.println(link + ": ");
                for (Element imgElement : imgElements) {
                    String imgUrl = imgElement.attr("src");
                    System.out.println(imgUrl);
                }

                linkElements.remove(0);
            }

            return "test/productCrawling";

        } catch (IOException e) {
            e.printStackTrace();
            return "스크래핑 실패";
        }
    }
}
