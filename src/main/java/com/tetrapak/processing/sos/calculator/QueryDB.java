/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Queries the database
 *
 * @author SEPALMM
 */
//@RequestScoped
public class QueryDB {

//    @EJB
//    private Neo4jService neo4jServiceBean;
    Neo4jService neo4jServiceBean = new Neo4jServiceBean();
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDB.class);
    private final Map<Integer, SOSdata> sparePartResultsMap = new HashMap<>();

    public void calculateSparePartPotential(String cluster) {
        sparePartResultsMap.clear();
        List<SOSdata> list = getData(cluster);
        list.forEach((sos) -> {
            int compositeKey = (sos.getSosCategory() + sos.getAssortment() + sos.getFinalCustomerNumber()).hashCode();
            sparePartResultsMap.put(compositeKey, sos);
        });
        try {
            neo4jServiceBean.close();
        } catch (Exception e) {
            LOGGER.error("Error when closing Neo4j Driver: {}", e.getMessage());
        }
    }

    private List<SOSdata> getData(String cluster) {
        try (Session session = neo4jServiceBean.getDRIVER().session();) {
            return session.readTransaction(new TransactionWork<List<SOSdata>>() {
                @Override
                public List<SOSdata> execute(Transaction tx) {
                    return matchSparePartData(tx, cluster);
                }
            });
        }
    }

    private List<SOSdata> matchSparePartData(Transaction tx, String cluster) {

        List<SOSdata> sosDataList = new ArrayList<>();
        boolean exceptionFlag = true;
        int rowCounter = 0;

        LocalDate date = LocalDate.now();

        int currentYear = date.getYear();
        int yearH12 = date.minusYears(1).getYear();
        int lastMonth = date.minusMonths(1).getMonthValue();

        try {

            StatementResult result = tx.run(
                    "MATCH (a:Assortment)-[r:POTENTIAL_AT]->(c:Customer)-[:LOCATED_IN]->(m:CountryDB)-[:MEMBER_OF]->(mgr:MarketGroup)-[:MEMBER_OF]->(cl:ClusterDB {name: $cluster}) "
                    + "WHERE m.mktName = m.countryName "
                    + "RETURN 'SP_POT' AS CATEGORY, cl.name AS Cluster, mgr.name AS MarketGrp, m.countryName AS Market, a.name AS Assortment, c.id AS FinalCustNo, c.name AS CustName, c.custGroup AS CustGrp, SUM(r.spEurPotential) AS RESULT "
                    + "UNION "
                    + "MATCH (c:Customer)<-[r1:FOR]-(t:Transaction)-[:BOOKED_AS]->(:ServiceCategory {name: 'Parts'}), (cl:ClusterDB {name: $cluster})<-[:MEMBER_OF]-(mgr:MarketGroup)<-[:MEMBER_OF]-(m:MarketDB)-[:MADE]->(t) "
                    + "OPTIONAL MATCH (t)<-[:IN]-(a:Assortment) "
                    + "WHERE ((t.year = $yearH12 AND t.month >= $lastMonth) OR t.year = $currentYear) AND m.mktName = m.countryName "
                    + "RETURN 'NET_SALES' AS CATEGORY, cl.name AS Cluster, mgr.name AS MarketGrp, m.mktName AS Market, a.name AS Assortment, c.id AS FinalCustNo, c.name AS CustName, c.custGroup AS CustGrp, SUM(r1.netSales) AS RESULT;",
                    Values.parameters(
                            "cluster", cluster,
                            "currentYear", currentYear,
                            "yearH12", yearH12,
                            "lastMonth", lastMonth
                    ));
            while (result.hasNext()) {
                Record next = result.next();

                String sosCategory = next.get("CATEGORY").asString();
                String qCluster = next.get("Cluster").asString();
                String marketGroup = next.get("MarketGrp").asString();
                String market = next.get("Market").asString();
                String assortment = next.get("Assortment").asString();
                String finalCustomerNumber = next.get("FinalCustNo").asString();
                String customerName = next.get("CustName").asString();
                String customerGroup = next.get("CustGrp").asString();
                double sosResult = next.get("RESULT").asDouble();

                sosDataList.add(new SOSdata(sosCategory, qCluster, marketGroup, market, assortment, finalCustomerNumber, customerName, customerGroup, sosResult));
                rowCounter++;
            }
            exceptionFlag = false;

        } catch (Exception e) {
            exceptionFlag = true;
            LOGGER.error("Error when matchSparePartData: {}", e.getMessage());
        }
        if (!exceptionFlag) {
            LOGGER.info("Generated {} result rows of Spare Part data.", rowCounter);

        }
        return sosDataList;
    }

    public Map<Integer, SOSdata> getSparePartResultsMap() {
        return sparePartResultsMap;
    }

}
