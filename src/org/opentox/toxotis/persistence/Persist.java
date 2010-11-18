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
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
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
        final boolean doAlter = true;

        if (doAlter) {
            try {
                Connection c = session.connection();
                Statement stmt = c.createStatement();
                stmt.addBatch("ALTER TABLE FeatOntol DROP PRIMARY KEY");
                stmt.addBatch("ALTER TABLE FeatOntol ADD `ID_W` INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
                stmt.executeBatch();
            } catch (HibernateException hbe) {
                hbe.printStackTrace();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MetaInfoImpl metaInfoToSave = new MetaInfoImpl();
        LiteralValue literal = new LiteralValue(new Double(93.24));
        metaInfoToSave.addComment(literal);
        metaInfoToSave.addSameAs(new ResourceValue(Services.ambitUniPlovdiv(), OTClasses.Compound()));
        metaInfoToSave.addTitle("some title goes here!");
        metaInfoToSave.addDescription("brief description");
        metaInfoToSave.addDescription("brief description 2");
        LiteralValue ll = new LiteralValue(new Date(System.currentTimeMillis()), XSDDatatype.XSDdate);
        metaInfoToSave.setDate(ll);
        metaInfoToSave.addSameAs(new ResourceValue(Services.ideaconsult(), OTClasses.Compound()));

        System.out.println("Loading from remote...");
        Algorithm algorithm = new Algorithm(Services.ntua().augment("algorithm", "svm")).loadFromRemote();
        System.out.println("DONE!!!");

        session = sf.openSession();
        Dataset ds = new Dataset(Services.ideaconsult().augment("dataset", "9").addUrlParameter("max", "55")).loadFromRemote();

        session.beginTransaction();
        for (Feature f :ds.getContainedFeatures()){
            session.saveOrUpdate(f);
        }
        session.getTransaction().commit();

        session.beginTransaction();
        
        Set<FeatureValue> ff = ds.getFeatureValues();
        for (FeatureValue p : ff){
                 session.saveOrUpdate(p);
        }
        session.getTransaction().commit();

        session.beginTransaction();
        session.save(ds);
        session.getTransaction().commit();

        //Possible successful procedure:
        /*
         * 0. (Possibly not needed!) Register features first!
         * 1. Register all feature value ( we need method getAllFeatureValues() )
         * 2. Register all dataentries and then the dataset or may, the dataset right away!
         */
        // Note: Throughout a session uuids are imutable

//
//        List resultsFoundInDB = session.createCriteria(Algorithm.class).list();
//        System.out.println("found " + resultsFoundInDB.size());
//        for (Object o : resultsFoundInDB) {
//            Algorithm a = (Algorithm) o;
//            System.out.println(a.getParameters());
//            System.out.println(a.getUri());
//            System.out.println(a.getOntologies());
//            System.out.println(a.getOntologies().iterator().next().getSuperClasses().iterator().next().getUri());
//            System.out.println(a.getMeta());
//        }

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
