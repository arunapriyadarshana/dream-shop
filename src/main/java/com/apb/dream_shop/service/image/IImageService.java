package com.apb.dream_shop.service.image;

import com.apb.dream_shop.dto.ImageDTO;
import com.apb.dream_shop.modal.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long imageId);

    void deleteImageById(Long imageId);

    List<ImageDTO> saveImage(List<MultipartFile> files, Long productId);

    Image updateImage(MultipartFile file, Long productId);
}
