// $Id: SystemInfoDialog.java,v 1.8 2004/06/24 06:25:40 linus Exp $
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

/*
 * CollabDialog.java
 */

package org.argouml.ui;

import java.awt.Frame;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ClienteConecta;

import org.argouml.kernel.ClienteConecta;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SelectionManager;

import org.tigris.gef.base.ClienteConectaGEF;
import org.tigris.gef.base.ClienteRecebe;

import javax.swing.JComboBox;


import java.util.Collection;

import java.util.Iterator;
import java.util.Vector;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.TabCht;


import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.*;

import org.argouml.util.ArgoDialog;

import org.argouml.i18n.Translator;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import org.argouml.util.ArgoFrame;

/**
 * Display System Information (JDK Version, JDK Vendor, etc).
 * A Copy to System Clipboard button is provided to help generate bug reports.
 *
 * @author Eugenio Alvarez
 */
public class CollabDialog extends ArgoDialog {

    ////////////////////////////////////////////////////////////////
    // instance varaibles

    JButton     _aplButton = new JButton();

    JLabel _labelservidor	= new JLabel("Servidor:");			
    JLabel _labelporta		= new JLabel("Porta:");			
    JLabel _labeluser		= new JLabel("Usuário:");			
    JLabel _labelpass		= new JLabel("Senha:");			

    JTextField _textoservidor = new JTextField("", 20);
    JTextField _textoporta = new JTextField("", 5);
    JTextField _textouser = new JTextField("", 15);
    JTextField _textopass = new JTextField("", 10);

    public static String PRIORITIES[] = {
    };


    ////////////////////////////////////////////////////////////////
    // constructors

    public CollabDialog() 
    {
    	this((Frame) null, false);
    }

    public CollabDialog(Frame owner) 
    {
    	this(owner, false);
    }

    public CollabDialog(Frame owner, boolean modal)
    {
		super("Configurações de conexão", ArgoDialog.CLOSE_OPTION, modal);
	
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout(20, 20));
	
	    JPanel detailsPanel = new JPanel(new GridBagLayout());
			
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.EAST;
		labelConstraints.gridy = 0;
		labelConstraints.gridx = 0;
		labelConstraints.gridwidth = 1;
		labelConstraints.gridheight = 1;
		labelConstraints.insets = new Insets(0, 10, 5, 4);
	
		GridBagConstraints fieldConstraints = new GridBagConstraints();
		fieldConstraints.anchor = GridBagConstraints.WEST;
		fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		fieldConstraints.gridy = 0;
		fieldConstraints.gridx = 1;
		fieldConstraints.gridwidth = 3;
		fieldConstraints.gridheight = 1;
		fieldConstraints.weightx = 1.0;
		fieldConstraints.insets = new Insets(0, 4, 5, 10);
		
	    if(!ProjectManager.getManager().clienteEnvia.connected)
		{
	
			labelConstraints.gridy = 0;
			fieldConstraints.gridy = 0;
			detailsPanel.add(_labelservidor, labelConstraints);
			detailsPanel.add(_textoservidor, fieldConstraints);
		
		
			labelConstraints.gridy = 1;
			fieldConstraints.gridy = 1;
			detailsPanel.add(_labelporta, labelConstraints);
			detailsPanel.add(_textoporta, fieldConstraints);
		
			labelConstraints.gridy = 2;
			fieldConstraints.gridy = 2;
			detailsPanel.add(_labeluser, labelConstraints);
			detailsPanel.add(_textouser, fieldConstraints);
		
			labelConstraints.gridy = 3;
			fieldConstraints.gridy = 3;
			detailsPanel.add(_labelpass, labelConstraints);
			detailsPanel.add(_textopass, fieldConstraints);
			
			mainContent.add(detailsPanel, BorderLayout.CENTER);
		
			// Botão ok
		
			// Botão Aplicar(conectar)
			_aplButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			
			    
					// Primeiro colocando a mensagem de conexão.
					ProjectBrowser.getInstance().getStatusBar().showStatus("Conectando ArgoUML...");
		
					// Criar a Thread que vai enviar os eventos (colocar esta prog. no main ou como um plug in)
					if ( ProjectManager.getManager().clienteEnvia.SetaConecta(_textoservidor.getText() 
							                                            ,Integer.valueOf(_textoporta.getText()).intValue() ) )
					{
		
						// Agora vou verificar se o login e a senha estão corretos
		
						if( ProjectManager.getManager().clienteEnvia.SetaUser(_textouser.getText(),_textopass.getText() ) )
						{
						
							ProjectManager.getManager().clienteEnvia.start();
						     
							ProjectBrowser.getInstance().getStatusBar().showStatus("Conectando GEF...");
			
			
							// Aqui vou fazer a conexão do argo e depois do GEF com o servidor
							Globals.curEditor().clienteEnvia.SetaConecta(_textoservidor.getText() 
																		,Integer.valueOf(_textoporta.getText()).intValue() 
																		,_textouser.getText()
																		,_textopass.getText() );
			
							Globals.curEditor().clienteEnvia.start(); 
			
							ProjectBrowser.getInstance().getStatusBar().showStatus("Conectado");
			
							// Testes para setar algo na janela de Chat
							TabCht t = (TabCht)	ProjectBrowser.getInstance().getNamedTab("Chat");
							
							t.setTarget("");
							
							// Colocando o nome no painel de locks
							Globals.curEditor().clienteEnvia.setaNomeLocal();
							
							// Criando o objeto que vai manipular o nome dos elementos
							Globals.curEditor().clienteEnvia.canal_nomes = new objNamesImpl();
							
							// Fechando a janela atual
					    	hide();
							dispose();
							
		
							// Aqui vou iniciar a nova janela com as informações para conexão
							SessionDialog sessionDialog;
							
							JFrame jFrame = ArgoFrame.getInstance();;
							// collabDialog = new CollabDialog(jFrame, true);
							sessionDialog = new SessionDialog(jFrame, true);
							
							Dimension siDim = sessionDialog.getSize();
							Dimension pbDim = jFrame.getSize();
		
							if ( siDim.width > pbDim.width / 2 ) {
								sessionDialog.setSize(pbDim.width / 2, siDim.height + 45);
							} else {
								sessionDialog.setSize(siDim.width+80, siDim.height + 100);
							}
		
							sessionDialog.setLocationRelativeTo(jFrame); 
							// sessionDialog.setSize(50, 80);
							sessionDialog.show();
							
						}
					}
				    			
			    }
			});
		
			nameButton(_aplButton, "Conectar");    
			addButton(_aplButton, 0);
	
		}
		else
		{
			_labelservidor.setText("");
			
			labelConstraints.gridy = 0;
			fieldConstraints.gridy = 0;
			detailsPanel.add(_labelservidor, labelConstraints);
			mainContent.add(detailsPanel, BorderLayout.CENTER);
			
			_aplButton.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) 
			    {
//			    	 Aqui vou enviar para o servidor quem deve ser removido dos outros clientes
			        ProjectManager.getManager().clienteEnvia.EnviaEvento(null,"PROT_fim_sessao");
			        
			    	
			    	_labelservidor.setText("Paricipação na sessão finalizada!");
			    	
			    	// Aqui preciso fazer as duas desconexões
			    	
			    	try 
			    	{
				    	ProjectManager.getManager().clienteEnvia.disconnect();
				    	Globals.curEditor().clienteEnvia.disconnect();
			    	}
			        catch (Exception e1) 
					{
			        	// Sem nenum tratamento!
			        	// e1.printStackTrace();
			        }
					
			    }
			});
			   
			nameButton(_aplButton, "Finalizar participação");    
			addButton(_aplButton, 0);
			
		}
		// preciso adicionar os controles ao 
	
		setContent(new JScrollPane(mainContent));
	
		addWindowListener(new WindowAdapter() 
				{
		    		public void windowActivated(WindowEvent e) {
		} 
		});
	
	    pack();
    } 


    private static ClipboardOwner defaultClipboardOwner =
	new ClipboardObserver();

    static class ClipboardObserver implements ClipboardOwner {
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
    }

} /* end class CollabDialog */



