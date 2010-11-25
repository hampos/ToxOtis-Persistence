package org.opentox.toxotis.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.persistence.db.RegisterTool;
import org.opentox.toxotis.persistence.util.HibernateUtil;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Persist {

    public static void main(String[] args) throws Exception {
        org.apache.log4j.LogManager.resetConfiguration();
        org.apache.log4j.PropertyConfigurator.configure("src/org/opentox/toxotis/persistence/config/log4j.properties");
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        // Question: How can we know if the database is newly created?
        // (In order to know whether we have to execute the following lines...)
        final boolean doAlter = true;

        if (doAlter) {
            try {
                Connection c = session.connection();
                Statement stmt = c.createStatement();
                stmt.addBatch("ALTER TABLE FeatOntol DROP PRIMARY KEY");
                stmt.addBatch("ALTER TABLE FeatOntol ADD `ID_W` INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
                stmt.addBatch("ALTER TABLE OTComponent ADD `created` TIMESTAMP NOT NULL DEFAULT NOW()");
                stmt.addBatch("ALTER TABLE User ADD `created` TIMESTAMP NOT NULL DEFAULT NOW()");
                stmt.executeBatch();
            } catch (HibernateException hbe) {
                hbe.printStackTrace();
            } catch (SQLException sqle) {
                System.err.println("Info: Alter failed (Probably not an error!)");
                //Logger.getLogger(Persist.class).debug("Alter failed (Probably not an error!)", sqle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * OPEN SESSION
         */
        session = sf.openSession();
        RegisterTool.storeAllOntClasses(session);


        System.out.println("Loading Algorithm");
        Algorithm algorithm = new Algorithm(Services.ntua().augment("algorithm", "svm")).loadFromRemote();
        System.out.println("Algorithm Loaded");
        System.out.println("Storing Algorithm");
        RegisterTool.storeAlgorithm(algorithm, session);
        System.out.println("Algorithm registered successfully!");
        System.out.println();

        System.out.println("Loading Dataset");
        Dataset d = new Dataset(Services.ideaconsult().augment("dataset", "6").addUrlParameter("max", 20)).loadFromRemote();
        System.out.println("Dataset Loaded");
        System.out.println("Storing Dataset");
        RegisterTool.storeDataset(d, session);
        System.out.println("Dataset registered successfully!");
        System.out.println();

        System.out.println("Loading Model");
        Model model = new Model(Services.ntua().augment("model", "934ef1d0-2080-48eb-9f65-f61b830b5783")).loadFromRemote();
        System.out.println("Model Loaded");
        System.out.println("Storing Model");
        RegisterTool.storeModel(model, session);
        System.out.println("Model registered successfully!");
        System.out.println();

        System.out.println("Loading Task");
        Task task = new Task(Services.ntua().augment("task", "68d471ad-0287-4f6e-a200-244d0234e8a1")).loadFromRemote();
        System.out.println("Task Loaded");
        System.out.println("Storing Task");
        RegisterTool.storeTask(session, task);
        System.out.println("Task registered successfully!");
        System.out.println();

        List resultsFoundInDB = session.createCriteria(Algorithm.class).list();
        System.out.println("found " + resultsFoundInDB.size());
        for (Object o : resultsFoundInDB) {
            Algorithm alg = (Algorithm) o;
            System.out.println(alg.getMeta().getComments());
            for (Parameter pp : alg.getParameters()) {
                System.out.println(pp.getMeta().getComments());
            }
        }
        session.close();

    }
}
//        Όταν μεγαλώσω θέλω,
//        θέλω να γίνω 82 χρονών
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

