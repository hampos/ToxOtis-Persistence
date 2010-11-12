package org.opentox.toxotis.persistence;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Persist {

    public static void main(String[] args) throws Exception {
        SessionFactory sf = new Configuration().configure("./org/opentox/toxotis/persistence/config/hibernate.cfg.xml").buildSessionFactory();
        Session s = sf.openSession();


        s.beginTransaction();
        MetaInfoImpl m1= new MetaInfoImpl();
        LiteralValue lv= new LiteralValue(new Double(2));
        m1.addComment(lv);
        m1.addSameAs(new ResourceValue(Services.ambitUniPlovdiv(), OTClasses.Compound()));
        m1.addTitle("--");
        m1.addDescription("      .");
        LiteralValue ll =new LiteralValue(new Date(System.currentTimeMillis()), XSDDatatype.XSDdate);
        m1.setDate(ll);
        m1.addSameAs(new ResourceValue(Services.ideaconsult(), OTClasses.Compound()));
        s.save(m1);
        s.getTransaction().commit();



//        List m = s.createCriteria(MetaInfo.class).list();
//        System.out.println("found "+m.size());
//        for (Object o : m){
//            try{
//            MetaInfo mi = (MetaInfo)o;
//            Set<LiteralValue> slv = mi.getComments();
//            for (LiteralValue l : slv){
//                System.out.println(l.getValue()+"))))");
//            }
//            } catch (HibernateException e){}
//        }



    }
}
