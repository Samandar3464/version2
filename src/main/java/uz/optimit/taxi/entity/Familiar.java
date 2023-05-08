package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Familiar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 9, max = 9)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private short age;

    private double status;
    private boolean active;

    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy ="passengersList")
    private List<AnnouncementPassenger> announcementPassenger;

    public static Familiar from(FamiliarRegisterRequestDto registerRequestDto, User user) {
        return Familiar.builder()
                .name(registerRequestDto.getName())
                .phone(registerRequestDto.getPhone())
                .gender(registerRequestDto.getGender())
                .age(registerRequestDto.getAge())
                .active(true)
                .user(user)
                .build();
    }

    public static Familiar fromUser(User user) {
        return Familiar.builder()
                .id(user.getId())
                .name(user.getFullName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .active(true)
                .age((short) (LocalDateTime.now().getYear() - user.getBirthDate().getYear()))
                .user(user)
                .build();
    }

}
