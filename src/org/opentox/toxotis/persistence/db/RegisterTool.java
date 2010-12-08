package org.opentox.toxotis.persistence.db;

import java.util.Set;
import org.hibernate.Session;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.*;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
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

    private static void preprocessComponent(OTComponent component) {
        component.getUri().clearToken();
    }

    public static void storeUser(User user, Session session) {
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeAlgorithm(Algorithm algorithm, Session session) {
        session.beginTransaction();
        preprocessComponent(algorithm);
        for (Parameter p : algorithm.getParameters()) {
            session.saveOrUpdate(p);
            session.flush();
            session.evict(p);
        }
        session.saveOrUpdate(algorithm);
        session.flush();
        session.evict(algorithm);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeModel(Model model, Session session) {
        preprocessComponent(model);
        session.beginTransaction();
        if (model.getAlgorithm() != null) {
            session.saveOrUpdate(model.getAlgorithm());
        }
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

    public static void storeDataset(Dataset dataset, Session session) {
        preprocessComponent(dataset);
        session.beginTransaction();
        for (Feature f : dataset.getContainedFeatures()) {
            session.saveOrUpdate(f);
            session.flush();
            session.evict(f);
        }
        session.getTransaction().commit();
        session.clear();

        session.beginTransaction();
        Set<FeatureValue> ff = dataset.getFeatureValues();
        for (FeatureValue p : ff) {
            session.saveOrUpdate(p);
            session.flush();
            session.evict(p);
        }
        session.saveOrUpdate(dataset);
        session.getTransaction().commit();
        session.clear();
    }

    public static void storeTask(Session session, Task task) {
        preprocessComponent(task);
        session.beginTransaction();
        User createdBy = task.getCreatedBy();
        if (createdBy != null) {
            session.saveOrUpdate(createdBy);
        }
        ErrorReport er = task.getErrorReport();
        if (er != null) {
            session.saveOrUpdate(er);
            session.flush();
            session.evict(er);
        }
        session.saveOrUpdate(task);
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

    public static void storeBibTeX(Session session, BibTeX bibtex) {
        preprocessComponent(bibtex);

        session.beginTransaction();
        User createdBy = bibtex.getCreatedBy();
        if (createdBy != null) {
            session.saveOrUpdate(createdBy);
        }
        session.saveOrUpdate(bibtex);
        session.getTransaction().commit();
        session.clear();
    }
}
