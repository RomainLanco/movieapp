package movieapp.dto;

public class MovieStatByActors {
	private Long count;
	private Integer minYear;
	private Integer maxYear;
	private String actorName;
	
	
	
	public MovieStatByActors() {
		super();
		
	}
	public MovieStatByActors(Long count, Integer minYear, Integer maxYear,String actorName) {
		super();
		this.count = count;
		this.minYear = minYear;
		this.maxYear = maxYear;
		this.actorName= actorName;
	}
	
	public String getActorName() {
		return actorName;
	}
	public void setActorName(String actorName) {
		this.actorName = actorName;
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
	public Integer getMaxYear() {
		return maxYear;
	}
	public void setMaxYear(Integer maxYear) {
		this.maxYear = maxYear;
	}
	@Override
	public String toString() {
		return "MovieStatByActors [count=" + count + ", minYear=" + minYear + ", maxYear=" + maxYear + ", actorName="
				+ actorName + "]";
	}
}
