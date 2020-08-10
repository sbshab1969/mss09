package acp.db.service;

import java.sql.*;
import java.util.Map;

import acp.db.DbConnect;
import acp.utils.DialogUtils;

public class VarManagerAbout {
  private Connection dbConn;

  public VarManagerAbout() {
    dbConn = DbConnect.getConnection();
  }

  public void fillCert(Map<String, String> varMap) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select upper(mssv_name) mssv_name, mssv_valuev");
    sbQuery.append("  from mss_vars");
    sbQuery.append(" where upper(mssv_name) like 'CERT%'");
    sbQuery.append(" order by mssv_id");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    try {
      Statement st = dbConn.createStatement();
      ResultSet rsq = st.executeQuery(query);
      while (rsq.next()) {
        String rsqName = rsq.getString("mssv_name");
        String rsqValue = rsq.getString("mssv_valuev");
        varMap.put(rsqName, rsqValue);
      }
      rsq.close();
      st.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // System.out.println(certMap.toString());
  }

  public void fillVersion(Map<String, String> varMap) {
    CallableStatement cs = null;
    String sql = null;
    String rsqValue = "";
    // ---------------------------
    sql = "{? = call getvarv(?)}";
    try {
      cs = dbConn.prepareCall(sql);
      cs.registerOutParameter(1, java.sql.Types.VARCHAR);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    rsqValue = getVarV(cs, "version_mss");
    varMap.put("VERSION", rsqValue);
    // ---------------------------
    sql = "{? = call getvard(?,?)}";
    try {
      cs = dbConn.prepareCall(sql);
      cs.registerOutParameter(1, java.sql.Types.VARCHAR);
      cs.setString(3, "dd.mm.yyyy");
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    rsqValue = getVarV(cs, "version_mss");
    varMap.put("VERSION_DATE", rsqValue);
    // ---------------------------
  }

  public String getVarV(CallableStatement cst, String varname) {
    String res = null;
    try {
      cst.setString(2, varname);
      cst.execute();
      res = cst.getString(1);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    if (res == null) {
      res = varname;
    }
    return res;
  }
  
}
