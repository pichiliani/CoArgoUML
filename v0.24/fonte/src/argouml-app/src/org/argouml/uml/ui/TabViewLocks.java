// $Id: TabSrc.java,v 1.18 2003/09/04 20:11:44 thierrylach Exp $
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.ui;


import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Vector;

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

import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;


import org.argouml.ui.LookAndFeelMgr;

import org.argouml.ui.targetmanager.TargetEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;


import org.argouml.ui.TabText;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


// Imports da colaboração

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Globals;

import org.argouml.kernel.ProjectManager;

/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabViewLocks   extends TabText
   {
    ////////////////////////////////////////////////////////////////
    // constructor
    private final Logger cat = Logger.getLogger(TabViewLocks.class);

    Object _target;
    boolean _shouldBeEnabled = false;
    JTable _table = new JTable(0, 0);
    JLabel _titleLabel;
    
    /** Create a tab that contains a toolbar.
     *  Then add a notation selector onto it.
     */
    public TabViewLocks() {
        // TODO:  Temporarily remove toolbar until src selection
        // is working better.
        //
        super("tab.vlck", true);
        
        // _tableModel = new TableModelTaggedValuesLock(this);
        // _table.setModel(_tableModel);

        ((DefaultTableModel )_table.getModel()).addColumn("Usuário");
        ((DefaultTableModel )_table.getModel()).addColumn("Objeto em lock");
		
        /* String []x = {"a","b"};
        ((DefaultTableModel )_table.getModel()).addRow(x); */
        
        _table.setRowSelectionAllowed(true);
        
        // _table.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane(_table);
        
        Font labelFont = LookAndFeelMgr.getInstance().getSmallFont();
        _table.setFont(labelFont);
        
        _titleLabel = new JLabel("none");
        resizeColumns();
        setLayout(new BorderLayout());
        add(_titleLabel, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
              
        
        // Globals.curEditor().clienteEnvia.janelaLocks =(DefaultTableModel )_table.getModel(); 
    }


    /**
     * Determines if the current tab should be enabled with the given target. 
     * @return true if the given target is either a modelelement or is a fig with
     * as owner a modelelement.
     */
    public boolean shouldBeEnabled(Object target) {
    	
    	return true;
    }

    public void resizeColumns() {
        TableColumn keyCol = _table.getColumnModel().getColumn(0);
        TableColumn valCol = _table.getColumnModel().getColumn(1);
        keyCol.setMinWidth(50);
        keyCol.setWidth(150);
        keyCol.setPreferredWidth(150);
        valCol.setMinWidth(250);
        valCol.setWidth(550);
        valCol.setPreferredWidth(550);
        //_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _table.sizeColumnsToFit(-1);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setTarget(Object target) {
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _table.sizeColumnsToFit(0);
        }

    public Object getTarget() { return _target; }

    public void refresh() { setTarget(_target); }


    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
	
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
	
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
	
    }

    
    
} /* end class TabViewLocks */



