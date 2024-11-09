package shop.freenanum.trade.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.domain.ProductModel;
import shop.freenanum.trade.model.domain.UserModel;
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
    public String register(@RequestParam("productTitle") String productTitle,
                           @RequestParam("productAddress") String productAddress,
                           @RequestParam("price") long price,
                           @RequestParam("productDescription") String productDescription,
                           @RequestParam("productImg") MultipartFile multipartFile,
                           HttpSession session) {
        System.out.println(multipartFile);

        ProductModel productModel = new ProductModel();
        productModel.setUserId(((UserModel)session.getAttribute("loginUser")).getId());
        productModel.setProductTitle(productTitle);
        productModel.setProductAddress(productAddress);
        productModel.setPrice(price);
        productModel.setProductDescription(productDescription);



        Long productId = productService.save(ProductEntity.toUploadEntity(productModel)).getId();
        productImageService.saveImage(productId, multipartFile);

        return "redirect:/api/products/" + productId;
    }


    @GetMapping("/upload")
    public String register(HttpSession session, Model model) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/api/products/list";
        }
        model.addAttribute("address", ((UserModel) session.getAttribute("loginUser")).getUserAddress());

        return "products/upload"; // JSON 형태로 리턴
    }


    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
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

    @GetMapping("/edit/{productId}")
    public String update(@PathVariable Long productId, Model model, HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/api/products/" + productId;
        }

        if (((UserModel) session.getAttribute("loginUser")).getId() != productId) {
            return "redirect:/api/products/" + productId;
        }

        model.addAttribute("product", ProductModel.toModel(productRepository.getProductById(productId)));
        model.addAttribute("productImgs", productImageRepository.findByProductId(productId));

        System.out.println(productRepository.getProductById(productId));

        return "products/edit";
    }
}
