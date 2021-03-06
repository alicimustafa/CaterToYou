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
import entity.Address;
import entity.Cart;
import entity.Company;
import entity.Customer;
import entity.Image;
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
		List<Company> companies = companyDAO.indexActive();
		model.addAttribute("allCompanies", companies);
		return "views/menus.jsp";
	}

	@RequestMapping(path="newUser.do", method=RequestMethod.POST)
	public String newUser(Model model, HttpSession session, User user) {
		customerDAO.createUser(user);
		String message = "New user has been created successfully.";

		return "redirect:actionSuccessful.do?message=" + message;
	}

	@RequestMapping(path="actionSuccessful.do", method=RequestMethod.GET)
	public String displayActionSuccessful(Model model, HttpSession session, String message) {
		model.addAttribute("message", message);
		return "views/actionSuccessful.jsp";
	}


	@RequestMapping(path = "ShopHere.do", method = RequestMethod.GET)
	public String show(@RequestParam("companyId") Integer id, Model model, HttpSession session) {
		List<Item> menuItems = customerDAO.showMenu(id);
		Company company = companyDAO.findCompanyById(id);
		model.addAttribute("menu", menuItems);
		model.addAttribute("company", company);
		model.addAttribute("address", company.getAddress());
		this.addCartToModel(model, session);
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

	@RequestMapping(path = "UpdateCustomer.do", method = RequestMethod.GET)
	public String customerUpdate(Model model, HttpSession session, @RequestParam("customerId") int id) {

		Customer customer = customerDAO.getCustomerById(id);
		
		session.setAttribute("customer", customer);
		model.addAttribute("user", customer.getUser());
		model.addAttribute("address", customer.getAddress());
		return "views/customerUpdate.jsp";
	}
	
	@RequestMapping(path= "udateUserPass.do", method = RequestMethod.POST)
	public String updateUserPasword(@RequestParam("id") int id, 
			@RequestParam("newUserName") String userName,
			@RequestParam("newPassword") String password) {
		User user = customerDAO.persistUserNamePassword(id, userName, password);
		return "redirect:UpdateCustomer.do?customerId=" + user.getCustomer().getId();
	}
	
	@RequestMapping(path = "editCustomerImage.do", method = RequestMethod.POST)
	public String customerImageEdit(Model model, HttpSession session, @RequestParam("customerId") int id
			, @RequestParam("imageUrl") String imageUrl) { //input is a class that we made to handle a form
		Customer customer = customerDAO.getCustomerById(id);
		User user = (User) session.getAttribute("user");
		
		if(customer.getImage()==null) {
			Image i = new Image();
			i.setImageUrl(imageUrl);
			customer.setImage(i);
			
			customerDAO.createImageForCustomer(customer);
//			customerDAO.updateImage(customer, customer.getImage());
		}
		else {
			Image i = new Image();
			i.setImageUrl(imageUrl);
			
//			customer.getImage().setImageUrl(imageUrl);
			customer.setImage(i);
			customerDAO.updateImage(customer);
		}
		session.setAttribute("user", user);
		session.setAttribute("customer", customer);
		model.addAttribute("customer", customer);
		model.addAttribute("user", user);
		return "redirect:customer.do";
	}
	

	@RequestMapping(path = "editCustomer.do", method = RequestMethod.POST)
	public String customerEdit(Model model, HttpSession session, CustomerInput input) { //input is a class that we made to handle a form
		Customer customer = customerDAO.getCustomerById(input.getId());
		User user = (User) session.getAttribute("user");
		String imageUrl = input.getImageUrl();

		if(customer.getAddress()==null) {
			Address a = new Address();


			a.setCity(input.getCity());
			a.setState(input.getState());
			a.setStreet(input.getStreet());
			a.setStreet2(input.getStreet2());
			a.setZip(Integer.parseInt(input.getZip()));
			customer.setAddress(a);
			customerDAO.createAddressForCustomer(customer);

			customerDAO.updateAddress(customer.getAddress());
		}
		else {
		customer.getAddress().setCity(input.getCity());
		customer.getAddress().setState(input.getState());
		customer.getAddress().setStreet(input.getStreet());
		customer.getAddress().setStreet2(input.getStreet2());
		customer.getAddress().setZip(Integer.parseInt(input.getZip()));
		customer.setAddress(customerDAO.updateAddress(customer.getAddress()));
		}

		
		user.setEmail(input.getEmail());
		user = customerDAO.updateEmail(user);

		session.setAttribute("user", user);
		session.setAttribute("customer", customer);
		model.addAttribute("customer", customer);
		model.addAttribute("address", customer.getAddress());
		model.addAttribute("user", user);
		return "redirect:customer.do";
	}

	@RequestMapping("showCart.do")
	public String showCart(Model model, HttpSession session) {
		this.addCartToModel(model, session);
		return "views/cart.jsp";
	}

	private double decimalFormatting(double num) {
		return Math.round(num * 100) / 100.00;
	}

	@RequestMapping(path = "removeItem.do", method = RequestMethod.POST)
	public String removeItemFromCart(@RequestParam("itemId") int itemId, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		customerDAO.removeItemFromCart(itemId, customer.getCart());
		return "redirect:showCart.do";
	}

	@RequestMapping(path = "changeQuantity.do", method = RequestMethod.POST)
	public String updateQuantity(@RequestParam("itemId") int id, @RequestParam("count") int count) {
		customerDAO.updateQuantityInCart(id, count);
		return "redirect:showCart.do";
	}

	@RequestMapping("checkout.do")
	public String showCheckout(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		List<Address> addressList = customerDAO.getPreviousAddress(customer);
		if(addressList.size() > 0) {
			model.addAttribute("addressList", addressList);
		}
		this.addCartToModel(model, session);
		return "views/checkout.jsp";
	}

	private void addCartToModel(Model model, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		Cart cart = customerDAO.showCartWithAllItems(customer);
		double subTotal = customerDAO.calculateCartTotal(cart);
		model.addAttribute("subTotal", decimalFormatting(subTotal));
		model.addAttribute("fee", decimalFormatting(subTotal * 0.1));
		model.addAttribute("tax", decimalFormatting(subTotal * 0.075));
		double total = (subTotal * 0.1) + (subTotal * 0.075) + subTotal;
		model.addAttribute("total", decimalFormatting(total));
		if (cart != null) {
			model.addAttribute("itemList", cart.getCartHasItemList());
		}
		model.addAttribute("cart", cart);
	}

	@RequestMapping(path="createOrder.do", 
			method = RequestMethod.POST)
	public String createOrder(@RequestParam(value ="date", required=false) String date,
							@RequestParam(value ="time", required=false) String time,
							@RequestParam(value ="cartId", required=false) int id,
							@RequestParam(value ="street", required=false) String street,
							@RequestParam(value ="street2", required=false) String street2,
							@RequestParam(value ="city", required=false) String city,
							@RequestParam(value ="state", required=false) String state,
							@RequestParam(value ="zip", required=false) Integer zip,
							@RequestParam(value="addressId", required=false) Integer addId,
							@RequestParam(value="addressType", required=false) Integer addType	
							){
		Address address = null;
		

		if(addType == 0){

			address = new Address();
			address.setStreet(street);
			address.setStreet2(street2);
			address.setCity(city);
			address.setState(state);
			address.setZip(zip);
		
		}
		else {
			address = customerDAO.getAddressById(addId);
		}
		customerDAO.checkoutEmptiesCartMovesToOrder(id, address, time, date);

		
		String message ="Your order has been placed.";
		return "redirect:actionSuccessful.do?message=" + message;
	}

}
