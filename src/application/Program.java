package application;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.Person;

public class Program {

	private EntityManagerFactory emf;
	private EntityManager em;
	private Integer p2Id;
	
	public static void main(String[] args) {
		Program obj = new Program();
		try {
			obj.run();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		obj = null;
		System.out.println("Pronto!");
	}

	public Program() {
		super();
		this.emf = Persistence.createEntityManagerFactory("exemplo-jpa");
		this.em = emf.createEntityManager();
		System.out.println("Program instanced");
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.em.close();
		this.emf.close();
		System.out.println("Program finalized");
	}

	public void run() throws Throwable {
		this.SavePerson();
		this.QueryPerson();
		this.ListAll();
		this.RemovePerson();
		this.ListAll();
		this.RemoveAll();
		this.ListAll();
		
		this.finalize();
	}
	
	private void SavePerson() {
		Person p1 = new Person(null, "Carlos da Silva", "carlos@gmail.com");
		Person p2 = new Person(null, "Joaquim Torres", "joaquim@gmail.com");
		Person p3 = new Person(null, "Ana Maria", "ana@gmail.com");

		this.em.getTransaction().begin();

		this.em.persist(p1);
		this.em.persist(p2);
		this.em.persist(p3);

		this.em.getTransaction().commit();
		
		this.p2Id = p2.getId();
		
		System.out.println("SavePerson - Pronto!");
	}

	private Person QueryPerson() {
		Person p = this.em.find(Person.class, this.p2Id);
		if (p != null) System.out.println(p.toString());
		System.out.println("QueryPerson - Pronto!");
		return p;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Person> QueryAllPerson() {
	    Query query = this.em.createQuery("SELECT p FROM Person p");
	    return (Collection<Person>) query.getResultList();		
	}
	
	private void ListAll() {
		this.QueryAllPerson().forEach((p)->System.out.println(p.toString()));
		System.out.println("ListAll - Pronto!");		
	}

	private void RemovePerson() {
		this.em.getTransaction().begin();
		Person p = QueryPerson();
		if (p != null) this.em.remove(p);
		this.em.getTransaction().commit();
		System.out.println("RemovePerson - Pronto!");
	}
	
	
	private void RemoveAll() {
		this.em.getTransaction().begin();
		Collection<Person> persons = this.QueryAllPerson();
		for (Person p : persons) {
			System.out.println("Remove - " + p.toString());
			this.em.remove(p);			
		}
		this.em.getTransaction().commit();
		System.out.println("RemoveAll - Pronto!");
	}
}
