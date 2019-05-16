package com.example.parser;

import com.example.parser.Model.Line;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static com.example.parser.Service.ConnectionService.getConnection;
import static com.example.parser.Service.TransactionService.*;

@SpringBootApplication
public class ParserApplication implements ApplicationRunner{

	public static void main(String[] args) { SpringApplication.run(ParserApplication.class, args); }

	private int lineNumb = 0;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(args.containsOption("logFile") ){

			String fileName = args.getOptionValues("logFile").get(0);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			Optional<Connection> con = getConnection("jdbc:postgresql://localhost:5432/parser",
					"postgres",
					"root");
			String line = null;
			if(con.isPresent()){
				while ((line = reader.readLine()) != null) {
					insertLine(parseLine(line), con.get());
				}
			}
			con.get().close();
		}
		if (args.containsOption("activity")){
			Optional<Connection> con = getConnection("jdbc:postgresql://localhost:5432/parser",
					"postgres",
					"root");
			if(con.isPresent()) {
				getActivity(con.get());
			}
			con.get().close();
		}
		if (args.containsOption("popRes")) {
			Optional<Connection> con = getConnection("jdbc:postgresql://localhost:5432/parser",
					"postgres",
					"root");
			if(con.isPresent()) {
				getPopResource(con.get());
			}
			con.get().close();
		}

	}

	private Line parseLine(String line) throws SQLException, ParseException {
		StringTokenizer matcher = new StringTokenizer(line);
		lineNumb++;
		System.out.println(lineNumb);

		Line record = new Line();
		record.setIp(matcher.nextToken());

		String date = matcher.nextToken("- [");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
		Date parsedDate = formatter.parse(date);
		record.setDate(parsedDate.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime());

		record.setTimezone(matcher.nextToken(" ]"));

		String method = matcher.nextToken("] \"");
		if(!"-".equals(method)){
			record.setMethod(method);
			record.setRequestLine(matcher.nextToken("\""));
		}

		record.setStatusCode(Integer.parseInt(matcher.nextToken("\" ")));

		String objectSize = matcher.nextToken();
		if(!"-".equals(objectSize)){
			record.setObjectSize(Integer.parseInt(objectSize));
		}

		return record;
	}

}
