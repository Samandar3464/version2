package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoCategory;

import java.util.List;


public interface AutoCategoryRepository extends JpaRepository<AutoCategory, Integer> {

    boolean existsByName(String name);

    List<AutoCategory> findAllByActiveTrue();
}
