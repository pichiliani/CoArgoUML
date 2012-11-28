import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*; 

public class CollabServer 
{
	public Vector clientVector;         // vector of currently connected clients
	private int id_telepointer = 1;
	private Vector telepointers = new Vector();
	private ArrayList userList = new ArrayList(); // Lista contendo os usuários e senhas
	
	public FileWriter LogChat;
	public FileWriter LogLock;
	public FileWriter LogJanelaLock;
	
	public void CollabServer(){
		
	}
	
	public void IniciaServico(int port) throws IOException {
		// Abre o servidor na porta escolhida
        ServerSocket ss = new  ServerSocket(port);
		System.out.println("Collaboration server listening on port " + port +".CTRL+C to finish.") ;

		// Iniciando a lista de usuarios e senhas

    	String []x = {"A" ,"A"};
    	userList.add (x);

    	String []y = {"B" ,"B"};
		userList.add (y);

		// Configurando os arquivos de logs
		try
		{ 
			// LogChat = new FileWriter("c:\\LogChat.txt",true);
			// LogLock = new FileWriter("c:\\LogLock.txt",true);
			// LogJanelaLock = new FileWriter("c:\\LogJanelaLock.txt",true);

			LogaMsg("Chat Log Started",0);
			LogaMsg("Lock Log Started",1);
			LogaMsg("JanelaLock Log Started",2);
			
			clientVector = new Vector(); 
			CollabServerT c;
			// A cada nova requisão uma nova Thread é criada
			while (true)
			{
				c = new CollabServerT (ss.accept(),this);
				c.start();
				
				// clientVector.addElement(c);
			}
			
		}
		catch (Exception ex)
		{
        		ex.printStackTrace();
		}
		
	}

	
	public static void main (String args[] ) 
	{

		// Checagens dos parâmetros
		if(args.length!=1)
			System.out.println("Uso: CollabServer port");
		
		int port = 0;

		try
		{ 
			port =	Integer.valueOf(args[0]).intValue();


		}
		catch (NumberFormatException ex)
		{
		   System.out.println("Invalid port number");  
		}

	
		try
		{ 
			CollabServer s = new CollabServer();
			s.IniciaServico(port);
		}
		catch (IOException ex)
		{
			   System.out.println("Servidor finalizado!" );
		}
		
	}
	
	public void RemoveClient (CollabServerT singleThread) {
        	clientVector.removeElement(singleThread);
    }
	
	
    public void BroadCastToAll (Object objrecebido, CollabServerT tEnviada, boolean skipItself) 	
    {
    	this.BroadCastToAll(objrecebido,tEnviada,skipItself,tEnviada.tipocon);
    }
    	
    public void BroadCastToAll (Object objrecebido, CollabServerT tEnviada, boolean skipItself,String tipocon) 	{
        ArrayList list = (ArrayList) objrecebido;

        Object eventoMouse = list.get(0);
        String nomeEvento = (String)list.get(1);
        
        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            
            // System.out.println("T:" + aSingleThread.tipocon + " S: " + aSingleThread.nome_sessao);
            
            
            // Modificação do olho: enviando para qualquer sessão!
            /*
            if(skipItself)
            {
            	if( (!aSingleThread.equals(tEnviada)) && 
                	(aSingleThread.tipocon.equals(tipocon)) && 
                	(aSingleThread.nome_sessao.equals(tEnviada.nome_sessao))  )
            	{
                
            		// System.out.println("Evento enviado!");
                	aSingleThread.BroadCastToClient(list);
            	}
            }
            else
            {
                if( 	(aSingleThread.tipocon.equals(tipocon)) && 
	                	(aSingleThread.nome_sessao.equals(tEnviada.nome_sessao))  )
            	
                		aSingleThread.BroadCastToClient(list);
            	
            } */
           
           //   NOVA MODIFICACAO - olho
            if(skipItself)
            {
            	if( (!aSingleThread.equals(tEnviada)) && 
                	(aSingleThread.tipocon.equals(tipocon))   )
            	{
                
            		// System.out.println("Evento enviado!");
                	aSingleThread.BroadCastToClient(list);
            	}
            }
            else
            {
                if( 	(aSingleThread.tipocon.equals(tipocon))   )
            	
                		aSingleThread.BroadCastToClient(list);
            	
            }                  
        }
	}

    
    
    
    public boolean ExisteSessao (String nomesessao) {
        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if( aSingleThread.nome_sessao.equals(nomesessao))
            	return true;
        }
        return false;
    }
    
    
    // Obtem o modelo atual da sessão de algum dos participantes
    public Object ModeloSessao (String nomesessao) {
        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if( aSingleThread.nome_sessao.equals(nomesessao))
            	return  aSingleThread.modelo_atual;
        }
        return null;
    }

    // Obtem o modelo atual da sessão de algum dos participantes
    public ArrayList ModeloSessaoInicial (String nomesessao) {
        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if( aSingleThread.nome_sessao.equals(nomesessao))
            	return  aSingleThread.modelo_inicial;
        }
        return null;
    }
    
    public ArrayList IdsSessaoInicial (String nomesessao) {
        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if( aSingleThread.nome_sessao.equals(nomesessao))
            	return  aSingleThread.ids_inicial;
        }
        return null;
    }
    
    // retorna todas os nomes da sesões colaborativas atuais
    public Vector NomeSessoes () {
        CollabServerT aSingleThread = null;
        Vector ret_aux = new Vector();
        Vector retorno  = new Vector();
        String nome_sessao = "";
        String user_sessao = "";
        
        // Neste primeiro loop eu objenho todos os nomes das sessoes
        for (int i = 0; i < clientVector.size(); i++) 
        {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if(  !ret_aux.contains(aSingleThread.nome_sessao) && !aSingleThread.nome_sessao.equals("")  )
            {
            	ret_aux.addElement(aSingleThread.nome_sessao);
            }
        }
        
        // Aqui objeto os usuarios de cada sessao
        for (int i = 0; i < ret_aux.size(); i++) 
        {
        	    nome_sessao = (String) ret_aux.get(i);
				user_sessao = "";  
				
                for (int j = 0; j < clientVector.size(); j++) 
                {
                    aSingleThread = (CollabServerT) clientVector.elementAt(j);
                    
                    if(aSingleThread.nome_sessao.equals(nome_sessao) && user_sessao.indexOf(aSingleThread.login) == -1 )
                    {
                    	user_sessao = user_sessao  + ";" + aSingleThread.login;     	
                    }
                }
        	    String []inserir = { nome_sessao, user_sessao.substring(1,user_sessao.length()) };
        	    
        	    retorno.add(inserir);    
        }
        
        return retorno;
    }
    
    // CheckLock verifica se existe um lock na fig e pode remover este lock
    
    public boolean CheckLock (String fig) 
    {
    	return this.CheckLock(fig,false);
    }
    
    public boolean CheckLock (String fig,boolean remove) 
    {
        CollabServerT aSingleThread = null;

        for (int i = 0; i < clientVector.size(); i++) {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            if( aSingleThread.resources_locked.contains(fig) )
            {
            	if(remove)
            		aSingleThread.resources_locked.remove(fig);	

            	return true;
            }
        }
        return false;
    }
    
    public ArrayList getLockStatus () {
        
    	ArrayList ret = new ArrayList();
    	CollabServerT aSingleThread = null;
        Vector r;
        
        String logaJanela = "";

        for (int i = 0; i < clientVector.size(); i++) 
        {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);

            r = aSingleThread.resources_locked;
            
            for (int j = 0; j < r.size(); j++) 
            {
            	// ret.add((String)  r.elementAt(j) );
            	
            	logaJanela = logaJanela + " usuario:" + aSingleThread.login + " - objeto: "  + (String)  r.elementAt(j) + ","; 
            	
            	String []x = {aSingleThread.login ,(String)  r.elementAt(j)};
            	
            	ret.add(x);
            	
            }
        }
        
        // Toda a vez que solicitarem o status global dos locks vou 
        // logar em um arquivo separado
        
        this.LogaMsg(logaJanela,2);
        
        return ret; 
    }

    public String getPointerID()
    {

    	String x = "1";
    	
    	for (int i = 0; i < 4; i++) 
    	{
    		id_telepointer = i+1;
    		if(!telepointers.contains(String.valueOf(id_telepointer)) )
    		{
    			x = String.valueOf(id_telepointer);
    			
    			telepointers.addElement(x); 
    			
    			return x;
    		}
    	}
    	
    	return x;
    }

    
    public Color getColor(String id)
    {
    	
    	// Aqui vou especificar as colores e os ID's dos telepointers
    	Color fColor = Color.cyan;
		if (id.equals("1")) 
			fColor = Color.red; 
		if (id.equals("2")) 
			fColor = Color.blue;
		if (id.equals("3")) 
			fColor = Color.yellow;
		if (id.equals("4")) 
			fColor = Color.orange;
		
		return fColor;
    }
    
    // Procurar a cor do telepointer do usuário
    public Color getEyeColor(String user)
    {
    	Color fColor = Color.red;

        CollabServerT aSingleThread = null;
        
        for (int i = 0; i < clientVector.size(); i++) 
        {
            aSingleThread = (CollabServerT) clientVector.elementAt(i);
            
            if( user.equals(aSingleThread.login) && aSingleThread.tipocon.equals("ARGO") )
            	fColor = this.getColor(aSingleThread.id_pointer); 
        }
            
    	return fColor;
    }
    
    
    public void RemovePointer(String id)
    {
    		if(telepointers.contains(id) )
    			telepointers.removeElement(id);
    		
    		return;
    }

    // Verifica se o usuário/senha existem
    public boolean checkUser(String l,String s)
    {
        for (int i = 0; i < userList.size(); i++) 
        {
            String user[] = (String []) userList.get(i);
            
            if(user[0].equals(l) && user[1].equals(s))
            	return true;
        }
        
        return false;
    }

    public void LogaMsg(String msg,int tipo) // 0 -> Chat, 1->Lock, 2->Janela de lock
    {
		// Logando o que foi falado no Chat...
		try
		{ 
			java.util.Date now = new java.util.Date();  

			String myString = java.text.DateFormat.getDateInstance().format(now); 
			String myString1 = java.text.DateFormat.getTimeInstance().format(now); 

			if(tipo==0)
			{
				// Logando a mensagem de chat
				this.LogChat.write(myString + "-" + myString1+ " " +msg+"\n\n");
				this.LogChat.flush();
			}
			
			
			if(tipo==1)
			{
//				 Logando a mensagem de lock
				this.LogLock.write(myString + "-" + myString1+ " " +msg+"\n\n");
				this.LogLock.flush();
			}
			if(tipo==2)
			{
//				 Logando a mensagem da janela de lock
				this.LogJanelaLock.write(myString + "-" + myString1+ " " +msg+"\n\n");
				this.LogJanelaLock.flush();
			}
		}
		catch (Exception ee) 
		{
			ee.printStackTrace();
		} 
    }
    
    
}

class CollabServerT extends Thread 
{
	Socket client;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    
    private InputStreamReader isEYE;
    private OutputStreamWriter osEYE;
    
    private String CON = "OBJ";
    
    private CollabServer servidor;
    public String id_pointer;
    public String tipocon; 
    public String login;
    public String senha;
    public String nome_sessao = "";
    public Object modelo_atual;
    public ArrayList modelo_inicial = new ArrayList();
    public ArrayList ids_inicial = new ArrayList();
    
    public Vector resources_locked = new Vector();
	
	// Construtor da classe
	CollabServerT (Socket client,CollabServer s) throws SocketException 
	{

		// Preenche os campos e seta a priopridade desta Thread
		this.client = client;
		this.servidor = s;
		setPriority( NORM_PRIORITY - 1);
		this.tipocon = "";


	}

	public void run() 
	{
        try {
            this.connect();
            boolean clientTalking = true;
            
            Object clientObject = null;
            
            //a loop that reads from and writes to the socket
            while (clientTalking) {
                
            	if(CON == "OBJ") // Caso conexão ARGO ou GEF
            	{
            		// get what client wants to say...
            		clientObject = is.readObject();
            	}
            	else  // Caso conexão EYE
            	{
            		
            		BufferedReader inBR = new BufferedReader(isEYE); 
                    
                    String text = inBR.readLine(); 
                    
                    if ( (inBR == null) || (text==null)) 
                        clientTalking = false;
                    else
                    {
                    	// System.out.println("Texto recebido:" + text);
                        
                    	// O EyeServer vai receber somente dois tipos de mensagem:
                        // A inicial: EYE. Formato: EYE,<login>,<senha>
                        // A movimentação do olho: PROT_EYE. Formato: PROT_EYE,<ID>,<login>,<pos_x>,<pos_y>
                        
                        // Eh preciso quebrar a mensagem de texto e transformá-la em um objeto
                        // para o método trataProtocolo()
                    	clientObject = parseEye(text);
                    }
            		
            	}
        
                // System.out.println("Servidor recebeu algum objeto!");
                
                if(!this.trataProtocolo(clientObject))
                {
	                // if client dissappeared.. 
	                if (clientObject == null) 
	                    clientTalking = false;
	                 else 
	                 {
	                 	// Preciso mandar o objeto somente para os clientes
	                 	// que participarem da mesma sessão
	                 	this.servidor.BroadCastToAll(clientObject, this, true);
	                 }
               	}
                
            }
            
        } catch (Exception e) {
        	// System.out.println("Conexão fechada pelo cliente.");
        	e.printStackTrace();
        }
        this.disconnect();
	}

	// Tranforma em objeto o que foi enviado em texto
	private Object parseEye(String text)
	{
		Object clientObject;
		String msg_protocolo = "";
		
		// Podem haver dois tipos de mensagem: EYE e PROT
		
		ArrayList l = new ArrayList();
		ArrayList dados = new ArrayList();
		
		 StringTokenizer st = new StringTokenizer(text,",");
	     while (st.hasMoreTokens()) {
	         l.add(st.nextToken());
	     }
	 
	    // Adequacao ao protocolo! 
	    msg_protocolo = (String) l.get(0); 
		l.remove(0);
	    
	    dados.add(l);
		dados.add(msg_protocolo);
		
		clientObject = (Object) dados;
		return clientObject;
	}

	// Trata as mensagens do protocolo
	private boolean trataProtocolo (Object clientObject) 
	{

        ArrayList list = (ArrayList) clientObject;

        Object objDados = list.get(0);
        String nomeEvento = (String)list.get(1);
        
        
        // Aqui são verificadas as mensagens iniciais, para identificar qual é a conexão
        if (  (nomeEvento.equals("GEF")) || (nomeEvento.equals("ARGO")) || (nomeEvento.equals("E")) )
        {
        	ArrayList l = (ArrayList) objDados;
        	
        	
        	if ( this.servidor.checkUser(l.get(0).toString(),l.get(1).toString())  )
        	{
        		this.tipocon = nomeEvento;
        		this.login = l.get(0).toString();
        		this.senha = l.get(1).toString();
        		 // System.out.println("Conexão autorizada!");
        		
        		// Adicionando ao array de clientes
        		this.servidor.clientVector.addElement(this);
    			
        		if(nomeEvento.equals("E"))
	        	{
        			this.id_pointer = this.servidor.getPointerID(); 
	        	}
        		
        		// Daqui para baixo o envio de dados eh apenas 
        		// para conexoes do ARGO e do GEF
        		if (  (nomeEvento.equals("GEF")) || (nomeEvento.equals("ARGO"))  )
        		{		
	        		// Enviar para o cliente o nome das sessões!
	        		l = new ArrayList();
		        	l.add(this.servidor.NomeSessoes()); 
		        	l.add("PROT_lista_sessoes");
	
		        	if(nomeEvento.equals("ARGO"))
		        	{
			        	// Aqui vou colocar uma cor para o telepointer do cliente
		        		this.id_pointer = this.servidor.getPointerID(); 
			        	l.add(this.id_pointer);
			        	l.add(this.servidor.getColor(this.id_pointer));
		        	}
		        	
					this.BroadCastToClient(l);
        		}
        	}
        	else // Erro porque o login e a senha estão incorretos
        	{
        		l = new ArrayList();
        		l.add("ERRO");
	        	l.add("ERRO");
	        	this.BroadCastToClient(l);
	        	
	        	// servidor.RemovePointer(id_pointer);
	            // servidor.RemoveClient(this);
        		
        		// this.disconnect();
        	}
        	return true;
        }

        
		// Aqui são verificadas as mensagens de 'protocolo'
    	if (  nomeEvento.startsWith("PROT") )
    	{
    		// O cliente quer abrir uma noca sessão
    		if (nomeEvento.equals("PROT_nova_sessao"))
    		{
    			ArrayList li = (ArrayList) objDados;
    			
    			// A verificação da possível criação de uma sessão que
    			// já exista deve ser feita no ArgoUML e não no servidor!
    			
				this.nome_sessao = ((String) li.get(0));
				
				// Colocando o modelo do diagrama atual no servidor
				// this.modelo_atual = ((String) li.get(1));

				// System.out.println("Sessao " +this.nome_sessao+ " criada!");
				
				this.modelo_inicial = ((ArrayList) li.get(1));
				
				// Este terceiro elemento eh mais um arraylist contendo os ID's das Figs
				this.ids_inicial = ((ArrayList) li.get(2));
    				
				// Logando o horário que o usuário entrou na sessao
				this.servidor.LogaMsg( login + " entrou na sessao:" + this.nome_sessao,0);
    		}
    		
		
    		// O cliente quer participar de uma sessão existente
    		if (nomeEvento.equals("PROT_sessao_existente"))
    		{
    			
    			
    			if(this.servidor.ExisteSessao((String) objDados) )
    			{
    				
    				// Aqui são feitas as devidas iniciações da sessão colaborativa
    				this.nome_sessao = (String) objDados;

    				// Obtem o modelo desta sessão de algum dos participantes
    				this.modelo_inicial = this.servidor.ModeloSessaoInicial(this.nome_sessao);
    				this.ids_inicial = this.servidor.IdsSessaoInicial(this.nome_sessao);
    				this.modelo_atual = this.servidor.ModeloSessao(this.nome_sessao);
    				
    				
    				// Envia este modelo para o cliente, para que
    				// ele atualize o seu modelo
	    			ArrayList l = new ArrayList();
	        		l.add(this.modelo_inicial); 
	        		l.add("PROT_atualiza_modelo_cliente_inicial");
	        		l.add(this.ids_inicial);
				
    				this.BroadCastToClient(l);
    				
    				if(this.tipocon.equals("ARGO"))
    				{
        				
    					// Logando o horário que o usuário entrou na sessao
    					this.servidor.LogaMsg( login + " entrou na sessao:" + this.nome_sessao,0);

    					// Vou enviar a notificão de novo usuário para os clientes!
    					l = new ArrayList();
    					l.add(login); 
    					l.add("PROT_inicio_sessao");
    	        	
    					this.servidor.BroadCastToAll(l, this, true,"ARGO");
    				}
    				
    				
    			}
    		}	
        		
//    		 O cliente quer participar de uma sessão existente
    		if (nomeEvento.equals("PROT_chat_msg"))
    		{
    			// Montando o objeto que será replicado
    			ArrayList l = new ArrayList();
	        	l.add(login + ":" + ((String) objDados)); 
	        	l.add("PROT_chat_msg");
    			
	        	//	Aqui vou colocar uma cor para a mensagem do cliente
	        	l.add(this.servidor.getColor( this.id_pointer ));
	        	
    			this.servidor.BroadCastToAll(l, this, false,"ARGO");
			
    			// 	Logando a conversa em um arquivo
    			this.servidor.LogaMsg( login + ":" + ((String) objDados),0 );

    		}
    		
    		// Algum cliente resolveu sair da sessão colaborativa
    		if (nomeEvento.equals("PROT_fim_sessao"))
    		{
//				 Logando o horário que o usuário entrou na sessao
				this.servidor.LogaMsg( login + " saiu na sessao:" + this.nome_sessao,0);
				
				// Enviando para os clientes o nome do usuário que saiu da sessão
    			// Montando o objeto que será replicado
    			ArrayList l = new ArrayList();
	        	l.add(login); 
	        	l.add("PROT_fim_sessao");
	        	
    			this.servidor.BroadCastToAll(l, this, true,"ARGO");

    			//Removendo as dependências deste cliente (Thread & afins)
    			this.servidor.RemovePointer(this.id_pointer);
                this.servidor.RemoveClient(this);
                
                this.interrupt();
    		}
    		
    		
            ////////////////////////////////////LOCK////////////////////////////////////////////
    		// Aqui sera verificado se o lock solicitado pode ser liberado
    		if (nomeEvento.equals("PROT_lock_request"))
    		{
	    			ArrayList l = new ArrayList();
	    			l.add(((String) objDados));
	    		
	    			// System.out.println("Lock request on:" + ((String) objDados));
	    			this.servidor.LogaMsg( login + ":" + " Lock request on:" + ((String) objDados)  ,1 );
    			
    				if (!this.servidor.CheckLock(  ((String) objDados) ))
    				{
    					this.resources_locked.addElement( ((String) objDados) );

    					this.servidor.LogaMsg( login + ":" + " Lock granted on:" + ((String) objDados)  ,1 );
						l.add("PROT_lock_granted"); 
						// System.out.println("Lock granted on:" + ((String) objDados));
						
	    				// Manda o status para os clientes
	    				this.BroadLockStatusToAll();
    				}
    				else
    				{
    					// Aqui vou fazer uma verificação. Se for esta conexão
    					// que esta com o lock, retornar OK
    					
    					if(this.resources_locked.contains((String) objDados) )
    					{
    					
    						this.servidor.LogaMsg( login + ":" + " Lock granted on:" + ((String) objDados)  ,1 );
    						l.add("PROT_lock_granted"); 
    						// System.out.println("Lock alredy granted on:" + ((String) objDados));
    						
    	    				// Manda o status para os clientes
    	    				this.BroadLockStatusToAll();
    					}
    					else
    					{
    						this.servidor.LogaMsg( login + ":" + " Lock deny on:" + ((String) objDados)  ,1 );
    						l.add("PROT_lock_deny");
    						// System.out.println("Lock deny on:" + ((String) objDados));
    					}
    				}
    	        	
    				// Manda a resposta da requisitção de lock
    				this.BroadCastToClient(l);
    				
    		}
        		
    		// Aqui sera verificado se os locks solicitados pode ser liberado
    		// Lock multiplo
    		if (nomeEvento.equals("PROT_lock_request_group"))
    		{
    				// O ArrayList abaixo vai montar a mensagem de lock_ok ou lock_deny
    				ArrayList l = new ArrayList();
    				
    				ArrayList liberados = new ArrayList();
    			
    				// Este ArrayList vai conter os objetos que querem lock 
    				ArrayList objs = (ArrayList) objDados;
    				
    				l.add(objs);
    				
    				boolean lock_ok = true;
    				
    				// Varrendo a ArrayList dos objetos que querem lock
    				for(int i = 0;i<objs.size();i++)
        			{
						if (!this.servidor.CheckLock(  (String) objs.get(i)  ))
						{
							liberados.add((String) objs.get(i));
						}
						else
						{
							//	Aqui vou fazer uma verificação. Se for esta conexão
	    					// que esta com o lock, OK, senão retorna erro! 
	    					if(!this.resources_locked.contains( (String) objs.get(i)  ))
	    						lock_ok = false;
						}
        			}
    				
    				String elementos ="";
    				
    				// Vendo se todos os locks estao ok
    				if(lock_ok)
    				{
    					// Agora vou colocar efetivamente o lock em todo mundo
    					for(int i = 0;i<liberados.size();i++)
            			{
    						this.resources_locked.addElement( (String) liberados.get(i) );
    						
    						elementos = elementos + "," + (String) liberados.get(i);
            			}
    					
    					// Mandando a mensagem
    					l.add("PROT_lock_granted");
    					
    					this.servidor.LogaMsg( login + ":" + " Lock request group granted:" + elementos,1 );
    				}
    				else
    				{
    					l.add("PROT_lock_deny");
    					this.servidor.LogaMsg( login + ":" + " Lock request group denyed:" + elementos,1 );	
    				}
    				
    				// Enviando a notificação para os outros clientes
    				this.BroadLockStatusToAll();
    				
    				// Enviado a mensagem para o cliente
    				this.BroadCastToClient(l);
    				
    		}
    		
    		// Aqui libera os locks que existem nas figuras
    		if (nomeEvento.equals("PROT_lock_release"))
    		{
				// System.out.println("Lock release on:" + ((String) objDados));
    			this.servidor.LogaMsg( login + ":" + " Lock release on:" + ((String) objDados)  ,1 );
    			this.resources_locked.remove(((String) objDados));
    			
				// Manda o status para os clientes
				this.BroadLockStatusToAll();
    			
    		}
    		if (nomeEvento.equals("PROT_lock_clear"))
    		{
    			// Antes de fazer o lock clear eu preciso montar um array 
    			// contendo os id's dos elementos que perderam o lock
    			// e que podem ter um novo nome
    			ArrayList modificados = new ArrayList();
    			
    			String compara = "";

    			if(objDados != null)
    			{
	    			ArrayList nomesElementos = (ArrayList) objDados;
	    			
	    			for(int i=0;i<nomesElementos.size();i++)
	    			{
	    				String dados[] = (String []) nomesElementos.get(i);
	    				
	    	            String []x = {dados[0],dados[1],dados[2],dados[3],dados[4],dados[5],dados[6]};
	    	            	
	    	            modificados.add(x);
	    			}
    			}
    			
    			// Agora posso apagar todos os elementos
    			this.servidor.LogaMsg( login + ":" + " Lock clear",1 );
    			this.resources_locked.clear(); 
    			// System.out.println("Lock Clear" );
    			
				// Manda o status para os clientes
				this.BroadLockStatusToAll(modificados);
    			
    		}
    		if (nomeEvento.equals("PROT_lock_clear_almost"))
    		{
    		
    			ArrayList modificados = new ArrayList();
    			
    			ArrayList selecionados = (ArrayList) objDados;

//   			 Aqui vou montar o array com apenas os elementos
    			// que perderam o lock e naum com todos
    			if(list.size() > 2)
    			{
    				ArrayList nomesElementos = (ArrayList) list.get(2);
	    			
	    			for(int i=0;i<nomesElementos.size();i++)
	    			{
	    				String dados[] = (String []) nomesElementos.get(i);
	    				
	        			for(int j = 0;j<selecionados.size();j++)
	        			{
	        				if(!selecionados.get(j).equals(dados[0]))
	        				{
	    	    				String []x = {dados[0],dados[1],dados[2],dados[3],dados[4],dados[5],dados[6]};
	    	    	            modificados.add(x);
	        				}
	        			}
	    			}
    			}
    			
    			
    			// Vou limpar os locks em todos, menos os que ja estão selecionados
    			this.resources_locked.clear(); 
    			
    			
    			String elementos ="";
    			
    			for(int i = 0;i<selecionados.size();i++)
    			{
    				// Preciso retirar o lock da Thread que a possui!
    				
    				this.resources_locked.addElement( selecionados.get(i) );
    				elementos = elementos + "," + selecionados.get(i);
    			}
    			
    			
    			
    			
    			this.servidor.LogaMsg( login + ":" + " Lock clear almost:" + elementos,1 );
    			
				// 	Manda o status para os clientes
				this.BroadLockStatusToAll(modificados);
    			
    			// System.out.println("Lock Released - liberando todos os locks menos os selecioandos" );
    		}
            ////////////////////////////////////LOCK////////////////////////////////////////////
    		
    		if (nomeEvento.equals("PROT_remove_elemento"))
    		{
    			ArrayList selecionados = (ArrayList) objDados;

    			for(int i = 0;i<selecionados.size();i++)
    			{
        			this.servidor.LogaMsg( login + ":" + " Lock release on:" + ((String) selecionados.get(i))  ,1 );
        			this.servidor.CheckLock( (String) selecionados.get(i), true);
    			}
    			
    			// Manda o status para os clientes
				this.BroadLockStatusToAll();
				
				// Vou encaminhar a mensagem PROT_remove_elemento para os demais usuários
				return false;
    		}
    		
    		
    		//  O cliente mandou um modelo para ser atualizado nos clientes
    		// Porem somente os clientes que são da mesma sessão!
    		if (nomeEvento.equals("PROT_atualiza_modelo_servidor"))
    		{
    			// Atualizando o modelo desta thread
    			this.modelo_atual = (Object) objDados;
    			
    			// System.out.println("Cliente mandou o modelo:" + this.modelo_atual);
    			
    			// Montando o objeto que será replicado
    			ArrayList l = new ArrayList();
	        	l.add(this.modelo_atual); 
	        	l.add("PROT_atualiza_modelo_cliente");
    			
    			this.servidor.BroadCastToAll(l, this, true);
    			
        	}
    		
    		// O cliente quer abrir uma noca sessão
    		if (nomeEvento.equals("PROT_EYE"))
    		{
    			// Enviar pela conexão do GEF
    			// a movimentação ocular para todos os usuários
    			
    			 
    			ArrayList l_olho = (ArrayList) objDados;
    			 
    			// System.out.println("Posicao do olho do usuario:" + (String) l_olho.get(0));
    			// System.out.println("Pos X:" + (String) l_olho.get(1));
    			// System.out.println("Pos Y:" + (String) l_olho.get(2));
    				
    			
    			ArrayList manda = new ArrayList();
    			ArrayList p = new ArrayList();
    			
    			p.add((String) l_olho.get(1)); // x
    			p.add((String) l_olho.get(2)); // y
    			manda.add(p); // objeto 'genérico' (pode ser um mouse event ou outro)
            	
    			manda.add("eyeMovedPointer");
            	
            	// Mandando a cor atual do telepointer
            	// manda.add(this.servidor.getColor(this.id_pointer));
    			manda.add(this.servidor.getEyeColor( (String) l_olho.get(0)) );
    			
    			
            	
            	// 	Mandando id do telepointer
            	manda.add(this.id_pointer);
            	
            	// Mandando o nome do proprietario do telepointer
            	manda.add((String) l_olho.get(0));
            	
    			this.servidor.BroadCastToAll(manda, this, true,"GEF");
    			
    			return true; 
    		}
    		
    		return true;
    	}

		return false;
	}

	
    private void BroadLockStatusToAll () 	{
        // ArrayLista que vai conter as notificações dos clientes (cores)
        ArrayList avisa;
        ArrayList manda = new ArrayList();

        avisa = this.servidor.getLockStatus(); 
		
        manda.add(avisa);
        manda.add("PROT_notify_lock");

		this.servidor.BroadCastToAll(manda, this, true,"GEF");
    
    }   
	
    private void BroadLockStatusToAll (ArrayList nomes) 	
    {
        // ArrayLista que vai conter as notificações dos clientes (cores)
        ArrayList avisa;
        ArrayList manda = new ArrayList();

        avisa = this.servidor.getLockStatus(); 
		
        manda.add(avisa);
        manda.add("PROT_notify_lock");
        manda.add(nomes);

		this.servidor.BroadCastToAll(manda, this, true,"GEF");
    
    }
    
	// when connection starts - opens streams and calls server to notify
    // all other currently connected clients about the joining of this user.
    private void connect () throws java.io.IOException	{

    	// Aqui verifica qual é o tipo conexão
    	try 
    	{
    		is = new ObjectInputStream(client.getInputStream());
            os = new ObjectOutputStream(client.getOutputStream());
	
            this.CON = "OBJ";
            os.flush();
    		
    	} catch (IOException e) 
    	{
            isEYE = new InputStreamReader(client.getInputStream());
            osEYE = new OutputStreamWriter(client.getOutputStream());

            this.CON = "TXT";
            osEYE.flush();
    	}
    	
    }   
    
    //  when connection ends - closes streams, stops this thread and notifies
    // server about the disconnection of this client.
    private void disconnect () {
        try {
            
        	if(CON == "OBJ") // Caso conexão ARGO ou GEF
        	{
        		is.close();
        		os.close();
        	} 
        	else
        	{
        		isEYE.close();
        		osEYE.close();
        	}
            
            client.close();
            
            // Liberando todos os locks que este cara tenha
            this.resources_locked.clear(); 
			
			// Manda o status para os clientes
			this.BroadLockStatusToAll();


            // Aqui vou remover o Id do Telepointer
            servidor.RemovePointer(this.id_pointer);
            servidor.RemoveClient(this);
            
            this.interrupt();
        } catch (IOException e) {
            // e.printStackTrace();
        } 
    }           // disconnect
    
    public synchronized void BroadCastToClient (Object obj) {
        try {
            os.reset();
            os.writeObject(obj);
            os.flush();
            os.reset();
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }           // BroadCastToClient

}
