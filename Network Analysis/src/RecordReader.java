import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class RecordReader {
	
	public RecordReader(){
		
	}

	public RecordReader(NetworkData nd, String fileName) {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					System.out.println("Line was empty!!");
				}
				String[] part = line.split("|");
				String ip = part[0];
				String destinationAddress = part[1];
				int port = Integer.valueOf(part[2]);
				long date = Long.valueOf(part[3]);
				double size = Double.valueOf(part[4]);
				String protocol = part[5];
				DataRecord dr = new DataRecord(ip, destinationAddress, port, date, size , protocol);
				nd.addDataRecords(dr);

			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save(DataRecord dr, FileWriter file) { // PRIVATE SAVE METHOD WHICH WILL ADD INFO TO A FILE 
		try {
			file.write("ip: " + dr.ip + " " );
			file.write(" destination ip: " + dr.destinationAddress + " ");
			file.write("Port: "+dr.port + " ");
			file.write("Date: "+dr.date+ " ");
			file.write("Size: "+dr.size + " ");
			file.write("Protocol:" +dr.protocol + " " + "\n");
		} catch (IOException ex) {
			System.out.println("IO Error recieved Exception in Private Save method : " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	public void save(String filename, NetworkData nd) { // PUBLIC SAVE METHOD WHICH WILL CREATE A NEW FILE AND USE PRIVATE SAVE METHOD TO ADD INFO TO IT
		FileWriter file;
		try {
			file = new FileWriter(filename);
			for (DataRecord dr : nd.getList()) {
				this.save(dr, file); // call for private Save method to save a
				// car into a file
			}
			file.close();
		} catch (IOException ex) {
			System.out.println("IO Error recieved Exception in public Save method : " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
