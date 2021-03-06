// $Id: ActionSaveAllGraphics.java 14780 2008-05-20 22:34:32Z tfmorris $
// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoStatusEvent;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.undo.UndoableAction;
import org.tigris.gef.util.Util;

/**
 * Wraps a SaveGraphicsAction allow selection of an output directory to which
 * all diagrams will be written. Introduced thanks to issue 2126. Saves diagrams
 * only using the default format.
 * <p>
 * 
 * TODO: Add a user choice for other formats (PNG, SVG,...) <p>
 * TODO: Why is this an UndoableAction? (and how?) - tfm
 * 
 * @author Leonardo Souza Mario Bueno (lsbueno@tigris.org)
 */

public class ActionSaveAllGraphics extends UndoableAction {
    private static final Logger LOG =
        Logger.getLogger(ActionSaveAllGraphics.class);

    /**
     * The constructor.
     *
     */
    public ActionSaveAllGraphics() {
        super(Translator.localize("action.save-all-graphics"),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.save-all-graphics"));
    }

    @Override
    public void actionPerformed( ActionEvent ae ) {
        super.actionPerformed(ae);
        trySave( false );
    }

    /**
     * @param overwrite true if we can overwrite without asking
     * @return success
     */
    public boolean trySave(boolean overwrite) {
        return trySave(overwrite, null);
    }
    
    /**
     * @param overwrite
     *            true if we can overwrite without asking
     * @param directory
     *            directory to save to. If null, user will be prompted to
     *            choose.
     * @return success save status
     */
    public boolean trySave(boolean overwrite, File directory) {
        Project p =  ProjectManager.getManager().getCurrentProject();
        TargetManager tm = TargetManager.getInstance();
        File saveDir = (directory != null) ? directory : getSaveDir(p);
        if (saveDir == null) {
            /* The user cancelled! */
            return false;
        }
        boolean okSoFar = true;
        ArgoDiagram activeDiagram = p.getActiveDiagram();
        for (ArgoDiagram d : p.getDiagramList()) {
            tm.setTarget(d);
            okSoFar = trySaveDiagram(overwrite, d, saveDir);
            if (!okSoFar) {
                break;
            }
        }
        tm.setTarget(activeDiagram);
        return okSoFar;
    }

    /**
     * @param overwrite true if we can overwrite without asking
     * @param target the diagram
     * @param saveDir the directory to save to
     * @return success
     */
    protected boolean trySaveDiagram(boolean overwrite, Object target,
            File saveDir) {
        if ( target instanceof Diagram ) {
            String defaultName = ((Diagram) target).getName();
            defaultName = Util.stripJunk(defaultName);
            // TODO: It's probably worthwhile to abstract and factor
            // this chooser and directory stuff. More file handling is
            // coming, I'm sure.
            try {
                File theFile = new File(saveDir, defaultName + "."
                    + SaveGraphicsManager.getInstance().getDefaultSuffix());
                String name = theFile.getName();
                String path = theFile.getParent();
                SaveGraphicsAction cmd = SaveGraphicsManager.getInstance()
                    .getSaveActionBySuffix(
                        SaveGraphicsManager.getInstance().getDefaultSuffix());
                if (cmd == null) {
                    showStatus("Unknown graphics file type with extension "
                            + SaveGraphicsManager.getInstance()
                                    .getDefaultSuffix());
                    return false;
                }
                showStatus( "Writing " + path + name + "..." );
                saveGraphicsToFile(theFile, cmd, overwrite);
                showStatus( "Wrote " + path + name );
                return true;
            }
            catch ( FileNotFoundException ignore ) {
                LOG.error("got a FileNotFoundException", ignore);
            }
            catch ( IOException ignore ) {
                LOG.error("got an IOException", ignore);
            }
        }
        return false;
    }


    /**
     * @param p the current project
     * @return returns null if the user did not approve his choice
     */
    protected File getSaveDir(Project p) {
        JFileChooser chooser = getFileChooser(p);

        String fn = Configuration.getString(
                SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }

        int retval = chooser.showSaveDialog(ArgoFrame.getInstance());

        if ( retval == JFileChooser.APPROVE_OPTION ) {
            File theFile = chooser.getSelectedFile();
            String path = theFile.getPath();
            Configuration.setString(
                    SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH,
                    path);
            return theFile;
        }
        return null;
    }

    private boolean saveGraphicsToFile(File theFile, SaveGraphicsAction cmd,
            boolean overwrite) throws IOException {
        if ( theFile.exists() && !overwrite ) {
            int response =
		JOptionPane.showConfirmDialog(ArgoFrame.getInstance(),
                    Translator.messageFormat("optionpane.confirm-overwrite",
                            new Object[] {theFile}),
                    Translator.localize("optionpane.confirm-overwrite-title"),
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.NO_OPTION) return false;
        }
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream( theFile );
            cmd.setStream(fo);
            cmd.setScale(Configuration.getInteger(
                    SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION, 1));
            cmd.actionPerformed(null);
        } finally {
            if (fo != null) {
                fo.close();
            }
        }
        return true;
    }

    private JFileChooser getFileChooser(Project p) {
        JFileChooser chooser = null;
        try {
	    if ( p != null 
                && p.getURI() != null
                && p.getURI().toURL().getFile().length() > 0 ) {
	        chooser = new JFileChooser(p.getURI().toURL().getFile());
            }
        }
        catch ( MalformedURLException ex ) {
            LOG.error("exception in opening JFileChooser", ex);
        }

        if ( chooser == null ) chooser = new JFileChooser();
        chooser.setDialogTitle(
                Translator.localize("filechooser.save-all-graphics"));
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        return chooser;
    }

    private void showStatus(String text) {
        ArgoEventPump.fireEvent(new ArgoStatusEvent(
                ArgoEventTypes.STATUS_TEXT, this, text));
    }
}
