package com.github.baniuk.JsonTree;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author p.baniukiewicz
 *
 */
public class ActionSearch extends JsonTreeAbstractAction {
  private int bookamrk = -1;

  public ActionSearch(String name, String desc, JsonTree jsonTree) {
    super(name, desc, jsonTree);
  }

  /**
   * @param name
   * @param desc
   * @param mnemonic
   * @param jsonTree
   */
  public ActionSearch(String name, String desc, Integer mnemonic, JsonTree jsonTree) {
    super(name, desc, mnemonic, jsonTree);
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.github.baniuk.JsonTree.JsonTreeAbstractAction#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String text = jt.tf_search.getText().trim();
    DefaultMutableTreeNode foundNode = null;
    jt.LOGGER.debug(e.getActionCommand());
    if (text.length() > 0) {
      if (e.getActionCommand().equals(Actions.A_SEARCH_DW)) {
        List<DefaultMutableTreeNode> searchNodes =
                getSearchNodes((DefaultMutableTreeNode) jt.tree.getModel().getRoot());
        for (int i = bookamrk + 1; i < searchNodes.size(); i++) {
          if (searchNodes.get(i).toString().startsWith(text)) {
            bookamrk = i;
            foundNode = searchNodes.get(i);
            break;
          }
        }
      }
      if (foundNode != null) {
        JsonTree.LOGGER.debug(foundNode.toString());
        TreePath p = new TreePath(foundNode.getPath());
        jt.tree.setSelectionPath(p);
        jt.tree.scrollPathToVisible(p);
      }
    }
    // TODO move this to sep method and add FWD BCK buttons in UI and status box
  }

  private final List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
    List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();

    Enumeration<?> e = root.preorderEnumeration();
    while (e.hasMoreElements()) {
      searchNodes.add((DefaultMutableTreeNode) e.nextElement());
    }
    return searchNodes;
  }
}
