package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Address;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.repository.AddressRepository;
import com.UCLLBackEnd.pony.repository.StableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    private StableRepository stableRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, StableRepository stableRepository) {
        this.addressRepository = addressRepository;
        this.stableRepository = stableRepository;
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address addNewStableToNewAddress(Stable stable) {

        addressRepository.save(stable.getAddress());
        stableRepository.save(stable);
        return stable.getAddress();
    }

    public Address addStableToAddress(Long addressId, Long stableid) {

        Stable stable = stableRepository.findById(stableid)
                .orElseThrow(() -> new  ServiceException("ServiceException", "Stable with id " + stableid + " doesn't exist"));

        if (stable.getAddress() != null) {
            throw new  ServiceException("ServiceException", "Stable with id " + stableid + " already has a address");
        }

        Address address = addressRepository.findAddressById(addressId);
        addressRepository.save(address);
        stable.setAddress(address);
        stableRepository.save(stable);
        return address;
    }

    public List<Address> getAddressesOfStableWithMoreThan3Animals() {

        return addressRepository.findAddressesOfStableWithMoreThan3Animals();
    }
}
