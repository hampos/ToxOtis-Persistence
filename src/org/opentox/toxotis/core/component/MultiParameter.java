package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.Parameter.ParameterScope;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MultiParameter extends OTComponent<MultiParameter> {

    /** ParameterValue values to the multi-valued multi-parameter*/
    private ArrayList<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
    private Parameter.ParameterScope scope;

    public ArrayList<ParameterValue> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(ArrayList<ParameterValue> setValues) {
        this.parameterValues = setValues;
    }

    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    @Override
    public Individual asIndividual(OntModel model) {

        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.SetValuedParameter().inModel(model));
        if (parameterValues.size() > 1) {
            indiv.addRDFType(OTClasses.VectorParameter().inModel(model));
            for (ParameterValue setValue : parameterValues) {
                Individual valueIndiv = setValue.asIndividual(model);
                indiv.addProperty(OTObjectProperties.parameterValues().asObjectProperty(model), valueIndiv);
            }
        } else if (parameterValues.size() == 1) {
            ParameterValue setValue = parameterValues.get(0);
            Individual valueIndiv = setValue.asIndividual(model);
            indiv.addProperty(OTObjectProperties.parameterValues().asObjectProperty(model), valueIndiv);
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (scope != null) {
            indiv.addLiteral(OTDatatypeProperties.paramScope().asDatatypeProperty(model),
                    model.createTypedLiteral(scope.toString(), XSDDatatype.XSDstring));
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
