

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.datafaker.Faker;
import net.datafaker.formats.Format;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

@SuppressWarnings("deprecation")
public class DataGenerator {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
	static SimpleDateFormat sdfNice = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static Date tsFrom = null ;
	static Date tsTo = null ;
	static String path = "." ;
	static int maxFiles = 20 ;

	
	static Faker myFaker = new Faker();
	
	public static void main(String[] args) {
		
		Options options = new Options();
		
		int numRecords = 5000 ;
		
		options.addOption(new Option("h", "help", false, "Print this help"));
		options.addOption(new Option("n", "numrecords", true, "Number of records in file (default 5000)"));
		options.addOption(new Option("p", "path", true, "Path to write the file"));
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null ;
		try {
			cmd = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		if ( cmd.hasOption('h') || cmd.getOptions().length < 0 ) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("FileGenerator", options);
			System.exit(0) ;
		}
		
		
		if ( cmd.hasOption('n')  ) {
			try {
				numRecords = Integer.parseInt(cmd.getParsedOptionValue("numrecords").toString());
			} catch (Exception e) {
			}
		} 

		if ( cmd.hasOption('p')  ) {
			try {
				path = cmd.getParsedOptionValue("path").toString();
			} catch (Exception e) {}
		} 

		String filename = "data_" + sdfFileName.format(new Date() ) ;
		generateFile( filename, numRecords ) ;
	}
	
	private static void generateFile(String filename, int numRecords) {
		
		
		String fullFileName = path + "/" + filename + ".tmp" ;
		System.out.println (String.format( "%s - Generating file: %s, records: %d",
				sdfNice.format(new Date() ), 
				fullFileName, numRecords))  ;
		
		int printed = 0 ;
		BufferedOutputStream bfout = null ;
		
		File myFile = new File(fullFileName) ;
		
		try {
			bfout = new BufferedOutputStream (new FileOutputStream(myFile ) ) ;
		
			while ( printed < numRecords ) {
				printed ++ ;
				String data = getData() ;
				bfout.write( ("set /test_perf/node_" + String.format("%03d", myFaker.number().numberBetween(1, 100)) + " '" + data + "'\n").getBytes() );
			}
			bfout.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println () ;
		}
		
		myFile.renameTo(new File(path + "/" + filename)) ;
	}

	private static String getData() {
		
		return  Format.toJson()
        .set("firstName", () -> myFaker.name().firstName())
        .set("lastName", () -> myFaker.name().lastName())
        .set("addCountry", () -> myFaker.address().country())
        .set("addCity", () -> myFaker.address().city())
        .set("addStreet", () -> myFaker.address().streetAddress())
        .set("addZipcode", () -> myFaker.address().zipCode())
        .set("telephone", () ->myFaker.phoneNumber().phoneNumber())
        .build().generate() ;
		
	}

}
