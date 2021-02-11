package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sun.jdi.connect.Connector.Argument;

import movieapp.persistence.ArtistRepository;
import movieapp.persistence.MovieRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace =Replace.NONE)
class TestSpringQueries {
	
	@Autowired
	MovieRepository movieRepository;

	@Autowired
	ArtistRepository artistRepository;

	@Test
	void test_artists_by_birthdate_year() {
		int year = 1930;
		artistRepository.findByBirthdateYear(year)
		.limit(10).forEach(System.out::println);
	}
	
	
	@ParameterizedTest
//	@CsvSource({"2010,2019",
//				"2030,2039"})
	@MethodSource("rangeYearSource")
	void test_total_duration_range_year(int yearMin, int yearMax) {
		
		var res = movieRepository.totalDuration(yearMin, yearMax);
		System.out.println(res);
	}
	
	@ParameterizedTest
//	@CsvSource({"2010,2019",
//				"2030,2039"})
	@MethodSource("rangeYearSource")
	void test_avg_duration_range_year(int yearMin, int yearMax) {
		
		var res = movieRepository.averageDuration(yearMin, yearMax);
		System.out.println(res);
	}
	
	static Stream<Arguments> rangeYearSource() {
		return Stream.of(Arguments.arguments(2010,2019),
		Arguments.arguments(2030,2039));
	}

	
	@Test
	void test_statistics() {
		var res = movieRepository.statistics();
		long nb_movies =  res.getCount();
		int min_year =  res.getMinYear();
		int max_year = res.getMaxYear();
		
		System.out.println("Nb : "+ nb_movies +"; min : "+ min_year+ "; max : "+ max_year );
	}
	
	@Test
	void test_filmography() {
		artistRepository.filmographyActor("John Wayne")
		.forEach(nyt -> 
			System.out.println(nyt.getName()+
					" ; "+nyt.getYear()+
					" ; "+nyt.getTitle()));;
	}
	
	@Test
	void test_movie_after_year() {
		Integer yearT= 1930;
		Long countT= 20L;
		movieRepository.movieAfterYear(yearT, countT).forEach(may -> System.out.println(may.getYear() + " ; "+may.getNbMovie()));
	}
	
	@Test
	void test_stat_by_director() {
		artistRepository.statisticsByDirector()
			.limit(10)
			.forEach(sbd -> 
				System.out.println(sbd.getName()+" ; "+sbd.getMinYear()+" ; "+sbd.getNbMovies()));
	}
	
	
}
