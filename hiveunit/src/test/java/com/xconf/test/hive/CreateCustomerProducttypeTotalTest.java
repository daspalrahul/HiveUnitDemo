package com.xconf.test.hive;

import com.xconf.maindb.table.CustomerProducttypeTotalTable;
import com.xconf.maindb.table.CustomerSalesDataTable;
import com.xconf.maindb.table.InvoiceDataTable;
import com.xconf.maindb.table.ProductDataTable;
import com.xconf.test.hive.utils.HiveTestRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static net.achalaggarwal.workerbee.Utils._row;
import static net.achalaggarwal.workerbee.Utils.head;
import static net.achalaggarwal.workerbee.Utils.table;

public class CreateCustomerProducttypeTotalTest extends HiveTestRunner {

  @Before
  public void setup() throws IOException, SQLException {
    repo
      .create(ProductDataTable.tb)
      .create(InvoiceDataTable.tb)
      .create(CustomerSalesDataTable.tb)
      .create(CustomerProducttypeTotalTable.tb)
    ;
  }

  @Override
  protected String scriptToTest() {
    return BASE_DIR + "/xconf/create-customer-producttype-total.hql";
  }

  @Test
  public void shouldCreateCustomerProductTypeTotal() throws Exception {

    repo
      .load(CustomerSalesDataTable.tb,
        table(CustomerSalesDataTable.tb,
          head("customerId", "productId", "invoiceId"),
          _row(1           , 256        , 89),
          _row(1           , 356        , 89),
          _row(1           , 994        , 89),
          _row(1           , 256        , 95),
          _row(1           , 994        , 95),
          _row(1           , 994        , 87),
          _row(2           , 265        , 67),
          _row(2           , 256        , 67)
        )
      )
      .load(InvoiceDataTable.tb,
        table(InvoiceDataTable.tb,
          head("invoiceId", "invoiceDate", "totalCost"),
          _row(89         , "2017-04-02" , 750.0),
          _row(95         , "2017-04-06" , 450.0),
          _row(87         , "2017-04-08" , 250.0),
          _row(67         , "2017-04-01" , 300.0)
        )
      )
      .load(ProductDataTable.tb,
        table(ProductDataTable.tb,
          head("productId", "productCost", "productType"),
          _row(256        , 200.0        , "FROZEN_FOOD"),
          _row(356        , 300.0        , "FROZEN_FOOD"),
          _row(994        , 250.0        , "COOKING_OIL"),
          _row(265        , 100.0        , "FROZEN_FOOD")
        )
      )
    ;

    repo.execute(scriptFileToTest());

    listAssert(CustomerProducttypeTotalTable.tb,
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
  }
}


