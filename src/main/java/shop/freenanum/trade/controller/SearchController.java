package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.repository.ProductRepository;

@RequestMapping("/api/search")
@RequiredArgsConstructor
@Controller
public class SearchController {
    private final ProductRepository productRepository;

    @GetMapping("/products/{pageNo}")
    public String searchProducts(@PathVariable int pageNo,
                                 @RequestParam String locationInput,
                                 @RequestParam String productInput,
                                 Model model) {

        model.addAttribute("productList", productRepository.search(pageNo, locationInput, productInput));

        return "/products/searchList";
    }
}
