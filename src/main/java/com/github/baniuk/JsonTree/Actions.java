package com.github.baniuk.JsonTree;

import java.awt.event.KeyEvent;

/**
 * @author p.baniukiewicz
 *
 */
class Actions {
  final static String A_TREE_FOLD = "Fold tree";
  final static String A_TREE_UNFOLD = "Unfold tree";
  final static Integer VK_TREE_FOLD = new Integer(KeyEvent.VK_C);

  final static String A_RELOAD = "Reload json";
  final static Integer VK_RELOAD = new Integer(KeyEvent.VK_R);

  final static String A_SEARCH_BACKWARD = "Search Bck";
  final static Integer VK_SEARCH_BACKWARD = new Integer(KeyEvent.VK_B);
  final static String A_SEARCH_FORWARD = "Search Fwd";
  final static Integer VK_SEARCH_FORWARD = new Integer(KeyEvent.VK_F);
}
