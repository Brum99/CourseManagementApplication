package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimetableRowModel {

    private final StringProperty time;
    private final StringProperty monday;
    private final StringProperty tuesday;
    private final StringProperty wednesday;
    private final StringProperty thursday;
    private final StringProperty friday;

    public TimetableRowModel(String time) {
        this.time = new SimpleStringProperty(time);
        this.monday = new SimpleStringProperty("");
        this.tuesday = new SimpleStringProperty("");
        this.wednesday = new SimpleStringProperty("");
        this.thursday = new SimpleStringProperty("");
        this.friday = new SimpleStringProperty("");
    }

    // Getter and Setter methods for the time property
    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    // Getter and Setter methods for the Monday property
    public String getMonday() {
        return monday.get();
    }

    public void setMonday(String monday) {
        this.monday.set(monday);
    }

    // Getter and Setter methods for the Tuesday property
    public String getTuesday() {
        return tuesday.get();
    }

    public void setTuesday(String tuesday) {
        this.tuesday.set(tuesday);
    }

    // Getter and Setter methods for the Wednesday property
    public String getWednesday() {
        return wednesday.get();
    }

    public void setWednesday(String wednesday) {
        this.wednesday.set(wednesday);
    }

    // Getter and Setter methods for the Thursday property
    public String getThursday() {
        return thursday.get();
    }

    public void setThursday(String thursday) {
        this.thursday.set(thursday);
    }

    // Getter and Setter methods for the Friday property
    public String getFriday() {
        return friday.get();
    }

    public void setFriday(String friday) {
        this.friday.set(friday);
    }

    // Property getter methods
    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty mondayProperty() {
        return monday;
    }

    public StringProperty tuesdayProperty() {
        return tuesday;
    }

    public StringProperty wednesdayProperty() {
        return wednesday;
    }

    public StringProperty thursdayProperty() {
        return thursday;
    }

    public StringProperty fridayProperty() {
        return friday;
    }
}
//The TimetableRowModel class represents a single row in the timetable table view. It contains properties for the time and the course names for each day of the week (Monday, Tuesday, Wednesday, Thursday, and Friday). It uses the JavaFX StringProperty to enable binding and observe changes in the values.
//
//The class has private instance variables time, monday, tuesday, wednesday, thursday, and friday of type StringProperty. These properties store the values for the corresponding fields in the row.
//The constructor takes a time parameter and initializes the properties with empty strings as default values.
//Getter and setter methods are provided for each property to access and modify their values.
//Property getter methods (e.g., timeProperty(), mondayProperty(), etc.) return the StringProperty instances associated with the properties. This allows other components to bind to these properties and observe their changes.
//The getTime(), getMonday(), getTuesday(), etc., methods provide access to the actual values of the properties without the StringProperty wrapper.
//The setTime(), setMonday(), setTuesday(), etc., methods modify the values of the properties.