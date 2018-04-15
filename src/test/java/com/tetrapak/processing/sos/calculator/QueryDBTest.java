/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import java.io.File;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test the Query class
 *
 * @author SEPALMM
 */
@RunWith(Arquillian.class)
public class QueryDBTest {

    @Deployment
    public static WebArchive createDeployment() {

        File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
                .resolve().withTransitivity().asFile();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(QueryDB.class, SOSdata.class,
                        Neo4jService.class, Neo4jServiceBean.class
                ).addAsLibraries(files)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(archive.toString(true));
        return archive;
    }

//    @EJB
//    QueryDB queryDB;
    
//    @Inject
//    QueryDB queryDB;
    
    @Test
    @InSequence(1)
    public void testQuery() {
        System.out.println("test query");
        QueryDB queryDB = new QueryDB();
        queryDB.calculateSparePartPotential("E&CA");
        int compositeKey = ("NET_SALES" + "Separator parts" + "0000010213").hashCode();
        Assert.assertTrue(queryDB.getSparePartResultsMap().containsKey(compositeKey));
    }
}
