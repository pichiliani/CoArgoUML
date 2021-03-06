package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Argument object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface Argument extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute value.
     * @return Value of attribute value.
     */
    public org.omg.uml.foundation.datatypes.Expression getValue();
    /**
     * Sets the value of value attribute. See {@link #getValue} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setValue(org.omg.uml.foundation.datatypes.Expression newValue);
    /**
     * Returns the value of reference action.
     * @return Value of reference action.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getAction();
    /**
     * Sets the value of reference action. See {@link #getAction} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setAction(org.omg.uml.behavioralelements.commonbehavior.Action newValue);
}
