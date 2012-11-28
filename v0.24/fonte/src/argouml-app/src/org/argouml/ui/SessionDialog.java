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
 * SessionDialog.java
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
import javax.swing.JTable;
import javax.swing.JTextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.TabCht;


import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.*;


import org.argouml.i18n.Translator;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;



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
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ClienteConectaGEF;
import org.tigris.gef.presentation.Fig;

import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ClienteConecta;

import org.argouml.ui.LookAndFeelMgr;
import org.argouml.util.ArgoDialog;
/**
 * Display System Information (JDK Version, JDK Vendor, etc).
 * A Copy to System Clipboard button is provided to help generate bug reports.
 *
 * @author Eugenio Alvarez
 */
public class SessionDialog extends ArgoDialog {

    ////////////////////////////////////////////////////////////////
    // instance varaibles

    JButton     _aplButton = new JButton();

    JLabel _labelsessao	= new JLabel("Nova sessão:");			

    JTextField _textosessao = new JTextField("", 20);
    JTable _table = new JTable(0, 0);
    JLabel _titleLabel;
    JLabel _userLabel;

    public static String PRIORITIES[] = {
    };


    ////////////////////////////////////////////////////////////////
    // constructors

    public SessionDialog() {
	this((Frame) null, false);
    }

    public SessionDialog(Frame owner) {
	this(owner, false);
    }

    public SessionDialog(Frame owner, boolean modal) {
	super("Escolha da sessão", ArgoDialog.CLOSE_OPTION, modal);

	JPanel mainContent = new JPanel();
	mainContent.setLayout(new BorderLayout(15, 15));

    JPanel detailsPanel = new JPanel(new GridBagLayout());

	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(0, 10, 5, 4);
	
	
	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.gridy = 0;
	fieldConstraints.gridx = 1;
	fieldConstraints.gridwidth = 3;
	fieldConstraints.gridheight = 1;
	fieldConstraints.weightx = 1.0;
	fieldConstraints.insets = new Insets(0, 4, 5, 10);

    fieldConstraints.gridy = 0;
	labelConstraints.gridy = 0;

	_userLabel = new JLabel("Usuário: " +ProjectManager.getManager().clienteEnvia.getLogin() );
	
	
	detailsPanel.add(_userLabel, labelConstraints);
	
	labelConstraints.gridy = 1;
    fieldConstraints.gridy = 1;
    
	detailsPanel.add(_labelsessao, labelConstraints);
	detailsPanel.add(_textosessao, fieldConstraints);

	
//	 Aqui vou adiconar os controles!
    ((DefaultTableModel )_table.getModel()).addColumn("Sessão");
    ((DefaultTableModel )_table.getModel()).addColumn("Usuários");
    
    _table.setRowSelectionAllowed(true);
    
    
    // _table.getSelectionModel().addListSelectionListener(this);
    JScrollPane sp = new JScrollPane(_table);
    
    Font labelFont = LookAndFeelMgr.getInstance().getSmallFont();
    _table.setFont(labelFont);
    
    _titleLabel = new JLabel("Sessões atuais:");
    
    
    _table.setPreferredScrollableViewportSize(new Dimension(30, 120));
    
    resizeColumns();
    
    
    _table.doLayout();
    _table.repaint();
    
    fieldConstraints.gridy = 2;
	labelConstraints.gridy = 2;
	
    detailsPanel.add(_titleLabel, labelConstraints);
    detailsPanel.add(sp, fieldConstraints);
    
    // detailsPanel.add(sp,labelConstraints);
    
    _table.doLayout();
    _table.repaint();
    
    // Agora vou adicionar os caras
    Vector totas_se = ProjectManager.getManager().clienteEnvia.listaSessoes;
    
	for (int i = 0; i < totas_se.size(); i++)
	{
		String[] x = (String[]) totas_se.elementAt(i);
		((DefaultTableModel )_table.getModel()).addRow(x);
	} 
    
    
    mainContent.add(detailsPanel, BorderLayout.CENTER);

	// 	// Botão Aplicar(conectar)
	_aplButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	
	    
//	    	 Aqui vou indicar para o servidor qual é a sessão que vai foi escolhida
	    	if ( !_textosessao.getText().equals(""))
	    	{
	    		// Vou obter o modelo do diagrama atual!
	            
			try {
		    		
				ArrayList o;
				ProjectBrowser pb = ProjectBrowser.getInstance();
				
            	o = pb.InitialSend(new File("c:\\teste.zargo"));
				
            	ArrayList l = new ArrayList();
	        	l.add(_textosessao.getText()); 
	        	l.add(o);
	        	
	        	// o terceiro elemento eh um arraylist contendo os Ids globais das Figs
	        	l.add(getIds());
            	
	    		//  Enviando para o argo uma mensagem de 'protocolo'
	    		ProjectManager.getManager().clienteEnvia.EnviaEvento(l,"PROT_nova_sessao"); 
	    		
	    		// Enviando para o GEF uma mensagem de 'protocolo'
	    		Globals.curEditor().clienteEnvia.EnviaEvento(l,"PROT_nova_sessao");
	            	
	            	
	            } catch (Exception erro) {
	            	System.out.println("Algum problema na gravação do arquivo!");
	            } 
	    		
	    		// ArgoDiagram di = (ArgoDiagram) ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	    		// String dia_pgml = ProjectManager.getManager().getCurrentProject().toPGML(di);
	    		
	    		// ArrayList l = new ArrayList();
	        	// l.add(_textonsessao.getText()); 
	        	// l.add(dia_pgml);
	    		
	    		//  Enviando para o argo uma mensagem de 'protocolo'
	    		// ProjectManager.getManager().clienteEnvia.EnviaEvento(l,"PROT_nova_sessao");
	    		
	    		// Enviando para o GEF uma mensagem de 'protocolo'
	    		// Globals.curEditor().clienteEnvia.EnviaEvento(l,"PROT_nova_sessao");
	            
	            // Aqui foi retirar qualquer seleção que possa existir nos elementos
	            // do diagrama atual
	            
	            ////////////////////////////////////LOCK////////////////////////////////////////////	            
	            // Enviando para o servidor um lock clear, para liberar todos os locks
	            
	            Globals.curEditor().getSelectionManager().deselectAll();
	            
	            
	            // Neste Loop todas as figuras vão ser retornadas. Vou passar todas elas para a 
	            // System.out.println("Loop1 - Fig - mouseReleased em ModeSelect:" + f);
	            if(  (Globals.curEditor().clienteEnvia.connected) &&
	       			 (Globals.curEditor().IsCollab==false) )  
	            {
	            	Globals.curEditor().clienteEnvia.EnviaEvento(null,"PROT_lock_clear");
	            }
	            ////////////////////////////////////LOCK////////////////////////////////////////////

	            
	    		hide();
				dispose();

	    	}
	    	else
	    	{
		    	// Aqui vou entrar na sessão escolhida ( e tb validar o usuário
	    		int linha = _table.getSelectedRow();
	    		
	    		if(linha == -1)
	    		{
	    			JOptionPane.showMessageDialog(ProjectBrowser.getInstance(),"Escolha uma sessão colaborativa ou cria uma nova!","Erro de conexão",JOptionPane.ERROR_MESSAGE);
	    			return;
	    		}
		
	    		String nome_sessao = (String) ((DefaultTableModel )_table.getModel()).getValueAt(linha, 0); 
	    		String usuarios = (String) ((DefaultTableModel )_table.getModel()).getValueAt(linha, 1); 
	    		
	    		nome_sessao = nome_sessao.trim();
	    		
	    		if(  usuarios.indexOf(ProjectManager.getManager().clienteEnvia.getLogin()) >= 0)
	    		{
	    				JOptionPane.showMessageDialog(ProjectBrowser.getInstance(),"Login já existe na sessão!","Erro de conexão",JOptionPane.ERROR_MESSAGE);
	    				return;
	    		}
	    		else
	    		{
	    			//	Enviando para o argo uma mensagem de 'protocolo'
		    		ProjectManager.getManager().clienteEnvia.EnviaEvento(nome_sessao,"PROT_sessao_existente");
		    		
		    		// Enviando para o GEF uma mensagem de 'protocolo'
		    		Globals.curEditor().clienteEnvia.EnviaEvento(nome_sessao,"PROT_sessao_existente");
		    		
			    	hide();
					dispose();
	    		}
	    	}
		    			
	    }
	});

    
	//  int getSelectedRow() 
    // Returns the index of the first selected row, -1 if no row is selected. 
	
	nameButton(_aplButton, "Iniciar");    
	addButton(_aplButton, 0);

	// preciso adicionar os controles ao 

	setContent(new JScrollPane(mainContent));

	addWindowListener(new WindowAdapter() {
	    public void windowActivated(WindowEvent e) {
	    } 
	});


      pack();
    } 

    // Obtendo todos os ID's globais dos elementos do diagrama atual
    public ArrayList getIds()
    {
    	ArrayList ret = new ArrayList();
    	
    	Editor e = Globals.curEditor();

    	Enumeration fs = e.figs();
    	
    	int id = e.GeraGID();
    	
		while(fs.hasMoreElements()) 
		{
		    Fig ff = (Fig) fs.nextElement();

		    // Primeiro vou colocar os ID's para depois pegá-los
		    ff.setGlobalID(id);
		    
			String []x = {ff.classNameAndBounds(),String.valueOf(ff.getGlobalID())}; 
			
			ret.add(x);
			
			id++;
		}
		
		return ret;
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
        _table.sizeColumnsToFit(-1);    }

} /* end class SessionDialog */



