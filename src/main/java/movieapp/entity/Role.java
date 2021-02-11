package movieapp.entity;

import javax.persistence.Entity;


public class Role {

	private Artist artist;
	private Movie movie;
	
	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Role(Artist artist, Movie movie) {
		super();
		this.artist = artist;
		this.movie = movie;
	}
	
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	
	
}
