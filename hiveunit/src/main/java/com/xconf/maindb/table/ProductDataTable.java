package com.xconf.maindb.table;

import com.xconf.database.Database;
import net.achalaggarwal.workerbee.Column;
import net.achalaggarwal.workerbee.TextTable;

public class ProductDataTable extends TextTable<ProductDataTable> {
  public static final ProductDataTable tb = new ProductDataTable();

  public static final Column productId = HavingColumn(tb, "productId", Column.Type.INT);
  public static final Column productCost = HavingColumn(tb, "productCost", Column.Type.DOUBLE);
  public static final Column productType = HavingColumn(tb, "productType", Column.Type.STRING);

  private ProductDataTable() {
    super(Database.db, "product_data", "ProductDataTable", 1);
  }
}
