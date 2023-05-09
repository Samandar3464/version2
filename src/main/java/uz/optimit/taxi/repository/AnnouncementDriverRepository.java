package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.AnnouncementDriver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementDriverRepository extends JpaRepository<AnnouncementDriver, UUID> {
    List<AnnouncementDriver> findAllByActive(boolean active);

    List<AnnouncementDriver> findAllByActiveAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToDriveBetweenOrderByCreatedTimeDesc(boolean active, Integer fromRegion, Integer toRegion, LocalDateTime fromTime, LocalDateTime toTime);

    List<AnnouncementDriver> findAllByActiveAndFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndDeletedFalseAndTimeToDriveBetweenOrderByCreatedTimeDesc(boolean active, Integer fromRegion_id, Integer toRegion_id, Integer fromCity_id, Integer toCity_id, LocalDateTime timeToDrive, LocalDateTime timeToDrive2);

    List<AnnouncementDriver> findAllByUserIdAndActiveAndDeletedFalse(UUID user_id, boolean active);
    List<AnnouncementDriver> findAllByUserIdAndDeletedFalse(UUID user_id);
    Optional<AnnouncementDriver> findByUserIdAndActive(UUID user_id, boolean active);
    Optional<AnnouncementDriver> findByUserIdAndActiveAndDeletedFalse(UUID user_id, boolean active);

    Optional<AnnouncementDriver> findByIdAndActive(UUID announcementId, boolean active);
    Optional<AnnouncementDriver> findByIdAndActiveAndDeletedFalse(UUID announcementId, boolean active);

    boolean existsByUserIdAndActiveTrueAndDeletedFalse(UUID user_id);
}
