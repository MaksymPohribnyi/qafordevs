package net.proselyte.qafordevs.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.entity.Status;
import net.proselyte.qafordevs.exception.DeveloperNotFoundException;
import net.proselyte.qafordevs.exception.DeveloperWithDuplicateEmailException;
import net.proselyte.qafordevs.repository.DeveloperRepository;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

	private final DeveloperRepository developerRepository;

	@Override
	public DeveloperEntity saveDeveloper(DeveloperEntity developer) {
		DeveloperEntity duplicateCandidate = developerRepository.findByEmail(developer.getEmail());
		if (Objects.nonNull(duplicateCandidate)) {
			throw new DeveloperWithDuplicateEmailException("Developer with defined email is already exist");
		}
		return developerRepository.save(developer);
	}

	@Override
	public DeveloperEntity updateDeveloper(DeveloperEntity developer) {
		boolean isExists = developerRepository.existsById(developer.getId());
		if (!isExists) {
			throw new DeveloperNotFoundException("Developer not found");
		}
		return developerRepository.save(developer);
	}

	@Override
	public DeveloperEntity getDeveloperById(Integer id) {
		return developerRepository.findById(id)
				.orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
	}

	@Override
	public DeveloperEntity getDeveloperByEmail(String email) {
		DeveloperEntity obtainedDeveloper = developerRepository.findByEmail(email);

		if (Objects.isNull(obtainedDeveloper)) {
			throw new DeveloperNotFoundException("Developer not found by email");
		}
		return obtainedDeveloper;
	}

	@Override
	public List<DeveloperEntity> getAllDevelopers() {
		return developerRepository.findAll().stream().filter(d -> d.getStatus().equals(Status.ACTIVE))
				.collect(Collectors.toList());
	}

	@Override
	public List<DeveloperEntity> getAllActiveDevelopersBySpecialty(String specialty) {
		return developerRepository.findAllActiveBySpecialty(specialty);
	}

	@Override
	public void softDeleteById(Integer id) {
		DeveloperEntity obtainedDeveloper = developerRepository.findById(id)
				.orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
		obtainedDeveloper.setStatus(Status.DELETED);
		developerRepository.save(obtainedDeveloper);
	}

	@Override
	public void hardDeleteById(Integer id) {
		DeveloperEntity obtainedDeveloper = developerRepository.findById(id)
				.orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
		developerRepository.deleteById(obtainedDeveloper.getId());
	}

}
