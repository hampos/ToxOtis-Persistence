package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.Serializable;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ResourceValue implements Serializable{

    private VRI uri;
    private OntologicalClass ontologicalClass;

    private ResourceValue(){}

    public ResourceValue(VRI uri, OntologicalClass ontologicalClass) {
        this.uri = uri;
        this.ontologicalClass = ontologicalClass;
    }

    public OntologicalClass getOntologicalClass() {
        return ontologicalClass;
    }

    public void setOntologicalClass(OntologicalClass ontologicalClass) {
        this.ontologicalClass = ontologicalClass;
    }

    public VRI getUri() {
        return uri;
    }

    public void setUri(VRI uri) {
        this.uri = uri;
    }

    public Resource inModel(OntModel model){
        return model.createResource(getUri()!=null?getUri().toString():null,
                getOntologicalClass()!=null?getOntologicalClass().inModel(model):OTClasses.Thing().inModel(model));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceValue other = (ResourceValue) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        if (this.ontologicalClass != other.ontologicalClass && (this.ontologicalClass == null || !this.ontologicalClass.equals(other.ontologicalClass))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        hash = 37 * hash + (this.ontologicalClass != null ? this.ontologicalClass.hashCode() : 0);
        return hash;
    }



}