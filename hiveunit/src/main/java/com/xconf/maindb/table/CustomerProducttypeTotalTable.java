package com.xconf.maindb.table;

import com.xconf.database.Database;
import net.achalaggarwal.workerbee.Column;
import net.achalaggarwal.workerbee.TextTable;

public class CustomerProducttypeTotalTable extends TextTable<CustomerProducttypeTotalTable> {
  public static final CustomerProducttypeTotalTable tb = new CustomerProducttypeTotalTable();

  public static final Column customerId = HavingColumn(tb, "customerId", Column.Type.INT);
  public static final Column invoiceId = HavingColumn(tb, "invoiceId", Column.Type.INT);
  public static final Column productType = HavingColumn(tb, "productType", Column.Type.STRING);
  public static final Column totalProductTypeCost = HavingColumn(tb, "totalProductTypeCost", Column.Type.DOUBLE);
  public static final Column percentTotal = HavingColumn(tb, "percentTotal", Column.Type.DOUBLE);

  private CustomerProducttypeTotalTable() {
    super(Database.db, "customer_producttype_total", "CustomerProducttypeTotalTable", 1);
  }
}
