import java.awt.FileDialog;
import java.awt.Frame;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class TopChoice {
	public static int[] mostPopular;
	public static Course[] courses;
	public static Report report;
	public static int studentAmount;
	public static int courseAmount;
	public static int sumOfCaps;
	
	
	public static void start(Link db){
		studentAmount = db.getStudentAmount();
		courseAmount = db.getCourseAmount();
		report = new Report(db.getFilePath());
		mostPopular = new int[courseAmount];
		courses = new Course[courseAmount];
		courses = db.getData();
		
		determineMostPopular();
			// Get current date, time;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		report.write("Created "+ df.format(date)+ ".\n");
		report.write("Database used: "+db.getFilePath()+db.getFileName()+"\n\n");
		report.write(" "+studentAmount + " students found - " + courseAmount + " courses found.\n");
		report.write("\n");
		
		determineOvertally();
		
			
		report.write("\n Giving everyone their first choice...\n");
		for(int i = 0; i < courseAmount; i++){
			courses[i] = shuffle(courses[i]);
			report.write("- "+courses[i].getName()+": "+courses[i].getPopulation() + " Students.\n");
		}
		report.write("\n Redistributing...\n");
		compute();
		for(int i = 0; i < courseAmount; i++){
			report.write("- "+courses[i].getName()+" has "+courses[i].getPopulation() + " Students.\n");
		}
		db.setResult(courses);
		finalStats();
		report.close();
		
		
	}
	
	public static void finalStats(){
		DecimalFormat df = new DecimalFormat("#.00");
		double total_ss = 0;
		int []picks = new int[courseAmount];
		double ss; // Student Satisfaction
		double ratio = (100/((double) (courseAmount - 1)));
		for(int j = 0; j < courseAmount; j++){
			for(int i = 0; i < courses[j].getPopulation(); i++){
				Student current = courses[j].getStudent(i);
				ss = courseAmount - current.getPickAssigned();
				++picks[current.getPickAssigned()-1];
				total_ss += (ss * ratio);
			}
		}
		total_ss /= studentAmount;
		for (int i = 0; i < courseAmount; i++){
			double percentage = (picks[i]*100)/(double)(studentAmount);
			report.write(picks[i]+"("+df.format(percentage)+"%) students received their No. "+(i+1)+" pick.\n");
		}
		
		report.write("\n- Distribution finished successfully with a "+df.format(total_ss)+"% "+
		"satisfaction rate.");
	}
	
	public static Course shuffle(Course course){
		Random rnd = new Random();
		int population = course.getPopulation();
		for(int i = 0; i < population; i++){
			int number = rnd.nextInt(population);
			Student current = course.getStudent(i);
			course.setStudent(course.getStudent(number), i);
			course.setStudent(current, number);
		}
		return course;
	}
	
	public static void determineOvertally(){
		sumOfCaps = 0;
		DecimalFormat df = new DecimalFormat("#.00");
		for(int i = 0; i < courseAmount; i++){
			double percentage = (courses[i].getPopulation()* 100)/(double)studentAmount;
			sumOfCaps += courses[i].getCap();
			report.write(courses[i].getPopulation() + " students ("+df.format(percentage)+"%) chose ["+ courses[i].getName()+
					"] as their first choice.\n");
		}
		report.write("\n");
		setupOvertally();
	}
	
	public static void setupOvertally(){
		int difference = studentAmount - sumOfCaps;
		int i = 0;
		while(difference > 0){
			courses[mostPopular[i]].increaseCap();
			if(i == courseAmount-1)
				i = 0;
			else{
				i++;
			}
			-- difference;
		}
		
		for(i = 0; i < courseAmount; i++){
			report.write("- ["+ courses[i].getName() +"]'s final capacity is " +courses[i].getCap()+".\n");
		}
	}
	
	public static Course[] compute(){
		int i;
		for (i = 0; i < courseAmount; i++){
			if(courses[i].isFull())
				continue;
			// Find Students while it is not full
			while(!courses[i].isFull()){
				courses[i].addStudent(findStudent(i));
			}
			
			
		}
		
		return courses;
	}
	
	public static void determineMostPopular(){
		int i;
		int j;
		int index = 0;
		for (i = 0; i < courseAmount; i++){
			int max = 0;
			for (j = 0; j < courseAmount; j++){
				if(courses[j].getPopularity() == 99 && courses[j].getPopulation() >= max){
					index = j;
					max = courses[j].getPopulation();
				}
					
			}
			courses[index].setPopularity(i);
			mostPopular[i] = index;
		}
	}
	
	// Will find a student to add to a given course;
	public static Student findStudent(int courseIndex){
		int i;
		int j;
		int k;
		boolean found = false;
		int noOfPicks = courseAmount;
		int pick = 0;
		int selectedCourse = 0;
		Student selected = null;
		Student current = null;
		Course currentCourse;
		Student currentStudent;
		// i = current top pick;
		// j = course index;
		// k = student index;
		// setPickAssigned;
		// remove
		// return
		for(i = 2; i <= noOfPicks; i++){
			for(j = 0; j < courseAmount; j++){
				// Check if course is overfilled.
				if(courses[j].getPopulation() > courses[j].getCap()){
					currentCourse = courses[j];
					for(k = 0; k < currentCourse.getPopulation(); k++){
						currentStudent = currentCourse.getStudent(k);
						if(currentStudent.getCourseChoice(courseIndex) == i){
							if(!found){
								current = currentStudent;
								selectedCourse = j;
								pick = i;
								found = true;
							}
							else{
								if(flip()){
									current = currentStudent;
									selectedCourse = j;
									pick = i;
								}
							}
						}
					}
				}
			}
			if(current != null) break;
		}
		selected = current;
		courses[selectedCourse].removeStudent(selected);
		selected.setPickAssigned(pick);
		
		return selected;
	
	}

	public static boolean flip(){
		Random rnd = new Random();
		int number = rnd.nextInt(1);
		if(number == 1)
			return true;
		return false;
	}
	
	public static void main(String[] args){		
	 	Boolean valid = false;
		String file, path;
		Link db = null;
		do{
		    FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
		    dialog.setMode(FileDialog.LOAD);
		    dialog.setVisible(true);
		    if(dialog.getFile()!=null){
			    file = dialog.getFile();
			    path = dialog.getDirectory();
			    if(file.endsWith(".xls") || file.endsWith(".xlsx")){
			    	db = new Link(path, file);
			    	if(db.valid())
			    		valid = true;
			    	else
			    		JOptionPane.showMessageDialog(null, "Database has been used before, or has no valid data.");
			    }
			    else{
			    	JOptionPane.showMessageDialog(null, "Invalid file. Please select an .xls file.");
			    }
		    }
		    else System.exit(0);
		    
		} while(!valid);
		
		start(db);		
		System.exit(0);
	}
	
}