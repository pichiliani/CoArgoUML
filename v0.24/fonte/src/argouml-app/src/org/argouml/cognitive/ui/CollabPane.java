// $Id: ToDoPane.java,v 1.32 2004/03/21 07:13:52 linus Exp $
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;
import org.argouml.i18n.Translator;

import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.tigris.gef.ui.ToolBar;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

// Imports da colaboração

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ClienteConectaGEF;

import org.argouml.kernel.ProjectManager;

import org.argouml.ui.LookAndFeelMgr;

public class CollabPane extends JPanel
    implements ItemListener,
        MouseListener {
    
    /**
     * @deprecated by Linus Tolke as of 0.15.5. Use your own instance of
     * Logger. This will become private.
     */
    protected static Logger cat = Logger.getLogger(CollabPane.class);
    
    ////////////////////////////////////////////////////////////////
    // constants
    
    public static final int WARN_THRESHOLD = 50;
    public static final int ALARM_THRESHOLD = 100;
    public static final Color WARN_COLOR = Color.yellow;
    public static final Color ALARM_COLOR = Color.pink;
    
    public static int _clicksInToDoPane;
    public static int _dblClicksInToDoPane;
    public static int _toDoPerspectivesChanged;
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    protected JTree _tree;
    protected ToolBar _toolbar;
    protected JComboBox _combo;
    
    /** vector of TreeModels */
    protected Vector _perspectives;
    protected ToDoPerspective _curPerspective;
    
    protected ToDoList _root;
    protected Action _flatView;
    protected JToggleButton _flatButton;
    protected JLabel _countLabel;
    protected boolean _flat;
    protected Object _lastSel;
    protected int _oldSize;
    protected char _dir;
    /** shouldn't need this */
    protected ProjectBrowser _pb;
    
    JTable _table = new JTable(0, 0);
    JLabel _titleLabel;
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     *
     *<pre>
     * TODO: - Bob Tarling 8 Feb 2003
     * Replace GEF ToolBar class with our own Toolbar class
     * (only rely on GEF for diagram functionality)
     *</pre>
     *
     */
    public CollabPane(boolean doSplash) {
        
        setLayout(new BorderLayout());
                
        _perspectives = new Vector();
        
        // _flatView = Actions.FlatToDo;
        /* JPanel toolbarPanel = new JPanel(new BorderLayout());
        
        
        add(toolbarPanel, BorderLayout.NORTH); */ 
     
        
        // Aqui vou adiconar os controles!
        ((DefaultTableModel )_table.getModel()).addColumn("Usuário");
        ((DefaultTableModel )_table.getModel()).addColumn("Objeto em lock");
        
        _table.setRowSelectionAllowed(true);
        
        // _table.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane(_table);
        
        Font labelFont = LookAndFeelMgr.getInstance().getSmallFont();
        _table.setFont(labelFont);
        
        _titleLabel = new JLabel("Locks");
        resizeColumns();
        setLayout(new BorderLayout());
        
        add(_titleLabel, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        
        setPerspectives(buildPerspectives());
        
        
        setMinimumSize(new Dimension(50, 30));
        
        Dimension preferredSize = getPreferredSize();
        preferredSize.height = 50;
        // setPreferredSize(preferredSize);
        setPreferredSize(new Dimension(160, 100));
        
        Globals.curEditor().clienteEnvia.janelaLocks =(DefaultTableModel)_table.getModel();
        Globals.curEditor().clienteEnvia.nome_lock  = _titleLabel;
        
    }
    
    
    
    public void resizeColumns() {
        TableColumn keyCol = _table.getColumnModel().getColumn(0);
        TableColumn valCol = _table.getColumnModel().getColumn(1);
        keyCol.setMinWidth(5);
        keyCol.setWidth(5);
        keyCol.setPreferredWidth(5);
        
        valCol.setMinWidth(20);
        valCol.setWidth(20);
        valCol.setPreferredWidth(20);
        //_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _table.sizeColumnsToFit(-1);
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors
    
    public void setRoot(ToDoList r) {
        _root = r;
    }
    
    public ToDoList getRoot() { return _root; }
    
    public Vector getPerspectives() { return _perspectives; }
    
    public void setPerspectives(Vector pers) 
    {
        _perspectives = pers;
        if (pers.isEmpty()) 
        {
        	_curPerspective = null;
        } 
        else 
        {
        	_curPerspective = (ToDoPerspective) pers.elementAt(0);
        }
        
        if (pers.isEmpty()) 
        {
        	_curPerspective = null;
        } else if (pers.contains(_curPerspective)) 
        {
            setCurPerspective(_curPerspective);
        } 
        else 
        {
            setCurPerspective((ToDoPerspective) _perspectives.elementAt(0));
        }

    }
    
    public ToDoPerspective getCurPerspective() { return _curPerspective; }
    public void setCurPerspective(TreeModel per) {
        if (_perspectives == null || !_perspectives.contains(per)) {
	    return;
	}
        // _combo.setSelectedItem(per);
        // _toDoPerspectivesChanged++;
    }
    
    
    
    /**
     * Return whether the todo pane is currently in flat hierachy mode.
     *
     * @return true if flat.
     */
    public boolean isFlat() { return _flat; }

    
    // ------------ ItemListener implementation ----------------------
    
    /**
     * Called when the user selects a perspective from the perspective
     * combo.
     *
     * @param e is the event.
     */
    public void itemStateChanged(ItemEvent e) {
        
    }
    
    
    // ------------- MouseListener implementation ---------------
    
    /** empty implementation */
    public void mousePressed(MouseEvent e) { }
    /** empty implementation */
    public void mouseReleased(MouseEvent e) { }
    /** empty implementation */
    public void mouseEntered(MouseEvent e) { }
    /** empty implementation */
    public void mouseExited(MouseEvent e) { }
    
    public void mouseClicked(MouseEvent e) {
        /* int row = _tree.getRowForLocation(e.getX(), e.getY());
        TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
        if (row != -1) {
            if (e.getClickCount() >= 2) {
                myDoubleClick(row, path);               
            } else {
                mySingleClick(row, path);
            }       
        } */
        e.consume();
    }
    
    
    ////////////////////////////////////////////////////////////////
    // other methods
    
    
  
    
    /**
     * The perspectives to be chosen in the combobox are built here.
     */
    private static Vector buildPerspectives() {
        
        ToDoPerspective priority = new ToDoByPriority();
        ToDoPerspective decision = new ToDoByDecision();
        ToDoPerspective goal = new ToDoByGoal();
        ToDoPerspective offender = new ToDoByOffender();
        ToDoPerspective poster = new ToDoByPoster();
        ToDoPerspective type = new ToDoByType();
        
        // add the perspetives to a vector for the combobox
        Vector perspectives = new Vector();
        
        perspectives.add(priority);
        perspectives.add(decision);
        perspectives.add(goal);
        perspectives.add(offender);
        perspectives.add(poster);
        perspectives.add(type);
        
        //
        ToDoPerspective.registerRule(new GoListToDecisionsToItems());
        ToDoPerspective.registerRule(new GoListToGoalsToItems());
        ToDoPerspective.registerRule(new GoListToPriorityToItem());
        ToDoPerspective.registerRule(new GoListToTypeToItem());
        ToDoPerspective.registerRule(new GoListToOffenderToItem());
        ToDoPerspective.registerRule(new GoListToPosterToItem());
        
        return perspectives;
    }
    
} /* end class ToDoPane */
