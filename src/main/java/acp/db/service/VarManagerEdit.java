package acp.db.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

//import java.sql.Date;
import java.util.Date;

import acp.db.DbConnect;
import acp.db.domain.VarClass;
import acp.db.utils.*;
import acp.utils.*;

public class VarManagerEdit {
  private Connection dbConn;

  public VarManagerEdit() {
    dbConn = DbConnect.getConnection();
  }

  public VarClass select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select mssv_id, mssv_name, mssv_type, mssv_valuen, mssv_valuev, mssv_valued");
    sbQuery.append("  from mss_vars");
    sbQuery.append(" where mssv_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    VarClass varObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("mssv_name");
        String rsqType = rsq.getString("mssv_type");
        String strValn = rsq.getString("mssv_valuen");
        Double rsqValn = null;
        if (strValn != null) {
          rsqValn = Double.valueOf(strValn);
        }
        String rsqValv = rsq.getString("mssv_valuev");
//        Date rsqVald = rsq.getDate("mssv_valued");
        Date rsqVald = rsq.getTimestamp("mssv_valued");
        // ---------------------
        varObj = new VarClass();
        varObj.setId(objId);
        varObj.setName(rsqName);
        varObj.setType(rsqType);
        varObj.setValuen(rsqValn);
        varObj.setValuev(rsqValv);
        varObj.setValued(rsqVald);
        // ---------------------
      }
      rsq.close();
      ps.close();
      // System.out.println(varObj);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return varObj;
  }

  public int insert(VarClass newObj) {
    int res = -1;
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_vars");
    sbQuery.append(" (mssv_id, mssv_name, mssv_type, mssv_len,");
    sbQuery.append(" mssv_valuen, mssv_valuev, mssv_valued, mssv_last_modify, mssv_owner)");
    sbQuery.append(" values (mssv_seq.nextval, upper(?), ?, 120, ?, ?, ?, sysdate, user)");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    // System.out.println(newObj);
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setString(2, newObj.getType());

      Double valn = newObj.getValuen();
      if (valn != null) {
        ps.setDouble(3, valn);
      } else {
        ps.setNull(3, Types.DOUBLE);
      }
      
      ps.setString(4, newObj.getValuev());
      
      // ps.setDate(5, newObj.getValued());
      Timestamp tsValued = StrSqlUtils.util2ts(newObj.getValued());
      ps.setTimestamp(5,tsValued);
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return res;
  }

  public int update(VarClass newObj) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_vars");
    sbQuery.append("   set mssv_name=upper(?)");
    sbQuery.append("      ,mssv_type=?");
    sbQuery.append("      ,mssv_valuen=?");
    sbQuery.append("      ,mssv_valuev=?");
    sbQuery.append("      ,mssv_valued=?");
    sbQuery.append("      ,mssv_last_modify=sysdate");
    sbQuery.append("      ,mssv_owner=user");
    sbQuery.append(" where mssv_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------
    // System.out.println(newObj);
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setString(2, newObj.getType());
      Double valn = newObj.getValuen();
      if (valn != null) {
        ps.setDouble(3, valn);
      } else {
        ps.setNull(3, Types.DOUBLE);
      }
      ps.setString(4, newObj.getValuev());
      // ps.setDate(5, newObj.getValued());
      Timestamp tsValued = StrSqlUtils.util2ts(newObj.getValued());
      ps.setTimestamp(5,tsValued);
      ps.setLong(6, newObj.getId());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------
    return res;
  }

  public int delete(Long objId) {
    int res = -1;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("delete from mss_vars where mssv_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      int ret = ps.executeUpdate();
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

}
