// $Id: PropPanelCreateAction.java 13799 2007-11-20 09:03:47Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.UMLMutableLinkedList;

/**
 * The properties panel for a CreateAction.
 * <p>
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelCreateAction extends PropPanelAction {

    /**
     * The constructor.
     */
    public PropPanelCreateAction() {
        super("label.create-action", lookupIcon("CreateAction"));

        AbstractActionAddModelElement2 action =
            new ActionAddCreateActionInstantiation();
        UMLMutableLinkedList list =
            new UMLMutableLinkedList(
                new UMLCreateActionClassifierListModel(),
                action, null, null, true);
        list.setVisibleRowCount(2);
        JScrollPane instantiationScroll = new JScrollPane(list);
        addFieldBefore(Translator.localize("label.instantiation"),
                instantiationScroll,
                argumentsScroll);

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6909604490593418840L;
}
