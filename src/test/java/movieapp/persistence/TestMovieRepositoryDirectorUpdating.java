package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.entity.Artist;
import movieapp.entity.Movie;

@DataJpaTest
class TestMovieRepositoryDirectorUpdating {

	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	void testSaveMovieWithDirector() {
		Artist clint = new Artist("Clint Eastwood", LocalDate.of(1930, 5, 31));
		Artist todd = new Artist("Todd Phillips", LocalDate.of(1970, 12, 20));
		Movie unforgiven = new Movie("Unforgiven",1992,130,clint);
		Movie hangover = new Movie("The Hangover",2009,90,todd);
		List<Artist> artists = List.of(clint, todd);
		List<Movie> movies = List.of( hangover,unforgiven);
		
		artists.forEach(entityManager::persist); //non necessaire si persist en cascade
		movies.forEach(m -> entityManager.persist(m));
		entityManager.flush();
//		System.out.println("Write :"+hangover+ "with director : "+ hangover.getDirector());
		entityManager.clear();
		Movie movieRead = entityManager.find(Movie.class, hangover.getId());
//		System.out.println(movieRead + "with director : "+ movieRead.getDirector());
		assertNotNull(movieRead.getDirector());
		
		
	}
	
	@Test
	void testSetDirectorMovieAndArtist() {
		
		//write data in database
		Artist todd = new Artist("Todd Phillips", LocalDate.of(1970, 12, 20));
		Movie hangover = new Movie("The Hangover",2009,90);
		
		entityManager.persist(todd);
		entityManager.persist(hangover);
		entityManager.flush();
		
		int idTodd = todd.getId();
		int idHangover = hangover.getId();
		//clear hibernate cache
		entityManager.clear();
		
		//read movie & artist from database
		
		var optArtistRead = artistRepository.findById(idTodd);
		var optMovieRead = movieRepository.findById(idHangover);
		assertTrue(optMovieRead.isPresent());
		assertTrue(optArtistRead.isPresent());
		
		var artistRead = optArtistRead.get();
		var movieRead = optMovieRead.get();
		
		System.out.println("Read : "+artistRead);
		System.out.println("Read : "+ movieRead +"with Director :" +movieRead.getDirector());
		
		//set association
		
		movieRead.setDirector(todd);
		
		//synchro
		entityManager.flush();
		
		var updatedMovieRead = movieRepository.findById(idHangover);
		
		System.out.println("Update Read : "+updatedMovieRead.get()+" with director : " + updatedMovieRead.get().getDirector());
		
	}

}
