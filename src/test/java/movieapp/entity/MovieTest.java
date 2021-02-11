package movieapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MovieTest {

	@Test
	void testConstructor() {
		Movie stupeflim = new Movie();

		
		assertAll(
		()->assertNull(stupeflim.getTitle(), "default title"),
		()->assertEquals(null, stupeflim.getDuration(),"default duration"),
		()->assertEquals(null, stupeflim.getYear(),"default year"));
	}
	
//	@Test
//	void testAllArgsConstructor() {
////		Movie movie= new Movie("kabir singh", 2019, 173);
//		
//		assertAll(
//				()->assertEquals("kabir singh",movie.getTitle(), " title"),
//				()->assertEquals(173, movie.getDuration()," duration"),
//				()->assertEquals(2019, movie.getYear()," year"));
//	}

}
