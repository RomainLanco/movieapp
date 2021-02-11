package movieapp.controller;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import movieapp.dto.MovieSimple;
import movieapp.service.IMovieService;




@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	
	@Autowired
	private IMovieService movieService;
	
//	@Autowired
//	private ArtistRepository artistRepository;

	/**
	 * url /api/movies
	 * @return
	 */
	@GetMapping
	public List<MovieSimple> movies() {
//		return List.of(new Movie("Blade Runner",1982,117)
//				,new Movie("Kabir Singh",2019,173));
		
		return movieService.getAll();
	}
	
	/**
	 * url /api/movies/un
	 * @return
	 */
	
	@GetMapping("/{id}")
	@ResponseBody
	public Optional<MovieSimple> movie(@PathVariable("id") int id) {

		return movieService.getById(id);
	}
	
	@PostMapping
	@ResponseBody
	public MovieSimple addMovie(@RequestBody MovieSimple movie) {
//		System.out.println(movie);
		
		return movieService.add(movie);
	}
	
	@PutMapping
	public Optional<MovieSimple> updateMovie(@RequestBody MovieSimple movie) {

		return movieService.updateMovie(movie);
	}
	
	/**
	 * path /api/movies/director?mid=1&did=3
	 * @param mid
	 * @param did
	 * @return
	 */
//	@PutMapping("/director")
//	public Optional<Movie> setDirectorMovie(@RequestParam("mid") Integer idMovie,@RequestParam("did") Integer idDirector){
//		
//		Optional<Movie> optMovie = movieRepository.findById(idMovie);
//		Optional<Artist> optArtist = artistRepository.findById(idDirector);
//		
//		return optMovie.flatMap(m-> optArtist
//				.map(a-> {m.setDirector(a);return m;})
//				);
		//ce qui fait la même chose que :
//		if(optMovie.isEmpty() || optArtist.isEmpty()) {
//			return Optional.empty();
//		}
//		
//		else {
//			Movie movie = optMovie.get();
//			Artist artist = optArtist.get();
//			
//			movie.setDirector(artist);
//			
//			return optMovie;
//		}
//	}
	
//	@DeleteMapping
//	public Optional<Movie> deleteMovie(@RequestBody Movie movie) {
//		return deleteMovie2(movie.getId());
//	}
//	
//	@DeleteMapping("/{id}")
//	public Optional<Movie> deleteMovie2(@PathVariable("id") int id) {
//		
//		Optional<Movie> optMovieDb = movieRepository.findById(id);
//		optMovieDb.ifPresent(m ->
//				movieRepository.deleteById(m.getId())
//				);
//		
//		return optMovieDb;
//	}
//	
	@GetMapping("/byTitle")
	public List<MovieSimple> moviesByTitle(@RequestParam("t") String title){
		return movieService.moviesByTitle(title);
	}
//	
//	@GetMapping("/byTitleYear")
//	public List<Movie> moviesByTitleYear(@RequestParam("t") String title,@RequestParam(value = "y",required = false) Integer year){
//		if (year==null) return movieRepository.findByTitleOrderByTitle(title);
//		else
//		return movieRepository.findByTitleAndYear(title, year);
//	}
//	
//	@GetMapping("/byMinMax")
//	public List<Movie> moviesByMinMaxBetween(
//			@RequestParam(value="min",required=false) Integer yearMin,
//			@RequestParam(value="max",required=false) Integer yearMax
//			){
//		if(Objects.isNull(yearMin) && Objects.isNull(yearMax))
//			return List.of();
//		if(Objects.isNull(yearMax))
//			return movieRepository.findByYearGreaterThanEqual(yearMin);
//		if(Objects.isNull(yearMin))
//			return movieRepository.findByYearLessThanEqual(yearMax);
//		
//		return movieRepository.findByYearBetween(yearMin, yearMax);
//	}
}
