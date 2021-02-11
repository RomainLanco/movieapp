package movieapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import movieapp.dto.MovieSimple;
import movieapp.entity.Movie;
import movieapp.persistence.MovieRepository;
import movieapp.service.IMovieService;

@Service
@Transactional
public class MovieServiceJpa implements IMovieService {
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public MovieSimple add(MovieSimple movie) {
		
		Movie movieEntityIn = modelMapper.map(movie, Movie.class);
		Movie movieEntityOut = movieRepository.save(movieEntityIn);
		
		MovieSimple movieDtoRes = modelMapper.map(movieEntityOut, MovieSimple.class);
		System.out.println("entity out: " + movieEntityOut);
		System.out.println("dto out: " + movieDtoRes);
		return movieDtoRes;
	}

	@Override
	public List<MovieSimple> getAll() {
		// TODO Auto-generated method stub
		
		List<Movie> entitySource = movieRepository.findAll();
		List<MovieSimple> moviesDtoRes = entitySource.stream().map(es -> modelMapper.map(es, MovieSimple.class)).collect(Collectors.toList());
		
		return moviesDtoRes;
	}

	@Override
	public Optional<MovieSimple> getById(int id) {
		
		Optional<Movie> optMovieEntity = movieRepository.findById(id);
		Optional<MovieSimple> optMovieDtoRes = optMovieEntity.map(me ->modelMapper.map(me,MovieSimple.class));
		
		return optMovieDtoRes;
	}

	@Override
	public List<MovieSimple> moviesByTitle(String title) {
		// TODO Auto-generated method stub
		var movieEntities = movieRepository.findByTitleOrderByTitle(title);
		
		var moviesDtoRes = movieEntities.stream().map(me -> modelMapper.map(me,MovieSimple.class)).collect(Collectors.toList());
		return moviesDtoRes;
	}

	@Override
	public Optional<MovieSimple> updateMovie(MovieSimple movieSimple) {
		Optional<Movie> optMovieDb =  movieRepository.findById(movieSimple.getId());
		optMovieDb.ifPresent(m -> {
			m.setTitle(movieSimple.getTitle());
			m.setYear(movieSimple.getYear());

		});		
		var optMovieDto = optMovieDb.map(m->modelMapper.map(m, MovieSimple.class));
		return optMovieDto;
	}

	
}
