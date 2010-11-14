package org.opentox.toxotis.persistence;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.BibTeX;
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

        BibTeX oc = new BibTeX(Services.ntua().augment("bibtex", "3"));
        oc.setBibType(BibTeX.BIB_TYPE.Article);
        oc.setAddress("addr");
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
