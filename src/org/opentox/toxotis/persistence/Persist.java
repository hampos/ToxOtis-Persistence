package org.opentox.toxotis.persistence;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
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


        try {
            session.beginTransaction();
            session.saveOrUpdate(oc);
            session.getTransaction().commit();
        } catch (ConstraintViolationException ex) {
            System.out.println("[EXCEPT] Attempt to violate a constraint");
        } finally {
            session.close();
        }

        session = sf.openSession();

        List resultsFoundInDB = session.createCriteria(MetaInfo.class).list();
        System.out.println("found " + resultsFoundInDB.size());
        for (Object o : resultsFoundInDB) {
            try {
                MetaInfo mi = (MetaInfo) o;
                Set<LiteralValue> slv = mi.getComments();
                for (LiteralValue l : slv) {
                    System.out.println(l.getValue() + " <----");
                }
                System.out.println(mi.getDate() + "<~~~~~");
            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }

    }
}
