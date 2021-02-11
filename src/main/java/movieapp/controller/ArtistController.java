package movieapp.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import movieapp.dto.ArtistSimple;
import movieapp.service.IArtistService;



@RestController
@RequestMapping("/api/artists")
public class ArtistController {
	
	
	@Autowired
	IArtistService artistService;
	
	
	@GetMapping("/{id}")
	@ResponseBody
	Optional<ArtistSimple> getById(@PathVariable("id") int id){
		return artistService.getById(id);
	}
	
	@PostMapping
	@ResponseBody
	ArtistSimple AjoutMovie(@RequestBody ArtistSimple artistDtoIn) {
		return artistService.add(artistDtoIn);
	}
	
	@GetMapping("/byName")
	@ResponseBody
	List<ArtistSimple> rechercheParName(@RequestParam("n") String name){
		return artistService.getByName(name);
	}
	
	@PutMapping
	@ResponseBody
	Optional<ArtistSimple> modifierArtist(@RequestBody ArtistSimple artistDtoIn){
		return artistService.updateArtist(artistDtoIn);
	}
	

}
