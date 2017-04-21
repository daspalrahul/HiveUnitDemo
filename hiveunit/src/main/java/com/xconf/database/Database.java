package com.xconf.database;

public class Database extends net.achalaggarwal.workerbee.Database {
  public static final Database db = new Database();

  private Database() {
    super("${mainDB}", "Database");
  }
}
