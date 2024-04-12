package net.proselyte.qafordevs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.exception.DeveloperNotFoundException;
import net.proselyte.qafordevs.exception.DeveloperWithDuplicateEmailException;
import net.proselyte.qafordevs.repository.DeveloperRepository;
import net.proselyte.qafordevs.utils.DataUtils;

@ExtendWith(MockitoExtension.class)
public class DeveloperServiceImplTests {

	@Mock
	private DeveloperRepository developerRepository;

	@InjectMocks
	private DeveloperServiceImpl serviceUnderTest;

	@Test
	@DisplayName("Test of save developer functionality")
	public void givenDeveloperToSave_whenSaveDeveloper_thenRepositoryIsCalled() {
		// given
		DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();
		BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(null);
		BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
				.willReturn(DataUtils.getJohnDoePersisted());
		// when
		DeveloperEntity savedDeveloper = serviceUnderTest.saveDeveloper(developerToSave);
		// then
		assertThat(savedDeveloper).isNotNull();
	}

	@Test
	@DisplayName("Test of exception when save duplicate email developer functionality")
	public void givenDeveloperWithDuplicateEmail_whenSaveDeveloper_thenExecptionIsThrown() {
		// given
		DeveloperEntity dublicateDeveloper = DataUtils.getJohnDoeTransient();
		BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(DataUtils.getJohnDoePersisted());
		// when
		assertThrows(DeveloperWithDuplicateEmailException.class,
				() -> serviceUnderTest.saveDeveloper(dublicateDeveloper));
		// then
		verify(developerRepository, never()).save(any(DeveloperEntity.class));
	}

	@Test
	@DisplayName("Test update developer functionality")
	public void givenDeveloperToUpdate_whenUpdateDeveloper_thenRepoMethodIsCalled() {
		// given
		DeveloperEntity developerToUpdate = DataUtils.getJohnDoePersisted();
		BDDMockito.given(developerRepository.existsById(anyInt())).willReturn(true);
		BDDMockito.given(developerRepository.save(any(DeveloperEntity.class))).willReturn(developerToUpdate);
		// when
		DeveloperEntity updatedDeveloper = serviceUnderTest.updateDeveloper(developerToUpdate);
		// then
		assertThat(updatedDeveloper).isNotNull();
		verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
	}

	@Test
	@DisplayName("Test update non existed developer functionality")
	public void givenDeveloperToUpdate_whenUpdateDeveloper_thenExceptionIsThrown() {
		// given
		DeveloperEntity developerToUpdate = DataUtils.getJohnDoePersisted();
		BDDMockito.given(developerRepository.existsById(anyInt())).willReturn(false);
		// when
		assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.updateDeveloper(developerToUpdate));
		// then
		verify(developerRepository, never()).save(any(DeveloperEntity.class));
	}

	@Test
	@DisplayName("Test get developer by id functionality")
	public void givenDeveloperId_whenGetById_thenDeveloperIsReturned() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt()))
				.willReturn(Optional.of(DataUtils.getJohnDoePersisted()));
		// when
		DeveloperEntity obtainedDeveloper = serviceUnderTest.getDeveloperById(1);
		// then
		assertThat(obtainedDeveloper).isNotNull();
	}

	@Test
	@DisplayName("Test get developer by incorrect id functionality")
	public void givenIncorrectDeveloperId_whenGetById_thenExceptionIsTrown() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt())).willThrow(DeveloperNotFoundException.class);
		// when
		assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.getDeveloperById(1));
		// then
	}

	@Test
	@DisplayName("Test get developer by email functionality")
	public void givenEmail_whenGetDeveloperByEmail_thenDeveloperIsReturned() {
		// given
		String email = "JohnDoe@mail.com";
		BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(DataUtils.getJohnDoePersisted());
		// when
		DeveloperEntity obtainedDeveloper = serviceUnderTest.getDeveloperByEmail(email);
		// then
		assertThat(obtainedDeveloper).isNotNull();
	}

	@Test
	@DisplayName("Test get developer by incorrect email functionality")
	public void givenIncorrectEmail_whenGetDeveloperByEmail_thenExceptionIsTrown() {
		// given
		String email = "JohnDoe@mail.com";
		BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(null);
		// when
		assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.getDeveloperByEmail(email));
		// then
	}

	@Test
	@DisplayName("Test get all developers functionality")
	public void givenThreeDevelopers_whenGetAll_thenThreeDevelopersAreReturned() {
		// given
		DeveloperEntity firstDev = DataUtils.getJohnDoePersisted();
		DeveloperEntity secondDev = DataUtils.getMikeSmithPersisted();
		DeveloperEntity thirdDev = DataUtils.getFrankJonesPersisted();

		List<DeveloperEntity> developers = List.of(firstDev, secondDev, thirdDev);

		BDDMockito.given(developerRepository.findAll()).willReturn(developers);
		// when
		List<DeveloperEntity> obtainedDevelopers = serviceUnderTest.getAllDevelopers();
		// then
		assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
		assertThat(obtainedDevelopers.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("Test get all active developers by speclialty functionality")
	public void givenTwoDevelopers_whenGetAllActiveBySpecialty_thenTwoDevelopersAreReturned() {
		// given
		DeveloperEntity firstDev = DataUtils.getJohnDoePersisted();
		DeveloperEntity secondDev = DataUtils.getMikeSmithPersisted();

		List<DeveloperEntity> activeDevelopers = List.of(firstDev, secondDev);

		BDDMockito.given(developerRepository.findAllActiveBySpecialty(anyString())).willReturn(activeDevelopers);
		// when
		List<DeveloperEntity> obtainedDevelopers = serviceUnderTest.getAllActiveDevelopersBySpecialty("Java");
		// then
		assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
		assertThat(obtainedDevelopers.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("Test soft delete developer functionality")
	public void givenDeveloperId_whenSoftDeleteById_thenRepoMethodIsCalled() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt()))
				.willReturn(Optional.of(DataUtils.getFrankJonesPersisted()));
		// when
		serviceUnderTest.softDeleteById(1);
		// then
		verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
		verify(developerRepository, never()).deleteById(anyInt());
	}

	@Test
	@DisplayName("Test soft delete developer by incorrect id functionality")
	public void givenIncorrectDeveloperId_whenSoftDeleteById_thenExceptionIsThrown() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.empty());
		// when
		assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.softDeleteById(1));
		// then
		verify(developerRepository, never()).save(any(DeveloperEntity.class));
	}

	@Test
	@DisplayName("Test hard delete developer functionality")
	public void givenDeveloperId_whenHardDelete_thenRepoMethodIsCalled() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt()))
				.willReturn(Optional.of(DataUtils.getJohnDoePersisted()));
		// when
		serviceUnderTest.hardDeleteById(1);
		// then
		verify(developerRepository, times(1)).deleteById(anyInt());
	}

	@Test
	@DisplayName("Test hard delete developer by incorrect id functionality")
	public void givenIncorrectDeveloperId_whenHardDeleteById_thenExceptionIsThrown() {
		// given
		BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.empty());
		// when
		assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.hardDeleteById(1));
		// then
		verify(developerRepository, never()).save(any(DeveloperEntity.class));
	}

}
