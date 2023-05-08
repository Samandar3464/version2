package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Seat;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findAllByIdIn(Collection<UUID> id);

    List<Seat> findAllByCarIdAndActive(UUID car_id, boolean active);
    List<Seat> findAllByCarId(UUID car_id);
}
