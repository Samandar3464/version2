package uz.optimit.taxi.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Familiar;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FamiliarRepository extends JpaRepository<Familiar, UUID> {

    List<Familiar> findAllByUserIdAndActive(UUID user_id,boolean active);
    List<Familiar> findByIdInAndActive(Collection<UUID> id,boolean active);
    boolean existsByPhoneAndUserIdAndActive(@Size(min = 9, max = 9) String phone, UUID user_id,boolean active);
    Familiar findFirstByUserIdAndPhone(UUID id, String phone);
}
