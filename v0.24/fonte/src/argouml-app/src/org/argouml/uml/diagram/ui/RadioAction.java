// $Id: RadioAction.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

// Created on 20 July 2003, 02:12

package org.argouml.uml.diagram.ui;

import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.kernel.ProjectManager;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.toolbar.toolbutton.AbstractButtonAction;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.tigris.gef.base.Diagram;

import java.util.Enumeration;
import java.util.Vector;
import java.util.*;



/**
 * A wrapper around a standard action to indicate that any buttons created
 * from this actions should act like radio buttons, i.e. that when the
 * toolbar buttons are double-clicked, they remain active, and every click
 * on the diagram will place a new modelelement.
 *
 * @author Bob Tarling
 */
public class RadioAction extends AbstractButtonAction {

    private Action realAction;

    /**
     * @param action the action
     */
    public RadioAction(Action action) {
        super((String) action.getValue(Action.NAME),(Icon) action.getValue(Action.SMALL_ICON));
        
        putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));
        realAction = action;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) 
    {
        
        try 
        {
			    	UMLDiagram diagram = (UMLDiagram)
			            ProjectManager.getManager().getCurrentProject().getActiveDiagram();
			    	
			        if (Globals.getSticky() && diagram.getSelectedAction() == this) 
			        {
			            // If the user selects an Action that is already selected in sticky
			            // mode (double clicked) then we turn off sticky mode and make sure
			            // no action is selected.
			            Globals.setSticky(false);
			            diagram.deselectAllTools();
			            Editor ce = Globals.curEditor();
			            if (ce != null) 
			            {
			                ce.finishMode();
			            }
			            return;
			        }
			        
			        super.actionPerformed(actionEvent);
			        realAction.actionPerformed(actionEvent);
			        diagram.setSelectedAction(this);
			        Globals.setSticky(isDoubleClick());
			        
			        // System.out.println("Ação Enviada:" + this.getAction().getValue(Action.SHORT_DESCRIPTION).toString());
			        
			        // COLABORAÇÃO
		            ProjectManager.getManager().clienteEnvia.AcaoAnterior = this.getAction().getValue(Action.SHORT_DESCRIPTION).toString();
		            ProjectManager.getManager().clienteEnvia.EnviaEventoAtraso(actionEvent,"SEL_" + this.getAction().getValue(Action.SHORT_DESCRIPTION).toString());
		   			        
			        if (!isDoubleClick()) 
			        {
			            Editor ce = Globals.curEditor();
			            if (ce != null) 
			            {
			                ce.finishMode();
			            }
			        }

			        /* 
			        Enumeration e = Globals.curEditor().getModeManager().getModes().elements();
			        while(e.hasMoreElements()) 
			        {
			        	System.out.println("Modo:" + e.nextElement());
			        } */
        }
        catch (Exception e) 
        {
        	System.out.println("Erro!");
        	e.printStackTrace();
         }

    }

    
    public void actionPerformedImpl(java.awt.event.ActionEvent actionEvent,boolean pinta) {
        // A linha de baixo so chama a superclasse sem grandes novidades
    	
    	// System.out.println("Ação Recebida:" + this.getAction().getValue(Action.SHORT_DESCRIPTION).toString());
    	
        /* Enumeration e = Globals.curEditor().getModeManager().getModes().elements();
        while(e.hasMoreElements()) 
        {
        	System.out.println("Modo:" + e.nextElement());
        } */

    	
    	// TODO Change this to ArgoDiagram
        UMLDiagram diagram = (UMLDiagram)ProjectManager.getManager().getCurrentProject().getActiveDiagram();
            
    	super.actionPerformed(actionEvent);
        
    	// A chamada abaixo que vai, eventualmente, criar um novo mode no editor
    	
    	// System.out.println("Mudando modes por:" + realAction);
    	
    	realAction.actionPerformed(actionEvent);
        
        // Aqui muda-se a cor do botão!
        
        //  if(pinta)
       diagram.setSelectedAction(this);
        
        /* System.out.println("Chamou RadioAction!");
        
        // Testes para ver se eu consigo 'apenas' mudar o status do botão na
        // barra de ferramentas
        diagram.NotificaTool(this); */
         
        Globals.setSticky(isDoubleClick());
        
        // As linhas abaixo fazem com que o modo atual seja finalizado
        // e um novo mode seja habilitado. Este novo estado permite a criação 
        // de elementos!
        if (!isDoubleClick()) {
            Editor ce = Globals.curEditor();
            if (ce != null) {
                ce.finishMode();
            }
        }
        
        /* 
        Enumeration e = Globals.curEditor().getModeManager().getModes().elements();
        while(e.hasMoreElements()) 
        {
        	System.out.println("Modo:" + e.nextElement());
        } */
        
    }

    
    /**
     * @return the action
     */
    public Action getAction() {
        return realAction;
    }
}
