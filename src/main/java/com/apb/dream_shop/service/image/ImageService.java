package com.apb.dream_shop.service.image;

import com.apb.dream_shop.dto.ImageDTO;
import com.apb.dream_shop.exception.ResourceNotFoundException;
import com.apb.dream_shop.modal.Image;
import com.apb.dream_shop.modal.Product;
import com.apb.dream_shop.repository.ImageRepository;
import com.apb.dream_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepo;
    private final IProductService productService;

    @Override
    public Image getImageById(Long imageId) {
        return imageRepo.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image Not Found!"));
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepo.findById(imageId).ifPresentOrElse(
                imageRepo::delete,
                () -> {
                    throw new ResourceNotFoundException("Image Not Found!");
                }
        );
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDTO> saveImageDTO = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image saveImage = imageRepo.save(image);

                saveImage.setDownloadUrl(buildDownloadUrl + saveImage.getId());
                imageRepo.save(saveImage);

                ImageDTO imageDto = new ImageDTO();
                imageDto.setImageId(saveImage.getId());
                imageDto.setImageName(saveImage.getFileName());
                imageDto.setDownloadUrl(saveImage.getDownloadUrl());

                saveImageDTO.add(imageDto);

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return saveImageDTO;
    }

    @Override
    public Image updateImage(MultipartFile file, Long productId) {
        Image image = getImageById(productId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepo.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
