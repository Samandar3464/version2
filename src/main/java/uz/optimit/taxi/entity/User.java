package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.optimit.taxi.entity.Enum.Gender;
import uz.optimit.taxi.model.request.UserRegisterDto;
import uz.optimit.taxi.repository.RoleRepository;
import uz.optimit.taxi.service.AttachmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.DRIVER;
import static uz.optimit.taxi.entity.Enum.Constants.PASSENGER;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 9,max = 9)
    private String phone;

    @NotBlank
    @Size(min = 6)
    private String password;

    private LocalDate birthDate;

    private LocalDateTime registeredDate;

    private boolean isBlocked;

    private String fireBaseToken;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Status status;

    private Integer verificationCode;

    private LocalDateTime verificationCodeLiveTime;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment profilePhoto;

    @ManyToMany(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Car> cars;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AnnouncementPassenger> announcementUser;

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AnnouncementDriver> announcementDrivers;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Familiar> passengersList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToOne(mappedBy = "user")
    private Balance balance;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        roles.forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User from(UserRegisterDto userRegisterDto,
                        Integer verificationCode ){
        return User.builder()
                .fullName(userRegisterDto.getFullName())
                .phone(userRegisterDto.getPhone())
                .birthDate(userRegisterDto.getBirthDate())
                .gender(userRegisterDto.getGender())
                .registeredDate(LocalDateTime.now())
                .verificationCode(verificationCode)
                .verificationCodeLiveTime(LocalDateTime.now())
                .isBlocked(true)


                .build();
    }
}
