package org.argouml.kernel;

import org.argouml.model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.application.Main;

import org.argouml.uml.diagram.ArgoDiagram;

import org.argouml.util.FileConstants;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SelectionManager;

import org.tigris.gef.base.ClienteConectaGEF;
import org.tigris.gef.base.objectNames;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPointer;
import org.tigris.gef.presentation.Handle;

//Estes imports são úteis para a colaboração!
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Point;


public class objNamesImpl extends objectNames 
{
	private ArrayList ele_atu = new ArrayList();
	
	public objNamesImpl() 
	{

	}

	
	// Vou retornar um arraylist com os id's e os nomes do elementos
	public ArrayList getNames()
	{
    	ArrayList ret = new ArrayList();
    	
		Editor e = Globals.curEditor();

    	Enumeration figs = e.figs();
    	
    	String nome = "";
    	String xi = "";
    	String yi = "";
    	String wi = "";
    	String hi = "";
    	
		while(figs.hasMoreElements()) 
		{
			    Fig f = (Fig) figs.nextElement();
			    
			    // Não faz sentido levar os FigPointer
			    if( f instanceof FigPointer )
			    	continue;
		    	
			    /* Inicialmente obtendo o nome e atributos */
            	if ( f instanceof FigNodeModelElement )
            	{
            		nome = ((FigNodeModelElement) f).getName();
            		
            		xi = String.valueOf(f.getX());
     			    yi = String.valueOf(f.getY());
     			    wi = String.valueOf(f.getWidth());
     			    hi = String.valueOf(f.getHeight());
            	}
            	
            	if ( f instanceof FigEdgeModelElement )
            	{
            		nome = ((FigEdgeModelElement) f).getName();
            		
            		FigEdgeModelElement ff = (FigEdgeModelElement) f;
            		
            		xi = String.valueOf(  ff.getFirstPoint().x);
     			    yi = String.valueOf(  ff.getFirstPoint().y);
     			    wi =  String.valueOf( ff.getLastPoint().x);
     			    hi = String.valueOf( ff.getLastPoint().y);
            		
            	}
            	
            	if( f.getOwner() != null )
            	{
            		if ( nome.equals("") && Model.getFacade().getName(f.getOwner()) != null ) 
            			nome = Model.getFacade().getName(f.getOwner());
            	}
            	
            	
            	
            	
            	
        		String []x = {f.classNameAndBounds() // ID 'novo'
         			   ,String.valueOf(f.getGlobalID()) // ID 'antigo' 
         			   ,nome
         			   ,xi
         			   ,yi
         			   ,wi
         			   ,hi};
         	
        			ret.add(x);
         	
         }

		
	  /*  for(int j=0;j<ret.size();j++)
	    {
	    	String dados[] = (String []) ret.get(j);

			System.out.println(" Enviando ClassNameAndBounds:" + dados[0]);
			System.out.println(" Enviando ID:" + dados[1]);
			System.out.println(" Enviando Nome:" + dados[2]);
			
			System.out.println(" Enviando xi:" + dados[3]);
			System.out.println(" Enviando yi:" + dados[4]);
			
			System.out.println(" Enviando wi:" + dados[5]);
			System.out.println(" Enviando hi:" + dados[6]);
			System.out.println("");
			
	    }*/

		
		
		return ret;
	}
	
	// Vou receber um arraylist com os id's e os nome dos elementos
	// e vou setá-los
	public void setNames(ArrayList figs)
	{
		
		
		Editor e = Globals.curEditor();

    	Enumeration fs = e.figs();
    	
		while(fs.hasMoreElements()) 
		{
			    Fig f = (Fig) fs.nextElement();

			    // Se for um FigPointer esquece
			    if( f instanceof FigPointer )
			    	continue;

			    // Se a cor estiver verde quer dizer que está em lock e naum mexo nele!
			    Color c;

			    if(f instanceof FigEdge) 
			  		c = f.getLineColor();
			  	else
			  		c = f.getFillColor();
			  		
			   if( c.equals(Color.green) || c.equals(Color.red) )
			    	continue;
			   
			   // Caso o elemento ainda tenha o lock de outra pessoa não mexo nele!
			   if ( Globals.curEditor().clienteEnvia.ExisteLockLocalUser( String.valueOf(f.getGlobalID()) ) )
			    	continue;
			   
			    
			    for(int j=0;j<figs.size();j++)
			    {
			    	String dados[] = (String []) figs.get(j);

					// System.out.println("Pesquisando:" + f.classNameAndBounds());
					
					// System.out.println("Comparando:" + dados[1] + " com " + String.valueOf(f.getGlobalID()));
					// System.out.println("Resultado:" + dados[1].equals(String.valueOf(f.getGlobalID())) );
			    	
			    	if(dados[1].equals(String.valueOf(f.getGlobalID())))
			    	{
			    		if(f instanceof FigNodeModelElement)
				    	{
			    			 // System.out.println("Atualizando:" + f.classNameAndBounds());
			    			
			    			
			    			// Antes de ir setando os valores vou verificar se houve mudança!
			    			if( (f.getX() != Integer.valueOf(dados[3]).intValue())      ||  
			    				(f.getY() != Integer.valueOf(dados[4]).intValue())      ||
			    				(f.getWidth() != Integer.valueOf(dados[5]).intValue())  ||
			    				(f.getHeight() != Integer.valueOf(dados[6]).intValue()) ||
			    				!((FigNodeModelElement) f).getName().equals(dados[2]) 
			    			)
			    			{
				    			
			    				// System.out.println("Atualizando:" + f.classNameAndBounds());
			    				
			    				// System.out.println("Antigo X:" + f.getX() + " Antigo Y" + f.getY());
				    			// System.out.println("Novo X:" + Integer.valueOf(dados[3]).intValue() + " Novo Y" + Integer.valueOf(dados[4]).intValue());  
				    			
			    				
			    				f.setX(Integer.valueOf(dados[3]).intValue());
					    		f.setY(Integer.valueOf(dados[4]).intValue());
					    		
					    		f.setWidth(Integer.valueOf(dados[5]).intValue());
					    		f.setHeight(Integer.valueOf(dados[6]).intValue());
				    			
				    			((FigNodeModelElement) f).setName(dados[2]);
				    			
			    				((FigNodeModelElement) f).damage();
			    				((FigNodeModelElement) f).redraw();
			    			}
				    		
				    		
				    	}
			    		
				    	if(f instanceof FigEdgeModelElement)
				    	{
				    		
				    		// Antes de ir setando os valores vou verificar se houve mudança!
				    		if(  !((FigEdgeModelElement) f).getName().equals(dados[2]) ||
				    		     (f.getFirstPoint().x != Integer.valueOf(dados[3]).intValue() ) || 		
				    			 (f.getFirstPoint().y != Integer.valueOf(dados[4]).intValue() ) ||
				    			 (f.getLastPoint().x  != Integer.valueOf(dados[5]).intValue() ) ||
				    			 (f.getLastPoint().y  != Integer.valueOf(dados[6]).intValue() ) 
				    		)
				    		{
				    			
				    			// System.out.println("Atualizando:" + f.classNameAndBounds());
				    			
				    			((FigEdgeModelElement) f).setName(dados[2]);
					    		
					    		((FigEdgeModelElement) f).setEndPoints(new Point(Integer.valueOf(dados[3]).intValue(),Integer.valueOf(dados[4]).intValue())
					    				                              ,new Point(Integer.valueOf(dados[5]).intValue(),Integer.valueOf(dados[6]).intValue()));
					    		
					    		((FigEdgeModelElement) f).damage();
					    		
					    		((FigEdgeModelElement) f).redraw();
				    		}
				    		
				    		
				    	} 
			    	}
			    }
		} 
		return;
	}
	
//	 Vou armazenar o 'nome' e os Id's dos elementos
/*
	
	public void gravaIDs()
	{
		Editor e = Globals.curEditor();

    	Enumeration figs = e.figs();
    	
    	// Aqui tenho que tomar cuidado! Se a fig. existir no array e estiver  em lock naum tira!
    	ArrayList ele_atu_back = new ArrayList();
    	
    	ele_atu_back = (ArrayList) ele_atu.clone();
    	
    	ele_atu.clear();
    	
    	boolean entrou = false;
    	
		while(figs.hasMoreElements()) 
		{
			    
				Fig f = (Fig) figs.nextElement();


			    
			    entrou = false;
		    	for(int i = 0;i<ele_atu_back.size();i++)
				{
					String []y = (String []) ele_atu_back.get(i);
					
					if(   y[1].equals(f.getId()) &&  !Globals.curEditor().clienteEnvia.ExisteLockLocal(y[0]) )
					{
							// Aqui eh preciso de mais alguma checagem antes de mudar o 'ID' interno
						
							String []n = {y[0], y[1]};
					    	ele_atu.add(n);
					    	entrou = true;
				    	
					}
				}

		    	if (!entrou)
		    	{
		    		String []n = {f.classNameAndBounds(), f.getId()};
		    		ele_atu.add(n);
		    	}
		}
		
		return;
	}
	
//	 Esta procedure vai receber o UID de um elemento e retornar seu 'nome' antigo
	public String obtemID(String UID)
	{
		
		String old = "";
		
		for(int i = 0;i<ele_atu.size();i++)
		{
			String []y = (String []) ele_atu.get(i);
			
			old = y[1]; 
			
			if(old.equals(UID))
				return y[0];
		}
		
		return "";
	} */
	
	public String getUID(Object o)
	{
		return Model.getFacade().getUUID(o); 
	}
	
	public void EnviaAtraso()
	{
		ProjectManager.getManager().clienteEnvia.EnviaAtraso();
	}
	
  public void AvisaDeny(Color c)
  {
    ProjectBrowser.getInstance().getStatusBar().showStatusBlink("Não foi possível conseguir o lock!",c);
  }

	
}
