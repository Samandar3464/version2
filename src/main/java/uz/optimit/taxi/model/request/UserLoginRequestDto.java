package uz.optimit.taxi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginRequestDto {

    @NotBlank
    @Size(min = 9, max = 10)
    private String phone;

    @NotBlank
    @Size(min = 6 )
    private String password;
}
