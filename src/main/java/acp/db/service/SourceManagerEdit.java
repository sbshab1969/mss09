package acp.db.service;

import java.sql.*;

import acp.db.DbConnect;
import acp.db.domain.SourceClass;
import acp.utils.DialogUtils;

public class SourceManagerEdit {
  private Connection dbConn;

  public SourceManagerEdit() {
    dbConn = DbConnect.getConnection();
  }

  public SourceClass select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select msss_id, msss_name");
    sbQuery.append("  from mss_source");
    sbQuery.append(" where msss_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    SourceClass sourceObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("msss_name");
        // ---------------------
        sourceObj = new SourceClass();
        sourceObj.setId(objId);
        sourceObj.setName(rsqName);
        // ---------------------
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return sourceObj;
  }

  public int insert(SourceClass newObj) {
    int res = -1;
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_source");
    sbQuery.append(" (msss_id, msss_name, msss_dt_create, msss_dt_modify, msss_owner)");
    sbQuery.append(" values (msss_seq.nextval, ?, sysdate, sysdate, user)");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int update(SourceClass newObj) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_source");
    sbQuery.append("   set msss_name=?");
    sbQuery.append("      ,msss_dt_modify=sysdate");
    sbQuery.append("      ,msss_owner=user");
    sbQuery.append(" where msss_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setLong(2, newObj.getId());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int delete(Long objId) {
    int res = -1;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("delete from mss_source where msss_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }
}
