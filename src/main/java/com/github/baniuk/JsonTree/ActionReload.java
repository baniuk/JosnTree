package com.github.baniuk.JsonTree;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;

/**
 * @author p.baniukiewicz
 *
 */
@SuppressWarnings("serial")
public class ActionReload extends JsonTreeAbstractAction {

  /**
   * @param name
   * @param desc
   * @param mnemonic
   * @param jsonTree
   */
  public ActionReload(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
    super(name, desc, mnemonic, jsonTree);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    jt.initGuiTree();
    String json;
    try {
      json = new String(Files.readAllBytes(jt.jsonFile));
      Object things = new Gson().fromJson(json, Object.class);
      jt.print(things, jt.jsonFile.getFileName().toString(), 0);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    jt.chbExpand.setSelected(false); // keep state of window when reloaded
    if (jt.lastRowIndex > jt.tree.getRowCount() - 1) {
      jt.lastRowIndex = jt.tree.getRowCount() - 1;
    }
    for (int i = 0; i <= jt.lastRowIndex; i++) {
      jt.tree.expandRow(i);
    }
    jt.tree.setSelectionRow(jt.lastRowIndex); // select last row (after reload it can be another
                                              // row)
  }

}
