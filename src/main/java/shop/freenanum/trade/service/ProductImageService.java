package shop.freenanum.trade.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {
    void saveImage(String productId, MultipartFile file);

    void saveImage(String productId, String url);
}
