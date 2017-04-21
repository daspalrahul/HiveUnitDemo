set mapred.job.name="create-customer-producttype-total.hql";
set mapred.job.queue.name=${queue};

use ${user};

INSERT OVERWRITE TABLE customer_producttype_total
SELECT
  CSD.customerId AS customerId,
  ID.invoiceId AS invoiceId,
  PD.productType AS productType,
  SUM(PD.productCost) AS totalProductTypeCost,
  ROUND(SUM(PD.productCost) / ID.totalCost, 2) AS percentTotal
FROM
  customer_sales_data CSD
INNER JOIN
  invoice_data ID
  ON CSD.invoiceId = ID.invoiceId
INNER JOIN
  product_data PD
  ON CSD.productId = PD.productId
GROUP BY
  CSD.customerId,
  ID.invoiceId,
  ID.totalCost,
  PD.productType
;
