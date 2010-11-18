package org.opentox.toxotis.persistence;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.FeatureValue;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;
import org.opentox.toxotis.persistence.db.RegisterTool;
import org.opentox.toxotis.persistence.util.HibernateUtil;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Persist {

    public static void main(String[] args) throws Exception {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        // Question: How can we know if the database is newly created?
        // (In order to know whether we have to execute the following lines...)
        final boolean doAlter = false;

        if (doAlter) {
            try {
                Connection c = session.connection();
                Statement stmt = c.createStatement();
                stmt.addBatch("ALTER TABLE FeatOntol DROP PRIMARY KEY");
                stmt.addBatch("ALTER TABLE FeatOntol ADD `ID_W` INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
                stmt.addBatch("ALTER TABLE OTComponent ADD `created` TIMESTAMP NOT NULL DEFAULT NOW()");
                stmt.executeBatch();
            } catch (HibernateException hbe) {
                hbe.printStackTrace();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * OPEN SESSION
         */
        session = sf.openSession();
//        RegisterTool.storeAllOntClasses(session);
        

//        System.out.println("Loading Algorithm");
//        Algorithm algorithm = new Algorithm(Services.ntua().augment("algorithm", "filter")).loadFromRemote();
//        System.out.println("Algorithm Loaded");
//        System.out.println("Storing Algorithm");
//        RegisterTool.storeAlgorithm(algorithm, session);
//        System.out.println("Algorithm registered successfully!");
//
//        System.out.println("Loading Dataset");
//        Dataset d = new Dataset(Services.ideaconsult().augment("dataset", "9").addUrlParameter("max", "5")).loadFromRemote();
//        System.out.println("Dataset Loaded");
//        System.out.println("Storing Dataset");
//        RegisterTool.storeDataset(d, session);
//        System.out.println("Dataset registered successfully!");
//
//        System.out.println("Loading Model");
//        Model model = new Model(Services.ntua().augment("model", "934ef1d0-2080-48eb-9f65-f61b830b5783")).loadFromRemote();
//        System.out.println("Model Loaded");
//        System.out.println("Storing Model");
//        RegisterTool.storeModel(model, session);

          List resultsFoundInDB = session.createCriteria(Model.class).list();
        System.out.println("found " + resultsFoundInDB.size());
        for (Object o : resultsFoundInDB) {
            Model mod = (Model) o;
            for (Parameter pp : mod.getParameters()){
                System.out.println(pp.getMeta().getComments());
            }
        }


        session.close();

    }
}
//        Όταν μεγαλώσω θέλω,
//        θέλω να γίνω 83 χρονών
//        τσατσά σ'ένα μπουρδέλο
//        χωρίς δόντια να μασάω τα κρουτόν
//        και να διαβάζω Οθέλο
//
//        Όταν μεγαλώσω θέλω
//        θελώ να γίνω διαστημικός σταθμός
//        και να παίζω μπουγέλο
//        κι από μένανε να βρέχει κι ο ουρανός
//        τα ρούχα να σας πλένω
//
//        Η ομορφιά του θέλω,
//        Μάρω Μαρκέλου
//

