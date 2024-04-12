package net.proselyte.qafordevs.service;

import java.util.List;

import net.proselyte.qafordevs.entity.DeveloperEntity;

public interface DeveloperService {

	DeveloperEntity saveDeveloper(DeveloperEntity developer);

	DeveloperEntity updateDeveloper(DeveloperEntity developer);

	DeveloperEntity getDeveloperById(Integer id);

	DeveloperEntity getDeveloperByEmail(String email);

	List<DeveloperEntity> getAllDevelopers();

	List<DeveloperEntity> getAllActiveDevelopersBySpecialty(String specialty);

	void softDeleteById(Integer id);

	void hardDeleteById(Integer id);

}
