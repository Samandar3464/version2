package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Region;

import java.util.Collection;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    boolean existsByName(String name);
    boolean existsByNameIn(Collection<String> name);
}
