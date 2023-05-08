package uz.optimit.taxi.model.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Enum.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamiliarRegisterRequestDto {
     private String name;
     @NotBlank
     @Size(min = 9, max = 9)
     private String phone;
     @Enumerated(EnumType.STRING)
     private Gender gender;
     private short age;
}
