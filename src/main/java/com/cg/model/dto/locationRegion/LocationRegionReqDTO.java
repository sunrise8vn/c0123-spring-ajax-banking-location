package com.cg.model.dto.locationRegion;

import com.cg.model.LocationRegion;
import com.cg.model.dto.customer.CustomerCreateReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotEmpty;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class LocationRegionReqDTO implements Validator {

    private String provinceId;
    private String provinceName;
    private String districtId;
    private String districtName;
    private String wardId;
    private String wardName;

    @NotEmpty(message = "Địa chỉ là không được rỗng 2345")
    private String address;


    public LocationRegion toLocationRegion() {
        return new LocationRegion()
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address)
                ;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return LocationRegionReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LocationRegionReqDTO locationRegionReqDTO = (LocationRegionReqDTO) target;

        String address = locationRegionReqDTO.getAddress();

        if (address == null) {
            errors.rejectValue("address", "address.null", "Địa chỉ là bắt buộc");
        } else {
            if (address.trim().length() == 0) {
                errors.rejectValue("address", "address.empty", "Địa chỉ là không được rỗng");
            } else {
                if (address.trim().length() > 20) {
                    errors.rejectValue("address", "address.length.max", "Địa chỉ không được quá 20 ký tự");
                }
                if (address.trim().length() < 5) {
                    errors.rejectValue("address", "address.length.min", "Địa chỉ không được nhỏ hơn 5 ký tự");
                }
            }
        }
    }
}
