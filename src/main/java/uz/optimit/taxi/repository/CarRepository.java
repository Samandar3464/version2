package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Car;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
     List<Car> findAllByActive(boolean Active);
     Optional<Car> findByUserIdAndActive(UUID user_id,boolean active);
     Optional<Car> findByUserId(UUID userId);
}
