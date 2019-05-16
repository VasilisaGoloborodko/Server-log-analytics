package com.example.parser.Model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class Line {

	private String ip;
	private LocalDateTime date;
	private String timezone;
	private String method;
	private String requestLine;
	private int statusCode;
	private int objectSize;

}
