package movieapp.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import movieapp.dto.ArtistSimple;
import movieapp.dto.INameYearTitle;
import movieapp.dto.IStatisticsByDirector;
import movieapp.entity.Artist;


public interface ArtistRepository extends JpaRepository<Artist, Integer>{

	
	Set<Artist> findByNameIgnoreCase(String name);
	
	Stream<Artist> findByBirthdate(LocalDate birthdate);
	
	@Query("select a from Artist a where extract(year from a.birthdate) = :year")
	Stream<Artist> findByBirthdateYear(int year);
	
	@Query("select a.name as name,m.year as year,m.title as title from Movie m join m.actors a where a.name like :name order by m.year")
	Stream<INameYearTitle> filmographyActor(String name);
	
	@Query("select "
			+"count(m) as nbMovies,min(m.year) as minYear,d.name as name "
			+"from Movie m "
			+"join m.director d "
			+"group by d.id, d.name "
			+"order by count(m) desc")
	Stream<IStatisticsByDirector> statisticsByDirector();
	
	Stream<Artist> findByName(String name);

	Optional<Artist> updateArtist(Artist artist);
}
