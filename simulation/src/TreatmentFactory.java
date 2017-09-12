
public class TreatmentFactory {
	

	// Offered treatment at VA based on Dr.Kaplan data
	public static String[] treatmentNames = { "Resection", "Ablation", "Trans-Arterial", "Sorafenib",
			"Radiation", "Experimental therapies", "No Therapy" };

	// Probability of Treatment[i] for stage[i] based on Dr.Kaplan data
	private static double[][] treatmentProbability = {  { 0.05,  0.35, 0.81, 0.85, 0.86, 1 },
														{ 0.05,  0.23, 0.78, 0.85, 0.85, 1 }, 
														{ 0.03,  0.06, 0.54, 0.79, 0.80, 1 },
														{ 0.01,  0.03, 0.23, 0.68, 0.72, 1 },
														{ 0.01,  0.05, 0.24, 0.41, 0.42, 1 } };

	public static Treatment getTreatmentFromCode(int code){
		
		Treatment t = new Treatment();
		t.code = code;
		
		switch (code) {
		case Treatment.PHYSICAL_EXAM:
			t.treatmentName = "PHYSICAL_EXAM";
			t.requirements.add(Resource.HEPATOLOGIST);
			break;
		case Treatment.LAB_TEST:
			t.treatmentName = "LAB_TEST";
			t.requirements.add(Resource.LAB);
			break;
		case Treatment.ULTRASOUND:
			t.treatmentName = "ULTRASOUND";
			t.requirements.add(Resource.RADIOLOGIST);
			break;
		case Treatment.MRI_CT:
			t.treatmentName = "MRI_CT";
			t.requirements.add(Resource.RADIOLOGIST);
			break;
		case Treatment.BIOPSY:
			t.treatmentName = "BIOPSY";
			t.requirements.add(Resource.PATHOLOGIST);
			t.requirements.add(Resource.INTRADIOLOGIST);
			break;
		case Treatment.GENERALTUMORBOARD:
			t.treatmentName = "GENERAL_TUMORBAORD";
			t.requirements.add(Resource.GENERAL_TUMORBOARD);
			break;
		case Treatment.LIVERTUMORBOARD:
			t.treatmentName = "LIVER_TUMORBAORD";
			t.requirements.add(Resource.LIVER_TUMORBOARD);
			t.waitAfter = 3;
			break;
		case Treatment.FOLLOWUP1:
			t.treatmentName = "FOLLOWUP RESECTION";
			t.requirements.add(Resource.ONCOLOGIC_SURGEON);
			break;
		case Treatment.FOLLOWUP2:
			t.treatmentName = "FOLLOWUP ABLATION";
			t.requirements.add(Resource.INTRADIOLOGIST);
			break;
		case Treatment.FOLLOWUP3:
			t.treatmentName = "FOLLOWUP Trans-Arterial";
			t.requirements.add(Resource.INTRADIOLOGIST);	
			break;
		case Treatment.FOLLOWUP4:
			t.treatmentName = "FOLLOWUP Sorafenib";
			t.requirements.add(Resource.MEDICAL_ONCOLOGY);
			break;
		case Treatment.FOLLOWUP5:
			t.treatmentName = "FOLLOWUP RADIATION";
			t.requirements.add(Resource.RADIATION_ONCOLOGY);
			break;
		case Treatment.FOLLOWUP6:
			t.treatmentName = "FOLLOWUP6 Experimental therapies";
			t.requirements.add(Resource.MEDICAL_ONCOLOGY);
			break;	

		default:
			break;
		}
		
		return t;
	}
	

	public static Treatment getTreatmentForStage(int stage) {

		Treatment t = new Treatment();

		double r = Simulation.RANDOM.nextDouble();

		if (r <= treatmentProbability[stage][0]) {
			t.treatmentName = treatmentNames[0];
			t.requirements.add(Resource.HOSPITAL_BED);
			t.requirements.add(Resource.ONCOLOGIC_SURGEON);
		} else if (r > treatmentProbability[stage][0] && r <= treatmentProbability[stage][1]) {
			t.treatmentName = treatmentNames[1];
			t.requirements.add(Resource.INTRADIOLOGIST);
		} else if (r > treatmentProbability[stage][1] && r <= treatmentProbability[stage][2]) {
			t.treatmentName = treatmentNames[2];
			t.requirements.add(Resource.INTRADIOLOGIST);
		} else if (r > treatmentProbability[stage][2] && r <= treatmentProbability[stage][3]) {
			t.treatmentName = treatmentNames[3];
			t.requirements.add(Resource.MEDICAL_ONCOLOGY);
		} else if (r > treatmentProbability[stage][3] && r <= treatmentProbability[stage][4]) {
			t.treatmentName = treatmentNames[4];
			t.requirements.add(Resource.RADIATION_ONCOLOGY);
		} else if(r > treatmentProbability[stage][4] && r <= treatmentProbability[stage][5]) {
			t.treatmentName = treatmentNames[5];	
			t.requirements.add(Resource.MEDICAL_ONCOLOGY);
		}else{
			t.treatmentName = treatmentNames[6];
			
		}

		t.waitAfter = 3;
		
		return t;
	}
	
}
