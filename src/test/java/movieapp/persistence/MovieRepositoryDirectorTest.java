package movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import movieapp.entity.Artist;
import movieapp.entity.Movie;

@DataJpaTest
class MovieRepositoryDirectorTest {

	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	TestEntityManager entityManager;
	
//	private List<Movie> moviesTodd;
//	private List<Movie> moviesClint;
	Movie movieTodd;
	Movie movieA;
	Artist clint;
	
	@BeforeEach
	void init() {
		//creation des listes de movies par director
		 movieTodd = new Movie("The Hangover", 2009);
		
		
		 movieA= new Movie("Alien",1979);
		 
		 var movieUnforgiven = 	new Movie("Unforgiven",1992);
		 var movieGranTorino = new Movie("Gran Torino",2008);
		 var movieInvictus = new Movie("Invictus",2009);
		 
		 var moviesClint = List.of(movieUnforgiven,movieGranTorino,movieInvictus);
		 
		 
		
		//creation des director+actors + persist
		 clint = new Artist("Clint Eastwood", LocalDate.of(1930, 5, 31));
		var todd = new Artist("Todd Phillips", LocalDate.of(1970, 12, 20));
		var morgan = new Artist("Morgan Freeman", LocalDate.of(1937, 6, 1));
		var bradley = new Artist("Bradley Cooper");
		var zach = new Artist("Zach Galifianakis");
		
		Stream.of(clint, todd, morgan, bradley, zach).forEach(entityManager::persist);
		
		
		//set director des movies
		moviesClint.forEach(m->m.setDirector(clint));
		movieTodd.setDirector(todd);
		
		//set actors
		movieUnforgiven.setActors(List.of(clint, morgan));
		 movieGranTorino.setActors(List.of(clint));
		 movieInvictus.setActors(List.of(morgan));
		 movieTodd.setActors(List.of(bradley, zach));
		
		//persist des movies
		moviesClint.forEach(m->entityManager.persist(m));
		entityManager.persist(movieTodd);
		entityManager.persist(movieA);
		
		//sauvegarde du cache dans database + clear du cache
		entityManager.flush();
		entityManager.clear();
		
	}
	
	
	@Test
	void testFindMovieWithExistingDirector() {
		
		int idMovie= movieTodd.getId();
		var optMovie = movieRepository.findById(idMovie);
		
		//check if movie has director
		assertTrue(optMovie.isPresent(),"movie present");
		assertNotNull(optMovie.get().getDirector(),"director present");
		
	}

	@Test
	void testFindMovieWithNoDirector() {
		int idMovie = movieA.getId();
		var optMovie = movieRepository.findById(idMovie);
		
		assertTrue(optMovie.isPresent(),"movie present");
		assertNull(optMovie.get().getDirector(),"director present");
	}
	
	@Test
	void testFindMoviesWithDirectorClint() {
		
		var optMovies = movieRepository.findByDirector(clint);
		var optMovies2 = movieRepository.findByDirectorNameContainingIgnoreCase("clint");
		var optClintDatabase = artistRepository.findById(clint.getId());
		
		var clintDatabase = optClintDatabase.get();
		assertAll(optMovies.stream()
				.map(m->
					()-> assertEquals(clintDatabase, m.getDirector(), "director of movie")
						));
		
		assertAll(optMovies2.stream()
				.map(m->
					()-> assertEquals("Clint Eastwood", m.getDirector().getName(), "director of movie")
						));
		
		assertAll(optMovies2.stream()
			.map(m->m.getDirector())
			.map(a -> a.getName())
			.map(n->()->assertEquals("Clint Eastwood",n,"director name")));
		
	}
	
	@Test
	void testFindWithActors() {
		int idMovie = movieTodd.getId();
		var movie = movieRepository.getOne(idMovie); //comme un findByID mais sans l'idée des optional -- bien en test mais pas si on est pas sur d'avoir un movie avec cet id
		var actors = movie.getActors();
		System.out.println("Movie : "+movie+"with actors : "+actors);
		assertEquals(2, actors.size());
		
		
	}
	
	@Test
	void testFindMovieWithNoActors() {
		int idMovie = movieA.getId();
		var movie = movieRepository.getOne(idMovie); //comme un findByID mais sans l'idée des optional -- bien en test mais pas si on est pas sur d'avoir un movie avec cet id
		var actors = movie.getActors();
		System.out.println("Movie : "+movie+"with actors : "+actors);
		assertEquals(0, actors.size());
	}
	
	@Test
	void testFindMoviesWithActorClint() {
		
		String name = "Clint Eastwood";
		
		var moviesFound = movieRepository.findByActorsName(name);
		
		System.out.println(moviesFound);
		
		assertEquals(2, moviesFound.size(), "number movies");
		
//		assertAll(
//				moviesFound.stream()
//				.map(Movie::getActors)
//				.map(Artist::getName)
//				.map(n-> ()-> assertEquals(name, n, "director name"))
//				);
		
		for(var m : moviesFound) {
			var actors = m.getActors();
			assertTrue(actors.stream()
			.anyMatch(a -> a.getName().equals(name))
			,"at least one actor named clint eastwood");
			
		}
		
	}
	
	
}
