package com.shoppingcart.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.entity.Product;
import com.shoppingcart.model.CartInfo;
import com.shoppingcart.model.CustomerInfo;
import com.shoppingcart.model.PaginationResult;
import com.shoppingcart.model.ProductInfo;
import com.shoppingcart.util.Utils;
import com.shoppingcart.validator.CustomerInfoValidator;

@Controller
public class MainController {

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private CustomerInfoValidator customerInfoValidator;
	
	@Autowired
	private OrderDAO orderDAO;

	// Danh sách sản phẩm
	@RequestMapping({ "/productList" })
	public String getAllProductInfos(Model model, @RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		final int maxResult = 5;
		final int maxNavigationPage = 10;
		PaginationResult<ProductInfo> productInfos = productDAO.getAllProductInfos(page, maxResult, maxNavigationPage,
				likeName);
		model.addAttribute("paginationProductInfos", productInfos);
		return "productList";
	}

	@RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam("code") String code) throws IOException {

		Product product = null;
		if (code != null) {
			product = productDAO.getProductByCode(code);
		}

		if (product != null && product.getImage() != null) {
			response.setContentType("image/jpeg, image.jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
		}
		response.getOutputStream().close();
	}

	@RequestMapping({ "/buyProduct" })
	public String buyProductHandler(HttpServletRequest request, Model model,
			@RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.getProductByCode(code);
		}

		if (product != null) {
			CartInfo cartInfo = Utils.getCartInfoInSession(request);
			ProductInfo productInfo = new ProductInfo(product);
			cartInfo.addProduct(productInfo, 1);
		}
		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInfoInSession(request);

		model.addAttribute("cartForm", cartInfo);
		return "shoppingCart";
	}

	@RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
	public String shoppingCartUpdateQuantity(HttpServletRequest request, Model model, 
			@ModelAttribute("cartForm") CartInfo cartForm) {
		CartInfo cartInfo = Utils.getCartInfoInSession(request);
		cartInfo.updateQuantity(cartForm);
		return "redirect:/shoppingCart";
		
	}
	
	@RequestMapping({"/shoppingCartRemoveProduct"})
		public String removeProductHanlder(HttpServletRequest request, Model model,
				@RequestParam(value = "code", defaultValue = "") String code) {
			Product product = null;
			
			if (code != null && code.length() > 0) {
				product = productDAO.getProductByCode(code);
			}
			
			if (product != null) {
				CartInfo cartInfo = Utils.getCartInfoInSession(request);
				ProductInfo productInfo = new ProductInfo(product);
				cartInfo.removeProduct(productInfo);
			}
			
			return "redirect:/shoppingCart";
		}
	
	@RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.GET)
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInfoInSession(request);
		
		if(cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		}
		
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		if(customerInfo == null) {
			customerInfo = new CustomerInfo();
		}
		
		model.addAttribute("customerForm", customerInfo);
		return "shoppingCartCustomer";
		
	}
	
	@RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.POST)
		public String shoppingCartCustomer(HttpServletRequest request, Model model,
				@ModelAttribute("customerForm") @Validated CustomerInfo customerForm, BindingResult result) {
			customerInfoValidator.validate(customerForm, result);
			if(result.hasErrors()) {
				customerForm.setValid(false);
				return "shoppingCartCustomer";
			}
			
			customerForm.setValid(true);
			CartInfo cartInfo = Utils.getCartInfoInSession(request);
			cartInfo.setCustomerInfo(customerForm);
			return "redirect:/shoppingCartConfirmation";
		}
	
	@RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInfoInSession(request);
		
		if(cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			return "redirect:/shoppingCartCustomer";
		}
		
		try {
		    orderDAO.saveOrder(cartInfo);
		} catch (Exception e) {
			// TODO: handle exception
			return "shoppoingCartConfirmation";
		}
		
		Utils.removeCartInfoSession(request);
		
		Utils.storeLastOrderedCartInfoInSession(request, cartInfo);
		
		return "redirect:/shoppingCartFinalize";
	}
	
	@RequestMapping(value = {"/shoppingCartFinalize"}, method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {
		CartInfo lastOrderedCart = Utils.getLastOrderedCartInfoInSession(request);
		
		if(lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		return "shoppingCartFinalize";
	}
    
	
}
