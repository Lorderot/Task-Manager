package DAO;

import model.Skill;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class SkillDAO {

    public List<Skill> getSkillsByPrimaryTask(Integer primaryTaskIdentifier) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from skills where skill_id IN (select skill_id " +
                "from con_primary_task_skill where primary_task_id = "
                + primaryTaskIdentifier + ");";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Skill.class);
        List<Skill> result = query.list();
        transaction.commit();
        session.close();
        if (result == null) {
            throw new NullPointerException("DB return null list");
        }
        return result;
    }
}
