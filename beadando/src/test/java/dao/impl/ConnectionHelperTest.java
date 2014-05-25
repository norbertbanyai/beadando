package dao.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import dao.PersistentLayerException;

public class ConnectionHelperTest {

	@Test
	public void testGetConnection() {
		try {
			Connection conn = ConnectionHelper.getConnection();
			assertNotNull(conn);
		} catch (IOException | SQLException e) {
			fail(":(");
		}
	}

}
