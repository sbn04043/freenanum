package shop.freenanum.trade.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.model.repository.ProductRepository;
import shop.freenanum.trade.pattern.proxy.Pagination;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequestMapping("/api/search")
@RequiredArgsConstructor
@Controller
public class SearchController {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductModel productModel;

    @GetMapping("/products")
    public String searchProducts(@RequestParam String locationInput,
                                 @RequestParam String productInput) throws UnsupportedEncodingException {
        System.out.println("locationInput = " + locationInput + ", productInput = " + productInput);
        return "redirect:/api/search/products/1" + "?locationInput=" + URLEncoder.encode(locationInput, StandardCharsets.UTF_8)
                + "&productInput=" + URLEncoder.encode(productInput, StandardCharsets.UTF_8);
    }

    @GetMapping("/products/{pageNo}")
    public String searchProducts(@PathVariable Long pageNo,
                                 @RequestParam String locationInput,
                                 @RequestParam String productInput,
                                 Model model,
                                 HttpServletResponse response) {
        List<ProductModel> productList = productRepository.search(pageNo, locationInput, productInput).stream()
                .map(ProductModel::toModel)
                .peek(productModel -> {
                    productModel.setImgUrl(productImageRepository.getOneById(productModel.getId()));
                })
                .toList();

        model.addAttribute("products", productList);
        model.addAttribute("pagination", new Pagination(pageNo, productRepository.searchCount(locationInput, productInput)));
        model.addAttribute("location", locationInput);
        model.addAttribute("product", productInput);

        return "/products/searchList";
    }

}
