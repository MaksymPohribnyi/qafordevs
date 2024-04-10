package net.proselyte.qafordevs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.proselyte.qafordevs.entity.DeveloperEntity;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer> {

	DeveloperEntity findByEmail(String email);

	@Query("SELECT d FROM DeveloperEntity d WHERE d.status = 'ACTIVE' AND d.specialty = ?1")
	List<DeveloperEntity> findAllActiveBySpecialty(String specialty);

}
