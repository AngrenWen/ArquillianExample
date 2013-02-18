package tv.beenius.greeter.dao;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import tv.beenius.greeter.entities.Hello;

public class HelloDao {
	
	EntityManager manager;
	
	public HelloDao(EntityManager manager) {
		this.manager = manager;
	}
	
	public long countRows() {

		Session session = (Session) manager.getDelegate();
		Criteria criteria = session.createCriteria(Hello.class)
				.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();

	}
}