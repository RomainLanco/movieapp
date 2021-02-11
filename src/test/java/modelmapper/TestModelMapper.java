package modelmapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import movieapp.dto.MovieSimple;
import movieapp.entity.Movie;

class TestModelMapper {

	static ModelMapper modelMapper;
	
	@BeforeAll
	static void initModelMapper() {
		modelMapper = new ModelMapper();
	}
	
	@Test
	void testEntityToDto() {
		//entity
		Movie movieEntity = new Movie("Blade Runner", 1982, 117);
		movieEntity.setId(1);
		//convert to DTO
		MovieSimple movieDto = modelMapper.map(movieEntity, MovieSimple.class);
		//is it ok 
		assertTrue(movieDto.getId().equals(movieEntity.getId()),"id");
		assertTrue(movieDto.getTitle().equals(movieEntity.getTitle()),"title");
		assertTrue(movieDto.getYear().equals(movieEntity.getYear()),"year");
	}
	
	@Test
	void testDtoToEntity() {
		//Dto
		MovieSimple movieDto = new MovieSimple();
		movieDto.setTitle("Blade Runner");
		movieDto.setYear(1982);
		
		//convert to entity
		Movie movieEntity = modelMapper.map(movieDto, Movie.class);
		//is it ok 
		System.out.println(movieEntity);
		assertEquals(movieDto.getId(),movieEntity.getId());
		assertTrue(movieDto.getTitle().equals(movieEntity.getTitle()),"title");
		assertTrue(movieDto.getYear().equals(movieEntity.getYear()),"year");
		assertNull(movieEntity.getDirector());
		assertTrue(movieEntity.getActors().isEmpty());
	}
	
	@Test
	void testDtoIntoEntity() {
		//Dto
		MovieSimple movieDto = new MovieSimple();
		movieDto.setTitle("Blade Runner (Director's cut)");
		movieDto.setYear(1982);
		movieDto.setId(1);
		
		//Entity
		Movie movieEntity = new Movie("Blade Runner",1980,117);
		movieEntity.setId(1);
		
		//
		//update entity with dto
		modelMapper.map(movieDto, movieEntity);
		//is it ok 
		System.out.println(movieEntity);
		System.out.println(movieDto);
		assertEquals(movieDto.getId(),movieEntity.getId());
		assertTrue(movieDto.getTitle().equals(movieEntity.getTitle()),"title");
		assertTrue(movieDto.getYear().equals(movieEntity.getYear()),"year");
		assertNull(movieEntity.getDirector());
		assertTrue(movieEntity.getActors().isEmpty());
	}
	
	@Test
	void testEntitiesToDtos() {
		//entities
		Stream<Movie> entitySource = Stream.of(new Movie("Blade Runner",1980,117)
				,new Movie("The Hangover",2009,90)
				, new Movie("Unforgiven",1992,130));
		
		//convert to Dtos
		var res = entitySource
				.map(m -> modelMapper.map(m, MovieSimple.class))
				.collect(Collectors.toCollection(
						//ArrayList::new
						//HaskSet::new
						()-> new TreeSet<MovieSimple>(Comparator.comparing(MovieSimple::getTitle))
						));
		
		System.out.println(res);
	}

}
