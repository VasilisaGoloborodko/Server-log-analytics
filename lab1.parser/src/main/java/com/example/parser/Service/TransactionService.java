package com.example.parser.Service;

import com.example.parser.Model.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionService {

	private static Logger log = LoggerFactory.getLogger(TransactionService.class);
	private static final String GET_ACTIVITY = "SELECT COUNT(*) amount, DAT\n" +
			"FROM PARSED_DATA\n" +
			"GROUP BY DAT\n" +
			"ORDER BY amount DESC\n" +
			"LIMIT 3 OFFSET 0";
	private static final String POPULAR_RESOURCES = "SELECT COUNT(*) amount,requestline\n" +
			"FROM PARSED_DATA\n" +
			"GROUP BY requestline\n" +
			"ORDER BY amount DESC\n" +
			"LIMIT 10 OFFSET 0";

	public static void insertLine(Line line, Connection con) throws SQLException {

		String insert = "INSERT INTO PARSED_DATA(IP, DAT, TIMEZONE, METHOD, REQUESTLINE, STATUSCODE, OBJECTSIZE)\n" +
				"VALUES(?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement stat = con.prepareStatement(insert);
		stat.setString(1, line.getIp());
		stat.setObject(2, line.getDate());
		stat.setString(3, line.getTimezone());
		stat.setString(4, line.getMethod());
		stat.setString(5, line.getRequestLine());
		stat.setInt(6, line.getStatusCode());
		stat.setInt(7, line.getObjectSize());

		stat.executeUpdate();
	}

	public static void getActivity(Connection con) throws SQLException {
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(GET_ACTIVITY);
		List<LocalDateTime> activity = new ArrayList<LocalDateTime>();
		while (rs.next()){
			activity.add(rs.getTimestamp("dat").toLocalDateTime());
		}
		System.out.println(activity);
	}

	public static void getPopResource(Connection con) throws SQLException {
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(POPULAR_RESOURCES);
		TreeMap<Integer, String> resources = new TreeMap<>();
		while (rs.next()){
			resources.put(rs.getInt("amount"), rs.getString("requestline"));
		}
		Set set = resources.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			System.out.println("The string '" + mentry.getValue() + "' is present " + mentry.getKey() + " times");
		}
	}
}
