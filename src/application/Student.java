package application;

public class Student {
    private static Student instance;
    private String studentNumber;

    // Private constructor to prevent instantiation from outside
    private Student() {
    }

    // Public static method to access the singleton instance
    public static Student getInstance() {
        if (instance == null) {
            instance = new Student();
        }
        return instance;
    }

    // Getter and setter for the student number
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}

//The class Student has a private constructor, which means it cannot be instantiated from outside the class. This prevents the creation of multiple instances of the Student class.
//
//The class has a private static variable instance of type Student. This variable holds the single instance of the class.
//
//The class provides a public static method getInstance(). This method allows access to the singleton instance of the Student class. If the instance variable is null, indicating that no instance has been created yet, the method creates a new instance of the class and assigns it to instance. Subsequent calls to getInstance() return the existing instance.
//
//The class also provides getter and setter methods for the studentNumber field. These methods allow retrieving and updating the student number associated with the singleton instance.