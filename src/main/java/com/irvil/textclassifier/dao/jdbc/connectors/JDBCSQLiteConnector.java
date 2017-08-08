package com.irvil.textclassifier.dao.jdbc.connectors;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCSQLiteConnector implements JDBCConnector {
  private final String dbName;

  public JDBCSQLiteConnector(String dbPath, String dbFileName) {
    if (dbFileName == null || dbFileName.equals("")) {
      throw new IllegalArgumentException();
    }

    this.dbName = dbPath + "/" + dbFileName;
  }

  @Override
  public Connection getConnection() throws SQLException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException ignored) {
    }

    return DriverManager.getConnection("jdbc:sqlite:" + dbName);
  }

  @Override
  public void createStorage() {
    // delete old database file
    new File(dbName).delete();

    List<String> sqlQueries = new ArrayList<>();

    // create database structure
    //

    sqlQueries.add("CREATE TABLE CharacteristicsNames " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE )");
    sqlQueries.add("CREATE TABLE CharacteristicsValues " +
        "( Id INTEGER, CharacteristicsNameId INTEGER, Value TEXT, PRIMARY KEY(Id,CharacteristicsNameId,Value) )");
    sqlQueries.add("CREATE TABLE ClassifiableTexts " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Text TEXT )");
    sqlQueries.add("CREATE TABLE ClassifiableTextsCharacteristics " +
        "( ClassifiableTextId INTEGER, CharacteristicsNameId INTEGER, CharacteristicsValueId INTEGER, PRIMARY KEY(ClassifiableTextId,CharacteristicsNameId,CharacteristicsValueId) )");
    sqlQueries.add("CREATE TABLE Vocabulary " +
        "( Id INTEGER PRIMARY KEY AUTOINCREMENT, Value TEXT UNIQUE )");

    // execute queries
    //

    try (Connection con = getConnection()) {
      Statement statement = con.createStatement();

      for (String sqlQuery : sqlQueries) {
        statement.execute(sqlQuery);
      }
    } catch (SQLException ignored) {
    }
  }
}