import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Patient {
	
	// Stages of the HCC based on the BCLC algorithm
	private static String[] stagesNames = {"0","A","B","C","D"};
	
	// Probability of diagnosing the patient by each stages based on Dr.Kaplan data
	private static double [] diagnosisProbability = { 0.06,0.362,0.684,0.856,1};
	private static int patientCounter = 0 ;
	
	String name;
	String stage;
	int stageNumber;
	int lastTime;
	int weeksWaiting;
	int treatmentdoneTime;
	
	List<Treatment> treatments = new ArrayList<Treatment>();
	String[] log = new String[Simulation.WEEKS+1];
	
	public Patient(){
		
		this.name = "Patient"+ patientCounter;
		patientCounter++;
		
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.PHYSICAL_EXAM));
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.LAB_TEST));
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.ULTRASOUND));
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.MRI_CT));
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.BIOPSY));
		treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.GENERALTUMORBOARD));
		//treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.LIVERTUMORBOARD));
	
		makeSick();
		
		Treatment lastTreatment = TreatmentFactory.getTreatmentForStage(stageNumber);
		
		if("Resection".equals(lastTreatment.treatmentName)){
			treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.LIVERTUMORBOARD));
			treatments.add(lastTreatment);
			treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP1));
		}else{
		    treatments.add(lastTreatment);
		    if("Ablation".equals(lastTreatment.treatmentName)){
                treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP2));
            }else if("Sorafenib".equals(lastTreatment.treatmentName)){
                treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP4));
            }else if("Radiation".equals(lastTreatment.treatmentName)){
                treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP5));
            }else if("Trans-Arterial".equals(lastTreatment.treatmentName)){
                treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP3));
            }else if("Experimental therapies".equals(lastTreatment.treatmentName)){
                treatments.add(TreatmentFactory.getTreatmentFromCode(Treatment.FOLLOWUP6));
            }
		}
			
	}

	
	public String getPatientInfo(){
		String info = name+" Stage: " +stage+ " Treatment: \n";
		for(int i=0;i<treatments.size();i++){
			info += "\t"+treatments.get(i).getTreatmentInfo()+"\n";
		}
		return info;
		
	}
	
	public void printLog(){
	    Simulation.log("Weekly log for "+name);
	    for(int i=0;i<log.length;i++){
	        if(log[i] != null){
	            Simulation.log("Week "+i+": "+log[i]);
	        }else{
	            Simulation.log("Week "+i+": --");
	        }
	    }
	}
		
		
	public void addLog(int time, String newLog){
	    if(log[time] != null){
	        log[time] = log[time] +"\n\t"+newLog;
	    }else{
	        log[time] = newLog;
	    }
	}
	
	public void makeSick() {
		
		double r = Simulation.RANDOM.nextDouble();
		
		if (r <= diagnosisProbability[0]){
			stage = stagesNames[0];
			stageNumber=0;
		}
		else if (r > diagnosisProbability[0]&&r<=diagnosisProbability[1]) {
			stage = stagesNames[1];
			stageNumber=1;
		}
		else if (r > diagnosisProbability[1]&& r<= diagnosisProbability[2]) {
			stage = stagesNames[2];
			stageNumber=2;
		}
		else if (r > diagnosisProbability[2]&& r<= diagnosisProbability[3]) {
			stage = stagesNames[3];
			stageNumber=3;
		}
		else {
			stage = stagesNames[4];
			stageNumber=4;
		}
		
	}
		
	
	
	

}
