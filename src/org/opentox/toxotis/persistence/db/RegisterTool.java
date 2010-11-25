package org.opentox.toxotis.persistence.db;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Session;
import org.opentox.toxotis.core.component.*;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;
import org.opentox.toxotis.util.aa.User;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RegisterTool {

    public static void storeUser(User user, Session session) {
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeAlgorithm(Algorithm a, Session session) {
        session.beginTransaction();
        for (Parameter p : a.getParameters()) {
            session.saveOrUpdate(p);
            session.flush();
            session.evict(p);
        }
        session.saveOrUpdate(a);
        session.flush();
        session.evict(a);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeModel(Model model, Session session) {
        session.beginTransaction();

        session.saveOrUpdate(model.getAlgorithm());
        session.flush();
        session.evict(model.getAlgorithm());

        for (Parameter p : model.getParameters()) {
            session.saveOrUpdate(p);
            session.flush();
            session.evict(p);
        }
        
        for (Feature ft : model.getIndependentFeatures()) {
            session.saveOrUpdate(ft);
            session.flush();
            session.evict(ft);
        }
        if (model.getCreatedBy() != null) {
            session.saveOrUpdate(model.getCreatedBy());
            session.flush();
            session.evict(model.getCreatedBy());
        }
        session.saveOrUpdate(model.getDependentFeature());
        session.flush();
        session.evict(model.getDependentFeature());

        session.saveOrUpdate(model.getPredictedFeature());
        session.flush();
        session.evict(model.getPredictedFeature());

        session.saveOrUpdate(model);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeDataset(Dataset ds, Session session) {
        session.beginTransaction();
        for (Feature f : ds.getContainedFeatures()) {
            session.saveOrUpdate(f);
            session.flush();
            session.evict(f);
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        Set<FeatureValue> ff = ds.getFeatureValues();
        for (FeatureValue p : ff) {
            session.saveOrUpdate(p);
            session.flush();
            session.evict(p);
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        session.saveOrUpdate(ds);
        session.getTransaction().commit();
        session.clear();
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

    public static void storeBibTeX(Session session) {
        session.beginTransaction();
        session.getTransaction().commit();
        session.clear();
    }
}
