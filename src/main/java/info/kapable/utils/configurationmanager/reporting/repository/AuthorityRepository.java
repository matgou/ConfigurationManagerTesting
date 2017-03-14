package info.kapable.utils.configurationmanager.reporting.repository;

import info.kapable.utils.configurationmanager.reporting.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
