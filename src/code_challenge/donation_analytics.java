package code_challenge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class donation_analytics {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String CMTE_ID,NAME,ZIP_CODE,YEAR,AMOUNT,OTHER_ID = null;       //Declaring and initializing the required variables
		
		String recipient_id = null;
		String rdonor_name = null;
		String rdonor_year = null;                                     //Declaring and initializing the required variables
		String rdonor_zipcode = null;
		
		String contr_amt = null;
		int contr_amount;
		String rdonor_cmte = null;
		String last_zip = null;
		String last_year = null;                                       //Declaring and initializing the required variables
		double percentile = 0;
		
		ArrayList nameList = new ArrayList();
		ArrayList zipList = new ArrayList();                          //Creating Array lists for each field
		ArrayList yearList = new ArrayList();
		ArrayList cmteList = new ArrayList();
		ArrayList otherid_list = new ArrayList();
		
		ArrayList repeatdonor_nameList = new ArrayList();             //creating Array Lists to hold repeat donor details
		ArrayList repeatdonor_zipList = new ArrayList();
		ArrayList repeatdonor_yearList = new ArrayList();
		ArrayList amount_List = new ArrayList();
		ArrayList RD_recipientList = new ArrayList();
		ArrayList RD_cmte_List = new ArrayList();
		ArrayList lastzip_List = new ArrayList();
		ArrayList lastyear_List = new ArrayList();
		ArrayList<Integer> contr_amntList = new ArrayList<>();
		
		try {
			//read input text files using bufferedreader
			
			File dir = new File(".");
			File fin = new File(dir.getCanonicalPath() + File.separator + "input/itcont.txt");
			BufferedReader br = new BufferedReader(new FileReader(fin));
			String line = null;
			
			File fin2 = new File(dir.getCanonicalPath() + File.separator + "input/percentile.txt");
			BufferedReader br2 = new BufferedReader(new FileReader(fin2));
			String line2 = null;
			
			while ((line2 = br2.readLine()) != null){
				 percentile = Double.parseDouble(line2);
			}
			
			while ((line = br.readLine()) != null) {  //while the input file is not empty
				String tmp[] = line.split("\\|");     //split the fields in input file by delimiter
				CMTE_ID = tmp[0];                     //assign the fields to corresponding variables 
				cmteList.add(CMTE_ID);                //add the values to the list
				
				NAME = tmp[7];
				nameList.add(NAME);
				
				ZIP_CODE = tmp[10];
				ZIP_CODE = ZIP_CODE.substring(0, 5);		//To obtain first five digits of zipcode 			
				zipList.add(ZIP_CODE);
				
				YEAR = tmp[13];
				YEAR = YEAR.substring(4, 8);                //To obtain the four digit year
				yearList.add(YEAR);
				
				AMOUNT = tmp[14];
				amount_List.add(AMOUNT);
				
				OTHER_ID = tmp[15];
				otherid_list.add(OTHER_ID);
			}
			
			br.close();
			br2.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Set sname = new HashSet<>();                      //using HashSet to find the repeated entry
		Set scmte = new HashSet<>();
		Set szip = new HashSet<>();
		int ns = nameList.size();
		int zs = zipList.size();
		int cs = cmteList.size();		
		
		for(int i = 0; i < ns; i++){
			if(otherid_list.get(i).equals("")){                   			//check for 'OTHER ID' is empty
				if(sname.add(nameList.get(i))==false){				
					rdonor_zipcode = zipList.get(i).toString();             //get 'zipcode' of repeat donor
					repeatdonor_zipList.add(rdonor_zipcode);
					
					rdonor_year = yearList.get(i).toString();               //get 'year' of repeat donor
					repeatdonor_yearList.add(rdonor_year);
					
					recipient_id = cmteList.get(i).toString();              //get 'CMTE ID' of repeat donor
					RD_recipientList.add(recipient_id);
				}
			}		
		}
		
		for(int i = 0; i < cs; i++){
			if (recipient_id.equals(cmteList.get(i))) {
					if(zipList.get(i).equals(rdonor_zipcode) && yearList.get(i).equals(rdonor_year)){
						rdonor_name = nameList.get(i).toString();
						repeatdonor_nameList.add(rdonor_name);
						rdonor_cmte = cmteList.get(i).toString();                   //to find the number of contributions from repeat donors
						RD_cmte_List.add(rdonor_cmte);
						last_zip = zipList.get(i).toString();
						lastzip_List.add(last_zip);
						last_year = yearList.get(i).toString();
						lastyear_List.add(last_year);
						contr_amt = amount_List.get(i).toString();
						contr_amount = Integer.parseInt(contr_amt);
						contr_amntList.add(contr_amount);
						int RD_cs = RD_cmte_List.size();                         //get the count of contributions
						String LAST_CID = "";
						String LAST_ZIP = "";                                    //Declaring and Initializing the local variables required
						String LAST_YEAR = "";
						String TOTAL_AMNT = "";
						int sum = 0;
						if(RD_cs==1){
							double cal_val1 = percentile / 100;                   //calculating the percentile value
							double cal_percnt = cal_val1 * (RD_cs + 1);
							int per_value = (int) Math.round(cal_percnt);
							int rank_value = contr_amntList.get(per_value-1);      //find the percentile rank value
							for(int j=0; j<RD_cs; j++){
							  LAST_CID = RD_cmte_List.get(j).toString();
							  LAST_ZIP = lastzip_List.get(j).toString();
							  LAST_YEAR = lastyear_List.get(j).toString();
							  TOTAL_AMNT = contr_amntList.get(j).toString();
							  File dir = new File(".");
							  File fin3 = new File(dir.getCanonicalPath() + File.separator + "output/repeat_donors.txt");
							  FileWriter file_writer = new FileWriter(fin3);
							  BufferedWriter bw_writer = new BufferedWriter(file_writer);
							  PrintWriter print_writer = new PrintWriter(bw_writer);
							  print_writer.println(LAST_CID+"|"+LAST_ZIP+"|"+LAST_YEAR+"|"+rank_value+"|"+TOTAL_AMNT+"|"+RD_cs);
							  print_writer.close();
							  System.out.println(LAST_CID+"|"+LAST_ZIP+"|"+LAST_YEAR+"|"+rank_value+"|"+TOTAL_AMNT+"|"+RD_cs);
							}
							
						}
						if(RD_cs>1){
							for(int k : contr_amntList){
								sum = sum + k;	
							}
							double cal_val1 = percentile / 100;                       //calculating the percentile value
							double cal_percnt = cal_val1 * (RD_cs + 1);
							int per_value = (int) Math.round(cal_percnt);
							int rank_value = contr_amntList.get(per_value-1);         //find the percentile rank value
							for(int j=0; j<RD_cs; j++){
									 LAST_CID = RD_cmte_List.get(j).toString();
									 LAST_ZIP = lastzip_List.get(j).toString();
									 LAST_YEAR = lastyear_List.get(j).toString();
							}
							File dir = new File(".");
							File fin3 = new File(dir.getCanonicalPath() + File.separator + "output/repeat_donors.txt");
							FileWriter file_writer = new FileWriter(fin3, true);
							BufferedWriter bw_writer = new BufferedWriter(file_writer);
							PrintWriter print_writer = new PrintWriter(bw_writer);
							print_writer.println(LAST_CID+"|"+LAST_ZIP+"|"+LAST_YEAR+"|"+rank_value+"|"+sum+"|"+RD_cs);
							print_writer.close();
							System.out.println(LAST_CID+"|"+LAST_ZIP+"|"+LAST_YEAR+"|"+rank_value+"|"+sum+"|"+RD_cs);    //Display the required fields with delimiter seperator.
						}	
					}
			}
		}
	}
}

		

	
