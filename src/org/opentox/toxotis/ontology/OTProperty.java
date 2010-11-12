package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.Collection;

public interface OTProperty extends OTResource, java.io.Serializable {

    String getNameSpace();

    void setNameSpace(String ns);

    String getName();

    /**
     * Set the local name of the property (namespace not included).
     * @param name
     */
    void setName(String name);

    /**
     * Get the meta information available abou the property.
     * @return
     *      Meta information as an instance of {@link MetaInfo }.
     */
    MetaInfo getMetaInfo();

    /**
     * Provide meta-information about the property
     * @param metaInfo
     *      Meta information about the property.
     */
    void setMetaInfo(MetaInfo metaInfo);

    /**
     * Get a collection of the super-properties of this ontological property
     * declared using <code>rdfs:subPropertyOf</code>.
     * @return
     *      Collection of super-properties.
     */
    Collection<OTProperty> getSuperProperties();

    /**
     *
     * @param superProperties
     *      Collection of super-properties.
     * @see OTProperty#getSuperProperties() getSuperProperties
     */
    void setSuperProperties(Collection<OTProperty> superProperties);

    /**
     * We quote an excperpt of the documentation about the property <code>rdfs:domain</code>
     * found at <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#domain">
     * W3C</a> online reference:
     * 
     * <blockquote align="justify">
     * A domain of a property limits the individuals to which the property can be applied.
     * If a property relates an individual to another individual, and the property has a
     * class as one of its domains, then the individual must belong to the class. For example,
     * the property hasChild may be stated to have the domain of Mammal. From this a
     * reasoner can deduce that if Frank hasChild Anna, then Frank must be a Mammal.
     * Note that rdfs:domain is called a global restriction since the restriction is stated on the
     * property and not just on the property when it is associated with a particular class.
     * See the discussion below on property restrictions for more information.
     * </blockquote>
     *
     * @return
     *      A collection of the ontological classes that constitute the domain of the
     *      ontological property.
     */
    Collection<OntologicalClass> getDomain();

    /**
     * Extract the corresponding property (if any) from the given ontological
     * model. If the property is not found in the data model, the method return
     * <code>null</code>. In that case where the property does not exist in the
     * data model and it should be created, consider using {@link OTProperty#asProperty(com.hp.hpl.jena.ontology.OntModel)
     * asProperty(OntModel)}.
     * @param model
     *      Ontological Model
     * @return
     *      Property found in that model of <code>null</code> otherwise.
     */
    Property getProperty(OntModel model);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * For a property one can define (multiple) <code>rdfs:domain</code> axioms. Syntactically,
     * <code>rdfs:domain</code> is a built-in property that links a property (some instance
     * of the class <code>rdf:Property</code>) to a class description. An <code>rdfs:domain
     * </code> axiom asserts that the subjects of such property statements must belong to the
     * class extension of the indicated class description.
     * @param domain
     *      A collection of the ontological classes that constitute the domain of the
     *      ontological property.
     */
    void setDomain(Collection<OntologicalClass> domain);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * Cast the <code>OTProperty</code> object as a Property object of Jena. If the property
     * already exists in the ontological model, it is not recreated but returned to the
     * user. In that case, this method is equivalent to {@link
     * OTProperty#getProperty(com.hp.hpl.jena.ontology.OntModel) getProperty}.
     * 
     * @param model
     *      The model to which is property is assigned.
     * @return
     *      The created proeprty in the provided Ontological Model.
     */
    Property asProperty(OntModel model);
}
