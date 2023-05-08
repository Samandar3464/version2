package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoModel;

import java.util.List;

public interface AutoModelRepository extends JpaRepository<AutoModel, Integer> {
    AutoModel getByIdAndAutoCategoryId(Integer id, Integer autoCategory_id);

    boolean existsByNameAndAutoCategoryId(String name, Integer autoCategory_id);

    List<AutoModel> findAllByAutoCategoryIdAndActiveTrue(Integer autoCategory_id);
}
