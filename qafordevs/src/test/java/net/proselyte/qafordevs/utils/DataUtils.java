package net.proselyte.qafordevs.utils;

import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.entity.Status;

public class DataUtils {

	public static DeveloperEntity getJohnDoeTransient() {
		return DeveloperEntity.builder()
				.firstName("John")
				.lastName("Doe")
				.email("JohnDoe@mail.com")
				.specialty("Java")
				.status(Status.ACTIVE)
				.build();
	}
	public static DeveloperEntity getMikeSmithTransient() {
		return DeveloperEntity.builder()
				.firstName("Mike")
				.lastName("Smith")
				.email("MikeSmith@mail.com")
				.specialty("Java")
				.status(Status.ACTIVE)
				.build();
	}
	public static DeveloperEntity getFrankJonesTransient() {
		return DeveloperEntity.builder()
				.firstName("Frank")
				.lastName("Jones")
				.email("FrankJones@mail.com")
				.specialty("Python")
				.status(Status.DELETED)
				.build();
	}
	public static DeveloperEntity getJohnDoePersisted() {
		return DeveloperEntity.builder()
				.id(1)
				.firstName("John")
				.lastName("Doe")
				.email("JohnDoe@mail.com")
				.specialty("Java")
				.status(Status.ACTIVE)
				.build();
	}
	public static DeveloperEntity getMikeSmithPersisted() {
		return DeveloperEntity.builder()
				.id(1)
				.firstName("Mike")
				.lastName("Smith")
				.email("MikeSmith@mail.com")
				.specialty("Java")
				.status(Status.ACTIVE)
				.build();
	}
	public static DeveloperEntity getFrankJonesPersisted() {
		return DeveloperEntity.builder()
				.id(1)
				.firstName("Frank")
				.lastName("Jones")
				.email("FrankJones@mail.com")
				.specialty("Python")
				.status(Status.DELETED)
				.build();
	}
	
}
