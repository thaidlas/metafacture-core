/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.morph.maps;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.culturegraph.mf.exceptions.MorphException;


/**
 * @author Daniel, "Markus Michael Geipel"
 *
 */
public final class SqlMap extends AbstractReadOnlyMap<String, String> implements Closeable {

	private boolean isUninitialized = true;
	
	private Connection conn;
	private String host;
	private String login;
	private String password;
	private String database;
	private String query;
	private String driver;


	private PreparedStatement preparedStatement;

	
	public void init() {

		try {
			preparedStatement = getMySqlConnection().prepareStatement(query);
		} catch (SQLException e) {
			throw new MorphException(e);
		}
		isUninitialized = false;
	}
	


	@Override
	public void close() throws IOException {
		try {

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new MorphException(e);
		}
	}

	private Connection getMySqlConnection(){

		try {
			Class.forName(driver );

			conn = DriverManager.getConnection("jdbc:mysql://"+host+"/"+database + "?" + "user=" + login + "&"
					+ "password=" + password);
		} catch (ClassNotFoundException e) {
			throw new MorphException(e);
		} catch (SQLException e) {
			throw new MorphException(e);
		}
		return conn;
	}

	@Override
	public String get(final Object key) {
		if(isUninitialized){
			init();
		}
		String resultString = null;
		final ResultSet resultSet;
		try {
			preparedStatement.setString(1, key.toString());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.first()){
				resultString =  resultSet.getString(1);
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new MorphException(e);
		}
		return resultString;
	}

	public void setDriver(final String driver) {
		this.driver = driver;
	}
	
	public void setHost(final String host) {
		this.host = host;
	}


	public void setLogin(final String login) {
		this.login = login;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setDatabase(final String database) {
		this.database = database;
	}

	public void setQuery(final String query) {
		this.query = query;
	}
}
