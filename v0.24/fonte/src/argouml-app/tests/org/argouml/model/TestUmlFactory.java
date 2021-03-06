// $Id: TestUmlFactoryBuildNode.java 12485 2007-05-03 05:59:35Z linus $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model;

import junit.framework.TestCase;

/**
 * Checks that the {@link UmlFactory#buildNode(Object)} method works with
 * all conceivable alternatives.
 */
public class TestUmlFactory extends TestCase {
    /**
     * Constructor.
     *
     * @param arg0 name of the test case
     */
    public TestUmlFactory(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Check that a 
     * Testing Core elements.
     * @throws IllegalModelElementConnectionException if buildConnection fails
     */
    public void testBuildConnection()
        throws IllegalModelElementConnectionException {
        Object class1 = (Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass()));
        Object package1 = (Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getPackage()));
        Model.getCoreHelper().setNamespace(class1, package1);
        
        Object class2 = (Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass()));
        Object package2 = (Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getPackage()));
        Model.getCoreHelper().setNamespace(class2, package2);
        
        Object assoc = Model.getUmlFactory().buildConnection(
                Model.getMetaTypes().getAssociation(), 
                class1, Model.getAggregationKind().getComposite(), 
                class2, Model.getAggregationKind().getNone(),
                true, null);
        
        // The association should have same namespace as from class
        assertSame(package1, Model.getFacade().getNamespace(assoc));
        
        // Only one end should be navigable
        assertFalse(Model.getFacade().isNavigable(
                Model.getFacade().getAssociationEnd(class1, assoc)));
        assertTrue(Model.getFacade().isNavigable(
                Model.getFacade().getAssociationEnd(class2, assoc)));
    }
}
