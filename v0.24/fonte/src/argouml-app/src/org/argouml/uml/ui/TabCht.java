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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

import javax.swing.*;


// Imports da colaboração

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ClienteConectaGEF;

import org.argouml.kernel.ProjectManager;

import org.argouml.kernel.ClienteConecta;
/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabCht
    extends TabText
   {
    ////////////////////////////////////////////////////////////////
    // constructor
    private final Logger cat = Logger.getLogger(TabCht.class);


    JLabel _labelMsg	= new JLabel("Mensagem:");
    JTextField _textoMsg = new JTextField("", 20);
    JButton     _enviaButton = new JButton();
    
    /** Create a tab that contains a toolbar.
     *  Then add a notation selector onto it.
     */
    public TabCht() {
        // TODO:  Temporarily remove toolbar until src selection
        // is working better.
        //
        super("tab.chat", true);
        
        // super("Source", false);
        /* _notationName = null;
        getToolbar().add(NotationComboBox.getInstance());
        NotationComboBox.getInstance().addItemListener(this); */
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
    	labelConstraints.anchor = GridBagConstraints.EAST;
    	labelConstraints.gridy = 0;
    	labelConstraints.gridx = 0;
    	labelConstraints.gridwidth = 1;
    	labelConstraints.gridheight = 1;
    	labelConstraints.insets = new Insets(0, 10, 5, 4);
        
        getToolbar().add(_labelMsg, labelConstraints);
        
        GridBagConstraints fieldConstraints = new GridBagConstraints();
    	fieldConstraints.anchor = GridBagConstraints.WEST;
    	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    	fieldConstraints.gridy = 0;
    	fieldConstraints.gridx = 1;
    	fieldConstraints.gridwidth = 3;
    	fieldConstraints.gridheight = 1;
    	fieldConstraints.weightx = 1.0;
    	fieldConstraints.insets = new Insets(0, 4, 5, 10);
        
        getToolbar().add(_textoMsg,fieldConstraints);
        
        _enviaButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	    	// Aqui vou enviar a mensagem para os demais participantes
    	    	
	    		//  Enviando para o argo uma mensagem de 'protocolo'
	    		ProjectManager.getManager().clienteEnvia.EnviaEvento(_textoMsg.getText(),"PROT_chat_msg");
	    		
	    		_textoMsg.setText("");
	    		
	    		_textoMsg.requestFocus();
    	    }
    	});
        _enviaButton.setText("enviar");
        _enviaButton.setBorderPainted(true);
        
        // _enviaButton.setMnemonic("e");
        
        _enviaButton.setBorder(BorderFactory.createLineBorder(Color.black));
        
        _enviaButton.setSize(3,1);
        
        getToolbar().add(_enviaButton);
        
        getToolbar().addSeparator();
        // ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        
        setTarget("");
        
        
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    protected String genText(Object modelObject) {
        return modelObject.toString();
    }

    /**
     * Sets the target of this tab. 
     */
    public void setTarget(Object t) {

    	if(t instanceof String )
    		super.setTarget(t);
    }

    /**
     * Determines if the current tab should be enabled with the given target. 
     * @return true if the given target is either a modelelement or is a fig with
     * as owner a modelelement.
     */
    public boolean shouldBeEnabled(Object target) {
    	
    	return true;
    }


    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    }

    public void refresh() {
        setTarget(getTarget());
    }

    private String getSourceFileFor(Object modelObject, Object nn) {
        //Project p = ProjectManager.getManager().getCurrentProject();
        //_outputDirectoryComboBox.getModel().setSelectedItem(p.getGenerationPrefs().getOutputDir());
        return null;
    }

} /* end class TabCht */
