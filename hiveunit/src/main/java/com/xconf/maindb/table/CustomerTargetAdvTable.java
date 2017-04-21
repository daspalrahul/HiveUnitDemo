package com.xconf.maindb.table;

import com.xconf.database.Database;
import net.achalaggarwal.workerbee.Column;
import net.achalaggarwal.workerbee.TextTable;

public class CustomerTargetAdvTable extends TextTable<CustomerTargetAdvTable> {
  public static final CustomerTargetAdvTable tb = new CustomerTargetAdvTable();

  public static final Column customerId = HavingColumn(tb, "customerId", Column.Type.INT);
  public static final Column productType = HavingColumn(tb, "productType", Column.Type.STRING);

  private CustomerTargetAdvTable() {
    super(Database.db, "customer_target_adv", "CustomerTargetAdvTable", 1);
  }
}
