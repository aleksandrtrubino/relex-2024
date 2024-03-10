package ru.trubino.farm.authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    boolean existsByAuthority(String authority);
    Optional<Authority> findByAuthority(String authority);
}
