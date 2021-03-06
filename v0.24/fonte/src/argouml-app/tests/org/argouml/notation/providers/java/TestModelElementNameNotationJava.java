// $Id: TestModelElementNameNotationJava.java 14863 2008-06-02 18:15:30Z mvw $
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.notation.providers.java;

import java.util.HashMap;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.providers.ModelElementNameNotation;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests the ModelElementNameNotationJava class.
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestModelElementNameNotationJava extends TestCase {
    private Object theClass;

    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        theClass = Model.getCoreFactory().buildClass("TheClass", getModel());
    }

    public void testToStringForRealization() {
        Object theInterface = Model.getCoreFactory().buildInterface(
                "TheInterface", getModel());
        Object realization = Model.getCoreFactory().buildRealization(theClass,
                theInterface, getModel());
        assertToStringForUnnamedRelationshipIsEmpty(realization);
        assertToStringForNamedRelationshipEqualsItsName(realization);
    }

    private void assertToStringForUnnamedRelationshipIsEmpty(
            Object relationship) {
        ModelElementNameNotation notation = new ModelElementNameNotationJava(
                relationship);
        assertEquals("", notation.toString(relationship, new HashMap()));
    }

    private void assertToStringForNamedRelationshipEqualsItsName(
            Object relationship) {
        ModelElementNameNotation notation = new ModelElementNameNotationJava(
                relationship);
        Model.getCoreHelper().setName(relationship, "relationshipName");
        assertEquals(Model.getFacade().getName(relationship), notation.toString(
                relationship, new HashMap()));
    }

    public void testToStringForGeneralization() {
        Object theBaseClass = Model.getCoreFactory().buildClass("TheBaseClass",
                getModel());
        Object generalization = Model.getCoreFactory().buildGeneralization(
                theClass, theBaseClass);
        assertToStringForUnnamedRelationshipIsEmpty(generalization);
        assertToStringForNamedRelationshipEqualsItsName(generalization);
    }

    private Object getModel() {
        return ProjectManager.getManager().getCurrentProject().getModel();
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelpOperation() {
        ModelElementNameNotation notation = 
            new ModelElementNameNotationJava(theClass); 
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
    
    /**
     * Test if the notationProvider refuses to instantiate 
     * without showing it the right UML element.
     */
    public void testValidObjectCheck() {
        try {
            new ModelElementNameNotationJava(new Object());
            fail("The NotationProvider did not throw for a wrong UML element.");
        } catch (IllegalArgumentException e) {
            /* Everything fine... */
        } 
    }
}
