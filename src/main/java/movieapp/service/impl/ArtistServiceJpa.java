package movieapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import movieapp.dto.ArtistSimple;
import movieapp.entity.Artist;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

@Service
@Transactional
public class ArtistServiceJpa implements IArtistService{

	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Optional<ArtistSimple> getById(int id) {
		// TODO Auto-generated method stub
		
		return artistRepository.findById(id)
								.map(artistEntity -> modelMapper
										.map(artistEntity, ArtistSimple.class));
		
	}
	@Override
	public ArtistSimple add(ArtistSimple artist) {
		Artist artistEntityFromRepo = artistRepository.save(
				modelMapper.map(artist, Artist.class));	// convert dto param to entity
		return modelMapper.map(artistEntityFromRepo, ArtistSimple.class); // convert entity to dto result
	}
	@Override
	public List<ArtistSimple> getByName(String name) {
		
		Stream<Artist> artistEntityOut = artistRepository.findByName(name);
		
		List<ArtistSimple> artistSimpleDtoOut = artistEntityOut
		.map(asdto -> modelMapper.map(asdto, ArtistSimple.class))
		.collect(Collectors.toList());
		return artistSimpleDtoOut;
	}
	@Override
	public Optional<ArtistSimple> updateArtist(ArtistSimple artistSimple) {
		// TODO Auto-generated method stub
		Artist artistEntityIn = modelMapper.map(artistSimple, Artist.class);
		Optional<Artist> artistEntityOut = artistRepository.updateArtist(artistEntityIn);
		Optional<ArtistSimple> artistSimpleDtoOut = artistEntityOut.map(aeo -> modelMapper.map(aeo, ArtistSimple.class));
		
		return artistSimpleDtoOut;
	}

	
}
