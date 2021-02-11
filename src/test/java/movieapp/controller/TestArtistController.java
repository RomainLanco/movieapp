package movieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.eq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import movieapp.dto.ArtistSimple;
import movieapp.entity.Artist;
import movieapp.persistence.ArtistRepository;
import movieapp.service.IArtistService;

@WebMvcTest(controllers = ArtistController.class)
class TestArtistController {

	private final static String BASE_URI = "/api/artists";
	
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	IArtistService artistService;
	
	
	
	@Test
	void testGetIdAbsent() throws Exception {
		//given 
		int id = 1;
		given(artistService.getById(id)).willReturn(Optional.empty());
		
		mockMvc.perform(get(BASE_URI+"/"+id)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").doesNotExist());
		
		//check mock service has been called
		then(artistService).should().getById(any());
		
		
		
		
	}

	
	@Test
	void testGetIdPresent() throws Exception {
		
		ArtistSimple artistDto = new ArtistSimple(1,"Will Smith", LocalDate.of(1968, 9, 25));
		
		given(artistService.getById(1)).willReturn(Optional.of(artistDto));
		
		mockMvc.perform(get(BASE_URI+"/"+1)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Will Smith"))
				.andExpect(jsonPath("$.birthdate").value("1968-09-25"));
		
		//check mock service has been called
		then(artistService).should().getById(any());
	}
	
	@Test
	void testGetByName() throws Exception {
		String name = "Will Smith";
		ArtistSimple artistDto = new ArtistSimple(1,name, LocalDate.of(1968, 9, 25));
		ArtistSimple artistDto2 = new ArtistSimple(2,name, LocalDate.of(1968, 9, 25));
		
		given(artistService.getByName(name)).willReturn(List.of(artistDto,artistDto2));
		
		mockMvc.perform(get(BASE_URI+"/byName?n="+name)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
		//TODO : test reponse Json avec 2 will Smith et 2 id differents 
				.andExpect(jsonPath("$").isArray());
		
		//check mock service has been called
		then(artistService).should().getByName(any());
	}
	
	@Test
	void testAdd() throws Exception {
		// 1. given
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		String artistJsonIn	= JsonProvider.artistJson(name, birthdate);
//		String artistJsonIn = "{\"name\":\"Will Smith\", \"birthdate\":\"1968-98-25\"}";
		
		//perfect response from Mock service
		int id = 1;
		given(artistService.add(any()))
				.willReturn(new ArtistSimple(id,name,birthdate));
		// 2. when/then
		mockMvc
			.perform(post(BASE_URI)	// build POST HTTP request
				.contentType(MediaType.APPLICATION_JSON)
				.content(artistJsonIn)
				.accept(MediaType.APPLICATION_JSON)) // + header request
			.andDo(print()) // intercept request to print 
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.name").value(name))
			.andExpect(jsonPath("$.birthdate").value(birthdate.toString()));
			;	
			//check mock service has been called
			then(artistService).should().add(any());
	}
	
	@Test
	void testPut() throws Exception {
		String name = "Will Smith";
		LocalDate birthdate = LocalDate.of(1968, 9, 25);
		ArtistSimple artistDto = new ArtistSimple(1,name, birthdate);
		String artistJsonIn = "{\"name\":\"Will Smith\", \"birthdate\":\"1968-98-25\", \"id\":\"1\"}";
		given(artistService.updateArtist(eq(artistDto))).willReturn(Optional.of(artistDto));
		
		
		mockMvc.perform(put(BASE_URI)
					.contentType(MediaType.APPLICATION_JSON)
					.content(artistJsonIn)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.birthdate").value(birthdate.toString()));
				;
		
		then(artistService).should().updateArtist(any());
	}
}
