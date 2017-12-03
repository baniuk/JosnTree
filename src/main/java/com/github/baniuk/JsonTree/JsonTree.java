package com.github.baniuk.JsonTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Json to Tree converter.
 * 
 * <p>This class contains also entry point at {@link #main(String[])} with CLI arguments parsing.
 *
 */
public class JsonTree {
  static final Logger LOGGER = LoggerFactory.getLogger(JsonTree.class.getName());
  private Options options = null;
  Path jsonFile;
  private boolean showGui = false;
  private int cliErrorStatus = 0; // returned to main
  JTree tree;
  private IView viewer;
  DefaultMutableTreeNode top;
  private JFrame frame = null;
  JCheckBox chbExpand;
  int lastRowIndex; // last row selected, used to restore after reload
  TreePath lastPath;
  JLabel lb_keyValue;
  JTextField tf_search;

  /**
   * Constructor used when object is initialised from cli.
   * 
   * <p>Run the processing according to CLI options.
   * 
   * @param args
   */
  public JsonTree(String[] args) {
    this();

    // define CLI parameters
    CommandLineParser parser = new DefaultParser();
    options = new Options();
    Option o = new Option("help", "print this message");
    options.addOption(o);
    o = Option.builder("f").required().hasArg().argName("FILE").desc("JSon file to open").build();
    options.addOption(o);
    o = Option.builder("gui").desc("Show GUI in place of stdout").build();
    options.addOption(o);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      CommandLine cmd = parser.parse(options, args);
      cliParser(cmd); // parse and fill class properties
      run(); // run according to options
    } catch (ParseException e) {
      System.err.println("Parsing failed. Reason: " + e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("JsonTree", options, true);
      cliErrorStatus = 1;
    } catch (ClassNotFoundException e) {
      System.err.println("Requested Look&Feel can not be loaded. Reason: " + e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    } catch (InstantiationException e) {
      System.err.println("Requested Look&Feel can not be loaded. Reason: " + e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      System.err.println("Requested Look&Feel can not be loaded. Reason: " + e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    } catch (UnsupportedLookAndFeelException e) {
      System.err.println("Requested Look&Feel can not be loaded. Reason: " + e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }

  }

  /**
   * Reset current tree object for GUI and clear all nodes except root.
   */
  void initGuiTree() {
    tree.setModel(null);
    top = new DefaultMutableTreeNode("Document");
    tree.setModel(new DefaultTreeModel(top));
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.updateUI();
    viewer = new TreeViewer(top); // replace viewer to GUI, must be here to reinitialise it with top
                                  // root node
    lastPath = new TreePath(top);
  }

  /**
   * API call constructor.
   */
  public JsonTree() {
    tree = new JTree();
    tree.putClientProperty("JTree.lineStyle", "Angled");
    // TODO Auto-generated constructor stub
    viewer = new TextViewer();
  }

  /**
   * Process json file given by Path.
   * 
   * <p>Respect class properties that must be set before calling this method. Note that parametrised
   * constructor {@link #JsonTree(String[])} set all properties from CLI parameters. If object is
   * created from {@link #JsonTree()} properties must be initialised before calling this method.
   */
  public void run() {
    try {
      String json = new String(Files.readAllBytes(jsonFile));
      Object things = new Gson().fromJson(json, Object.class);
      if (showGui) { // if gui switch
        buildWindow();
        print(things, jsonFile.getFileName().toString(), 0);
      } else { // use CLI mode
        print(things, "root", 0);
        System.out.println("x--...END");
      }
    } catch (IOException e) {
      System.err.println("Error: " + e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
  }

  /**
   * Build and show window. Process requests as well.
   */
  private void buildWindow() {
    if (frame != null) {
      return; // window already built
    }
    frame = new JFrame("JsonTree");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel(); // main panel
    frame.setPreferredSize(new Dimension(800, 1200));
    panel.setLayout(new BorderLayout());
    JScrollPane treeView = new JScrollPane(tree);
    panel.add(treeView, BorderLayout.CENTER);

    // -- south panel
    JPanel panelB = new JPanel();
    panelB.setLayout(new GridLayout(3, 1));
    JPanel row1 = new JPanel();
    JPanel row2 = new JPanel();
    JPanel row3 = new JPanel();
    panelB.add(row2);
    panelB.add(row1);
    panelB.add(row3);
    panel.add(panelB, BorderLayout.SOUTH);

    // -- row 1 south panel
    row1.setLayout(new FlowLayout(FlowLayout.LEFT));
    chbExpand = new JCheckBox(); // expand or collapse tree
    chbExpand.setAction(
            new ActionFold("Fold/Expand all", "Fold or unfold all", Actions.VK_TREE_FOLD, this));
    chbExpand.addItemListener(new ItemListener() {
      // set action command depending on button state
      @Override
      public void itemStateChanged(ItemEvent e) {
        // expand or collapse all
        if (e.getStateChange() == ItemEvent.DESELECTED) {
          ((JCheckBox) e.getItem()).setActionCommand(Actions.A_TREE_FOLD);
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
          ((JCheckBox) e.getItem()).setActionCommand(Actions.A_TREE_UNFOLD);
        }
      }
    });
    row1.add(chbExpand);

    JButton btnReload = new JButton(); // reload json
    btnReload.setAction(new ActionReload("Reload", "Reload json file", Actions.VK_RELOAD, this));
    row1.add(btnReload);

    // -- row 2 south panel
    row2.setLayout(new FlowLayout(FlowLayout.LEFT));
    lb_keyValue = new JLabel();
    row2.add(lb_keyValue);

    // -- row 3 south panel
    row3.setLayout(new FlowLayout(FlowLayout.LEFT));
    tf_search = new JTextField(16);
    row3.add(tf_search);
    ActionSearch search = new ActionSearch.ActionSearchForward(Actions.A_SEARCH_FORWARD,
            "Search json forward", Actions.VK_SEARCH_FORWARD, this);
    tf_search.setAction(search);
    JButton btn_up = new JButton();
    btn_up.setAction(search);
    JButton btn_down = new JButton();
    btn_down.setAction(new ActionSearch.ActionSearchBackward(Actions.A_SEARCH_BACKWARD,
            "Search json bacward", Actions.VK_SEARCH_BACKWARD, this));
    row3.add(btn_up);
    row3.add(btn_down);

    tree.addTreeSelectionListener(new TreeSelectionListener() { // display selected tree path

      @Override
      public void valueChanged(TreeSelectionEvent e) {
        final String sep = "->";
        TreePath node = tree.getSelectionPath();
        if (node == null) { // can be after clearing tree
          return;
        }
        lastPath = tree.getSelectionPath();
        lastRowIndex = tree.getRowForPath(lastPath);
        String str = "";
        for (int i = 1; i < node.getPathCount(); i++) // produce output string
          str = str.concat(node.getPathComponent(i).toString()).concat(sep);
        str = (str.length() > 0) ? str.substring(0, str.lastIndexOf(sep)) : ""; // cut last sep
        LOGGER.info(str);
        lb_keyValue.setText(node.getPathComponent(node.getPathCount() - 1).toString());

      }
    });
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Parse cli options decoded to org.apache.commons.cli.CommandLine object and sets properties of
   * JsonTree.
   * 
   * @param cmd parsed cli string as org.apache.commons.cli.CommandLine object
   */
  private void cliParser(CommandLine cmd) {
    if (cmd.hasOption("help")) { // show help and finish
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("JsonTree", options, true);
      return;
    }
    if (cmd.hasOption('f')) { // set path to json
      jsonFile = Paths.get(cmd.getOptionValue('f'));
    }
    if (cmd.hasOption("gui")) { // will show GUI
      showGui = true;
      initGuiTree();
    }

  }

  /**
   * Print json structure line by line starting from most outward class.
   * 
   * <p>This is recursive method that uses {@link IView} object to process discovered branches and
   * leafs.
   * 
   * @param node JSon node - object not being Java primitive
   * @param keyname print name of the node
   * @param level level of the indent. 0 means root of json.
   */
  @SuppressWarnings("rawtypes")
  void print(Object node, String keyname, int level) {
    boolean all = false; // true to show all elements of list, false to show first on eonly
    if (node == null) { // stop processing if null value
      viewer.viewLeaf("null value", level);
      return;
    }
    // GSon internal representation of json contains:
    // LinkedMap - if json node refers to class
    // Collection - if json node refers to array
    // java class for primitives (if value can not be expanded)
    if (node instanceof Map) { // node is class
      String firstNodeLabel = level == 0 ? "File" : "Class";
      viewer.viewBranch(keyname + " (" + firstNodeLabel + ")", level);
      for (Object k : ((Map) node).keySet()) { // iterate over class properties (keys in map)
        print(((Map) node).get(k), k.toString(), level + 1); // pass value recursive
      }
    } else if (node instanceof Collection) { // if node is array
      int l = ((Collection) node).size();
      viewer.viewBranch(keyname + " (" + "Array" + "[" + l + " elements]" + ")", level);
      Iterator it = ((Collection) node).iterator();
      int i = 0; // array index
      if (l > 0) { // if there is at least one element
        do {
          print(it.next(), keyname + "[" + i++ + "]", level + 1); // pass it for evaluation
        } while (it.hasNext() && all); // continue until end or stop after first element
      } else {
        viewer.viewLeaf("empty", level + 1); // TODO should not be printed maybe
      }
    } else { // not class nor array - primitive value (but can be java class e.g. string)
      viewer.viewLeaf(keyname + " (" + node.getClass().getSimpleName() + ")", level);
    }
  }

  /**
   * Application runner.
   * 
   * @param args CLI arguments
   * @return program exit status
   */
  public static void main(String[] args) {
    JsonTree app = new JsonTree(args);
    System.exit(app.cliErrorStatus);
  }
}
