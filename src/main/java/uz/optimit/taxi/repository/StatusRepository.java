package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Status;

import java.util.Optional;
import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status , UUID> {

    Optional<Status> findByUserId(UUID user_id);
}
