package uz.optimit.taxi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerifyRequestDto {

//    @NotBlank
//    @Size(min = 9, max = 9)
    private String phone;
//    @NotBlank
//    @Size(min = 6, max = 6)
    private int verificationCode;
}
