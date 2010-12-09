package org.opentox.toxotis.persistence.db;

import org.hibernate.Session;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteTool {

    public static void deleteTask(Session session, Task.Status... status) {
        session.beginTransaction();
        String hql = "DELETE from Task WHERE status = :status";
        for (Task.Status s : status) {
            session.createQuery(hql).setString("status", s.toString()).executeUpdate();
        }
        session.getTransaction().commit();
        session.clear();

    }
}
