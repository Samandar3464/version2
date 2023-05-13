package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {

    List<AnnouncementPassenger> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToTravelAfterAndTimeToTravelBetweenOrderByCreatedTimeDesc(Integer fromRegion_id, Integer toRegion_id, LocalDateTime timeToTravel, LocalDateTime timeToTravel2, LocalDateTime timeToTravel3);

    Optional<AnnouncementPassenger> findByIdAndActive(UUID id, boolean active);
    Optional<AnnouncementPassenger> findByIdAndActiveAndDeletedFalse(UUID id, boolean active);

    Optional<AnnouncementPassenger> findByUserIdAndActiveAndDeletedFalse(UUID userId, boolean active);

    Optional<AnnouncementPassenger> findByUserId(UUID userId);

    boolean existsByUserIdAndActiveTrueAndDeletedFalse(UUID userId);

    List<AnnouncementPassenger> findAllByActiveTrueAndTimeToTravelAfterOrderByCreatedTimeDesc(LocalDateTime timeToTravel);

    List<AnnouncementPassenger> findAllByUserIdAndDeletedFalse(UUID id);
    List<AnnouncementPassenger> findAllByUserIdAndActiveAndDeletedFalseAndTimeToTravelAfter(UUID user_id, boolean active, LocalDateTime timeToTravel);
}
