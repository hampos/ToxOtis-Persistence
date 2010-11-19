package org.opentox.toxotis.persistence.db;

import java.util.Set;
import org.hibernate.Session;
import org.opentox.toxotis.core.component.*;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RegisterTool {

    public static void storeAlgorithm(Algorithm a, Session session) {
        session.beginTransaction();
        for (Parameter p : a.getParameters()) {
            session.saveOrUpdate(p);
        }
        session.saveOrUpdate(a);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeModel(Model a, Session session) {
        session.beginTransaction();
        for (Parameter p : a.getParameters()) {
            session.saveOrUpdate(p);
        }
        for (Feature ft : a.getIndependentFeatures()) {
            session.saveOrUpdate(ft);
        }
        session.saveOrUpdate(a.getDependentFeature());
        session.saveOrUpdate(a.getPredictedFeature());
        session.saveOrUpdate(a);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeDataset(Dataset ds, Session session) {
        session.beginTransaction();
        for (Feature f : ds.getContainedFeatures()) {
            session.saveOrUpdate(f);
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        Set<FeatureValue> ff = ds.getFeatureValues();
        for (FeatureValue p : ff) {
            session.saveOrUpdate(p);
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        session.saveOrUpdate(ds);
        session.getTransaction().commit();
    }

    public static void storeAllOntClasses(Session session) {
        session.beginTransaction();
        for (OntologicalClass oc : OTClasses.getAll()) {
            OntologicalClassImpl occ = new OntologicalClassImpl();
            occ.setMetaInfo(oc.getMetaInfo());
            occ.setName(oc.getName());
            occ.setNameSpace(oc.getNameSpace());
            session.saveOrUpdate(occ);
        }
        for (OntologicalClass oc : OTAlgorithmTypes.getAll()) {
            if (!oc.getName().equals("Thing")) {
                OntologicalClassImpl occ = new OntologicalClassImpl();
                occ.setMetaInfo(oc.getMetaInfo());
                occ.setName(oc.getName());
                occ.setNameSpace(oc.getNameSpace());
                session.saveOrUpdate(occ);
            }
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        for (OntologicalClass oc : OTClasses.getAll()) {
            session.saveOrUpdate(oc);
        }
        for (OntologicalClass oc : OTAlgorithmTypes.getAll()) {
            if (!oc.getName().equals("Thing")) {
                session.saveOrUpdate(oc);
            }
        }
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeBibTeX(Session session){
        session.beginTransaction();
        session.getTransaction().commit();
        session.clear();
    }
}
