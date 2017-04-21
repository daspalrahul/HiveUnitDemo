package com.xconf.maindb.table;

import com.xconf.database.Database;
import net.achalaggarwal.workerbee.Column;
import net.achalaggarwal.workerbee.TextTable;

public class InvoiceDataTable extends TextTable<InvoiceDataTable> {
  public static final InvoiceDataTable tb = new InvoiceDataTable();

  public static final Column invoiceId = HavingColumn(tb, "invoiceId", Column.Type.INT);
  public static final Column invoiceDate = HavingColumn(tb, "invoiceDate", Column.Type.STRING);
  public static final Column totalCost = HavingColumn(tb, "totalCost", Column.Type.DOUBLE);

  private InvoiceDataTable() {
    super(Database.db, "invoice_data", "InvoiceDataTable", 1);
  }
}
