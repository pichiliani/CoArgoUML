package org.omg.uml.behavioralelements.activitygraphs;

/**
 * A_type_classifierInState association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface ATypeClassifierInState extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param type Value of the first association end.
     * @param classifierInState Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Classifier type, org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param classifierInState Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Classifier getType(org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param type Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getClassifierInState(org.omg.uml.foundation.core.Classifier type);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param type Value of the first association end.
     * @param classifierInState Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Classifier type, org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param type Value of the first association end.
     * @param classifierInState Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Classifier type, org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState);
}
