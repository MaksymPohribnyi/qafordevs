package net.proselyte.qafordevs.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.utils.DataUtils;

@DataJpaTest
public class DeveloperRepositoryTests {

	@Autowired
	private DeveloperRepository developerRepository;

	@BeforeEach
	public void setUp() {
		developerRepository.deleteAll();
	}

	@Test
	@DisplayName("Test save developer functionality")
	public void givenDeveloperObject_whenSave_ThenDeveloperIsCreated() {

		// given
		DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();

		// when
		DeveloperEntity savedDeveloper = developerRepository.save(developerToSave);

		// then
		assertThat(savedDeveloper).isNotNull();
		assertThat(savedDeveloper.getId()).isNotNull();
	}

	@Test
	@DisplayName("Test update developer functionality")
	public void givenDeveloperToUpdate_whenChangeEmail_thenDeveloperIsUpdated() {
		// given
		DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerToSave);
		// when
		String updatedEmail = "updatedEmial@mail.com";
		developerToSave.setEmail(updatedEmail);
		DeveloperEntity updatedDeveloper = developerRepository.save(developerToSave);
		// then
		assertThat(updatedDeveloper).isNotNull();
		assertThat(updatedDeveloper.getEmail()).isEqualTo(updatedEmail);
	}

	@Test
	@DisplayName("Test found developer by id functionality")
	public void givenId_whenGetDevloperById_thenDeveloperIsReturned() {
		// given
		DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerToSave);
		// when
		DeveloperEntity foundedDeveloper = developerRepository.findById(developerToSave.getId()).orElse(null);
		// then
		assertThat(foundedDeveloper).isNotNull();
	}

	@Test
	@DisplayName("Test developer not found functionality")
	public void givenId_whenGetDevloperById_thenDeveloperIsNull() {
		// given
		// when
		DeveloperEntity foundedDeveloper = developerRepository.findById(1).orElse(null);
		// then
		assertThat(foundedDeveloper).isNull();
	}

	@Test
	@DisplayName("Test find all developers functionality")
	public void givenThreeDevelopers_whenFindAll_thenThreeDevelopersReturned() {
		// given
		DeveloperEntity firstDev = DataUtils.getJohnDoeTransient();
		DeveloperEntity secondDev = DataUtils.getMikeSmithTransient();
		DeveloperEntity thirdDev = DataUtils.getFrankJonesTransient();
		developerRepository.saveAll(List.of(firstDev, secondDev, thirdDev));
		// when
		List<DeveloperEntity> listOfDevs = developerRepository.findAll();
		// then
		assertThat(CollectionUtils.isEmpty(listOfDevs)).isFalse();
		assertThat(listOfDevs.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("Test find developer by emial functionality")
	public void givenEmail_whenFindByEmail_thenDeveloperIsReturned() {

		// given
		DeveloperEntity johnDoeDev = DataUtils.getJohnDoeTransient();
		developerRepository.save(johnDoeDev);
		// when
		DeveloperEntity obtainedDev = developerRepository.findByEmail(johnDoeDev.getEmail());
		// then
		assertThat(obtainedDev).isNotNull();
		assertThat(obtainedDev.getEmail()).isEqualTo(johnDoeDev.getEmail());

	}

	@Test
	@DisplayName("Test of searching active developers by specialty functionality")
	public void givenThreeDevsAndTwoAreActive_whenFindAllActiveBySpecialty_thenOnlyTwoDevsIsReturned() {
		// given
		DeveloperEntity firstDev = DataUtils.getJohnDoeTransient();
		DeveloperEntity secondDev = DataUtils.getMikeSmithTransient();
		DeveloperEntity thirdDev = DataUtils.getFrankJonesTransient();
		developerRepository.saveAll(List.of(firstDev, secondDev, thirdDev));
		// when
		List<DeveloperEntity> activeJavaDevs = developerRepository.findAllActiveBySpecialty("Java");
		// then
		assertThat(CollectionUtils.isEmpty(activeJavaDevs)).isFalse();
		assertThat(activeJavaDevs.size()).isEqualTo(2);

	}

}
