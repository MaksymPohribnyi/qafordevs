package net.proselyte.qafordevs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.entity.Status;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDTO {

	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String specialty;

	private Status status;

	public DeveloperEntity toEntity() {
		return DeveloperEntity.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.specialty(specialty)
				.status(status)
				.build();
	}
	
	public static DeveloperDTO fromEntity(DeveloperEntity developer) {
		return DeveloperDTO.builder()
				.id(developer.getId())
				.firstName(developer.getFirstName())
				.lastName(developer.getLastName())
				.email(developer.getEmail())
				.specialty(developer.getSpecialty())
				.status(developer.getStatus())
				.build();
	}

}
