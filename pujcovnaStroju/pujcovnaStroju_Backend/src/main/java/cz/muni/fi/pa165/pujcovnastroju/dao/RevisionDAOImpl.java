package cz.muni.fi.pa165.pujcovnastroju.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import cz.muni.fi.pa165.pujcovnastroju.entity.Revision;

/**
 * 
 * @author Matej Fucek
 */
public class RevisionDAOImpl implements RevisionDAO {

	private EntityManager em;

	public RevisionDAOImpl(EntityManager em) {
		this.em = em;
	}

	public Revision create(Revision revision) {
		em.getTransaction().begin();
		em.persist(revision);
		em.getTransaction().commit();
		return em.find(Revision.class, revision.getRevID());
	}

	public Revision delete(Revision revision) {
		em.getTransaction().begin();
		Revision revision1 = em.merge(revision);
		em.remove(revision1);
		em.getTransaction().commit();
		return em.find(Revision.class, revision.getRevID());
	}

	public Revision update(Revision revision) {
		em.getTransaction().begin();
		Revision revision1 = em.merge(revision);
		em.persist(revision1);
		em.getTransaction().commit();
		return em.find(Revision.class, revision1.getRevID());
	}

	public Revision read(Long RevID) {
		return em.find(Revision.class, RevID);
	}

	public List<Revision> findAllrevisions() {
		TypedQuery<Revision> query1 = em.createQuery(
				"SELECT r FROM Revision r", Revision.class);
		return query1.getResultList();
	}

	public List<Revision> findRevisionsByDate(Date dateFrom, Date dateTo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Revision> cq = cb.createQuery(Revision.class);
		Root<Revision> revisionRoot = cq.from(Revision.class);
		cq.select(revisionRoot);
		if (dateFrom != null) {
			Expression<Date> dateFromExp = revisionRoot.get("revDate");
			cq.where(cb.greaterThanOrEqualTo(dateFromExp, dateFrom));
		}
		if (dateTo != null) {
			Expression<Date> dateToExp = revisionRoot.get("revDate");
			cq.where(cb.lessThanOrEqualTo(dateToExp, dateTo));
		}
		return em.createQuery(cq).getResultList();
	}

}