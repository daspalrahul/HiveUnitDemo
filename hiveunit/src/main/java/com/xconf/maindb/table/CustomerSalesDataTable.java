package com.xconf.maindb.table;

import com.xconf.database.Database;
import net.achalaggarwal.workerbee.Column;
import net.achalaggarwal.workerbee.TextTable;

public class CustomerSalesDataTable extends TextTable<CustomerSalesDataTable> {
  public static final CustomerSalesDataTable tb = new CustomerSalesDataTable();

  public static final Column customerId = HavingColumn(tb, "customerId", Column.Type.INT);
  public static final Column productId = HavingColumn(tb, "productId", Column.Type.INT);
  public static final Column invoiceId = HavingColumn(tb, "invoiceId", Column.Type.INT);

  private CustomerSalesDataTable() {
    super(Database.db, "customer_sales_data", "CustomerSalesDataTable", 1);
  }
}
