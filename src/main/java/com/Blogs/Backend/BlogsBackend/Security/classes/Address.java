package com.Blogs.Backend.BlogsBackend.Security.classes;

import com.Blogs.Backend.BlogsBackend.Security.dto.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address{

    @Id
    private String id;
    @Indexed(unique = true)
    private String addressName;
    private String fullName;
    private String mobileNumber;
    private String houseNumber;
    private String street;
    private String landMark;
    private int pinCode;
    private String city;
    private String state;
    private String country;



    public Address setAddress(AddressDto dto){

        this.id = dto.getId();
        this.addressName = dto.getAddressName();
        this.fullName = dto.getFullName();
        this.mobileNumber = dto.getMobileNumber();
        this.houseNumber = dto.getHouseNumber();
        this.street = dto.getStreet();
        this.landMark = dto.getLandMark();
        this.pinCode = dto.getPinCode();
        this.city = dto.getCity();
        this.state = dto.getState();
        this.country = dto.getCountry();

        return this;
    }
}
