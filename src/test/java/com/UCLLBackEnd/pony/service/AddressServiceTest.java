package com.UCLLBackEnd.pony.service;

import com.UCLLBackEnd.pony.model.Address;
import com.UCLLBackEnd.pony.model.Stable;
import com.UCLLBackEnd.pony.repository.AddressRepository;
import com.UCLLBackEnd.pony.repository.StableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StableRepository stableRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenAddress_whenAddAddress_thenAddressIsAdded() {

        Address address = new Address("Erenstraat", 23, "Turnhout");
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.addAddress(address);

        assertEquals(address, result);
        verify(addressRepository).save(address);
    }

    @Test
    public void givenStable_whenAddNewStableToNewAddress_thenStableWithAddressIsAdded() {

        Address address = new Address("Erenstraat", 23, "Turnhout");
        Stable stable = new Stable("Sunny's Stable", 5);
        stable.setAddress(address);
        when(stableRepository.save(stable)).thenReturn(stable);
        when(addressRepository.save(address)).thenReturn(address);


        Address result = addressService.addNewStableToNewAddress(stable);
        assertEquals(stable.getAddress(), result);
        verify(stableRepository).save(stable);
        verify(addressRepository).save(address);
    }

    @Test
    public void givenNonExistingStableId_whenAddStableToAddress_thenExceptionIsThrown() {

        Long stableId = 1L;
        Address address = new Address("Erenstraat", 23, "Turnhout");
        address.setId(1L);
        when(stableRepository.findById(stableId)).thenReturn(Optional.empty());

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            addressService.addStableToAddress(address.getId(), stableId);
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Stable with id " + stableId + " doesn't exist", ex.getMessage());
        verify(stableRepository).findById(stableId);
    }

    @Test
    public void givenStableIdWithAlreadyAssignedAddress_whenAddStableToAddress_thenExceptionIsThrown() {

        Stable stable = new Stable("Sunny's Stable", 5);
        stable.setId(1L);
        Address address = new Address("Erenstraat", 23, "Turnhout");
        address.setId(1L);
        stable.setAddress(address);
        Address address1 = new Address("Johnstraat", 32, "Burnout");
        address1.setId(2L);
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));

        ServiceException ex = assertThrows(ServiceException.class, () -> {
            addressService.addStableToAddress(address1.getId(), stable.getId());
        });

        assertEquals("ServiceException", ex.getField());
        assertEquals("Stable with id " + stable.getId() + " already has a address", ex.getMessage());
        verify(stableRepository).findById(stable.getId());
    }

    @Test
    public void givenStableIdAndAddressID_whenAddStableToAddress_thenStableToAddressIsAdded() {

        Stable stable = new Stable("Sunny's Stable", 5);
        stable.setId(1L);
        Address address = new Address("Erenstraat", 23, "Turnhout");
        address.setId(1L);
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(addressRepository.findAddressById(address.getId())).thenReturn(address);

        Address result = addressService.addStableToAddress(address.getId(), stable.getId());

        assertEquals(stable.getAddress(), result);
        verify(stableRepository).findById(stable.getId());
        verify(addressRepository).findAddressById(address.getId());
        verify(stableRepository).save(stable);
        verify(addressRepository).save(address);
    }

    @Test
    public void getAddressesOfStableWithMoreThan3Animals_thenAddressesAreReturned() {

        Address address1 = new Address("Erenstraat", 23, "Turnhout");
        Address address2 = new Address("Johnstraat", 29, "Burnhout");

        when(addressRepository.findAddressesOfStableWithMoreThan3Animals()).thenReturn(List.of(address1, address2));

        List<Address> addresses = addressService.getAddressesOfStableWithMoreThan3Animals();

        assertEquals(2, addresses.size());
        assertEquals(address1, addresses.get(0));
        assertEquals(address2, addresses.get(1));
        verify(addressRepository).findAddressesOfStableWithMoreThan3Animals();
    }

}