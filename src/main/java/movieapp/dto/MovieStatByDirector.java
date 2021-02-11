package movieapp.dto;

public class MovieStatByDirector {
	
	private Long count;
	private Integer minYear;
	private String director;
	
	
	
	public MovieStatByDirector() {
		super();
		
	}
	public MovieStatByDirector(Long count, Integer minYear, String director) {
		super();
		this.count = count;
		this.minYear = minYear;
		this.director = director;
	}
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getMinYear() {
		return minYear;
	}
	public void setMinYear(Integer minYear) {
		this.minYear = minYear;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	@Override
	public String toString() {
		return "MovieStatByDirector [count=" + count + ", minYear=" + minYear + ", director=" + director + "]";
	}

}
