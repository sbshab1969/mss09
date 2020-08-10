package acp.db.service;

import java.sql.*;

import acp.db.DbConnect;
import acp.db.domain.ConstClass;
import acp.utils.DialogUtils;

public class ConstManagerEdit {
  private Connection dbConn;

  public ConstManagerEdit() {
    dbConn = DbConnect.getConnection();
  }

  public ConstClass select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select mssc_id, mssc_name, mssc_value");
    sbQuery.append("  from mss_const");
    sbQuery.append(" where mssc_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    ConstClass constObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("mssc_name");
        String rsqValue = rsq.getString("mssc_value");
        // ---------------------
        constObj = new ConstClass();
        constObj.setId(objId);
        constObj.setName(rsqName);
        constObj.setValue(rsqValue);
        // ---------------------
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      constObj = null;
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return constObj;
  }

  public int insert(ConstClass newObj) {
    int res = -1;
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_const");
    sbQuery.append(" (mssc_id, mssc_name, mssc_value)");
    sbQuery.append(" values (mssc_seq.nextval, upper(?), ?)");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setString(2, newObj.getValue());
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

  public int update(ConstClass newObj) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_const");
    sbQuery.append("   set mssc_name=upper(?)");
    sbQuery.append("      ,mssc_value=?");
    sbQuery.append(" where mssc_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setString(2, newObj.getValue());
      ps.setLong(3, newObj.getId());
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
    sbQuery.append("delete from mss_const where mssc_id=?");
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
