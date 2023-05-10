package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.PassengerParcel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParcelRepository extends JpaRepository<PassengerParcel , UUID> {
    List<PassengerParcel> findAllByActiveTrueAndDeletedFalse();

    Optional<PassengerParcel> getByIdAndActiveTrueAndDeletedFalse(UUID id);
    Optional<PassengerParcel> getByIdAndDeletedFalse(UUID id);

    List<PassengerParcel> findAllByUserIdAndActiveAndDeletedFalse(UUID id, boolean b);

    List<PassengerParcel> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToTravelBetweenOrderByCreatedTimeDesc(Integer fromRegionId, Integer toRegionId, LocalDateTime time1, LocalDateTime time2);
}
