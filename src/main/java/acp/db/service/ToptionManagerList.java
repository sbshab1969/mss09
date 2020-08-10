package acp.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;

//import java.sql.Date;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.*;
import acp.db.utils.*;
import acp.utils.*;

public class ToptionManagerList extends ManagerList {

  protected List<ToptionClass> cacheObj = new ArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(ToptionManagerList.class);

  private String path;
  private ArrayList<String> attrs;
  private int attrSize;
  private int attrMax = 5;
  private String attrPrefix;

  public ToptionManagerList(String path, ArrayList<String> attrs) {
    this.path = path;
    this.attrs = attrs;
    this.attrSize = attrs.size();
    String[] pathArray = path.split("/");
    this.attrPrefix = pathArray[pathArray.length - 1];

    createFields();
    cntColumns = headers.length;
    
    strFields = StrSqlUtils.buildSelectFields(fields, null);
    // strFrom = null;
    createTable(-1L);  
    strWhere = strAwhere;
    strOrder = pkColumn;
    // -------------
    prepareQuery();
    // -------------
  }

  private void createFields() {
    fields = new String[attrSize + 3];
    headers = new String[attrSize + 3];
    types = new Class<?>[attrSize + 3];
    // ---
    int j = 0;
    fields[j] = "config_id";
    headers[j] = "ID";
    types[j] = Long.class;
    pkColumn = fields[j];
    // ---
    for (int i = 0; i < attrSize; i++) {
      j++;
      fields[j] = "P" + j;
      headers[j] = FieldConfig.getString(attrPrefix + "." + attrs.get(i));
      types[j] = String.class;
    }
    // ---
    j++;
    fields[j] = "date_begin";
    headers[j] = Messages.getString("Column.DateBegin");
    types[j] = Date.class;
    // ---
    j++;
    fields[j] = "date_end";
    headers[j] = Messages.getString("Column.DateEnd");
    types[j] = Date.class;
    // ---
  }

  public void createTable(Long src) {
    String res = "table(mss.spr_options(" + src + ",'" + path + "'";
    for (int i = 0; i < attrSize; i++) {
      res += ",'" + attrs.get(i) + "'";
    }
    for (int i = attrSize; i < attrMax; i++) {
      res += ",null";
    }
    res += "))";
    strFrom = res;
  }

  public String selectSources() {
    String query = "select msss_id, msss_name from mss_source order by msss_name";
    return query;
  }
  
  public List<ToptionClass> queryAll() {
    openCursor();  // forward
    cacheObj = fetchAll();
    closeCursor();
    return cacheObj;    
  }

  public List<ToptionClass> queryPart(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<ToptionClass> fetchAll() {
    ArrayList<ToptionClass> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        ToptionClass record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private List<ToptionClass> fetchPart(int startPos, int cntRows) {
    ArrayList<ToptionClass> cache = new ArrayList<>();
    if (startPos <= 0 || cntRows<=0) { 
      return cache;
    }
    try {
      boolean res = rs.absolute(startPos);
      if (res == false) {
        return cache;
      }
      int curRow = 0;
      //------------------------------------------
      do {
        curRow++;
        ToptionClass record = getObject(rs);
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
  
  private ToptionClass getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("config_id");
    // ----------------------
    int j = 0;
    ArrayList<String> pArr = new ArrayList<>();
    for (int i = 0; i < attrSize; i++) {
      j = i + 1;
      String pj = rs.getString("p" + j);
      pArr.add(pj);
    }
    // ----------------------
    // Date rsDateBegin = rs.getDate("date_begin");
    // Date rsDateEnd = rs.getDate("date_end");
    Date rsDateBegin = rs.getTimestamp("date_begin");
    Date rsDateEnd = rs.getTimestamp("date_end");
    //---------------------------------------
    ToptionClass obj = new ToptionClass();
    obj.setId(rsId);
    obj.setArrayP(pArr);
    obj.setDateBegin(rsDateBegin);
    obj.setDateEnd(rsDateEnd);
    //---------------------------------------
    return obj;
  }
  
}
