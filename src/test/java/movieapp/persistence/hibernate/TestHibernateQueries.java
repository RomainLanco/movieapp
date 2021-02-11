package movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import movieapp.dto.MovieStat;
import movieapp.dto.MovieStatByActors;
import movieapp.dto.MovieStatByDirector;
import movieapp.dto.NameYearTitle;
import movieapp.entity.Artist;
import movieapp.entity.Movie;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TestHibernateQueries {
	
	@Autowired
	EntityManager entitymanager;
	

	@Test
	void testJPQL_Select_all_as_list() {
		TypedQuery<Movie> query = entitymanager.createQuery(
				//"from Movie",
				"select m from Movie m", //JPQL request
				Movie.class); // result will be adapted in 0, 1 ou * object Movie
		List<Movie> movies = query.getResultList(); // execute + convert resultset as list
		System.out.println("movies :"+ movies.size());
		
	}
	
	@Test
	void testJPQL_Select_all_as_stream() {
		entitymanager.createQuery(
				//"from Movie",
				"select m from Movie m", //JPQL request
				Movie.class)  // result will be adapted in 0, 1 ou * object Movie
				.getResultStream() // execute + convert resultset as list
				.limit(10)
				.forEach(System.out::println);
		
	}
	
	@ParameterizedTest
	@ValueSource(ints= {2019,2020,2021})
	void test_select_predicate(int year){
		//System.out.println(" - Movies from year : "+year);
		assertAll(entitymanager.createQuery(
				"select m from Movie m where m.year = :year",
				Movie.class)
				.setParameter("year", year)
				.getResultStream()
				.map(m -> ()-> assertEquals(year, m.getYear())));
	}

	
	@Test
	void test_select_where_title_year(){
		//System.out.println(" - Movies from year : "+year);
		assertAll(entitymanager.createQuery(
				"select m from Movie m where m.year = :year and m.title = :title",
				Movie.class)
				.setParameter("year", 1956)
				.setParameter("title", "The Man Who Knew Too Much")
				.getResultStream()
				.map(m -> ()-> assertTrue((1956+"The Man Who Knew Too Much").equals(m.getYear()+m.getTitle()))))
				;
	}
	
	@ParameterizedTest
	@CsvSource({
			"1934,The Man Who Knew Too Much",
			"1956,The Man Who Knew Too Much"
		})
	void test_select_where_title_year_CSV_Source(int year, String title){
		//System.out.println(" - Movies from year : "+year);
		assertAll(entitymanager.createQuery(
				"select m from Movie m where m.year = :year and m.title = :title",
				Movie.class)
				.setParameter("year", year)
				.setParameter("title", title)
				.getResultStream()
				.map(m -> ()-> assertTrue((year+title).equals(m.getYear()+m.getTitle()))))
				;
	}

	
	@Test
	void test_select_artist_birthdate_year() {
		entitymanager.createQuery(
				"select a from Artist a where extract(year from a.birthdate) = :year",
				Artist.class)
				.setParameter("year", 1930)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
	}
	
	@Test
	void test_select_artist_of_Age() {
		entitymanager.createQuery(
				"select a from Artist a where extract(year from a.birthdate) = :year",
				Artist.class)
				.setParameter("year", LocalDate.now().getYear()-30)
				.getResultStream()
				.forEach(System.out::println);
	}
	
	@Test
	void test_select_artist_of_age_SQL() {
		entitymanager.createQuery(
				"select a from Artist a where extract(year from current_date) - extract(year from a.birthdate) = :age",
				Artist.class)
				.setParameter("age", 30)
				.getResultStream()
				.forEach(System.out::println);
	}
	
	@Test
	void test_select_movie_with_director_named() {
		//Hibernate: select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
		//from movies movie0_ inner join stars artist1_ on movie0_.id_director=artist1_.id 
		//where artist1_.name=?

		entitymanager.createQuery(
				"select m from Movie m join m.director a where a.name = :name",
				Movie.class)
				.setParameter("name", "Clint Eastwood")
				.getResultStream()
				.forEach(System.out::println);
	}
	
	@Test
	void test_select_movie_with_actor_named() {
		//Hibernate: select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
		//from movies movie0_ 
		//inner join play actors1_ on movie0_.id=actors1_.id_movie 
		//inner join stars artist2_ on actors1_.id_actor=artist2_.id 
		//where artist2_.name=?


		entitymanager.createQuery(
				"select m from Movie m join m.actors a where a.name = :name",
				Movie.class)
				.setParameter("name", "Clint Eastwood")
				.getResultStream()
				.forEach(System.out::println);
	}
	
	@Test
	void test_movies_stats() {
		//count(*)
		var query = entitymanager.createQuery(
				"select count(m) from Movie m ",
				Long.class);
				
		long nb_movies =query.getSingleResult();
		System.out.println(nb_movies);
		
		//min(year)
		
		var min_year= entitymanager.createQuery("select min(year) from Movie m",Integer.class).getSingleResult();
			
		System.out.println(min_year);
		
		//sum(duration) between year1 & year 2
		
		var sum = entitymanager.createQuery(
				"select coalesce(sum(m.duration),0) from Movie m where m.year between :year1 and :year2"
				,Long.class)
				.setParameter("year1", 2019)
				.setParameter("year2", 2020)
				.getSingleResult();
		
		System.out.println("sum = "+sum);
		
		//min(duration) between year1 & year2
		Optional<Integer> min = Optional.ofNullable(entitymanager.createQuery(
				"select min(m.duration) from Movie m where m.year between :year1 and :year2"
				,Integer.class)
				.setParameter("year1", 2020)
				.setParameter("year2", 2029)
				.getSingleResult());
		
		System.out.println("min duration = "+min);
	}
	
	@Test
	void test_movie_several_stats() {
		var res = entitymanager.createQuery("select count(m), min(year), max(year) from Movie m",Object[].class).getSingleResult();
		System.out.println(Arrays.toString(res));
		long nb_movies = (long) res[0];
		int min_year = (int) res[1];
		int max_year = (int) res[2];
		
		System.out.println("Nb : "+ nb_movies +"; min : "+ min_year+ "; max : "+ max_year );
	}
	
	@Test
	void test_movie_several_stats_as_Tuple() {
		var res = entitymanager.createQuery("select count(m), min(year), max(year) from Movie m",Tuple.class).getSingleResult();
		System.out.println(res);
		long nb_movies = (long) res.get(0);
		int min_year = (int) res.get(1);
		int max_year = res.get(2, Integer.class); //possibilité de surcharger la methode get pour eviter de cast 
		
		System.out.println("Nb : "+ nb_movies +"; min : "+ min_year+ "; max : "+ max_year );
	}
	
	@Test
	void test_movie_several_stats_as_DTO() {
		
		var res = entitymanager.createQuery("select new movieapp.dto.MovieStat(count(m), min(year), max(year)) from Movie m",MovieStat.class).getSingleResult();
		System.out.println(res);
		long nb_movies =  res.getCount();
		int min_year =  res.getMinYear();
		int max_year = res.getMaxYear();
		
		System.out.println("Nb : "+ nb_movies +"; min : "+ min_year+ "; max : "+ max_year );
	}
	
	@Test
	void test_movie_projection() {
		var res = entitymanager.createQuery("select new movieapp.dto.NameYearTitle(a.name,m.year,m.title) from Movie m join m.actors a where a.name like :name order by m.year "
				,NameYearTitle.class)
				.setParameter("name", "John Wayne")
				.getResultStream()
				.limit(10)
				.collect(Collectors.toList());
		
			res.forEach(System.out::println);
		
	}
	
	
	
	@Test
	void test_movie_by_year_stats() {
		//nb movies by year (params : thresholdCount, thresholdYear) order by year/count desc
		
		entitymanager.createQuery("select m.year, count(*) "
				+ "from Movie m "
				+ "where m.year >= :yearT "
				+ "group by m.year "
				+ "having count(*) >= :countT "
				+ "order by m.year, count(*) desc"
				,Object[].class)
		.setParameter("yearT", 2000)
		.setParameter("countT", 20L)
		.getResultStream()
		.limit(10)
		.forEach(row -> System.out.println(Arrays.toString(row)));
		
	}
	
	@Test
	void test_movie_by_director() {
		//stats by director (count , min(year)) order by count desc
		
		var query = entitymanager.createQuery("select "
				+ "new movieapp.dto.MovieStatByDirector(count(m),min(m.year),d.name) "
				+ "from Movie m "
				+ "join m.director d "
				+ "group by d.id, d.name "
				+ "order by count(m) desc  "
				,MovieStatByDirector.class)
				.getResultStream()
				.limit(10)
				.collect(Collectors.toList());
		
		query.forEach(System.out::println);
	}
	
	
	
	
	@Test
	void test_movie_by_actor() {
		//stats by actor (count , min(year), max(year)) order by count desc
		
		var query = entitymanager.createQuery("select "
				+ "new movieapp.dto.MovieStatByActors(count(m),min(m.year),max(m.year), a.name) "
				+ "from Movie m "
				+ "join m.actors a "
				+ "group by a.id, a.name "
				+ "order by count(m) desc"
				,MovieStatByActors.class)
				.getResultStream()
				.limit(10)
				.collect(Collectors.toList());
		
		query.forEach(System.out::println);
	}

}
