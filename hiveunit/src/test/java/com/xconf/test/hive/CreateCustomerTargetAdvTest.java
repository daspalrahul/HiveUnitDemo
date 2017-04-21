package com.xconf.test.hive;

import com.xconf.maindb.table.*;
import com.xconf.test.hive.utils.HiveTestRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static net.achalaggarwal.workerbee.Utils._row;
import static net.achalaggarwal.workerbee.Utils.head;
import static net.achalaggarwal.workerbee.Utils.table;

public class CreateCustomerTargetAdvTest extends HiveTestRunner {

  @Before
  public void setup() throws IOException, SQLException {
    repo
      .create(CustomerProducttypeTotalTable.tb)
      .create(CustomerTargetAdvTable.tb)
    ;
  }

  @Override
  protected String scriptToTest() {
    return BASE_DIR + "/xconf/create-customer-target-adv.hql";
  }

  @Test
  public void shouldCreateCustomerProductTypeTotal() throws Exception {

    repo
      .load(CustomerProducttypeTotalTable.tb,
        table(CustomerProducttypeTotalTable.tb,
          head("customerId", "invoiceId", "productType", "totalProductTypeCost", "percentTotal"),
          _row(1           , 89         , "FROZEN_FOOD", 500.0                 , 0.67),
          _row(1           , 89         , "COOKING_OIL", 250.0                 , 0.33),
          _row(1           , 95         , "FROZEN_FOOD", 200.0                 , 0.44),
          _row(1           , 95         , "COOKING_OIL", 250.0                 , 0.56),
          _row(1           , 87         , "COOKING_OIL", 250.0                 , 1.0),
          _row(2           , 67         , "FROZEN_FOOD", 300.0                 , 1.0)
        )
      );

    repo.execute(scriptFileToTest());

    listAssert(CustomerTargetAdvTable.tb,
      table(CustomerTargetAdvTable.tb,
        head("customerId", "productType"),
        _row(1           , "COOKING_OIL"),
        _row(2           , "FROZEN_FOOD")
      )
    );
  }
}


