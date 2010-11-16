package org.opentox.toxotis.persistence;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
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
        session.setFlushMode(FlushMode.AUTO);

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

        ErrorReport other = new ErrorReport();
        other.setActor("someone else");
        other.setDetails("(#(#*(*W(&$#@*^*&@%%#$^#(");
        other.setErrorCode("254");
        other.setHttpStatus(400);
        other.setMessage("short message 2dfjshg");

        ErrorReport oc = new ErrorReport();
        oc.setActor("me");
        oc.setDetails("sdjghkrjhg kjsfkjhsrkjg ");
        oc.setErrorCode("4");
        oc.setHttpStatus(342);
        oc.setMessage("short message");
        //oc.setErrorCause(other);
        oc.setMeta(metaInfoToSave);

        Dataset dataset = new Dataset(Services.ideaconsult().augment("dataset", "6").addUrlParameter("max", "5")).loadFromRemote();
        dataset.setMeta(metaInfoToSave);
       
        
        session.beginTransaction();
        session.saveOrUpdate(dataset);
        session.getTransaction().commit();




//        session = sf.openSession();
//
//        List resultsFoundInDB = session.createCriteria(OntologicalClass.class).list();
//        System.out.println("found " + resultsFoundInDB.size());
//        for (Object o : resultsFoundInDB) {
//            try {
//                OntologicalClass mi = (OntologicalClass) o;
//                System.out.println(mi.getName());
//                System.out.println(mi.getNameSpace());
//            } catch (HibernateException e) {
//                e.printStackTrace();
//            }
//        }

    }
}
