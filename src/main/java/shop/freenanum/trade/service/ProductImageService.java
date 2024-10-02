package shop.freenanum.trade.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    void saveImage(Long productId, MultipartFile file);

    void saveImage(Long productId, String url);
}
