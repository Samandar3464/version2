package uz.optimit.taxi.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Notification;
import uz.optimit.taxi.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(@NotBlank @Size(min = 9, max = 9) String phone);

    Optional<User> findByPhoneAndVerificationCode(@NotBlank @Size(min = 9, max = 9) String phone, @NotBlank @Size(min = 6, max = 6) Integer verificationCode);
    boolean existsByPhone(@NotBlank @Size(min = 9, max = 9) String phone);

    Optional<User> findByCarsIn(List<Car> cars);


}
