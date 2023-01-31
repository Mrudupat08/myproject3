package test;
        import java.io.*;
       // import java.text.SimpleDateFormat;
        import java.util.*;
        import java.util.regex.*;

        /**
         *Declares a public class "LogAnalyzer"contains a static method called "extractTimestamp" 
         *that takes in a string (json) parameter and returns a string (timestamp).
         */
        public class LogAnalyzer {
        	
            private static String extractTimestamp(String json)
            {
            	//Initializes an empty string variable called "timeStamp"
                String timeStamp = "";      
                /*
                 * Creates a pattern for regular expression matching using the "compile" method from the Pattern class.
                 * The regular expression is looking for a "timeStamp" field in the json string and captures the numeric value after it.
                 */
                Pattern pattern = Pattern.compile("\"timestamp\":(\\d+)");
                /*
                 * Creates a Matcher object using the pattern and the json string passed in as a parameter.
                 */
                Matcher matcher = pattern.matcher(json);
                
                if (matcher.find()) {// assigns the captured value to the "timeStamp" variable.
                    timeStamp = matcher.group(1);
                }
                return timeStamp;
            }
            private static String extractContent(String json) {
                String utterance = "";
                Pattern pattern = Pattern.compile("content\":\"(.+?)\"");
                Matcher matcher = pattern.matcher(json);
                if (matcher.find()) {
                	utterance = matcher.group(1);
                }
                return utterance;
            }
            public static void main(String[]args) throws FileNotFoundException, IOException  {
            	
                File myFile= new File("C:\\Users\\mrudula.patankar\\eclipse-workspace\\test\\src\\test\\cerence_ark_sdk_2023-01-18_18-05-07.log");
                /*
                 * Declares a string variable "SearchWord" and "SearchWord2" to store the word the user wants to search for.
                 */
                
                String endpoint;
                String startpoint;
                
                
                //Declares a Scanner object to read user input.
                
                Scanner sc=new Scanner(System.in);
                System.out.print("Enter endpoint");
                endpoint=sc.nextLine();
                System.out.print("Enter startpoint");
                startpoint=sc.nextLine();
                
              //Creates a new Lists object to store the timestamps of lines that contain the search word.
                List<String> endTimestamp = new ArrayList<>();
                List<String> startTimestamp = new ArrayList<>();
                List<String> contents = new ArrayList<>();     //to store the utterances 
                
                
                
                /**
                 * Uses try block to create a BufferedReader object to read the file. 
                 */
                try (BufferedReader br = new BufferedReader(new FileReader(myFile)))
                
                { 
                	//Declares a variable "json" to temporarily store each line of the file as it is read.
                    String json;
                    /*
                     * Begins a while loop that continues until all lines of the file have been read.
                       Reads the next line of the file and assigns it to the "json" variable.
                     */
                    while ((json = br.readLine()) != null)//
                    {
                    	
                        if (json.contains(endpoint )&& json.contains("TimeMarker")) {
                        /*
                         * Calls"extractTimestamp" method on the current line to get the timeStamp value and adds it to the "timestamps" list.
                         */
                        String timeStamp = extractTimestamp(json);
                        endTimestamp.add(timeStamp);
                        }
                        if (json.contains(startpoint)&&json.contains("TimeMarker")) {
                           
                            String timeStamp2 = extractTimestamp(json);
                            startTimestamp.add(timeStamp2);
                        }
                        if(json.contains(endpoint)&&json.contains("ace.TimeMarker ")) {
                        	 if (json.contains("content")) {
                        		 String content=extractContent(json);
                        		 contents.add(content);
                        	 } 
                        } 
                       
                        
                       
                    }
                }

                
                for (int i = 0; i < endTimestamp.size(); i++) {
                	endTimestamp.set(i, endTimestamp.get(i).replaceAll("[^\\d]", ""));
                }
                
                for (int i = 0; i < startTimestamp.size(); i++) {
                	startTimestamp.set(i, startTimestamp.get(i).replaceAll("[^\\d]", ""));
                }
                
                
                /*SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                for(String timestamp1:endTimestamp) {
                	long time=Long.parseLong(timestamp1);Date date=new Date(time);
                	System.out.println(formatter.format(date));
                }
                
                for(String timestamp2:startTimestamp) {
                	long time=Long.parseLong(timestamp2);Date date=new Date(time);
                	System.out.println(formatter.format(date));
                }
               */
                
                List<Long> differences = new ArrayList<>();
                for (int i = 0; i < endTimestamp.size(); i++) {
                    long timestamp1 = Long.parseLong(endTimestamp.get(i));
                    long timestamp2 = Long.parseLong(startTimestamp.get(i));
                    long difference = (timestamp1- timestamp2);
                    
                    differences.add(difference);      //calculates difference and add it to difference list	
                }
               
                for (int i = 0; i < endTimestamp.size(); i++) {
           
                	 System.out.printf("For utterance: %s, the timestamps are: %s and %s and latency is %s .\n", contents.get(i), endTimestamp.get(i),startTimestamp.get(i),differences.get(i));
                }
                
                
                FileWriter writer = new FileWriter("C:\\Users\\mrudula.patankar\\eclipse-workspace\\test\\src\\test\\Solution.txt");
                writer.write("Differences between timestamps: " + differences + "\n");
                writer.write("utterances:"+contents + "\n");
                writer.write("Timestamps for searchWord1: " + endTimestamp + "\n");
                writer.write("Timestamps for searchWord2: " + startTimestamp + "\n");
                writer.close();
                sc.close();
            }
        } 