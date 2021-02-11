package movieapp.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapp.dto.IMovieAfterYear;
import movieapp.dto.INameYearTitle;
import movieapp.dto.MovieStat;
import movieapp.dto.NameYearTitle;
import movieapp.entity.Artist;
import movieapp.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{

	
	
	List<Movie> findByTitleOrderByTitle(String title);
	
	
	List<Movie> findByTitleContainingIgnoreCase(String title);
	
	List<Movie> findByYearOrderByTitle(int year);
	
	List<Movie> findByTitleAndYear(String title, int Year);
	
	//where year >= 2000
	List<Movie> findByYearGreaterThanEqual(int yearMin);
	
	List<Movie> findByYearLessThanEqual(int yearMax);
	
	//where year between 2000 & 2009
	List<Movie> findByYearBetween(int year1,int year2);
	List<Movie> findByYearBetween(int year1,int year2,Sort sort);
	
	
	//where title =LionKing & year=1994
	List<Movie> findByDurationNull();
	
	List<Movie> findByDirector(Artist director);
	List<Movie> findByDirectorNameContainingIgnoreCase(String directorName);


	List<Movie> findByActorsName(String name);
	
	@Query("select coalesce(sum(m.duration),0) from Movie m where m.year between ?1 and ?2")
	Long totalDuration(int yearMin, int yearMax);
	
	@Query("select avg(m.duration) from Movie m where m.year between :yearMin and :yearMax")
	Optional<Double> averageDuration(int yearMin, int yearMax);
	
	
	@Query("select new movieapp.dto.MovieStat(count(m), min(year), max(year)) from Movie m")
	MovieStat statistics() ;
	
	@Query("select m.year as year, count(*) as nbMovie "+
			"from Movie m "
			+"where m.year >= :yearT "
			+"group by m.year "
			+"having count(*) >= :countT "
			+"order by m.year, count(*) desc")
	Stream<IMovieAfterYear> movieAfterYear(Integer yearT, Long countT);
	
	
	
}
