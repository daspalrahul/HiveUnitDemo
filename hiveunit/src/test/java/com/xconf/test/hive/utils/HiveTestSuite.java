package com.xconf.test.hive.utils;

import com.xconf.test.hive.*;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  CreateCustomerProducttypeTotalTest.class,
  CreateCustomerTargetAdvTest.class
})
public class HiveTestSuite {
  private static HiveServerRunner serverRunner = new HiveServerRunner();

  @ClassRule
  public static ExternalResource resource = new ExternalResource() {
    @Override
    protected void before() throws Throwable {
      super.before();
      serverRunner.cleanAll();
      serverRunner.startAll();
    }

    @Override
    protected void after() throws Throwable {
      try {
        serverRunner.stopAll();
        serverRunner.cleanAll();
      } catch (Exception e) {
        e.printStackTrace();
      }
      super.after();
    }
  };
}
