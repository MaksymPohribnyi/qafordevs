package net.proselyte.qafordevs.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.proselyte.qafordevs.dto.DeveloperDTO;
import net.proselyte.qafordevs.dto.ErrorDTO;
import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.exception.DeveloperNotFoundException;
import net.proselyte.qafordevs.exception.DeveloperWithDuplicateEmailException;
import net.proselyte.qafordevs.service.DeveloperService;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperRestControllerV1 {

	private final DeveloperService developerService;

	@PostMapping
	public ResponseEntity<?> createDeveloper(@RequestBody DeveloperDTO dto) {
		try {
			DeveloperEntity developerToCreate = dto.toEntity();
			DeveloperEntity savedDeveloper = developerService.saveDeveloper(developerToCreate);
			return ResponseEntity.ok(DeveloperDTO.fromEntity(savedDeveloper));
		} catch (DeveloperWithDuplicateEmailException e) {
			return ResponseEntity.badRequest().body(
					ErrorDTO.builder()
					.status(400)
					.message(e.getMessage())
					.build());
		}
	}

	@PutMapping
	public ResponseEntity<?> updateDeveloper(@RequestBody DeveloperDTO dto) {
		try {
			DeveloperEntity developerToUpdate = dto.toEntity();
			DeveloperEntity updatedDeveloper = developerService.updateDeveloper(developerToUpdate);
			return ResponseEntity.ok(DeveloperDTO.fromEntity(updatedDeveloper));
		} catch (DeveloperNotFoundException e) {
			return ResponseEntity.badRequest().body(
					ErrorDTO.builder()
					.status(400)
					.message(e.getMessage())
					.build());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDeveloperById(@PathVariable("id") Integer id) {
		try {
			DeveloperEntity obtainedDeveloper = developerService.getDeveloperById(id);
			return ResponseEntity.ok(DeveloperDTO.fromEntity(obtainedDeveloper));
		} catch (DeveloperNotFoundException e) {
			return ResponseEntity.status(404).body(
					ErrorDTO.builder()
					.status(404)
					.message(e.getMessage())
					.build());
		}
	}

	@GetMapping
	public ResponseEntity<?> getAllDevelopers() {
		List<DeveloperEntity> developers = developerService.getAllDevelopers();
		List<DeveloperDTO> result = developers.stream().map(DeveloperDTO::fromEntity).toList();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/specialty/{specialty}")
	public ResponseEntity<?> getAllActiveDevsBySpecialty(@PathVariable("specialty") String specialty) {
		List<DeveloperEntity> developers = developerService.getAllActiveDevelopersBySpecialty(specialty);
		List<DeveloperDTO> result = developers.stream().map(DeveloperDTO::fromEntity).toList();
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Integer id,
			@RequestParam(name = "isHard", defaultValue = "false") boolean isHard) {
		try {
			if (isHard) {
				developerService.hardDeleteById(id);
			} else {
				developerService.softDeleteById(id);
			}
			return ResponseEntity.ok().build();
		} catch (DeveloperNotFoundException e) {
			return ResponseEntity.status(404).body(
					ErrorDTO.builder()
					.status(404)
					.message(e.getMessage())
					.build());
		}
	}
	
}
