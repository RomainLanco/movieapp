package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import movieapp.entity.Movie;


@DataJpaTest
class MovieRepositoryTest {

	Movie movie;
	
	
	@Autowired
	private MovieRepository movieRepository;
	
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
	String title= "Blade Runner";
	Integer year = 1982;
	Integer duration = 173;
	String director = "IdontKnow";
	
	movie = new Movie();
	movie.setTitle(title);
	movie.setDuration(duration);
	movie.setYear(year);
	
	
	
	}
	
	@Test
	void testFindByYearBetweenOrder() {
		
		
		var movies = List.of(new Movie("Blade Runner", 1934),
				new Movie("Star Wars",2020)
				,new Movie("Dr No",1962)
				,new Movie("Outside the Wire",2021)
				, new Movie("Wonder Woman 1984",2020)
				,new Movie("Tyler Rake",2020)
				,new Movie("Tenet",2020)
				);
		
		
		//for(Movie m : movies){entityManager.persist(m);} --> est remplacé par
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		var moviesFound = movieRepository.findByYearBetween(1934, 2020, Sort.by(Direction.DESC,"year","title"));
		System.out.println(moviesFound);
	}
	@Test
	void testGreaterThanEqual() {
		var movies = List.of(new Movie("the Man who knew too much", 1934),
				new Movie("Wonder woman",2020), new Movie("Men in Black", 1997)
				,new Movie("Iron Man",2008)
				);
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		//where year >= 2000
		var moviesFound = movieRepository.findByYearGreaterThanEqual(2000);
		assertEquals(2, moviesFound.size(),"nb movie >=2000");
		assertAll(moviesFound.stream().map(m-> ()-> assertTrue(m.getYear()>=2000)));
		//where year between 2000 & 2009
		
		
		//where title =LionKing & year=1994
	}
	@Test
	void testBetween() {
		var movies = List.of(new Movie("the Man who knew too much", 1934),
				new Movie("Wonder woman",2020), new Movie("Men in Black", 1997)
				,new Movie("Iron Man",2008)
				);
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		
		//where year between 2000 & 2009
		var moviesFound = movieRepository.findByYearBetween(2000, 2009);
		assertEquals(1, moviesFound.size(),"movies between 2000 & 2009");
		assertAll(moviesFound.stream().map(m-> ()->assertTrue(m.getYear()>2000 && m.getYear()<2009,m.getTitle()+"is not between 2000 & 2009 ("+m.getYear()+")")));
		
	}
	@Test
	void testTitleAndYear() {
		var movies = List.of(new Movie("The Lion King", 1994),
				new Movie("The Lion King",2019), new Movie("Men in Black", 1997)
				,new Movie("Iron Man",2008)
				);
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		
		//where title =LionKing & year=1994
		
		var moviesFound = movieRepository.findByTitleAndYear("The Lion King", 1994);
		assertAll(moviesFound.stream().map(m-> ()->assertTrue(m.getTitle().equals("The Lion King") && m.getYear()==1994,"lion king & 1994")));
		
		//where duration is NULL
	}
	
	@Test
	void testDurationNull() {
		var movies = List.of(new Movie("The Lion King", 1994),
				new Movie("The Lion King",2019), new Movie("Men in Black", 1997)
				,new Movie("Iron Man",2008),movie
				);
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		
		//where duration is NULL
		var moviesFound = movieRepository.findByDurationNull();
		
		assertAll(moviesFound.stream().map(m-> ()->assertNull(m.getDuration(),"duration not null for "+m.getTitle())));
		
	}
	
	@Test
	void testFindByTitleOrder() {
		
		
		var movies = List.of(new Movie("Blade Runner", 1934),
				new Movie("Star Wars",2020)
				,new Movie("Dr No",1962)
				,new Movie("Outside the Wire",2021)
				, new Movie("Wonder Woman 1984",2020)
				,new Movie("Tyler Rake",2020)
				,new Movie("Tenet",2020)
				);
		
		
		//for(Movie m : movies){entityManager.persist(m);} --> est remplacé par
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		var moviesFound = movieRepository.findByYearOrderByTitle(2020);
		
		
		assertAll(moviesFound.stream().map(
				m -> ()->assertEquals(2020,m.getYear(),"year")));
		System.out.println(moviesFound);
	}
	
	@Test
	void testFindByTitle() {
		String title= "Blade Runner 2";
		Integer year = 1982;
		
		Movie movie2 = new Movie(title,year);
		
		var movies = List.of(new Movie(title, 1934),
				new Movie(title,1956)
				);
		
		entityManager.persist(movie);
		entityManager.persist(movie2);
		//for(Movie m : movies){entityManager.persist(m);} --> est remplacé par
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		var moviesFound = movieRepository.findByTitleOrderByTitle(title);
		
		assertEquals(3, moviesFound.size());
		
		assertAll(moviesFound.stream().map(
				m -> ()->assertEquals(title,m.getTitle(),"title")));
	}
	
	@Test
	void testFindByContainsTitle() {
		
		
		var movies = List.of(new Movie("the Man who knew too much", 1934),
				new Movie("Wonder woman",2020), new Movie("Men in Black", 1997)
				);
		
		entityManager.persist(movie);
	
		//for(Movie m : movies){entityManager.persist(m);} --> est remplacé par
		movies.forEach(entityManager::persist);
		entityManager.flush();
		
		var moviesFound = movieRepository.findByTitleContainingIgnoreCase("man");
		
		assertEquals(2, moviesFound.size());
		
		assertAll(moviesFound.stream().map(
				m -> ()->assertTrue(m.getTitle().toLowerCase().contains("man"),"man not in title")));
	}
	
	
	
	@Test
	void testSaveMovie() {
		

		
		movieRepository.save(movie);
		
		assertNotNull(movie.getId());
		
		
		//useless mais pour montrer l'utilisation de l'optionnal
		var optMovieFromRepo = movieRepository.findById(movie.getId());
		var movieFromRepo = optMovieFromRepo.get();
		assertEquals(movie, movieFromRepo);
		
		System.out.println(movieFromRepo);
		
	}
	
	@Test
	void testSaveMovie2() {
		

		
		movieRepository.save(movie);
		
		movieRepository.findById(movie.getId())
		.ifPresent( m -> assertEquals(movie,m));
		
	}

	
	@ParameterizedTest
	@ValueSource(strings= {"Z","Blade Runner","Night of the Day of the Dawn of the Son of the Bride of the Return of the Revenge of the Terror of the Attack of the Evil Mutant Hellbound Flesh Eating Crawling Alien Zombified Subhumanoid Living Dead, Part 5"})
	void testSaveTitle(String title) {
		movie.setTitle(title);
		movieRepository.save(movie);
		
		assertNotNull(movie.getId());
		
	}
	@Test
	void testTitleNullNOK() {
		movie.setTitle(null);
		assertThrows(DataIntegrityViolationException.class, () -> movieRepository.save(movie));
		
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1888,1982,Integer.MAX_VALUE})
	void testSaveYear(int year) {
		movie.setYear(year);
		movieRepository.save(movie);
		
		assertNotNull(movie.getId());
		
	}
	
	@Test
	void testYearNullNOK() {
		movie.setYear(null);
		assertThrows(DataIntegrityViolationException.class, () -> movieRepository.save(movie));
		
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1,177,Integer.MAX_VALUE})
	@NullSource
	void testSaveDurationNullOK(Integer duration) {
		movie.setDuration(duration);
		movieRepository.save(movie);
		
		assertNotNull(movie.getId());
		
	}
	
	

	
	
}
