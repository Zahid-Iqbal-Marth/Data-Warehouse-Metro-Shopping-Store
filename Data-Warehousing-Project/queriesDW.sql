-- Q1-
SELECT t.product_id, t.supplier_id, d.t_quarter, d.t_month, sum(t.total_sale)
FROM  metro_dw.sale as t, metro_dw.tdate as d
WHERE t.Tdate_id = d.Tdate_id
GROUP BY  t.supplier_id, t.product_id, d.t_quarter, d.t_month
ORDER BY  t.supplier_id, t.product_id, d.t_quarter, d.t_month;



-- Q2-
SELECT  t.product_id, sum(t.total_sale), d.t_month, t.supplier_id, d.t_year
FROM metro_dw.sale as t, metro_dw.tdate as d
WHERE t.supplier_id = (select s.supplier_id from metro_dw.supplier as s where s.supplier_name = "3M Company")  AND d.t_year = 2016 AND t.Tdate_id = d.Tdate_id
GROUP BY t.product_id ,d.t_month
ORDER BY t.product_id, d.t_month;


-- Q3-
SELECT p.product_name, sum(t.QUATITY)
FROM  metro_dw.sale as t, metro_dw.product as p ,  metro_dw.tdate as d
WHERE  (d.T_DAYOFWEEK = 7 or d.T_DAYOFWEEK = 1) AND t.Tdate_id = d.Tdate_id AND t.product_id = p.product_id
GROUP BY p.product_name
ORDER BY sum(t.QUATITY) desc
LIMIT 5;

-- Q4-
SELECT Q1.p as Product_ID, Q1.q1, Q2.q2, Q3.q3, Q4.q4, Yearly.y as Yearly
FROM (
	SELECT t.product_id as p, sum(t.total_sale) as q1
    FROM metro_dw.sale as t, metro_dw.tdate as d
    WHERE d.t_quarter = 1 AND t.tdate_id = d.tdate_id
    GROUP BY t.product_id
    order by t.product_id
)  Q1, (
	SELECT t.product_id, sum(t.total_sale) as q2
    FROM metro_dw.sale as t, metro_dw.tdate as d
    WHERE d.t_quarter = 2 AND t.tdate_id = d.tdate_id
    GROUP BY t.product_id
    order by t.product_id
)  Q2, (
	SELECT t.product_id, sum(t.total_sale) as q3
    FROM metro_dw.sale as t, metro_dw.tdate as d
    WHERE d.t_quarter = 3 AND t.tdate_id = d.tdate_id
    GROUP BY t.product_id
    order by t.product_id
)  Q3, (
	SELECT t.product_id, sum(t.total_sale) as q4
    FROM metro_dw.sale as t, metro_dw.tdate as d
    WHERE d.t_quarter = 4 AND t.tdate_id = d.tdate_id
    GROUP BY t.product_id
    order by t.product_id
)  Q4, (
	SELECT t.product_id, sum(t.total_sale) as y
    FROM metro_dw.sale as t, metro_dw.tdate as d
    WHERE d.t_year AND t.tdate_id = d.tdate_id
    GROUP BY t.product_id
    order by t.product_id
)  Yearly
WHERE Q1.p = Q2.product_id AND Q2.product_id = Q3.product_id AND Q3.product_id = Q4.product_id AND Q4.product_id = Yearly.product_id
;




-- Q6-
DROP VIEW IF EXISTS metro_dw.STOREANALYSIS_MV;

CREATE VIEW metro_dw.STOREANALYSIS_MV
AS SELECT s.store_name, p.product_name ,sum(t.total_sale)
FROM  metro_dw.sale as t, metro_dw.product as p ,  metro_dw.store as s
WHERE t.store_id = s.store_id AND t.product_id = p.product_id
GROUP BY p.product_name, s.store_name;

select * from metro_dw.STOREANALYSIS_MV;
