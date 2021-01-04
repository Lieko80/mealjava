package org.isa.dal;

import junit.framework.TestCase;
import org.junit.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MealwithDBTest extends TestCase {

    public void testGetConn() throws SQLException {

        MealwithDB test = new MealwithDB();

        Assert.assertNotNull(test.getConn());

        test.conn.close();

    }
}