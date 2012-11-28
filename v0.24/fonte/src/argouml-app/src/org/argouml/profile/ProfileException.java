// $Id: ProfileException.java 13850 2007-12-02 01:03:27Z tfmorris $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.profile;

/**
 * An exception to be thrown during failure in the Profile subsystem.
 * This will typically be a wrapped exception containing the exception
 * representing the underlying failure cause.
 * 
 * @author Marcus Aurelio (maurelio1234)
 */
public class ProfileException extends Exception {

    /**
     * The constructor.
     *
     * @param message the message to show
     */
    public ProfileException(String message) {
        super(message);
    }

    /**
     * The constructor.
     *
     * @param message the message to show
     * @param theCause the cause for the exception
     */
    public ProfileException(String message, Throwable theCause) {
        super(message, theCause);
    }

    /**
     * The constructor.
     *
     * @param theCause the cause for the exception
     */
    public ProfileException(Throwable theCause) {
        super(theCause);
    }
}

