package gamehub.game_Hub.Repository.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Role.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleName);

}
