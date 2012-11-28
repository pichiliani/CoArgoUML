// $Id: Wizard.java,v 1.7 2004/03/06 21:16:21 mvw Exp $
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: Wizard.java
// Classes: Wizard
// Original Author: jrobbins@ics.uci.edu
// $Id: Wizard.java,v 1.7 2004/03/06 21:16:21 mvw Exp $

package org.argouml.kernel;

import javax.swing.JOptionPane;

import org.argouml.uml.diagram.ui.UMLDiagram;

// TODO: PENSAR EM UTILIZAR a classe ActionDeleteModelElements.java  no LUGAR!
// import org.argouml.uml.ui.ActionRemoveFromModel;

import org.argouml.uml.diagram.use_case.ui.*;
import org.tigris.gef.base.Editor;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPointer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.util.Util;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.graph.GraphNodeHooks;
import org.tigris.toolbar.toolbutton.AbstractButtonAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;
import javax.swing.Action;
import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.Main;

import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.ArgoDiagram;

import org.argouml.util.FileConstants;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// MAURO: REMOVIDO PARA A V 0.24
// import org.argouml.ui.Actions;
import org.argouml.ui.*;
import org.argouml.kernel.*;

import org.argouml.uml.diagram.ui.*; //Estes imports são úteis para a colaboração!
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;

import java.util.Collection; // import org.argouml.xml.pgml.PGMLParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.argouml.model.Model;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.TabCht;

public class ClienteRecebe extends Thread {
	private Socket socket;
	private ObjectInputStream is;

	// private Editor e;

	public ClienteRecebe(Socket s, ObjectInputStream i) {
		this.socket = s;
		this.is = i;
	}

	public void run() {

		try {

			boolean clientTalking = true;

			// a loop that reads from and writes to the socket
			while (clientTalking) {
				// get what client wants to say...
				Object clientObject = this.is.readObject();

				ArrayList list = (ArrayList) clientObject;

				Object o = list.get(0);
				String nomeEvento = (String) list.get(1);

				if (nomeEvento.equals("PROT_atualiza_modelo_cliente")) {

					// System.out.println("Protocolo: PROT_atualiza_modelo_cliente");
					
					ArgoDiagram d_atual = (ArgoDiagram) ProjectManager
							.getManager().getCurrentProject()
							.getActiveDiagram();
					// Aqui vou fazer uma varredura do diagrama atual

					// MostraDiagrama(d_atual,"Diagrama Atual");

					ArrayList lista = ((ArrayList) o);

					// System.out.println("XMI:" + (String) lista.get(0));
					// Faz o parser dos dados no formato XMI
					parseXMI((String) lista.get(0));
					ArgoDiagram d_recebido = parsePGML((String) lista.get(1));

					// Seta o diagrama ativo, para evitar problemas
					ProjectManager.getManager().getCurrentProject()
							.setActiveDiagram(d_atual);

					if (d_recebido != null) {

						// MostraDiagrama(d_recebido,"Diagrama Recebido");

						// Aqui é feito o 'merge' entre o modelo que se está
						// trabalhando
						// e o modelo que veio pela colaboração

						ArrayList Fs = NovasFigs(d_atual, d_recebido);

						for (int i = 0; i < Fs.size(); i++) {
							// System.out.println("Figura nova!");
							Fig f = (Fig) Fs.get(i);

							// System.out.println("Fig Id: " +
							// f.classNameAndBounds());

							// Aqui chamo a procedure que adiciona a fig no
							// modelo
							AdicionaNoDiagrama(f);
						}

						// A verificação do que foi deleteado esta ok!
						/*
						 * Fs = RemovidasFigs(d_atual,d_recebido);
						 * 
						 * for(int i= 0;i<Fs.size();i++) {
						 * System.out.println("Figura Renovida!"); Fig f = (Fig)
						 * Fs.get(i);
						 * 
						 * System.out.println("Fig Id: " +
						 * f.classNameAndBounds());
						 *  }
						 */

					} else {
						System.out.println("Não pode criar o diagrama!");
					}
				}

				if (nomeEvento.equals("PROT_atualiza_modelo_cliente_inicial")) {
					try {

						int i;

						// Preciso remontar a estrutura. O primeiro passo eh
						// gravar o arquivo em um local e depois carregá-lo

						ArrayList li = ((ArrayList) o);
						// byte[] dados_binarios = new byte[(int) li.get(0) ];

						File f = new File("c:\\teste1.zargo");
						FileOutputStream fos = new FileOutputStream(f);

						fos.write((byte[]) li.get(0));

						fos.close();

						// Agora vou fazer o load deste arquivo
						// URL end = new URL("file:/C:/teste1.zargo");
						URL end = Util.fileToURL(f);

						// OK!  ESTUDAR COM CALMA COMO É O NOVO LOADPROJECT() DA VERSÃO V.204

						// Certo, na versão v.024 é preciso chamar loadProjectWithProgressMonitor(File,boolean)
						// da classe ProjectBrowser. NÃO É MAIS PRECISO CHAMAR O SETCURRENTPROJECT!
						
						ProjectBrowser.getInstance().loadProject(f,false,null);
						
						// Depois de carregado o modelo eu preciso atribuir os
						// ID's globais
						// para cada um dos elementos que existe no modelo

						Object obj = list.get(2);
						ArrayList IdsIniciais = ((ArrayList) obj);

						Editor e = Globals.curEditor();

						Enumeration fs = e.figs();

						while (fs.hasMoreElements()) {

							Fig ff = (Fig) fs.nextElement();

							for (int j = 0; j < IdsIniciais.size(); j++) {
								String dados[] = (String[]) IdsIniciais.get(j);

								// Se achou o elemento coloca o seu ID global
								if (dados[0].equals(ff.classNameAndBounds())) {
									ff.setGlobalID(Integer.parseInt(dados[1]));
								}
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				// Recebeu uma mensagem de chat!
				if (nomeEvento.equals("PROT_chat_msg")) {
					ProjectBrowser.getInstance().getStatusBar()
							.showStatusBlink("Nova mensagem de chat!",
									(Color) list.get(2));

					TabCht t = (TabCht) ProjectBrowser.getInstance()
							.getNamedTab("Chat");
					t.setTexto((String) o, (Color) list.get(2));

				}

				// Recebeu a notificação que algum cliente entrou na da sessão!
				if (nomeEvento.equals("PROT_inicio_sessao")) {
					ProjectBrowser.getInstance().getStatusBar()
							.showStatusBlink(
									"Usuário " + ((String) o)
											+ " entrou na sessão.");
				}

				// Recebeu a notificação que algum cliente saiu da sessão!
				if (nomeEvento.equals("PROT_fim_sessao")) {
					ProjectBrowser.getInstance().getStatusBar()
							.showStatusBlink(
									"Usuário " + ((String) o)
											+ " saiu da sessão.");
				}

				// Esta ação foi removida para a experiência
				/*
				 * if
				 * (nomeEvento.equals("ActionDeleteFromDiagram-actionPerformed"))
				 * ActionDeleteFromDiagram.SINGLETON.actionPerformedImpl((ActionEvent)
				 * o);
				 */

				// TODO: PENSAR EM UTILIZAR a classe ActionDeleteModelElements.java  no LUGAR!
				/* if (nomeEvento.equals("PROT_remove_elemento"))
					ActionRemoveFromModel.SINGLETON.actionPerformedImpl((ArrayList) o); */

				if (nomeEvento.startsWith("SEL_")) {
					nomeEvento = nomeEvento.substring(4, nomeEvento.length());

					SelecionaTool(nomeEvento, o, false);

					// if(!ProjectManager.getManager().clienteEnvia.AcaoAnterior.equals(""))
					// {
					// SelecionaTool(ProjectManager.getManager().clienteEnvia.AcaoAnterior,o,true);
					// }
				}

				if (clientObject == null)
					clientTalking = false;
			}

		} catch (Exception e) {
			// Retirado para evitar mostrar o erro da desconexão!
			// e.printStackTrace();
		}
	}

	private void SelecionaTool(String nome_tool, Object o, boolean pinta) {
		
		// Estas ações abaixo fazem o click na barra de ferramentas!
		UMLDiagram diagram = (UMLDiagram) ProjectManager.getManager().getCurrentProject().getActiveDiagram();

		Object[] a = diagram.getActions();

		// Este loop eh um exemplo de como setar a ferramenta a ser utilizada
		for (int i = 0; i < a.length; i++) 
		{

			if (a[i] == null) {
				Globals.curEditor().liberado = true;
				continue;
			}

			// Caso simples: a Action ja estah no array
			if (a[i] instanceof RadioAction)
			{
				if (((RadioAction) a[i]).getAction().getValue(Action.SHORT_DESCRIPTION).equals(nome_tool))
				{
					((RadioAction) a[i]).actionPerformedImpl((java.awt.event.ActionEvent) o, pinta);
				}
			}

			// Aqui caso tenha clicado em alguma acao que
			// esta dentro de um array de objetos
			if (a[i] instanceof Object[]) 
			{
				Object[] b = (Object[]) a[i];

				for (int j = 0; j < b.length; j++) 
				{
					if (b[j] instanceof Object[]) 
					{
						Object[] c = (Object[]) b[j];

						for (int k = 0; k < c.length; k++) 
						{
							if (c[k] instanceof RadioAction)
								if (((RadioAction) c[k]).getAction().getValue(Action.SHORT_DESCRIPTION).equals(nome_tool))
								{
									((RadioAction) c[k]).actionPerformedImpl((java.awt.event.ActionEvent) o,pinta);
								}
						}
					}
				}
			}
		}

	}

	private void parseXMI(String dia_xmi) {

		// TODO: VERIFICAR O QUE ACONTECEU COM O XML!!!
		/*
		 * try {
		 *  // Lendo o XMI // Estas linhas abaixo são BEM experimentais (mas
		 * funcionam)
		 * 
		 * XMIReader xmiReader = null; try {
		 * 
		 * xmiReader = new XMIReader(); } catch (SAXException se) { throw se; }
		 * catch (ParserConfigurationException pc) { throw pc; }
		 * 
		 * Object mmodel = null;
		 * 
		 * StringBufferInputStream sub_XMI = new
		 * StringBufferInputStream(dia_xmi);
		 * 
		 * InputSource source = new InputSource(sub_XMI);
		 * source.setEncoding("UTF-8");
		 * 
		 * mmodel = xmiReader.parseToModel(source); if (xmiReader.getErrors()) {
		 * 
		 * System.out.println("XMI file could not be parsed."); throw new
		 * SAXException("XMI file could not be parsed."); }
		 * 
		 * if (xmiReader.getErrors()) { System.out.println("XMI file could not
		 * be parsed."); throw new SAXException("XMI file could not be
		 * parsed."); }
		 * 
		 * UmlHelper.getHelper().addListenersToModel(mmodel);
		 * 
		 * HashMap _UUIDRefs = new HashMap(xmiReader.getXMIUUIDToObjectMap());
		 * 
		 * PGMLParser.SINGLETON.setOwnerRegistry(_UUIDRefs);
		 *  } catch (Exception ee) { System.out.println("Erro na leitura do
		 * XMI!"); ee.printStackTrace(); }
		 */

	}

	private ArgoDiagram parsePGML(String dia_pgml) {

		// TODO: VERIFICAR O QUE HOUVE COM O PGML!

		// Lendo o PGML

		// StringBufferInputStream sub = new StringBufferInputStream(dia_pgml);

		// ArgoDiagram da = (ArgoDiagram)
		// PGMLParser.SINGLETON.leDiagram(sub,false);

		// return da;

		return null;
	}

	// O metodo abaixo vai retornar as Fig que existem no diagrama atual
	// mas não existem no diagrama que acabou de chegar
	private ArrayList RemovidasFigs(ArgoDiagram di_atual, ArgoDiagram di_novo) {

		ArrayList ret = new ArrayList();

		ArrayList UID_novo = new ArrayList();

		// Primeiro obter todas os UID's do diagrama novo
		Layer lay = di_novo.getLayer();
		java.util.List nodes = lay.getContents();

		Iterator ite = nodes.iterator();

		while (ite.hasNext()) {
			Fig f = (Fig) ite.next();

			// Ignoro o que for FigPointer
			if (!(f instanceof FigPointer)) {
				UID_novo.add(f.classNameAndBounds());
			}
		}

		// Agora obter as Figs que não existem no diagrama atual
		lay = di_atual.getLayer();
		nodes = lay.getContents();

		ite = nodes.iterator();

		while (ite.hasNext()) {
			Fig f = (Fig) ite.next();

			if (!UID_novo.contains(f.classNameAndBounds())
					&& !(f instanceof FigPointer))
				ret.add(f);
		}

		return ret;

	}

	// O metodo abaixo vai retornar as novas figuras que não existem no diagrama
	// atual
	private ArrayList NovasFigs(ArgoDiagram di_atual, ArgoDiagram di_novo) {
		ArrayList ret = new ArrayList();

		ArrayList UID_atual = new ArrayList();

		// Primeiro obter todas os UID's do diagrama atual
		Layer lay = di_atual.getLayer();
		java.util.List nodes = lay.getContents();

		// Agora vou procurar o elemento e mudar sua cor
		Enumeration ite = Globals.curEditor().figs();

		while (ite.hasMoreElements()) {
			Fig f = (Fig) ite.nextElement();

			// Ignoro o que for FigPointer
			if (!(f instanceof FigPointer)) {
				UID_atual.add(f.classNameAndBounds());
			}
		}

		// Agora obter as Figs do novo diagrama
		lay = di_novo.getLayer();
		nodes = lay.getContents();

		Iterator ite_novo = nodes.iterator();

		while (ite_novo.hasNext()) {
			Fig f = (Fig) ite_novo.next();

			if (!UID_atual.contains(f.classNameAndBounds()))
				ret.add(f);
		}

		return ret;
	}

	private void MostraDiagrama(ArgoDiagram di, String texto) {

		// Vou obter os nós (objetos FigNode)
		Collection NosC = di.getNodes();

		Iterator it = NosC.iterator();

		// System.out.println(texto + " - Nos");
		while (it.hasNext()) {
			// O objeto vai ser o que FigNode.getOwner() retornar
			Object fig = it.next();

			// System.out.println("No - " + fig);

		}

		// MUDANÇA PARA A V.024
		Collection ArcosC = di.getEdges();
		it = ArcosC.iterator();

		// System.out.println(texto + " - Arcos");

		while (it.hasNext()) {
			// O objeto vai ser o que FigNode.getOwner() retornar
			Object fig = it.next();
			System.out.println("Arco - " + fig);
		}

		// Neste loop vou tentar pegar os objetos Fig

		Layer lay = di.getLayer();
		java.util.List nodes = lay.getContents();

		Iterator ite = nodes.iterator();
		System.out.println(texto + " - Fig");

		while (ite.hasNext()) {
			Fig f = (Fig) ite.next();
			// System.out.println("Fig: " + f);

			if (!(f instanceof FigPointer)) {
				System.out.println("Fig Id: " + f.classNameAndBounds());
				// System.out.println("Fig Outro Id: " + f.getId());

				// This will get all the figs on the current diagram
				// Layer lay =
				// Globals.curEditor().getLayerManager().getActiveLayer()
				// List nodes = lay.getContents();

				/*
				 * Este bloco de codigo mostra como obter o elemento atraves de
				 * uma Fig Class nodeClass = f.getOwner().getClass(); Object
				 * modelElement = f.getOwner();
				 *  // You now have the FigNode representing the // use case, do
				 * what you like with it here. }
				 */
			}
		}
	}

	private void AdicionaNoDiagrama(Fig f) {
		// As linhas abaixo adicionam a figura, mas não o elemento no
		// diagrama...
		// Globals.curEditor().add(f);
		// Globals.curEditor().damaged(f);

		Object _node;
		FigNode _pers;

		// GraphFactory _factory;

		// 1 - Editor.mousePressed()
		// Fig.mousePressed()

		// 2 - ModeManager.mouseReleased()
		// Fig.mouseReleased()

		// 3 - ModePlace.mouseClicked()
		// Fig.mouseClicked()

		// _nome é um objeto
		_node = f.getOwner();
		// System.out.println("Novo _node:" + _node );

		// _node = _factory.makeNode();

		// OUtra maneira de criar o _node eh chamar o makenode()

		Editor editor = Globals.curEditor();
		GraphModel gm = editor.getGraphModel();

		GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
		Layer lay = editor.getLayerManager().getActiveLayer();
		// _pers = renderer.getFigNodeFor(gm, lay, _node);

		_pers = (FigNode) f;

		int x = f.getX();
		int y = f.getY();

		editor.damageAll();
		Point snapPt = new Point(x, y);

		editor.snap(snapPt);

		_pers.setLocation(snapPt.x, snapPt.y);

		editor.damageAll();

		// 4 - Editor.mouseReleased()

		// 5- ModeManager.mouseReleased()

		// 6 - ModePlace.mouseReleased()

		gm = editor.getGraphModel();

		MutableGraphModel mgm = (MutableGraphModel) gm;
		if (mgm.canAddNode(_node)) {

			editor.add(_pers);

			// Eh necessario setar a namespace. TODO: RETIRADO PARA TESTES NA
			// NOVA VERSÃO
			// ((MModelElement) _node).setNamespace(null);

			// A linha abaixo vai chamar, para o diagrama de classes
			// O Metodo AddNode da classe ClassDiagramGraphModel
			// (ou outra classe que herde de UMLMutableGraphSupport)
			mgm.addNode(_node);

			Fig encloser = null;
			Rectangle bbox = _pers.getBounds();
			lay = editor.getLayerManager().getActiveLayer();

			// MUDANÇA PARA A V.024
			Collection otherFigs = lay.getContents();

			Iterator it = otherFigs.iterator();
			while (it.hasNext()) {
				Fig otherFig = (Fig) it.next();
				if (!(otherFig instanceof FigNode)) {
					continue;
				}
				if (otherFig.equals(_pers)) {
					continue;
				}
				Rectangle trap = otherFig.getTrapRect();
				if (trap != null
						&& (trap.contains(bbox.x, bbox.y) && trap.contains(
								bbox.x + bbox.width, bbox.y + bbox.height))) {
					encloser = otherFig;
				}
			}

			_pers.setEnclosingFig(encloser);
			if (_node instanceof GraphNodeHooks) {
				((GraphNodeHooks) _node).postPlacement(editor);
			}

			// editor.getSelectionManager().select(_pers);
		}

		editor.setCursor(Cursor.getDefaultCursor());

	}

}
