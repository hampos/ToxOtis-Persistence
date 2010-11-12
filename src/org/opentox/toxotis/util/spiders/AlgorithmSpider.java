package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.OpenToxAlgorithms;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A parser for data models of OpenTox algorithms.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class AlgorithmSpider extends Tarantula<Algorithm> {

    /**
     * URI of the algorithm to be downloaded and parsed stored in
     * a private field.
     */
    private VRI uri;

    /**
     * Create a new AlgorithmSpider providing the URI of the algorithm to be
     * <em>spidered</em> and an authentication token that will allow the client
     * to access the content of the algorithm.
     * @param uri
     *      The URI of the algorithm to be downloaded and parsed into an {@link
     *      Algorithm } object. You can pick an algorithm URI from the list inside
     *      {@link OpenToxAlgorithms }; for example {@link OpenToxAlgorithms#NTUA_MLR NTUA MLR}
     *      from the {@link Services#ntua() NTUA SERVICES} server.
     * @param token
     *      An authentication token that will grant the client access to the resource.
     * @throws ToxOtisException
     *      In case some exceptional event occurs during the server-client communication,
     *      the connection is not possible (e.g. the remote server is down), or the
     *      response status is 403 (Authentication Failed), 401 (The user is not authorized),
     *      404 (Algorithm not found on the server), 500 (Some internal server error
     *      occured) or other exceptional status code.
     */
    public AlgorithmSpider(VRI uri, AuthenticationToken token) throws ToxOtisException {
        this(uri.addUrlParameter("tokenid", token.stringValue()));
    }


    public AlgorithmSpider(Resource resource, OntModel model) throws ToxOtisException {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
            if (!Algorithm.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("Bad URI : Not an algorithm URI (" + uri + ")");
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(AlgorithmSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param uri
     * @throws ToxOtisException
     */
    public AlgorithmSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient(uri);
        client.setMediaType("application/rdf+xml");
        try {
            /*
             * Handle excpetional events caused during the server-client communiation.
             */
            int status = client.getResponseCode();
            if (status != 200) {
                OntModel om = client.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(uri, om);
                ErrorReport er = ersp.parse();

                if (status == 403) {
                    throw new ToxOtisException(ErrorCause.AuthenticationFailed,
                            "Access denied to : '" + uri + "'", er);
                }
                if (status == 401) {
                    throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                            "User is not authorized to access : '" + uri + "'", er);
                }
                if (status == 404) {
                    throw new ToxOtisException(ErrorCause.AlgorithmNotFound,
                            "The following algorithm was not found : '" + uri + "'", er);
                } else {
                    throw new ToxOtisException(ErrorCause.CommunicationError,
                            "Communication Error with : '" + uri + "'", er);
                }
            }
            model = client.getResponseOntModel();
            resource = model.getResource(uri.getStringNoQuery());
        } catch (IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + uri, ex);
        } finally { // Have to close the client (disconnect)
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose,
                            "Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.clearToken().toString() : null) + "'", ex);
                }
            }
        }
    }

    @Override
    public Algorithm parse() throws ToxOtisException {
        Algorithm algorithm = null;
        try {
            algorithm = new Algorithm(uri.getStringNoQuery());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        if (algorithm == null) {
            throw new ToxOtisException("Make sure that the URI you provided holds a valid representation of an OpenTox algorithm.");
        }
        algorithm.setOntologies(getOTATypes(resource));
        MetaInfoSpider metaSpider = new MetaInfoSpider(model, uri.getStringNoQuery());
        algorithm.setMeta(metaSpider.parse());
        StmtIterator itParam = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.parameters().asObjectProperty(model),
                (RDFNode) null));
        while (itParam.hasNext()) {
            ParameterSpider paramSpider = new ParameterSpider(model, itParam.nextStatement().getObject().as(Resource.class));
            algorithm.getParameters().add(paramSpider.parse());
        }
        return algorithm;

    }
}
