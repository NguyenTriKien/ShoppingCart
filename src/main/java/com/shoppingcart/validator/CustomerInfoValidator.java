package com.shoppingcart.validator;

import org.apache.commons.validator.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.shoppingcart.model.CustomerInfo;

@SuppressWarnings("deprecation")
@Component
public class CustomerInfoValidator implements Validator {


	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerInfo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		CustomerInfo customerInfo = (CustomerInfo) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.customerForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.customerForm.phone");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.customerForm.address");
		
		if(!emailValidator.isValid(customerInfo.getEmail())) {
			errors.rejectValue("email", "Pattern.customerForm.email");
		}
		
	}
	
}
