/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This bean provides the database service
 *
 * @author SEPALMM
 */
//@Singleton
public class Neo4jServiceBean implements Neo4jService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Neo4jServiceBean.class);
    private static final String URI = "bolt://localhost:7687";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "Tokyo2000";

    private Driver DRIVER;
    public Neo4jServiceBean() {
    }

    @PreDestroy
    @Override
    public void destroyMe() {
        DRIVER.session().close();
        DRIVER.close();
        LOGGER.info("Neo4jDriver in Neo4jBean has been disposed of.");
    }

    /**
     *
     * @return the DB driver
     */
    @Override
    public Driver getDRIVER() {
        DRIVER = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
        LOGGER.info("Aquire Neo4jDriver.");
        return DRIVER;
    }

    /**
     * Close the DB driver
     *
     * @throws Exception
     */
    @Override
    public void close() {
        DRIVER.close();
        LOGGER.info("Closed Neo4jDriver.");
    }
}
