package com.lnt.mvc.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.mvc.model.Document;

@Repository
public class DocumentDAOImpl implements IDocumentDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public void save(Document document) {
		Session session = sessionFactory.openSession();
		session.persist(document);
		session.flush();
		session.close();
	}

	@Transactional
	public List<Document> list() {
		Session session = sessionFactory.openSession();
		List<Document> documents = null;
		try {
			documents = (List<Document>) session.createQuery("from Document").list();

		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return documents;
	}

	@Transactional
	public Document get(Integer id) {
		Session session = sessionFactory.openSession();
		Document doc = (Document) session.get(Document.class, id);
		session.close();
		return doc;
	}

	@Transactional
	public void remove(Integer id) {
		Session session = sessionFactory.openSession();

		Document document = (Document) session.get(Document.class, id);

		session.delete(document);
	}
}