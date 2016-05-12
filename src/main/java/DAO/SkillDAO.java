package DAO;

import model.Skill;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SkillDAO {
    private Session session;

    public SkillDAO(Session session) {
        this.session = session;
    }

    public List<Skill> getSkillsByPrimaryTask(Integer primaryTaskIdentifier) {
        Transaction transaction = session.beginTransaction();
        String sqlQuery = "select * from skills where skill_id IN (select skill_id " +
                "from con_primary_task_skill where primary_task_id = "
                + primaryTaskIdentifier + ");";
        Query query = session.createSQLQuery(sqlQuery).addEntity(Skill.class);
        List<Skill> result = query.list();
        if (result == null) {
            throw new NullPointerException("DB return null list");
        }
        transaction.commit();
        session.flush();
        return result;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
