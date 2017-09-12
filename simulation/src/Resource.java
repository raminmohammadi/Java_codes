public class Resource {
	
	
	public static final int HEPATOLOGIST = 3;
	public static final int LAB = 4;
	public static final int RADIOLOGIST = 5;
	public static final int PATHOLOGIST = 7;
	public static final int HOSPITAL_BED = 8;
	public static final int ONCOLOGIST = 9;
	public static final int INTRADIOLOGIST = 10;
	public static final int GENERAL_TUMORBOARD = 12;
	public static final int LIVER_TUMORBOARD = 13;
	public static final int ONCOLOGIC_SURGEON = 14;
	public static final int MEDICAL_ONCOLOGY =15;
	public static final int RADIATION_ONCOLOGY = 16;
	
	String name;
	int type;
	int[] schedule;
	int[] boardSchedule;
	
	
	public Resource(String newName, int resourceType, int weeklyCapacity, int weeks){
    	name = newName;
    	type = resourceType;
    	schedule = new int[weeks];
    	
    	for(int i=0; i< weeks ;i++){
    		
    		schedule[i] = weeklyCapacity;
    		
    	}	
	}
}
