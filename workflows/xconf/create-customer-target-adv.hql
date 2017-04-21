set mapred.job.name="create-customer-target-adv.hql";
set mapred.job.queue.name=${queue};

use ${user};
WITH COSTS AS (
SELECT
  CPT.customerId AS customerId,
  CPT.productType AS productType,
  SUM(CPT.totalProductTypeCost) as totalProductTypeCost
FROM
  customer_producttype_total CPT
GROUP BY
  CPT.customerId,
  CPT.productType,
  CPT.totalProductTypeCost
),
RANKED_COSTS AS (
SELECT
  RANK() OVER (PARTITION BY customerId ORDER BY totalProductTypeCost DESC) AS ranks,
  customerId,
  productType
FROM
  COSTS
)
INSERT OVERWRITE TABLE customer_target_adv
SELECT
  customerId,
  productType
FROM
  RANKED_COSTS
WHERE
  ranks = 1
;
