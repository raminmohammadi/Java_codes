import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ResourcePool {
	public List<Resource> pool = new ArrayList<Resource>();
	public int schedule(int startTime,List<Integer> requiredResources){
		List<Resource> resourcesFound = null;
		int time;
		for(time = startTime;time<Simulation.WEEKS;time++){
			resourcesFound = new LinkedList<Resource>();
			boolean foundAtLeastOne;
			for(Integer requiredResource : requiredResources){
			    foundAtLeastOne = false;
				for(Resource currentResource : pool){
					//Simulation.log("Looking for resource "+requiredResource+". Comparing against resource "+currentResource.name+" ("+currentResource.type+") available spots "+currentResource.schedule[time]);
				    if(currentResource.type == requiredResource){
				        foundAtLeastOne = true;
					    if(currentResource.schedule[time]>0){
                            Simulation.log("RP) Found resource \""+currentResource.name+"\" available at time "+time);
					        resourcesFound.add(currentResource);
					        break;
					    }
					}
				}
				if(!foundAtLeastOne){
				    Simulation.log("RP) There were zero of a required resource ("+requiredResource+"), this treatment is unavailable");
				    return -2;
				}
			}
			if(resourcesFound.size() == requiredResources.size()){
				break;
			}
			//Simulation.log("Couldn't find the resources for time "+time+" ("+resourcesFound.size()+"!="+requiredResources.size());
		}
		
		// Also check for max wait
		if(time >= Simulation.WEEKS || time-startTime > Simulation.MAX_WAIT){
		    Simulation.log("RP) Exeeded the number of weeks, or the max wait.");
			return -1;
		}
		
		if(resourcesFound.size() == requiredResources.size()){
			Simulation.log("RP) We found all the resources at time "+time);
			for(Resource resourceFound : resourcesFound){
				resourceFound.schedule[time] -= 1;
			}
		    return time;
	    } else {
			Simulation.log("RP) We did not find all the resources");
			return -1;
		}
		
	}
}
