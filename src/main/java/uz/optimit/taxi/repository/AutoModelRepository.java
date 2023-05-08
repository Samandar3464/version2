package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AutoModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutoModelRepository extends JpaRepository<AutoModel, Integer> {
    AutoModel getByIdAndAutoCategoryId(Integer id, Integer autoCategory_id);

    Optional<AutoModel> findByNameAndAutoCategoryId(String name, Integer autoCategory_id);

    List<AutoModel> findAllByAutoCategoryId(Integer autoCategory_id);
}
