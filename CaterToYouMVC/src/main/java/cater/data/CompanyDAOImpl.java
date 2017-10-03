package cater.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Company;
import entity.Employee;
import entity.Image;
import entity.Item;
import entity.Menu;

@Repository
@Transactional
public class CompanyDAOImpl implements CompanyDAO {

	@PersistenceContext
	EntityManager em;

	@Override
	public Company updateCompanyInfo(Company company) {

		int id = company.getId();
		Company c = em.find(Company.class, id);

		if (c != null)

		{

			c.setAddress(company.getAddress());
			c.setImage(company.getImage());
			c.setName(company.getName());

		}

		em.close();
		return c;
	}

	@Override //session will give us the menu
	public Menu updateMenuItem(Menu m) {

		int id = m.getId();
		Menu menu = em.find(Menu.class, id);

		if (menu != null) {

			menu.setCompany(m.getCompany());
			menu.setItemList(m.getItemList());

		}

		return menu;
	}

	@Override
	public Menu addMenuItem(Item i, Menu menu) {

		String sql = "SELECT m FROM Menu m WHERE m.item.id = :id";
		Menu m = em.createQuery(sql, Menu.class).setParameter("id", menu.getId()).getSingleResult();

		if (m == null) {

			m = new Menu();
			m.setItemList(m.getItemList());
			em.persist(m);

		} else {

			m.setItemList(m.getItemList());
			em.persist(m);
		}

		return m;
	}

	@Override
	public Menu makeMenuItemInactive(Item i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee updateEmployee(Employee e) {

		int id = e.getEmployeeID();
		Employee employee = em.find(Employee.class, id);

		if (employee != null) {

			employee.setCompany(e.getCompany());
			employee.setUser(e.getUser());

		}

		em.close();
		return employee;
	}

	@Override
	public Employee addEmployee(Employee e) {

		String sql = "SELECT e FROM Employee e WHERE e.id = :id";

		Employee employee = em.createQuery(sql, Employee.class).setParameter("id", e.getEmployeeID()).getSingleResult();

		if (employee == null) {

			employee = new Employee();
			em.persist(employee);
		} else {

			employee.setUser(employee.getUser());
			em.persist(employee);
		}

		return null;
	}

	@Override
	public Employee makeEmployeeInactive(Employee e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateImage(Image i) {

		Image image = em.find(Image.class, 0);

		if (image != null) {

			image.setCompanyList(i.getCompanyList());
			image.setCustomerList(i.getCustomerList());
			image.setImageUrl(i.getImageUrl());
			image.setItemList(i.getItemList());

		}

	}

	@Override
	public void addImage(Image i) {

		String sql = "SELECT i FROM Image i WHERE i.id = :id";

		Image image = em.createQuery(sql, Image.class)
				.setParameter("id", i.getId())
				.getSingleResult();

		image = new Image();
		em.persist(image);

	}

	@Override
	public List<Company> index() {
		String sql = "SELECT c FROM Company c";
		return em.createQuery(sql, Company.class).getResultList();
	}

	@Override
	public Company findCompanyById(int id) {
		String sql = "SELECT c FROM Company c where c.id= :id";

		return em.createQuery(sql, Company.class).setParameter("id", id).getResultList().get(0);

	}
}
