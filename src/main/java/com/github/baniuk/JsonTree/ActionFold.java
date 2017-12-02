package com.github.baniuk.JsonTree;

import java.awt.event.ActionEvent;

/**
 * Fold or unfold all.
 * 
 * @author p.baniukiewicz
 *
 */
@SuppressWarnings("serial")
public class ActionFold extends JsonTreeAbstractAction {

  /**
   * @param name
   * @param desc
   * @param mnemonic
   * @param jsonTree
   */
  public ActionFold(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
    super(name, desc, mnemonic, jsonTree);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    // expand or collapse all
    if (arg0.getActionCommand().equals(Actions.A_TREE_FOLD)) {
      for (int i = 0; i < jt.tree.getRowCount(); i++) {
        jt.tree.collapseRow(i);
      }
    }
    if (arg0.getActionCommand().equals(Actions.A_TREE_UNFOLD)) {
      for (int i = 0; i < jt.tree.getRowCount(); i++) {
        jt.tree.expandRow(i);
      }
    }
  }

}
