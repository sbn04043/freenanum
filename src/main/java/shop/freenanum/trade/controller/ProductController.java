package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.stylesheets.LinkStyle;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.model.repository.ProductRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.ProductService;
import shop.freenanum.trade.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productRepository.getProductWithNickname(id));
        model.addAttribute("productImgUrls", productImageRepository.findByProductId(id));
        return "products/showOne";
    }


    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("products", productRepository.getHotList().stream().map(productEntity ->
                ProductModel.builder()
                        .id(productEntity.getId())
                        .productTitle(productEntity.getProductTitle())
                        .productAddress(productEntity.getProductAddress())
                        .productStatus(productEntity.getProductStatus())
                        .views(productEntity.getViews())
                        .imgUrl(productImageRepository.getOneById(productEntity.getId()))
                        .build()).toList());
        return "products/hotList";
    }
}
