package org.opentox.toxotis.persistence.db;

import org.hibernate.Session;
import org.opentox.toxotis.core.component.Task;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteTool {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteTool.class);

    public static void deleteTask(Session session, Task.Status... status) {
        try {
            session.beginTransaction();
            String hql = "DELETE from Task WHERE status = :status";
            for (Task.Status s : status) {
                session.createQuery(hql).setString("status", s.toString()).executeUpdate();
            }
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Deletion of tasks failed. Logging the corresponding exception for details", ex);
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        }


    }
}
