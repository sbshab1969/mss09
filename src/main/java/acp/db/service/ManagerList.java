package acp.db.service;

import java.sql.*;
import java.util.*;

import acp.db.DbConnect;
import acp.db.utils.DbUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ManagerList {
  protected Connection dbConn;
  protected Statement stmt;
  protected ResultSet rs;

  protected String[] fields;
  protected String[] headers;
  protected Class<?>[] types;
  protected int cntColumns = 0;

  protected String tableName;
  protected String pkColumn;
  protected String strAwhere;
  protected Long seqId = 0L;

  protected String strFields;
  protected String strFrom;
  protected String strWhere;
  protected String strOrder;
  
  protected String strQuery;
  protected String strQueryCnt;

  private static Logger logger = LoggerFactory.getLogger(ManagerList.class);

  public ManagerList() {
    dbConn = DbConnect.getConnection();
  }

  protected void setWhere(Map<String,String> mapFilter) {
  }

  public void prepareQuery() {
    if (strFields != null && strFrom != null) {
      strQuery = DbUtils.buildQuery(strFields, strFrom, strWhere, strOrder);
      strQueryCnt = DbUtils.buildQuery("select count(*) cnt", strFrom, strWhere, null);
    }  
  }

  public String[] getHeaders() {
    return headers;    
  }

  public Class<?>[] getTypes() {
    return types;    
  }

  public Long getSeqId() {
    return seqId;
  }

  public int countRecords() {
    int cntRecords = DbUtils.getValueN(strQueryCnt);
    return cntRecords;    
  }

  public void openQuery() {
    openCursor(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
  }  

  public void closeQuery() {
    closeCursor();
  }

  protected void openCursor() {
    // System.out.println("OpenCursor: " + dbConn);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(strQuery);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }

  protected void openCursor(int typeCursor, int typeConcur) {
    /*
    ResultSet.TypeCursor:
      TYPE_FORWARD_ONLY
      TYPE_SCROLL_INSENSITIVE
      TYPE_SCROLL_SENSITIVE
      TYPE_FORWARD_ONLY
   ResultSet.TypeConcur:
      CONCUR_READ_ONLY;
      CONCUR_UPDATABLE;
      CONCUR_READ_ONLY;
 */
    // System.out.println("OpenCursor2: " + dbConn);
    try {
      stmt = dbConn.createStatement(typeCursor, typeConcur);
      rs = stmt.executeQuery(strQuery);
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }

  protected void closeCursor() {
    // System.out.println("CloseCursor: " + dbConn);
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    stmt = null;
    rs = null;
  }

}
