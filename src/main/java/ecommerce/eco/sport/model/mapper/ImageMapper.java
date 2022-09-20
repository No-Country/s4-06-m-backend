package ecommerce.eco.sport.model.mapper;

import ecommerce.eco.sport.model.entity.Image;
import ecommerce.eco.sport.model.response.ImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageResponse imageToDto(Image img){
        return ImageResponse.builder()
                .id(img.getId())
                .name(img.getFileName())
                .fileUrl(img.getImageUrl())
                .build();
    }

}