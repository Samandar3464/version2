package uz.optimit.taxi.repository;

import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.optimit.taxi.entity.CountMassage;
import uz.optimit.taxi.model.response.CountResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CountMassageRepository extends JpaRepository<CountMassage, Integer> {
    Integer countAllByCount(int count);

    Integer countAllBySandedTimeBetween(LocalDateTime sandedTime, LocalDateTime sandedTime2);

    @Query(value = "SELECT  c.phone ,COUNT(c.count) FROM count_massage  c GROUP BY c.phone ",nativeQuery = true)
    List<CountResponse> countCountMassage();


}
