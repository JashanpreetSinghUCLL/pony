package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressRowMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address(rs.getString("street"), rs.getInt("number"), rs.getString("place"));
        address.setId(rs.getLong("id"));
        return address;
    }
}
