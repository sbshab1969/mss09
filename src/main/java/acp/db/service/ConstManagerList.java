package acp.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.ConstClass;
import acp.db.utils.*;
import acp.utils.*;

public class ConstManagerList extends ManagerList {

  protected List<ConstClass> cacheObj = new ArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(ConstManagerList.class);

  public ConstManagerList() {
    fields = new String[] { "mssc_id", "mssc_name", "mssc_value" };

    headers = new String[] {"ID"
      , Messages.getString("Column.Name")
      , Messages.getString("Column.Value") 
    };

    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
    };

    cntColumns = headers.length;

    tableName = "mss_const";
    pkColumn = "mssc_id";
    strAwhere = null;
    seqId = 1000L;

    strFields = StrSqlUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strWhere = strAwhere;
    strOrder = pkColumn;
    // ------------
    prepareQuery();
    // ------------
  }

  public void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vName = mapFilter.get("name"); 
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!DbUtils.emptyString(vName)) {
      str = "upper(mssc_name) like upper('" + vName + "%')";
      phWhere = StrSqlUtils.strAddAnd(phWhere, str);
    }
    strWhere = StrSqlUtils.strAddAnd(strAwhere, phWhere);
  }

  public List<ConstClass> queryAll() {
    openCursor();  // forward
    cacheObj = fetchAll();
    closeCursor();
    return cacheObj;    
  }

  public List<ConstClass> queryPart(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  
 
  private List<ConstClass> fetchAll() {
    ArrayList<ConstClass> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        ConstClass record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private List<ConstClass> fetchPart(int startPos, int cntRows) {
    ArrayList<ConstClass> cache = new ArrayList<>();
    if (startPos <= 0 || cntRows<=0) { 
      return cache;
    }
    try {
      // --------------------------------
      boolean res = rs.absolute(startPos);
      // --------------------------------
      if (res == false) {
        return cache;
      }
      int curRow = 0;
      //------------------------------------------
      do {
        curRow++;
        ConstClass record = getObject(rs);
        cache.add(record);
        if (curRow>=cntRows) break;
        //----------------------------------------
      } while (rs.next());
      //------------------------------------------
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private ConstClass getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("mssc_id");
    String rsName = rs.getString("mssc_name");
    String rsValue = rs.getString("mssc_value");
    //---------------------------------------
    ConstClass obj = new ConstClass();
    obj.setId(rsId);
    obj.setName(rsName);
    obj.setValue(rsValue);
    //---------------------------------------
    return obj;
  }

}
