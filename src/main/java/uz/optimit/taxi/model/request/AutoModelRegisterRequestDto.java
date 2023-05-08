package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoModelRegisterRequestDto {
     private String name;
     private byte countSeat;
     private Integer categoryId;
}
