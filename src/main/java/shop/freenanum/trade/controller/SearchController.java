package shop.freenanum.trade.controller;

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
        return "redirect:/api/search/products/1" + "?locationInput=" + URLEncoder.encode(locationInput, "UTF-8")
                + "&productInput=" + URLEncoder.encode(productInput, "UTF-8");
    }

    @GetMapping("/products/{pageNo}")
    public String searchProducts(@PathVariable Long pageNo,
                                 @RequestParam String locationInput,
                                 @RequestParam String productInput,
                                 Model model) {
        System.out.println("pageNo = " + pageNo + ", locationInput = " + locationInput + ", productInput = " + productInput + ", model = " + model);

        List<ProductModel> productList = productRepository.search(pageNo, locationInput, productInput).stream()
                .map(ProductModel::toModel)
                .peek(productModel -> {
                    productModel.setImgUrl(productImageRepository.getOneById(productModel.getId()));
                    // productModel을 반환
                })
                .toList();
        System.out.println("productList: " + productList);
        model.addAttribute("products", productList);
//        model.addAttribute("pagination", new Pagination(pageNo, productRepository.searchCount(locationInput, productInput)));

        return "/products/searchList";
    }

}
