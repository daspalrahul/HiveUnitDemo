package com.xconf.test.hive.utils;

import com.github.sakserv.minicluster.config.ConfigVars;
import com.github.sakserv.minicluster.impl.HiveLocalMetaStore;
import com.github.sakserv.minicluster.impl.HiveLocalServer2;
import com.github.sakserv.minicluster.impl.ZookeeperLocalCluster;
import com.github.sakserv.propertyparser.PropertyParser;
import org.apache.hadoop.hive.conf.HiveConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HiveServerRunner {
  private static final Logger LOG = LoggerFactory.getLogger(HiveServerRunner.class);

  // Setup the property parser
  private static PropertyParser propertyParser;
  static {
    try {
      propertyParser = new PropertyParser(ConfigVars.DEFAULT_PROPS_FILE);
      propertyParser.parsePropsFile();
    } catch(IOException e) {
      LOG.error("Unable to load property file: {}", propertyParser.getProperty(ConfigVars.DEFAULT_PROPS_FILE));
    }
  }

  private ZookeeperLocalCluster zookeeperLocalCluster;
  private HiveLocalMetaStore hiveLocalMetaStore;
  private HiveLocalServer2 hiveLocalServer2;

  public HiveServerRunner() {
    zookeeperLocalCluster = new ZookeeperLocalCluster.Builder()
      .setPort(Integer.parseInt(propertyParser.getProperty(ConfigVars.ZOOKEEPER_PORT_KEY)))
      .setTempDir(propertyParser.getProperty(ConfigVars.ZOOKEEPER_TEMP_DIR_KEY))
      .setZookeeperConnectionString(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY))
      .build();

    hiveLocalMetaStore = new HiveLocalMetaStore.Builder()
      .setHiveMetastoreHostname(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_HOSTNAME_KEY))
      .setHiveMetastorePort(Integer.parseInt(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_PORT_KEY)) + 50)
      .setHiveMetastoreDerbyDbDir(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_DERBY_DB_DIR_KEY))
      .setHiveScratchDir(propertyParser.getProperty(ConfigVars.HIVE_SCRATCH_DIR_KEY))
      .setHiveWarehouseDir(propertyParser.getProperty(ConfigVars.HIVE_WAREHOUSE_DIR_KEY))
      .setHiveConf(buildHiveConf())
      .build();

    hiveLocalServer2 = new HiveLocalServer2.Builder()
      .setHiveServer2Hostname(propertyParser.getProperty(ConfigVars.HIVE_SERVER2_HOSTNAME_KEY))
      .setHiveServer2Port(Integer.parseInt(propertyParser.getProperty(ConfigVars.HIVE_SERVER2_PORT_KEY)))
      .setHiveMetastoreHostname(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_HOSTNAME_KEY))
      .setHiveMetastorePort(Integer.parseInt(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_PORT_KEY)) + 50)
      .setHiveMetastoreDerbyDbDir(propertyParser.getProperty(ConfigVars.HIVE_METASTORE_DERBY_DB_DIR_KEY))
      .setHiveScratchDir(propertyParser.getProperty(ConfigVars.HIVE_SCRATCH_DIR_KEY))
      .setHiveWarehouseDir(propertyParser.getProperty(ConfigVars.HIVE_WAREHOUSE_DIR_KEY))
      .setHiveConf(buildHiveConf())
      .setZookeeperConnectionString(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY))
      .build();
  }

  public HiveServerRunner startAll() throws Exception {
    zookeeperLocalCluster.start();
    hiveLocalMetaStore.start();
    hiveLocalServer2.start();

    return this;
  }

  public HiveServerRunner stopAll() throws Exception {
    zookeeperLocalCluster.stop(false);
    hiveLocalMetaStore.stop(false);
    hiveLocalServer2.stop(false);

    return this;
  }

  public HiveServerRunner cleanAll() throws Exception {
    zookeeperLocalCluster.cleanUp();
    hiveLocalMetaStore.cleanUp();
    hiveLocalServer2.cleanUp();

    return this;
  }

  public static void main(String[] args) throws Exception {
    new HiveServerRunner()
      .cleanAll()
      .startAll();
  }

  public static HiveConf buildHiveConf() {
    HiveConf hiveConf = new HiveConf();
    hiveConf.set(HiveConf.ConfVars.HIVE_TXN_MANAGER.varname, "org.apache.hadoop.hive.ql.lockmgr.DbTxnManager");
    hiveConf.set(HiveConf.ConfVars.HIVE_COMPACTOR_INITIATOR_ON.varname, "true");
    hiveConf.set(HiveConf.ConfVars.HIVE_COMPACTOR_WORKER_THREADS.varname, "5");
    hiveConf.set("hive.root.logger", "DEBUG,console");
    hiveConf.set(HiveConf.ConfVars.HADOOPBIN.varname, System.getenv("HADOOP_HOME") + "/bin/hadoop");
    hiveConf.set(HiveConf.ConfVars.SUBMITLOCALTASKVIACHILD.varname, "false");
    hiveConf.setIntVar(HiveConf.ConfVars.METASTORETHRIFTCONNECTIONRETRIES, 3);
    hiveConf.set(HiveConf.ConfVars.PREEXECHOOKS.varname, "");
    hiveConf.set(HiveConf.ConfVars.POSTEXECHOOKS.varname, "");
    System.setProperty(HiveConf.ConfVars.PREEXECHOOKS.varname, " ");
    System.setProperty(HiveConf.ConfVars.POSTEXECHOOKS.varname, " ");
    return hiveConf;
  }
}
