package movieapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.eq;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import movieapp.dto.ArtistSimple;
import movieapp.entity.Artist;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;


//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TestArtistServiceJpa {
	
	@MockBean //mock with Spring
	ArtistRepository artistRepository;
	

	
	@Autowired
	IArtistService artistService; 

	static ModelMapper modelMapper;
	
	@BeforeAll
	static void initModelMapper() {
		modelMapper = new ModelMapper();
	}
	@Test
	void GetById() {
		//given 
		int id = 1;
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		//perfect answer from mock
		Artist artistEntity = new Artist(name, birthdate);
		artistEntity.setId(id);
		
		given(artistRepository.findById(id)).willReturn(Optional.of(artistEntity));
		//when 
		Optional<ArtistSimple> optArtistSimpleDto = artistService.getById(id);
		//then 
		then(artistRepository)
			.should()
			.findById(eq(id));
		
		assertTrue(optArtistSimpleDto.isPresent(),"is present");
		
		optArtistSimpleDto.ifPresent(artistSimpleDto -> assertAll(
				()-> assertEquals(id, artistSimpleDto.getId(),"id"),
				()-> assertEquals(birthdate, artistSimpleDto.getBirthdate(),"birthdate"),
				()-> assertEquals(name, artistSimpleDto.getName(),"name")
				));
	}
	
	@Test
	void testGetByIdAbsent() {
				int id = 1;
				given(artistRepository.findById(id)).willReturn(Optional.empty());
				//when 
				Optional<ArtistSimple> optArtistSimpleEmpty = artistService.getById(id);
				//then 
				then(artistRepository)
					.should()
					.findById(eq(id));
				
				assertTrue(optArtistSimpleEmpty.isEmpty());
	}

	
	@Test
	void testAdd() {
		// 1. given
		// DTO to add
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		ArtistSimple artistSimpleDtoIn = new ArtistSimple(null, name, birthdate);
		// Entity response from mock repository
		int id = 1;
		Artist artistEntity = new Artist(name,birthdate);
		artistEntity.setId(id);
		given(artistRepository.save(any()))
			.willReturn(artistEntity);
		// 2. when
		ArtistSimple artistSimpleDtoOut = artistService.add(artistSimpleDtoIn);
		// 3. then
		then(artistRepository)
			.should()
			.save(any());
		assertNotNull(artistSimpleDtoOut.getId());
		assertEquals(id, artistSimpleDtoOut.getId()); // from repo response
		assertEquals(name, artistSimpleDtoOut.getName()); 
		assertEquals(birthdate, artistSimpleDtoOut.getBirthdate());
	}
	
	@Test
	void testGetByName() {
		
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		int id = 1;
		int id2 = 2;
		Artist artistEntity1 = new Artist(name, birthdate);
		Artist artistEntity2 = new Artist(name, birthdate);
		artistEntity1.setId(id);
		artistEntity2.setId(id2);
		
		given(artistRepository.findByName(eq(name))).willReturn(Stream.of(artistEntity1,artistEntity2));
		
		List<ArtistSimple> artistSimpleDtoOut = artistService.getByName(name);
		
		then(artistRepository)
		.should()
		.findByName(any());
		
		assertEquals(2,artistSimpleDtoOut.size());
		
		assertAll(artistSimpleDtoOut.stream()
				.map(asdo -> ()->assertEquals(name, asdo.getName());
		));
	}
	
	@Test
	void testUpdateArtist() {
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		int id = 1;
		Artist artistEntityIn = new Artist(name, birthdate);
		Artist artistEntityOut = new Artist(name,birthdate);
		artistEntityIn.setId(id);
		artistEntityOut.setId(id);
		
		given(artistRepository.updateArtist(eq(artistEntityIn))).willReturn(Optional.of(artistEntityOut));
		
		ArtistSimple artistSimpleDtoIn = new ArtistSimple(id, name, birthdate);
		
		Optional<ArtistSimple> optArtistSimpleDtoOut = artistService.updateArtist(artistSimpleDtoIn);
		
		then(artistRepository).should().updateArtist(any());
		
		optArtistSimpleDtoOut.ifPresent(asdo -> assertAll(
				()-> assertEquals(asdo.getId(), artistSimpleDtoIn.getId()),
				()-> assertEquals(asdo.getName(), artistSimpleDtoIn.getName()),
				()-> assertEquals(asdo.getBirthdate(),artistSimpleDtoIn.getBirthdate())
				));
		
		
	}
}
