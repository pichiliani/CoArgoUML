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
import java.io.FileWriter;
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

import java.util.Enumeration;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPointer;


// Imports da colaboração

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ClienteConectaGEF;

import org.argouml.kernel.ProjectManager;
/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabLck
    extends TabText
   {
    ////////////////////////////////////////////////////////////////
    // constructor
    private final Logger cat = Logger.getLogger(TabLck.class);

    // Writer para logar a mudança de nível de lock
    private FileWriter ClienteMudaLock;

    JLabel _labelMsg	= new JLabel("Níveis: ");
    JButton     _n1Button = new JButton();
    JButton     _n2Button = new JButton();
    JButton     _n3Button = new JButton();
    JButton     _n4Button = new JButton();
    
    /** Create a tab that contains a toolbar.
     *  Then add a notation selector onto it.
     */
    public TabLck() {
        // TODO:  Temporarily remove toolbar until src selection
        // is working better.
        //
        super("tab.lck", true);
        
        // Iniciando o Writer
        try
		{ 
        	ClienteMudaLock = new FileWriter("c:\\ObjetosCliente.txt",true);
		}
		catch (Exception ex)
		{
        		ex.printStackTrace();
		}
        
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
        
        // getToolbar().add(_textoMsg,fieldConstraints);
        
        _n1Button.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {

    	    _n1Button.setBackground(Color.green);
        	_n2Button.setBackground(Color.lightGray);
        	_n3Button.setBackground(Color.lightGray);
        	_n4Button.setBackground(Color.lightGray);

        	LogaMsg("Usuario: " + Globals.curEditor().clienteEnvia.getLogin() + " mudou para o Nivel 1 de lock.");
        	
        	
    	    	// Aqui vou enviar a mensagem para os demais participantes
    	    	if(  Globals.curEditor().clienteEnvia.connected)
    	    	{
    	    		Globals.curEditor().clienteEnvia.setaTipoLock(1);
    	    		Globals.curEditor().clienteEnvia.setaNivelLock(1);
    	    	}
    	    	
    	    		
    	    }
    	});

        _n1Button.setText("Nível 1");
        _n1Button.setBorderPainted(true);
        _n1Button.setBorder(BorderFactory.createLineBorder(Color.black));
        _n1Button.setSize(3,1);
        
        getToolbar().add(_n1Button);
        getToolbar().addSeparator();
        getToolbar().addSeparator();
        
        _n2Button.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {

    	    	_n2Button.setBackground(Color.green);
            	_n1Button.setBackground(Color.lightGray);
            	_n3Button.setBackground(Color.lightGray);
            	_n4Button.setBackground(Color.lightGray);

            	LogaMsg("Usuario: " + Globals.curEditor().clienteEnvia.getLogin() + " mudou para o Nivel 2 de lock.");

    	    	// Aqui vou enviar a mensagem para os demais participantes
    	    	if(  Globals.curEditor().clienteEnvia.connected)
    	    	{
    	    		Globals.curEditor().clienteEnvia.setaTipoLock(1);
    	    		Globals.curEditor().clienteEnvia.setaNivelLock(2);
    	    	}
    	    	
    	    		
    	    }
    	});
        
        _n2Button.setText("Nível 2");
        _n2Button.setBorderPainted(true);
        _n2Button.setBorder(BorderFactory.createLineBorder(Color.black));
        _n2Button.setSize(3,1);
        
        getToolbar().add(_n2Button);
        getToolbar().addSeparator();
        getToolbar().addSeparator();
        
        _n3Button.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {

		
    	    	_n3Button.setBackground(Color.green);
            	_n1Button.setBackground(Color.lightGray);
            	_n2Button.setBackground(Color.lightGray);
            	_n4Button.setBackground(Color.lightGray);
            	
            	LogaMsg("Usuario: " + Globals.curEditor().clienteEnvia.getLogin() + " mudou para o Nivel 3 de lock.");

    	    	// Aqui vou enviar a mensagem para os demais participantes
    	    	if(  Globals.curEditor().clienteEnvia.connected)
    	    	{
    	    		Globals.curEditor().clienteEnvia.setaTipoLock(1);
    	    		Globals.curEditor().clienteEnvia.setaNivelLock(3);
    	    	}
    	    	
    	    		
    	    }
    	});
        
        _n3Button.setText("Nível 3");
        _n3Button.setBorderPainted(true);
        _n3Button.setBackground(Color.green);
        _n3Button.setBorder(BorderFactory.createLineBorder(Color.black));
        _n3Button.setSize(3,1);
        
        getToolbar().add(_n3Button);
        getToolbar().addSeparator();
        getToolbar().addSeparator();
        
        _n4Button.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {

    	    	_n4Button.setBackground(Color.green);
            	_n1Button.setBackground(Color.lightGray);
            	_n2Button.setBackground(Color.lightGray);
            	_n3Button.setBackground(Color.lightGray);

            	LogaMsg("Usuario: " + Globals.curEditor().clienteEnvia.getLogin() + " mudou para o Nivel 4 de lock.");
            	
            	// Aqui vou enviar a mensagem para os demais participantes
    	    	if(  Globals.curEditor().clienteEnvia.connected)
    	    	{
    	    		Globals.curEditor().clienteEnvia.setaTipoLock(1);
    	    		Globals.curEditor().clienteEnvia.setaNivelLock(4);
    	    		
    	    		// Tenta conseguir um lock em todos os objetos
    	    		LockAllObjects();
    	    		
    	    		
    	    	}
    	    	
    	    	
    	    		
    	    }
    	});
        
        _n4Button.setText("Nível 4");
        _n4Button.setBorderPainted(true);
        _n4Button.setBorder(BorderFactory.createLineBorder(Color.black));
        _n4Button.setSize(3,1);
        
        getToolbar().add(_n4Button);
        getToolbar().addSeparator();
        
        getToolbar().addSeparator();
        // ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        
        setTarget("");
        
        
        // Robada para que o Get acesse o Texto Area
        
        Globals.curEditor().clienteEnvia.texto_lock = super.retornaCampo();
        Globals.curEditor().clienteEnvia.texto_lock.setVisible(false); 
        
    }

    private void LockAllObjects()
    {
		// Aqui fou fazer um PROT_lock_request_group, pois quero fazer
		// vários locks de uma vez soh
		ArrayList varios_locks = new ArrayList();

	    // varios_locks.add(underMouse.classNameAndBounds());

		// Colocando um lock em todos os elementos do diagrama...
		Enumeration figs = Globals.curEditor().figs();
	    while(figs.hasMoreElements()) 
	    {
		    Fig fff = (Fig) figs.nextElement();
		    
		    if( !(fff instanceof FigPointer) )
		    	varios_locks.add(String.valueOf( fff.getGlobalID() ) );
	    }
		
		
    	// Agora vou pedir todos os locks de uma vez soh!
	    Globals.curEditor().clienteEnvia.lockOK = null;
    	
	    Globals.curEditor().clienteEnvia.EnviaEvento(varios_locks,"PROT_lock_request_group");
    	
	    // Retirado para a experiência
	    // Globals.curEditor().clienteEnvia.texto_lock.setText(Globals.curEditor().clienteEnvia.texto_lock.getText() + "Lock request on several objects\n");
    	
    	boolean resp = Globals.curEditor().clienteEnvia.WaitLock();
    	
    	// Liberando o array de locks interno
    	if(resp)
    	{
			Globals.curEditor().clienteEnvia.AdicionaLockLocal(varios_locks);

			// Agora faz a seleção nos objetos
			
			// Colocando um lock em todos os elementos do diagrama...
			figs = Globals.curEditor().figs();
		    while(figs.hasMoreElements()) 
		    {
		    	Fig fff = (Fig) figs.nextElement();
		    	
		    	if( (fff instanceof FigPointer) ) 
		    		Globals.curEditor().getSelectionManager().toggle(fff);
		    }

			// Agora muda as cor!
		    
			// Colocando um lock em todos os elementos do diagrama...
			figs = Globals.curEditor().figs();
		    while(figs.hasMoreElements()) 
		    {
		    	Fig fff = (Fig) figs.nextElement();
		    	
		    	if( !(fff instanceof FigPointer) )
		    	{
    		    	if(fff instanceof FigEdge )
    		    		fff.setLineColor(Color.GREEN);
    		    	else
    		    	{
    		        	fff.setFillColor(Color.GREEN);
    		         	fff.setFilled(true);
    		    	}

    		    	fff .endTrans();
		    	}
		    }
			
    	}

    	
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
    

    public void LogaMsg(String msg) 
    {
		// Logando 
		try
		{ 
			java.util.Date now = new java.util.Date();  

			String myString = java.text.DateFormat.getDateInstance().format(now); 
			String myString1 = java.text.DateFormat.getTimeInstance().format(now); 

//			Logando a mensagem de lock
			
			this.ClienteMudaLock.write(myString + "-" + myString1+ " " +msg+"\n\n");
			this.ClienteMudaLock.flush();
		}
		catch (Exception ee) 
		{
			ee.printStackTrace();
		} 
    }

} /* end class TabLck */
