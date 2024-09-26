package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.service.ProductImageService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private static final String IMAGE_DIRECTORY = "src/main/resources/static/image/productImage/";
    private final ProductImageRepository productImageRepository;

    @Override
    public void saveImage(Long productId, String url) {
        // URL 유효성 검사
        try {
            URL imageUrl = new URL(url);

            // 파일 이름 추출
            String fileName = imageUrl.getPath().substring(imageUrl.getPath().lastIndexOf('/') + 1);
            File destinationFile = new File(IMAGE_DIRECTORY + fileName);

            // 디렉터리 존재 확인
            File dir = new File(IMAGE_DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 데이터베이스에 저장
            productImageRepository.save(ProductImgEntity.builder()
                    .productId(productId)
                    .productImg(fileName) // 저장된 파일 이름
                    .build());

            // URL에서 이미지 다운로드
            FileUtils.copyURLToFile(imageUrl, destinationFile);
        } catch (MalformedURLException e) {
            // 유효하지 않은 URL 처리
            e.printStackTrace();
            System.out.println("잘못된 URL 형식입니다: " + url);
        } catch (IOException e) {
            // 파일 다운로드 오류 처리
            e.printStackTrace();
            System.out.println("이미지 다운로드 실패: " + e.getMessage());
        }
    }
}
