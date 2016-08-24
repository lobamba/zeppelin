/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.zeppelin.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
/**
 * 
 * @author Lomou
 *
 */
public class DemonThread extends Thread {
  
  private Connection connection;
  private String propertykey;
  private SqlCompleter sqlCompleter;
  
  private Logger logger = LoggerFactory.getLogger(JDBCInterpreter.class);
  
  public DemonThread(Connection connection, String propertykey, SqlCompleter sqlCompleter){
    this.connection = connection;
    this.propertykey = propertykey;
    this.sqlCompleter = sqlCompleter;
  }
  
  
  public void run(){
    if (Thread.currentThread().isDaemon()){
      //System.out.println("DemonThread.run() " + "demon existing");
      try {
        sqlCompleter = createSqlCompleter(connection);
      } catch (Exception e){
        sqlCompleter = createSqlCompleter(null);
      }
    }
  }
  
  private SqlCompleter createSqlCompleter(Connection jdbcConnection) {

    SqlCompleter completer = null;
    try {
      Set<String> keywordsCompletions = SqlCompleter.getSqlKeywordsCompletions(jdbcConnection);
      Set<String> dataModelCompletions =
          SqlCompleter.getDataModelMetadataCompletions(jdbcConnection);
      SetView<String> allCompletions = Sets.union(keywordsCompletions, dataModelCompletions);
      completer = new SqlCompleter(allCompletions, dataModelCompletions);

    } catch (IOException | SQLException e) {
      logger.error("Cannot create SQL completer", e);
    }
    return completer;
  }

}
