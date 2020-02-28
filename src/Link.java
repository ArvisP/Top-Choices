import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Link {
	private Workbook xlWBook;
    private Sheet xlSheet;
    private Row xlRow;
    private String fileName;
    private String filePath;
    private int courseAmount;
    private int studentAmount;
    
	public Link(String path, String name){
    	setFilePath(path);
    	setFileName(name);
    	init();
    	setCourseAmount();
    	setStudentAmount();
    	
    }
	
	public void init(){
		try{
			FileInputStream xlFile = new FileInputStream(getFilePath()+getFileName());
			if(getFileName().endsWith(".xls")){
				xlWBook = new HSSFWorkbook(xlFile);
			}
			else
				xlWBook = new XSSFWorkbook(xlFile);
			
    		
    		xlSheet = xlWBook.getSheetAt(0);
    	} catch (IOException e){
    		JOptionPane.showMessageDialog(null, "Invalid data.");
    	}
	}
	
	public Course[] getData(){
		int amountOfCourses = getCourseAmount();
		Course []input = new Course[amountOfCourses];
		xlRow = xlSheet.getRow(0);
		
		for (int i = 3; i < amountOfCourses+3; ++i){
			String name = xlRow.getCell(i).getStringCellValue();
			String courseName = name.replaceAll("[\\[\\]^+.]", "");
			courseName = courseName.trim();
			input[i-3] = new Course(courseName, getStudentAmount());
		}
		setStudents(input);
		return input;
	}
	
	// Gets student from DB and assigns them to their top pick.
	public Course[] setStudents(Course[] courses){
		Student []students = new Student[getStudentAmount()];
		for(int i = 1; i <= getStudentAmount(); i++){
			xlRow = xlSheet.getRow(i);
			String name = xlRow.getCell(0).getStringCellValue();
			String empl = Integer.toString((int)(xlRow.getCell(1).getNumericCellValue()));
			String email = xlRow.getCell(2).getStringCellValue();
			int topPick = 0;
			int []picks = new int[getCourseAmount()];
			
			for(int j = 0; j < getCourseAmount(); j++){
				picks[j] = (int)(xlRow.getCell(j+3).getNumericCellValue());
				if(picks[j] == 1){
					topPick = j;
				}
			}
			students[i-1] = new Student(name, empl, email, picks);
			courses[topPick].addStudent(students[i-1]);
		}
		
		return courses;
	}
	
	public int countStudents(){
		int total;
		total = xlSheet.getPhysicalNumberOfRows() - 1;
		return total;
	}
	
	public int countCourses(){
		int total;
		total = xlSheet.getRow(0).getLastCellNum() - 3;
		return total;
	}
	
	public boolean valid(){
		if(xlWBook.getNumberOfSheets() != 1)
			return false;
		return true;
	}
	
	public void setStudentAmount(){
		this.studentAmount = countStudents();
	}
	
	public int getStudentAmount(){
		return this.studentAmount;
	}
	
    public void setFilePath(String path){
    	this.filePath = path;
    }
    
    public String getFilePath(){
    	return this.filePath;
    }
    
    public void setFileName(String name){
    	this.fileName = name;
    }
    
    public String getFileName(){
    	return this.fileName;
    }
    
    public void setCourseAmount(){
    	this.courseAmount = countCourses();
    }
    
    public int getCourseAmount(){
    	return this.courseAmount;
    }
    
    public void setResult(Course[] courses){
    	int i;
    	int j;
    	Cell name;
    	Cell empl;
    	Cell course;
    	Cell email;
    	Student current;
    	for(i = 0; i < getCourseAmount(); i++){
    		courses[i].sortStudents();
    		xlWBook.createSheet(courses[i].getName());
    		xlSheet = xlWBook.getSheetAt(i+1);
    		xlSheet.setColumnWidth(0, 8000);
    		xlSheet.setColumnWidth(1, 4000);
    		xlSheet.setColumnWidth(2, 8000);
    		xlRow = xlSheet.createRow(0);
    		course = xlRow.createCell(0);
    		CellStyle style = xlWBook.createCellStyle();
    		Font header = xlWBook.createFont();
    		header.setFontHeightInPoints((short)14);
    		header.setBold(true);
    		style.setFont(header);
    		course.setCellValue(courses[i].getName()+" Roster");
    		course.setCellStyle(style);
    		xlRow = xlSheet.createRow(1);
    		name = xlRow.createCell(0);
    		empl = xlRow.createCell(1);
    		email = xlRow.createCell(2);
    		name.setCellValue("Student Name");
    		empl.setCellValue("EMPLID");
    		email.setCellValue("Email");
    		name.setCellStyle(style);
    		empl.setCellStyle(style);
    		email.setCellStyle(style);
    		for(j = 0; j < courses[i].getPopulation(); j++){
    			xlRow = xlSheet.createRow(j+2);
    			name = xlRow.createCell(0);
    			empl = xlRow.createCell(1);
    			email = xlRow.createCell(2);
    			current = courses[i].getStudent(j);
    			if(current.getLastName().equals(current.getFirstName()))
    				name.setCellValue(current.getFirstName());
    			else
    				name.setCellValue(current.getFirstName() + " " + current.getLastName());
    			empl.setCellValue(current.getEMPLID());
    			email.setCellValue(current.getEmail());

    		}
    		output();
    	}
    	close();
    	JOptionPane.showMessageDialog(null, "Students distributed successfully!");
    }
    
	public void output(){
        try {
            FileOutputStream output_file = new FileOutputStream(getFilePath()+getFileName());
            xlWBook.write(output_file);
            output_file.close();
        } catch(IOException e){
            System.out.println("Database not found.");
        }
    }

	public void close() {
        try {
            xlWBook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
