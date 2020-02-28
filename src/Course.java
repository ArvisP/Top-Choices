import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

// Created by Arvis Peralta for SSA 2017

public class Course {
	private String name;
	private int population;
	private int cap;
	private Student[] student;
	private int popularity;
	
	// Parameters : Course name, Total number of students
	public Course (String n, int totalNumOfStudents) 
	{
		// n = Name
		// c = capacity
		setName(n);
		student = new Student[totalNumOfStudents];
		population = 0;
		setCap(1);
		setPopularity(99);
	}
	
	/* isFull() : Returns true if the course is full. Returns False otherwise.
	* addStudent(Student s) : Appends a student to this course.
	* removeStudent(Student s) : Finds the specified student and removes him from the course.*/
	
	// --------------------------------------	
	public Boolean isFull(){
		if(getPopulation() >= getCap()) 
			return true; 
		return false;
	}
	
	public void sortStudents(){
	    for (int i = 0; i < getPopulation()-1; i++) {
	        for (int j = i+1; j < getPopulation(); j++) {
	        	String first = student[i].getLastName();
	        	String second = student[j].getLastName();
	        	
	        	if (compare(first, second) >= 0){
	        		swap(i,j);
	        	}	
	        }
	    }		    
	}
	
	public void swap(int i, int j){
		Student temp = student[i];
		student[i] = student[j];
		student[j] = temp;
	}
	
    public int compare(String s1, String s2) {
    	
        int res =  s1.compareToIgnoreCase(s2);
        return res;
        
    }
		    
	public void addStudent(Student s){
		this.student[population] = s;
		++ population;
	}
	
	public Student getStudent(int index){
		return this.student[index];
	}
	
	public void setPopularity(int p){
		this.popularity = p;
	}
	
	public int getPopularity(){
		return this.popularity;
	}
	
	public void setStudent(Student s, int index){
		this.student[index] = s;
	}
	
	public void removeStudent(Student s){
		for(int i = 0; i < population; ++i){
			// Only comparing if the EMPLID is the same to find student
			if(s.getEMPLID().equals(student[i].getEMPLID())){
				while(i < population-1){ // When found make student @ i = student @ i+1;
					student[i] = student[i+1];
					++i;
				}
			}
		}
		--population;
	}
	
	public int getPopulation(){
		return this.population;
	}
	
	public void setName(String n)
	{
		this.name = n;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setCap(int c){
		this.cap = c;
	}
	
	public void increaseCap(){
		++ this.cap;
	}
	
	public int getCap(){
		return this.cap;
	}
	
	
}
