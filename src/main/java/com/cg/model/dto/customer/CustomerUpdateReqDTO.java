package com.cg.model.dto.customer;

import com.cg.model.Customer;
import com.cg.model.dto.locationRegion.LocationRegionReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUpdateReqDTO implements Validator {

    private String fullName;
    private String email;
    private String phone;
    private LocationRegionReqDTO locationRegion;

    public Customer toCustomer() {
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setLocationRegion(locationRegion.toLocationRegion());

        return customer;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerUpdateReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerUpdateReqDTO customer = (CustomerUpdateReqDTO) target;

        String fullName = customer.fullName;
//        String address = customer.address;

        if (fullName == null) {
            errors.rejectValue("fullName", "fullName.null", "Họ tên là bắt buộc");
        }
        else {
            if (fullName.trim().length() == 0) {
                errors.rejectValue("fullName", "fullName.empty", "Họ tên là không được rỗng");
            }
            else {
                if (fullName.trim().length() > 20) {
                    errors.rejectValue("fullName", "fullName.length.max", "Họ tên không được quá 20 ký tự");
                }
                if (fullName.trim().length() < 5) {
                    errors.rejectValue("fullName", "fullName.length.min", "Họ tên không được nhỏ hơn 5 ký tự");
                }
            }
        }

//        if (address == null) {
//            errors.rejectValue("address", "address.null", "Địa chỉ là bắt buộc");
//        }
//        else {
//            if (address.trim().length() == 0) {
//                errors.rejectValue("address", "address.empty", "Địa chỉ là không được rỗng");
//            }
//            else {
//                if (address.trim().length() > 20) {
//                    errors.rejectValue("address", "address.length.max", "Địa chỉ không được quá 20 ký tự");
//                }
//                if (address.trim().length() < 5) {
//                    errors.rejectValue("address", "address.length.min", "Địa chỉ không được nhỏ hơn 5 ký tự");
//                }
//            }
//        }
    }
}
