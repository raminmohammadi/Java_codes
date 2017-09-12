/*
 * Within this class we will create a array list and add each data record which we have created within DataRecord class to the array list
 */
import java.util.ArrayList;


public class NetworkData {

	ArrayList list = new ArrayList();

	public void addDataRecords(DataRecord dr){
		list.add(dr);
		if(dr!=null){ 

		}
		else{
			System.out.println("Date " + dr.date + " destination address " + dr.destinationAddress + " ip address " + dr.ip + " port #" + dr.port + " Size " + dr.size + " Protocol " + dr.protocol);
		}
	}



	public static void main(String args[]){
		NetworkData nd = new NetworkData();

	}
	public ArrayList<DataRecord> getList() {
		return list;
	}

	public int getTotalRecords() {
		return list.size();

	}
}
