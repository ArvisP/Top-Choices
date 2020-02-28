// Created by Arvis Peralta

public class Student {
	private String firstName;
	private String lastName;
	private String emplID;
	private String email;
	private int pickAssigned;
	private int[] choices;
	
	// Parameters: name, emplid, number of choices, pick order;
	public Student(String n, String emp, String email, int[] picks){
		String name[] = n.split(" ");
		String first = name[0];
		String last = "";
		if(name.length>1)
			for(int i = 1; i< name.length;i++){
				last+=name[i]+" ";
			}
		else
			last = first;
		setFirstName(first);
		setLastName(last);		
		setEMPLID(emp);
		setEmail(email);
		setChoices(picks);
		setPickAssigned(1);
	}
	
	public void setChoices(int[] picks){
		this.choices = picks;
	}
	
	public int getPickAssigned(){
		return this.pickAssigned;
	}
	
	public void setPickAssigned(int index){
		this.pickAssigned = index;
	}
	
	public int getCourseChoice(int courseNumber){
		return this.choices[courseNumber];
	}
	
	public void setEMPLID(String emp){
		this.emplID = emp;
	}
	
	public String getEMPLID(){
		return this.emplID;
	}
	
	
	
	public void setFirstName(String n){
		this.firstName = n;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setLastName(String n){
		this.lastName = n;
	}
	
	public String getLastName(){
		return this.lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
