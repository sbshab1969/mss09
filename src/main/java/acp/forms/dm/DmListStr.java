package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;

import acp.db.service.*;

public class DmListStr extends DmPanel {
  private static final long serialVersionUID = 1L;

  private ManagerListStr tableManager;
  private List<String[]> cacheStr = new ArrayList<>();

  public DmListStr(ManagerListStr tblMng) {
    tableManager = tblMng;
    setHeaders();
  }

  public ManagerListStr getManager() {
    return tableManager;
  }

  public void setManager(ManagerListStr tblMng) {
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
  
  // --- AbstractTableModel ---

  public Class<?> getColumnClass(int col) {
    return String.class;
  }

  // --- TableModel ---

  public int getRowCount() {
    return cacheStr.size();
  }

  public Object getValueAt(int row, int col) {
    return cacheStr.get(row)[col];
  }
  // --------------------------------------
  
  public void calcRecCount() {
    recCount = tableManager.countRecords();
  }

  public void queryAll() {
    cacheStr = tableManager.queryAll();
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
    cacheStr = tableManager.queryPart(startRec,recPerPage);
    fireTableChanged(null);
  }

  public void closeQuery() {
    tableManager.closeQuery();
  }

}
