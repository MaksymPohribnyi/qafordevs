package net.proselyte.qafordevs.it;

import static org.assertj.core.api.Assertions.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.proselyte.qafordevs.dto.DeveloperDTO;
import net.proselyte.qafordevs.entity.DeveloperEntity;
import net.proselyte.qafordevs.entity.Status;
import net.proselyte.qafordevs.repository.DeveloperRepository;
import net.proselyte.qafordevs.utils.DataUtils;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItDeveloperRestControllerV1Tests extends AbstractRestContollerBaseTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DeveloperRepository developerRepository;
	
	@BeforeEach
	public void setUp() {
		developerRepository.deleteAll();
	}

	@Test
	@DisplayName("Test create developer functionality")
	public void givenDeveloperDTO_whenCreateDeveloper_thenSuccesRespoonse() throws Exception {
		// given
		DeveloperDTO developerDTO = DataUtils.getDTOJohnDoeTransient();
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/developers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(developerDTO)));
		// then
		result
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
	}
	
	@Test
	@DisplayName("Test create developer with duplicate email functionality")
	public void givenDeveloperDTO_whenCreateDeveloper_thenErrorResponse() throws Exception {
		// given
		DeveloperDTO developerDTO = DataUtils.getDTOJohnDoeTransient();
		developerRepository.save(developerDTO.toEntity());
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/developers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(developerDTO)));
		// then
		result
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message",
						CoreMatchers.is("Developer with defined email is already exists")));
	}
	
	@Test
	@DisplayName("Test update developer functionality")
	public void givenDeveloperDTO_whenUpdateDeveloper_thenSuccesRespoonse() throws Exception {
		// given
		String updatedEmail = "updatedEmail@mail.com";
		DeveloperEntity developerEntity = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerEntity);
		DeveloperDTO developerDTO = DataUtils.getDTOJohnDoeTransient();
		developerDTO.setEmail(updatedEmail);
		developerDTO.setId(developerEntity.getId());
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/developers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(developerDTO)));
		// then
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmail)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
	}
	
	@Test
	@DisplayName("Test create developer with incorrect id functionality")
	public void givenDeveloperDTOWithIncorrectID_whenUpdateDeveloper_thenErrorResponse() throws Exception {
		// given
		DeveloperDTO developerDTO = DataUtils.getDTOJohnDoePersisted();
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/developers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(developerDTO)));
		// then
		result
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message",
						CoreMatchers.is("Developer not found by defined id")));
	}
	
	@Test
	@DisplayName("Test get developer by id functionality")
	public void givenDeveloperId_whenGetDeveloperById_thenSuccesRespoonse() throws Exception{
		//given
		DeveloperEntity developerEntity = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerEntity);
		//when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/developers/" + developerEntity.getId())
				.contentType(MediaType.APPLICATION_JSON));
		//then
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")));
	}
	
	@Test
	@DisplayName("Test get developer by incorrect id functionality")
	public void givenIncorrectDeveloperID_whenGetDeveloperById_thenErrorResponse() throws Exception {
		// given
		
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/developers/1")
				.contentType(MediaType.APPLICATION_JSON));
		//then
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(404))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));
	}

	@Test
	@DisplayName("Test soft delete functionality")
	public void givenDeveloperId_whenSoftDeleteById_thenSuccesResponse() throws Exception{
		//given
		DeveloperEntity developerEntity = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerEntity);
		//when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/developers/" + developerEntity.getId())
				.contentType(MediaType.APPLICATION_JSON));
		//then
		DeveloperEntity obtainedDeveloper = developerRepository.findById(developerEntity.getId()).orElse(null);
		
		assertThat(obtainedDeveloper).isNotNull();
		assertThat(obtainedDeveloper.getStatus()).isEqualTo(Status.DELETED);
		
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@DisplayName("Test soft delete by incorrect id functionality")
	public void givenIncorrectDeveloperId_whenSoftDeleteById_thenErrorResponse() throws Exception{
		// given
		
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/developers/1")
				.contentType(MediaType.APPLICATION_JSON));
		//then
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));
	}
	
	@Test
	@DisplayName("Test hard delete functionality")
	public void givenDeveloperId_whenHardDeleteById_thenSuccesResponse() throws Exception{
		//given
		DeveloperEntity developerEntity = DataUtils.getJohnDoeTransient();
		developerRepository.save(developerEntity);
		//when
		ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/developers/" + developerEntity.getId() + "?isHard=true")
						.contentType(MediaType.APPLICATION_JSON));
		// then
		DeveloperEntity obtainedDeveloper = developerRepository.findById(developerEntity.getId()).orElse(null);
		assertThat(obtainedDeveloper).isNull();
		
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@DisplayName("Test hard delete by incorrect id functionality")
	public void givenIncorrectDeveloperId_whenHardDeleteById_thenErrorResponse() throws Exception{
		// given
		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/developers/1?isHard=true")
				.contentType(MediaType.APPLICATION_JSON));
		//then
		result
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Developer not found")));
	}
	
}
