import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;


public class Simulation {
	
	public static final int WEEKS = 48;
	public static final int MAX_WAIT = 6;

	public static final Random RANDOM = new Random(1235);
	
	public static StringBuffer sb = new StringBuffer();
	
	public static void main(String[] args) throws IOException {

	    ResourcePool rp = new ResourcePool();
		
		HashMap<String, Integer> resourceMap = new HashMap<String, Integer>();
		resourceMap.put("HEPATOLOGIST", Resource.HEPATOLOGIST);
		resourceMap.put("ONCOLOGIST",   Resource.ONCOLOGIST);
		resourceMap.put("RADIOLOGIST", Resource.RADIOLOGIST);
        resourceMap.put("PATHOLOGIST",   Resource.PATHOLOGIST);
        resourceMap.put("HOSPITAL_BED", Resource.HOSPITAL_BED);
        resourceMap.put("INTRADIOLOGIST", Resource.INTRADIOLOGIST);
        resourceMap.put("GENERAL_TUMORBOARD", Resource.GENERAL_TUMORBOARD);
        resourceMap.put("LAB",   Resource.LAB);
        resourceMap.put("ONCOLOGIC SURGEON",   Resource.ONCOLOGIC_SURGEON);
        resourceMap.put("RADIATION_ONCOLOGY",   Resource.RADIATION_ONCOLOGY);
        resourceMap.put("MEDICAL_ONCOLOGY",   Resource.MEDICAL_ONCOLOGY);
        resourceMap.put("LIVER_TUMORBOARD",   Resource.LIVER_TUMORBOARD);
        
        Scanner scn = new Scanner(System.in);
        
        log("Do you want to read a CSV file named resources.csv? (yes/no)");
        String response = scn.next();
        if(!response.equals("no")){
            BufferedReader br = new BufferedReader(new FileReader(new File("resources.csv")));
            String line;
            boolean header = true;
            while( (line = br.readLine()) != null){
                if(header){
                    header=false;
                    continue;
                }
                String parts[] = line.split(",");
                Resource r = new Resource(parts[1],Integer.valueOf(parts[0]),Integer.valueOf(parts[2]),WEEKS);
                rp.pool.add(r);
            }
            br.close();
        }else{
            for(Entry<String, Integer> entry : resourceMap.entrySet()){
                log("How different resources of type "+entry.getKey()+" do you have? ");
                int total = scn.nextInt();
                for(int i=0;i<total;i++){
                    log("How many patients per week can "+entry.getKey()+" #"+(i+1)+" serve?");
                    int perWeek = scn.nextInt();
                    Resource r = new Resource(entry.getKey()+" #"+(i+1),entry.getValue(),perWeek,WEEKS);
                    rp.pool.add(r);
                }
            }
        }
        scn.close();
		
		List<Patient> patients =  new ArrayList<Patient>();
		for(int i =0;i<80;i++){
			Patient p = new Patient();
			//log(p.getPatientInfo());
			patients.add(p);
			
		}

		List<Patient> removedPatients =  new LinkedList<Patient>();
		
		int transferedPatients = 0;
		int recievedTreatmentAndFollowupPatients = 0;
		int t =0;
		List<Patient> waitList =  new LinkedList<Patient>();
		while(t <= WEEKS){
			if(patients.size()>0){
				int p = Double.valueOf(Simulation.RANDOM.nextDouble()*10).intValue();
				log("Selected "+p+" patients. Waitlist size="+waitList.size());
				for(int i=0;i<Math.min(p, patients.size());i++){
				    Patient tmp = patients.remove(0);
				    tmp.lastTime = t;
					waitList.add(tmp);
				}
			}
			
			Iterator<Patient> iter = waitList.iterator();
			
			// for every patient in the waitlist
			while(iter.hasNext()){
				Patient patient = iter.next();
				// Only if this patient has been finished one of his treatments (last)
				if(patient.lastTime <= t){
					// For every treatment that the patient has
					for(Treatment treatment : patient.treatments){
						// If this treatment is not complete
						if(!treatment.complete){
							log("S) "+patient.name+" with ("+patient.treatments.size()+" treatments) treatment "+treatment.treatmentName);
							// Schedule it
							int time = rp.schedule(patient.lastTime, treatment.requirements);
							if(time<0){
							    log("S) This treatment is unavailable");
							    if(time == -1){
    							    log("S) The wait was too long");
							    }
							    if(time == -2){
                                    log("S) Not resources available, there were zero available to treat ("+treatment.treatmentName+")");
                                }
							    log("S) Patient is sent to another hospital. Will be back in two weeks (t="+(t+2)+")");
							    patient.addLog(patient.lastTime, "Transfered out to receive treatment "+treatment.treatmentName);
                                transferedPatients+=1;
                                // Set time in the future, so we don't attempt to treat this patient until then (line 168)
                                patient.lastTime+=2;
							}else{
							    
							    if(time-patient.lastTime>0){
							        patient.addLog(patient.lastTime, "Treatment "+treatment.treatmentName+" was scheduled for week "+time);
							    }
							    
								patient.lastTime = time+treatment.waitAfter;
								patient.addLog(time, "Receiving treatment "+treatment.treatmentName+" at time "+time+". Next treatment at "+patient.lastTime);
								log("S) Receiving treatment "+treatment.treatmentName+" at time "+time+". Next treatment at "+patient.lastTime);
							}
							treatment.complete = true;
						}
					}
					
					// Find the last treatment that the patient received
					Treatment last = patient.treatments.get(patient.treatments.size()-1);
					// If the last treatment wasn't a followup, we need to add a followup
					// Treatment codes >100 are followups
					if(last.code > 100){
					    patient.addLog(patient.lastTime, "Patient recieved full treatment (including follow up)");
					    log("S) Patient recieved full treatment (including follow up)");
					    recievedTreatmentAndFollowupPatients+=1;
					    iter.remove();
					    removedPatients.add(patient);
					}
					last.complete = true;
				}
			}
			t += 1;
			log("S) Moving time forward: "+t);
		}
		
		for(Resource resource : rp.pool){
			log(resource.name +")\t\t\t"+Arrays.toString(resource.schedule));
		}
		
		for(Patient patient : removedPatients){
			log(patient.name+" Cured on week "+patient.lastTime);
			patient.printLog();
		}
		
		for(Patient patient : waitList){
            log(patient.name+" still in waitlist");
            patient.printLog();
        }

        log("Total patients still in waitlist: " + waitList.size());
        log("Total transfered patients: " + transferedPatients);
        log("Total patients that recieved treatment and followup: " + recievedTreatmentAndFollowupPatients);
        
        FileWriter fw = new FileWriter(new File("output.txt"));
        fw.write(sb.toString());
        fw.close();
		
	}
	
	public static void log(String log){
	    System.out.println(log);
	    sb.append(log+"\n");
	}

}
