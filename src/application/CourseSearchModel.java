package application;

public class CourseSearchModel {
	String CourseName;
	Integer Capacity;
	String Year;
	String DeliveryMode;
	String DayOfLecture;
	String TimeOfLecture;
	String DurationOfLecture;
	int TimeInMinutes;
	int DurationOfLectureInMinutes;
	
	public CourseSearchModel(String CourseName, Integer Capacity, String Year, String DeliveryMode, String DayOfLecture, String TimeOfLecture, String DurationOfLecture, int TimeInMinutes, int DurationOfLectureInMinutes) {
		this.CourseName = CourseName;
		this.Capacity = Capacity;
		this.Year = Year;
		this.DeliveryMode = DeliveryMode;
		this.DayOfLecture = DayOfLecture;
		this.TimeOfLecture = TimeOfLecture;
		this.DurationOfLecture = DurationOfLecture;
		this.TimeInMinutes = TimeInMinutes;
	    this.DurationOfLectureInMinutes = DurationOfLectureInMinutes;
}
	public String getCourseName() {
		return CourseName;
}
	public Integer getCapacity() {
		return Capacity;
	}
	public String getYear() {
		return Year;
	}
	public String getDeliveryMode() {
		return DeliveryMode;
	}
	public String getDayOfLecture() {
		return DayOfLecture;
	}
	public String getTimeOfLecture() {
		return TimeOfLecture;
	}
	public String getDurationOfLecture() {
		return DurationOfLecture;
	}
	public void setCourseName(String Coursename) {
		this.CourseName = Coursename;
	}
	public void setCapacity(Integer Capacity) {
		this.Capacity = Capacity;
	}
	public void setYear(String Year) {
		this.Year = Year;
	}
	public void setDeliveryMode(String DeliveryMode) {
		this.DeliveryMode = DeliveryMode;
	}
	public void setDayOfLecture(String DayOfLecture) {
		this.DayOfLecture = DayOfLecture;
	}
	public void setTimeOflecture(String TimeOfLecture) {
		this.TimeOfLecture = TimeOfLecture;
	}
	public void setDurationOfLecture(String DurationOfLecture) {
		this.DurationOfLecture = DurationOfLecture;
	}
    public int getTimeInMinutes() {
        return TimeInMinutes;
    }

    public void setTimeOfLectureInMinutes(int timeInMinutes) {
        TimeInMinutes = timeInMinutes;
    }

    public int getDurationOfLectureInMinutes() {
        return DurationOfLectureInMinutes;
    }

    public void setDurationOfLectureInMinutes(int durationOfLectureInMinutes) {
        DurationOfLectureInMinutes = durationOfLectureInMinutes;
    }
}
