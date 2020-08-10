package acp.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogUtils {

  private static Logger logger = LoggerFactory.getLogger(DialogUtils.class);

  public static void errorMsg(Component parentComp, String msg) {
    JOptionPane.showMessageDialog(parentComp, msg,
        Messages.getString("Title.Error"), JOptionPane.ERROR_MESSAGE);
    logger.error(msg);
  }

  public static void errorMsg(String msg) {
    errorMsg(null, msg);
  }

  public static void errorPrint(String msg) {
    // System.err.println(msg);
    errorMsg(msg);
  }

  public static void errorPrint(Exception e) {
    // errorMsg(null, e.toString());
    // errorMsg(null, e.getClass().getName());
    logger.error(e.getMessage());
    e.printStackTrace();
    // System.err.println(e.toString());
    errorMsg(null, e.getMessage());
  }

  public static void infoDialog(String str) {
    JOptionPane.showMessageDialog(null, str, Messages.getString("Title.Info"),
        JOptionPane.INFORMATION_MESSAGE);
  }

  public static int confirmDialog(String message, String title, int initialValue) {
    int res = initialValue;
    Object[] options = { Messages.getString("Button.Yes"),
        Messages.getString("Button.No") };
    res = JOptionPane.showOptionDialog(null, message, title,
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
        options[initialValue]);
    return res;
  }

}
