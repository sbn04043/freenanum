package shop.freenanum.trade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.model.repository.ProductRepository;
import shop.freenanum.trade.model.repository.UserRepository;
import shop.freenanum.trade.service.ProductImageService;
import shop.freenanum.trade.service.ProductService;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    public String register(@RequestBody ProductModel productModel, Model model, MultipartFile multipartFile) {
        Long productId = productService.save(ProductEntity.toUploadEntity(productModel)).getId();
        productImageService.saveImage(productId, multipartFile);

        return "redirect:/api/products/" + productId;
    }

    @GetMapping("/upload")
    public String register() {
        return "products/upload";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {
        ProductModel productModel = ProductModel.toModel(productRepository.getProductById(id));
        System.out.println("product: " + productModel);
        model.addAttribute("product", productModel);
        System.out.println("productImg");
        model.addAttribute("productImgUrls", productImageRepository.findByProductId(id));
        model.addAttribute("seller", userRepository.getByUserId(productModel.getUserId()));
        System.out.println("product done");

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
