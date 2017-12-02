package com.github.baniuk.JsonTree;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author p.baniukiewicz
 *
 */
@SuppressWarnings("serial")
public abstract class JsonTreeAbstractAction extends AbstractAction {
  protected JsonTree jt;

  /**
   * @param name
   * @param desc
   * @param mnemonic
   * @param jsonTree
   */
  public JsonTreeAbstractAction(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
    super(name);
    putValue(SHORT_DESCRIPTION, desc);
    putValue(MNEMONIC_KEY, mnemonic);
    jt = jsonTree;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

}
