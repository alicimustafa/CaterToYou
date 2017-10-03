package controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cater.data.CompanyDAO;
import cater.data.CustomerDAO;
import cater.data.CustomerInput;
import entity.Cart;
import entity.Company;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.User;

@Controller
public class CustomerController {

	@Autowired
	private CompanyDAO companyDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@RequestMapping(path = "Shop.do", method = RequestMethod.GET)
	public String index(Model model) {
		List<Company> companies = companyDAO.index();
		model.addAttribute("allCompanies", companies);
		return "views/menus.jsp";
	}

	@RequestMapping(path = "ShopHere.do", method = RequestMethod.GET)
	public String show(@RequestParam("companyId") Integer id, Model model) {
		List<Item> menuItems = customerDAO.showMenu(id);
		Company company = companyDAO.findCompanyById(id);
		model.addAttribute("menu", menuItems);
		model.addAttribute("company", company);
		model.addAttribute("address", company.getAddress());
		return "views/menu.jsp";
	}

	@RequestMapping(path = "addToCart.do", method = RequestMethod.POST)
	public String addItemToCart(@RequestParam("itemId") Integer id, @RequestParam("quantity") int count,
			@RequestParam("company") int companyId, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (count > 0) {
			customerDAO.addItemToCart(id, customer.getCart(), count);
		}
		return "redirect:ShopHere.do?companyId=" + companyId;
	}

	@RequestMapping(path = "OrderHistory.do", method = RequestMethod.GET)
	public String showHistory(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		List<Order> orderHistory = customerDAO.returnOrdersForCustomer(customer);
		model.addAttribute("orders", orderHistory);
		return "views/orderHistory.jsp";
	}

	@RequestMapping(path = "UpdateCustomer.do", method = RequestMethod.POST)
	public String customerUpdate(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			model.addAttribute("address", customer.getAddress());
		}
		return "views/customerUpdate.jsp";
	}

	@RequestMapping(path = "editCustomer.do", method = RequestMethod.POST)
	public String customerEdit(Model model, HttpSession session, CustomerInput input) {
		Customer customer = (Customer) session.getAttribute("customer");
		customer.getAddress().setCity(input.getCity());
		customer.getAddress().setState(input.getState());
		customer.getAddress().setStreet(input.getStreet());
		customer.getAddress().setStreet2(input.getStreet2());
		customer.getAddress().setZip(Integer.parseInt(input.getZip()));
		User user = (User) session.getAttribute("user");
		user.setEmail(input.getEmail());
		user = customerDAO.updateEmail(user);
		customer = customerDAO.updateAddress(customer);
		session.setAttribute("user", user);
		session.setAttribute("customer", customer);
		model.addAttribute("customer", customer);
		model.addAttribute("address", customer.getAddress());
		model.addAttribute("user", (User) session.getAttribute("user"));
		return "redirect:customer.do";
	}

	@RequestMapping("showCart.do")
	public String showCart(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		Cart cart = customerDAO.showCartWithAllItems(customer);
		model.addAttribute("total", customerDAO.calculateCartTotal(cart));
		model.addAttribute("itemList", cart.getCartHasItemList());
		model.addAttribute("cart", cart);
		return "views/cart.jsp";
	}

}