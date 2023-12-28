package edu.comillas.bigdata.avro_generator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class AvroGenerator {

	
	static String path = "." ;
	static SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public static void main(String[] args) throws IOException {
	
		
		Options options = new Options();
		
		int numRecords = 100 ;
		
		options.addOption(new Option("h", "help", false, "Print this help"));
		options.addOption(new Option("n", "numrecords", true, "Number of records in file (default 100)"));
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
			formatter.printHelp("AvroGenerator", options);
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

		String filename = "data_" + sdfFileName.format(new Date() ) +".avro";
		
	    Schema schema = ReflectData.get().getSchema(MyDataObject.class);

	    // create a file of packets
	    DatumWriter<MyDataObject> writer = new ReflectDatumWriter<MyDataObject>(MyDataObject.class);
	    DataFileWriter<MyDataObject> dataFileWriter = new DataFileWriter<MyDataObject>(writer);
		DataFileWriter<MyDataObject> out = dataFileWriter
	      .create(schema, new File(path + "/" + filename))
	     ;

	    // write numRecords packets to the file
	    for (int i = 0; i < numRecords; i++) {
	      out.append( new MyDataObject() );
	    }

	    // close the output file
	    dataFileWriter.close();
	    out.close();
		
	    System.exit(0);
	    

	    
	    
	}
	
}
