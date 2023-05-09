package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementPassenger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementPassengerRepository extends JpaRepository<AnnouncementPassenger, UUID> {

    List<AnnouncementPassenger> findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToTravelBetweenOrderByCreatedTimeDesc(Integer fromRegionId, Integer toRegionId, LocalDateTime timeToTravel, LocalDateTime timeToTravel2);

    Optional<AnnouncementPassenger> findByIdAndActive(UUID id, boolean active);
    Optional<AnnouncementPassenger> findByIdAndActiveAndDeletedFalse(UUID id, boolean active);

    Optional<AnnouncementPassenger> findByUserIdAndActiveAndDeletedFalse(UUID userId, boolean active);

    Optional<AnnouncementPassenger> findByUserIdAndActive(UUID userId, boolean active);

    boolean existsByUserIdAndActiveTrueAndDeletedFalse(UUID userId);

    List<AnnouncementPassenger> findAllByActive(boolean active);

    List<AnnouncementPassenger> findAllByUserIdAndActive(UUID id, boolean active);
    List<AnnouncementPassenger> findAllByUserIdAndActiveAndDeletedFalse(UUID id, boolean active);
}
