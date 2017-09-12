/*
 within this class we are creating one records and using constructor we will assign the necesseary values to the records
 later we will use networkData and add this record to the our data set
 */
public class DataRecord {
	String ip, destinationAddress;
	int port;
	long date;
	String protocol;
	double size;
	
	
	
	public DataRecord(String ip, String destinationAddress, int port, long date, double size , String protocol) {
		this.date=date;
		this.destinationAddress=destinationAddress;
		this.ip = ip;
		this.size = size;
		this.date = date;
		this.protocol = protocol;
		
	}

}
