package uz.gilt.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.gilt.oauth2.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
