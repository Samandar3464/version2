package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;

    private String fullName;

    private String phone;

    private int age;

    private double status;

    private Gender gender;

    private String profilePhotoUrl;

    private List<Familiar> passengersList;

    public static UserResponseDto from(User user) {
        double status= (double) (user.getStatus().getStars()) /user.getStatus().getCount();
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .age(LocalDate.now().getYear() - user.getBirthDate().getYear())
                .gender(user.getGender())
                .status(status)
                .build();
    }
}
