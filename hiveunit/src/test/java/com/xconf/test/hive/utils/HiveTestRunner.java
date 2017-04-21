package com.xconf.test.hive.utils;


import com.xconf.database.Database;
import com.google.common.io.Files;
import junitx.framework.ListAssert;
import net.achalaggarwal.workerbee.*;
import org.apache.hadoop.conf.Configuration;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.io.File.separator;
import static net.achalaggarwal.workerbee.QueryGenerator.recover;
import static net.achalaggarwal.workerbee.Utils.getRandomPositiveNumber;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public abstract class HiveTestRunner {
  protected String caseInstanceId;
  protected Repository repo;

  public static final String BASE_DIR = System.getProperty("workflow-dir.path");
  public static final String SETUP_BASE_DIR = System.getProperty("setup-dir.path");

  private Repository localHs2Repo() throws SQLException, IOException {
    return new Repository(
      "jdbc:hive2://localhost:20103/default",
      new Properties() {{
        put("user", "user");
        put("password", "pass");
        put("hiveconf:hadoop.tmp.dir",System.getProperty("temp-base.path"));
      }},
      new Configuration(){{
        set("hadoop.tmp.dir",System.getProperty("temp-base.path"));
      }}
    );
  }

  @Before
  public void createRepo() throws IOException, SQLException {
    repo = localHs2Repo();

    caseInstanceId = testCaseInstanceId();
    repo
      .hiveVar("mainDB", "maindb_" + caseInstanceId)
      .hiveVar("queue", "default")
      .hiveVar("user", "maindb_" + caseInstanceId)
      .hiveVar("tableBasePath", createTempDir("hive-unit"));
    //TODO: Move database creation to tests
    repo.execute(QueryGenerator.create(Database.db).ifNotExist());
//    repo.execute("ADD JAR " + System.getProperty("equipment-import.jar.path"));
  }

  public static String createTempDir(String prefix) {
    File dir = new File("target/test-data/hive-unit" + separator + prefix + "-" + randomAlphanumeric(20));
    dir.mkdirs();
    return dir.getAbsoluteFile().getAbsolutePath();
  }

  @After
  public void closeRepo() throws SQLException {
    repo.close();
  }

  protected static String testCaseInstanceId() {
    return String.valueOf(getRandomPositiveNumber());
  }

  protected <T extends TextTable> void listAssert(T table, List<Row<T>> tableData, Column... partitions) throws IOException, SQLException {
    final List<Row<T>> actualData = repo.unload(table, partitions);
    ListAssert.assertEquals(
      tableData,
      actualData
    );
  }

  protected <T extends TextTable> void listAssert(T table, String[] head, Object[][] rows, Column... partitions) throws IOException, SQLException {
    repo.execute(recover(table).generate());
    ListAssert.assertEquals(
      createTempTableRows(table, head, rows),
      repo.<T>unload(table, head, partitions)
    );
  }

  private static <T extends Table> List<Row<T>> createTempTableRows(T table, String[] head, Object[][] rowValues){
    List<Row<T>> rows = new ArrayList<Row<T>>();

    TextTable tempTable = new TextTable(table.getDatabaseName() + " " + table.getName());

    for (String columnNameToAssert : head) {
      Column column = table.getColumn(columnNameToAssert);
      tempTable.havingColumn(new Column(tempTable, column.getName(), column.getType()));
    }

    for (Object[] rowValue : rowValues) {
      Row<T> newRow = tempTable.getNewRow();
      for (int i = 0; i < head.length; i++) {
        newRow.set(tempTable.getColumn(head[i]), rowValue[i]);
      }
      rows.add(newRow);
    }

    return rows;
  }

  protected <T extends AvroTable> void listAssert(T table, List<Row<T>> tableData, Column... partitions) throws IOException, SQLException {
    ListAssert.assertEquals(
      tableData,
      repo.unload(table, partitions)
    );
  }

  protected abstract String scriptToTest();

  protected File scriptFileToTest() {
    return new File(scriptToTest());
  }
}
