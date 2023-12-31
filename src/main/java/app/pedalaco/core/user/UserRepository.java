package app.pedalaco.core.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAllByNameStartingWith(String nameStart);

}
