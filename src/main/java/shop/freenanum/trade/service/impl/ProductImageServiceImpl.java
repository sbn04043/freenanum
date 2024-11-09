package shop.freenanum.trade.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.freenanum.trade.model.entity.ProductImgEntity;
import shop.freenanum.trade.model.repository.ProductImageRepository;
import shop.freenanum.trade.service.ProductImageService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private static final String CRAWLING_IMAGE_DIRECTORY = "src/main/resources/static/image/productImage/";
    private final ProductImageRepository productImageRepository;
    @Value("${file.upload-dir}")
    private String imageDirectory;

    @Override
    public void saveImage(Long productId, MultipartFile productImg) {
        // 파일이 비어 있지 않은지 확인
        if (productImg.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        // 고유한 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + sanitizeFileName(productImg.getOriginalFilename());

        // 실제 파일 저장 경로 (절대 경로 사용)
        String absolutePath = "C:\\Users\\bitcamp\\IdeaProjects\\freenanum\\src\\main\\resources\\static\\image\\productImage";  // 실제 경로로 수정

        File directory = new File(absolutePath);
        // 디렉토리 없으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(Paths.get(absolutePath, fileName).toString());

        try {
            // 파일을 해당 경로에 저장
            productImg.transferTo(file);

            // DB에 파일 정보 저장
            ProductImgEntity productImageEntity = new ProductImgEntity();
            productImageEntity.setProductId(productId);
            productImageEntity.setProductImg(fileName);  // 파일 이름만 저장
            productImageRepository.save(productImageEntity); // DB 저장

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void saveImage(Long productId, MultipartFile productImg) {
//        // 고유한 파일 이름 생성 (UUID 사용)
//        String fileName = UUID.randomUUID().toString() + "_" + productImg.getOriginalFilename();
//        // 파일 저장 경로
//        File directory = new File(imageDirectory);
//        // 디렉토리 없으면 생성
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        File file = new File(Paths.get(imageDirectory, fileName).toString());
//
//        // 파일 저장
//        try {
//            productImg.transferTo(file);
//
//            // 파일 이름을 DB에 저장 (파일 경로는 DB에 저장하지 않고, 파일 이름만 저장)
//            ProductImgEntity productImageEntity = new ProductImgEntity();
//            productImageEntity.setProductId(productId);
//            productImageEntity.setProductImg(fileName);
//            productImageRepository.save(productImageEntity); // DB에 이미지 정보 저장
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 파일 저장 중 오류 처리 로직을 추가할 수 있습니다.
//        }
//    }

    @Override
    public void saveImage(Long productId, String url) {
        // URL 유효성 검사
        try {
            URL imageUrl = new URL(url);

            // 파일 이름 추출
            String fileName = imageUrl.getPath().substring(imageUrl.getPath().lastIndexOf('/') + 1);
            File destinationFile = new File(CRAWLING_IMAGE_DIRECTORY + fileName);

            // 디렉터리 존재 확인
            File dir = new File(CRAWLING_IMAGE_DIRECTORY);
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

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }
}
