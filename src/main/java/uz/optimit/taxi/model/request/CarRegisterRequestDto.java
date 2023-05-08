package uz.optimit.taxi.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarRegisterRequestDto {

    private Integer autoCategoryId;

    private Integer autoModelId;

    private String color;

    private String carNumber;

    private String texPassport;

    private byte countSeat;

    private MultipartFile photoDriverLicense;

    private MultipartFile texPassportPhoto;

    private List<MultipartFile> autoPhotos;

}
