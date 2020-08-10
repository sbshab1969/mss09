package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;

import acp.db.domain.*;
import acp.db.service.ConfigManagerList;

public class DmConfigList extends DmPanel {
  private static final long serialVersionUID = 1L;

  private ConfigManagerList tableManager;
  private List<ConfigClass> cacheObj = new ArrayList<>();

  public DmConfigList(ConfigManagerList tblMng) {
    tableManager = tblMng;
    setHeaders();
  }

  public void setManager(ConfigManagerList tblMng) {
    tableManager = tblMng;
    setHeaders();
  }
  
  private void setHeaders() {
    if (tableManager != null) {
      headers = tableManager.getHeaders();
      types = tableManager.getTypes();
      colCount = headers.length;
    } else {
      headers = new String[] {};
      types = new Class<?>[] {};
      colCount = 0;
    }
  }
  
  // --- TableModel ---

  public int getRowCount() {
    return cacheObj.size();
  }

  public Object getValueAt(int row, int col) {
    ConfigClass obj = cacheObj.get(row); 
    switch (col) {
    case 0:  
      return obj.getId();
    case 1:  
      return obj.getName();
    case 2:  
      return obj.getSource().getName();
    case 3:
      return obj.getDateBegin();
    case 4:
      return obj.getDateEnd();
    case 5:
      return obj.getComment();
    case 6:
      return obj.getOwner();
    }  
    return null;
  }
  // --------------------------------------
  
  public void calcRecCount() {
    recCount = tableManager.countRecords();
  }

  public void queryAll() {
    cacheObj = tableManager.queryAll();
    fireTableChanged(null);
  }

  public void queryPage() {
    calcPageCount();
    if (currPage > pageCount) {
      currPage = pageCount;
    }  
    tableManager.openQuery();
    fetchPage(currPage);
  }

  public void fetchPage(int page) {
    int startRec = calcStartRec(page);
    cacheObj = tableManager.queryPart(startRec,recPerPage);
    fireTableChanged(null);
  }

  public void closeQuery() {
    tableManager.closeQuery();
  }

}
