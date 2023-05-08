package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Role;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

}
