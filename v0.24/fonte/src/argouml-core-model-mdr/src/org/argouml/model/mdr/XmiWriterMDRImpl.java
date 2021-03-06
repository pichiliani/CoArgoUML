// $Id: XmiWriterMDRImpl.java 14772 2008-05-19 19:24:00Z tfmorris $
// Copyright (c) 2005-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.jmi.reflect.RefObject;

import org.apache.log4j.Logger;
import org.argouml.model.UmlException;
import org.argouml.model.XmiExtensionWriter;
import org.argouml.model.XmiWriter;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;
import org.omg.uml.UmlPackage;

/**
 * XmiWriter implementation for MDR.
 * 
 * This implementation is clumsy because the specified Writer interface wants
 * characters, while the XmiWriter wants an OutputStream dealing in bytes. We
 * could easily create a Writer from an OutputStream, but the reverse is not
 * true. 
 * 
 * TODO: The old Writer based interface can be removed when the deprecated
 * ModelImplementation.getXmiWriter is removed.
 * 
 * @author lmaitre
 * 
 */
class XmiWriterMDRImpl implements XmiWriter {

    private Logger LOG = Logger.getLogger(XmiWriterMDRImpl.class);

    private MDRModelImplementation modelImpl;

    private Object model;
    
    private OutputConfig config;

    private Writer writer;
    
    private OutputStream oStream;
    
    private static final String ENCODING = "UTF-8";
    
    private static final String XMI_VERSION = "1.2";
    
    private XmiExtensionWriter xmiExtensionWriter;

    private static final char[] TARGET = "/XMI.content".toCharArray();

    /*
     * Private constructor for common work needed by both public
     * constructors.
     */
    private XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
             String version) {
        if (theModel == null) {
            throw new IllegalArgumentException("A model must be provided");
        }
        if (theParent == null) {
            throw new IllegalArgumentException("A parent must be provided");
        }
        this.modelImpl = theParent;
        this.model = theModel;
        config = new OutputConfig();
        config.setEncoding(ENCODING);
        config.setReferenceProvider(new XmiReferenceProviderImpl(modelImpl
                .getObjectToId()));
        config.setHeaderProvider(new XmiHeaderProviderImpl(version));
    }
    
    /**
     * Create an XMI writer for the given model.
     * 
     * @param theParent
     *            The ModelImplementation
     * @param theModel
     *            The Model to write. If null, write all top-level model
     *            elements.
     * @param theWriter
     *            The writer to write to
     * @param version the ArgoUML version
     * @throws IllegalArgumentException if no writer provided
     * @deprecated for 0.25.4 by tfmorris.  Use 
     * {@link #XmiWriterMDRImpl(MDRModelImplementation, Object, OutputStream, String)}.
     */
    public XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
            Writer theWriter, String version) {
        this(theParent, theModel, version);
        if (theWriter == null) {
            throw new IllegalArgumentException("A writer must be provided");
        }
        writer = theWriter;
    }
    
    /**
     * Create an XMI writer for the given model.
     * 
     * @param theParent
     *            The ModelImplementation
     * @param theModel
     *            The Model to write. If null, write all top-level model
     *            elements.
     * @param theStream
     *            The OutputStream to write to.
     * @param version
     *            the ArgoUML version
     * @throws IllegalArgumentException
     *             if no output stream is provided
     * @since 0.25.4
     */
    public XmiWriterMDRImpl(MDRModelImplementation theParent, Object theModel,
            OutputStream theStream, String version) {
        this(theParent, theModel, version);
        if (theStream == null) {
            throw new IllegalArgumentException("A writer must be provided");
        }
        oStream = theStream;
    }


    public void write() throws UmlException {
        XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(
                config);
        try {
            ArrayList elements = new ArrayList();
            UmlPackage pkg = modelImpl.getUmlPackage();
            // Make sure user model is first
            elements.add(model);
            for (Iterator it = pkg.getCore().getElement().refAllOfType()
                    .iterator(); it.hasNext();) {
                RefObject obj = (RefObject) it.next();
                // Find top level objects which aren't part of profile
                if (obj.refImmediateComposite() == null) {
                    if (!elements.contains(obj)) {
                        elements.add(obj);
                    }
                }
            }
            LOG.info("Saving " + elements.size() + " top level model elements");

            OutputStream stream;
            if (oStream == null) {
                stream = new WriterOuputStream(writer);                
            } else {
                stream = oStream;
            }

            xmiWriter.write(stream, "file:///ThisIsADummyName.xmi", elements,
                    XMI_VERSION);
        } catch (IOException e) {
            throw new UmlException(e);
        }
    }

    /**
     * Class which wraps a Writer into an OutputStream.
     * 
     * TODO: This entire class can go away when we remove
     * the Writer based interface.
     * 
     * @author lmaitre
     * @deprecated for 0.25.4 by tfmorris
     */
    private class WriterOuputStream extends OutputStream {

        private Writer myWriter;
        private boolean inTag = false;
        private char tagName[] = new char[12];
        private int tagLength = 0;

        /**
         * Constructor.
         * @param wrappedWriter The myWriter which will be wrapped
         */
        public WriterOuputStream(Writer wrappedWriter) {
            if (wrappedWriter == null) {
                throw new IllegalArgumentException("No writer provided");
            }
            this.myWriter = wrappedWriter;
        }

        /*
         * @see java.io.OutputStream#close()
         */
        public void close() throws IOException {
            myWriter.close();
        }

        /*
         * @see java.io.OutputStream#flush()
         */
        public void flush() throws IOException {
            myWriter.flush();
        }

        /*
         * @see java.io.OutputStream#write(byte[], int, int)
         */
        public void write(byte[] b, int off, int len) throws IOException {
            char[] c = new String(b, off, len, ENCODING).toCharArray();
            if (xmiExtensionWriter != null) {
                write(c);
            } else {
                myWriter.write(c, 0, c.length);
            }
        }

        /*
         * @see java.io.OutputStream#write(byte[])
         */
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        /*
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            write(new byte[] {(byte) (b & 255)}, 0, 1);
        }
        
        /*
         * @see java.io.OutputStream#write(int)
         */
        private void write(char[] ca) throws IOException {
            
            int len = ca.length;
            for (int i = 0; i < len; ++i) {
                char ch = ca[i];
                if (inTag) {
                    if (ch == '>') {
                        inTag = false;
                        if (Arrays.equals(tagName, TARGET)) {
                            if (i > 0) {
                                myWriter.write(ca, 0, i + 1);
                            }
                            xmiExtensionWriter.write(myWriter);
                            xmiExtensionWriter = null;
                            if (i + 1 != len - 1) {
                                myWriter.write(ca, i + 1, (len - i) - 1);
                            }
                            return;
                        }
                    } else if (tagLength == 12) {
                        inTag = false;
                    } else {
                        tagName[tagLength++] = ch;
                    }
                }
                
                if (ch == '<') {
                    inTag = true;
                    Arrays.fill(tagName, ' ');
                    tagLength = 0;
                }
            }
            myWriter.write(ca, 0, ca.length);
        }
    }

    public void setXmiExtensionWriter(XmiExtensionWriter theWriter) {
        xmiExtensionWriter = theWriter;
    }
}
