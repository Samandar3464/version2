package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Advertising;

import java.util.UUID;

public interface AdvertisingRepository extends JpaRepository<Advertising,UUID> {
}
