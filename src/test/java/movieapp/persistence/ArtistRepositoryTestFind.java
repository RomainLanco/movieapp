package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import movieapp.entity.Artist;

@DataJpaTest
class ArtistRepositoryTestFind {

	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	EntityManager entityManager;
	
	private List<Integer> ids;
	
	@BeforeEach
	void init() {
		var artists = List.of(new Artist("Steve McQueen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7))
				,new Artist("Steve McQueen", LocalDate.of(1969, 10, 9))
				,new Artist("Alfred Hitcock"));
		artists.forEach(entityManager::persist);
		entityManager.flush();
		
		 ids = artists.stream()
		.map(Artist::getId) // = map(a-> a.getId())
		.collect(Collectors.toList());
	}
	@Test
	void testFindAll() {
		
		
		var artistsFound = artistRepository.findAll();
		assertEquals(3, artistsFound.size(),"nb d'artistes trouvés dans la database");
		
	}
	@Test
	void testFindbyId() {
		
		
		var artistsFound = artistRepository.findById(ids.get(0));
		artistsFound.ifPresent(a -> assertEquals(ids.get(0), a.getId(),"id artist"));
		
	}
	
	@Test
	void testFindByName() {
//		var artists = List.of(new Artist("Steve McQueen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7))
//				,new Artist("Steve McQueen", LocalDate.of(1969, 10, 9))
//				,new Artist("Alfred Hitcock"));
//		artists.forEach(entityManager::persist);
//		entityManager.flush();
		
		var artistsFound = artistRepository.findByNameIgnoreCase("steve McQueen");
		
		assertEquals(2, artistsFound.size(),"nb d'artistes avec pseudo Steve McQueen");
		assertAll(artistsFound.stream().map(a -> ()->assertEquals("Steve McQueen",a.getName(),"artist name")));
	}
	@Test
	void testFindByBirthdate() {
//		var artists = List.of(new Artist("Steve McQueen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7))
//				,new Artist("Steve McQueen", LocalDate.of(1969, 10, 9))
//				,new Artist("Alfred Hitcock"));
//		artists.forEach(entityManager::persist);
//		entityManager.flush();
		
		var artistsFound = artistRepository.findByBirthdate(LocalDate.of(1969, 10, 9));
		
		
		assertAll(artistsFound.map(a-> {System.out.println(a);return a;}).
				map(a -> ()->assertEquals(LocalDate.of(1969, 10, 9),a.getBirthdate(),"artist birthdate")));
	}
	@Test
	void testArtistNull() {
		assertThrows(DataIntegrityViolationException.class, ()->artistRepository.save(new Artist()));
	}

}
