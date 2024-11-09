package shop.freenanum.trade.restController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.freenanum.trade.model.domain.UserModel;
import shop.freenanum.trade.model.entity.ProductEntity;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.model.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<Boolean> deleteImage(@PathVariable Long imageId) {
        productImageRepository.deleteById(imageId);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long productId, HttpSession httpSession) {
        // 로그인한 사용자 정보 가져오기
        UserModel loginUser = (UserModel) httpSession.getAttribute("loginUser");

        // 상품 조회
        ProductEntity product = productRepository.getByProductId(productId);

        // 로그인한 사용자와 상품 작성자가 일치하는지 확인
        if (product == null || !Objects.equals(product.getUserId(), loginUser.getId())) {
            // 권한이 없으면 해당 상품 페이지로 리다이렉션
            System.out.println("권한이 없습니다");
            return ResponseEntity.ok(false);
        }

        // 권한이 있으면 삭제 처리
        productRepository.deleteById(productId); // 상품 삭제
        return ResponseEntity.ok(true);
    }
}
