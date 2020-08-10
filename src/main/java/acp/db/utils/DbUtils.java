package acp.db.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import acp.db.DbConnect;
import acp.utils.*;

public class DbUtils {

  public static boolean emptyString(String str) {
    return StrUtils.emptyString(str);
  }

  public static String buildQuery(String selFrom, String where, String order) {
    StringBuilder query = new StringBuilder(selFrom);
    if (!emptyString(where)) {
      query.append(" where " + where);
    }
    if (!emptyString(order)) {
      query.append(" order by " + order);
    }
    // System.out.println(query);
    return query.toString();
  }

  public static String testQuery(String selFrom, String selWhere, String selOrder) {
    String query;
    String where = "1=2";
    if (!emptyString(selWhere)) {
      where += " and " + selWhere;
    }
    // ------------------------------------
    query = buildQuery(selFrom, where, selOrder);
    boolean res = executeQuery(query);
    if (res) {
      query = buildQuery(selFrom, selWhere, selOrder);
    } else {
      query = null;
    }
    // ------------------------------------
    return query;
  }

  public static String buildQuery(String selFields, String tblFrom,
      String where, String order) {
    StringBuilder query = new StringBuilder(selFields);
    query.append(" from " + tblFrom);
    if (!emptyString(where)) {
      query.append(" where " + where);
    }
    if (!emptyString(order)) {
      query.append(" order by " + order);
    }
    // System.out.println(query);
    return query.toString();
  }

  public static String testQuery(String selFields, String tblFrom, String selWhere, String selOrder) {
    String query;
    String where = "1=2";
    if (!emptyString(selWhere)) {
      where += " and " + selWhere;
    }
    // ------------------------------------
    query = buildQuery(selFields, tblFrom, where, selOrder);
    boolean res = executeQuery(query);
    if (res) {
      query = buildQuery(selFields, tblFrom, selWhere, selOrder);
    } else {
      query = null;
    }
    // ------------------------------------
    return query;
  }

  public static boolean executeQuery(String query) {
    boolean res = false;
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      stmt.executeQuery(query);
      stmt.close();
      res = true;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return res;
  }

  public static int executeUpdate(String query) {
    int res = -1;
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      res = stmt.executeUpdate(query);
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return res;
  }

  public static String getValueV(String query) {
    String res = null;
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      res = rs.getString(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return res;
  }

  public static int getValueN(String query) {
    int res = -1;
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      res = rs.getInt(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return res;
  }

  public static long getValueL(String query) {
    long res = -1L;
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      res = rs.getLong(1);
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return res;
  }

  public static List<String[]> getListString(String query) {
    ArrayList<String[]> cache = new ArrayList<>();
    int cntCols = 2; 
    try {
      Connection dbConn = DbConnect.getConnection();
      Statement stmt = dbConn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        //---------------------------------------
        String[] record = new String[cntCols];
        for (int i = 0; i < cntCols; i++) {
          record[i] = rs.getString(i+1);
        }
        cache.add(record);
        //---------------------------------------
      }
      rs.close();
      stmt.close();
    } catch (SQLException e) {
      cache = new ArrayList<>();
      DialogUtils.errorPrint(e);
    }
    return cache;
  }

  public static String clob2String(Clob clob) throws SQLException {
    if (clob == null) {
      return null;
    }
    String txtClob = clob.getSubString(1L, (int) clob.length());
    return txtClob;
  }

  public static Clob string2Clob(String str) throws SQLException {
    if (str == null) {
      return null;
    }
    Connection dbConn = DbConnect.getConnection();
    Clob clob = dbConn.createClob();
    clob.setString(1L, str);
    return clob;
  }

}
