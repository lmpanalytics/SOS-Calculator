/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tetrapak.processing.sos.calculator;

import javax.ejb.Local;
import org.neo4j.driver.v1.Driver;

/**
 * Local business interface for the Neo4jService Bean.
 *
 * @author SEPALMM
 */
@Local
public interface Neo4jService extends AutoCloseable {

    public Driver getDRIVER();

    @Override
    public void close() throws Exception;

    public void destroyMe();

}
