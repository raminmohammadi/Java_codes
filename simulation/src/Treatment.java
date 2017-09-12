import java.util.ArrayList;
import java.util.List;

public class Treatment {

	public static final int PHYSICAL_EXAM = 15;
	public static final int LAB_TEST = 16;
	public static final int MRI_CT = 18;
	public static final int ULTRASOUND = 20;
	public static final int BIOPSY = 22;
	public static final int GENERALTUMORBOARD = 31;
	public static final int LIVERTUMORBOARD=23;
	
	public static final int FOLLOWUP1 = 101;
	public static final int FOLLOWUP2 = 102;
	public static final int FOLLOWUP3 = 103;
	public static final int FOLLOWUP4 = 104;
	public static final int FOLLOWUP5 = 105;
	public static final int FOLLOWUP6 = 106;
	
		
	
	String treatmentName;
	boolean complete = false;
	int code;
	int waitAfter = 1;

	List<Integer> requirements = new ArrayList<Integer>();

	public String getTreatmentInfo() {
		return treatmentName + " requirenments :" + requirements + "\n";
	}

}
