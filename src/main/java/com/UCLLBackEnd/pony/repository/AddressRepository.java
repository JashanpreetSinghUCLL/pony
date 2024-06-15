package com.UCLLBackEnd.pony.repository;

import com.UCLLBackEnd.pony.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class AddressRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Address save(Address address) {
        String sql = "INSERT INTO addresses(street, number, place) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, address.getStreet());
            ps.setInt(2, address.getNumber());
            ps.setString(3, address.getPlace());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            address.setId(keyHolder.getKey().longValue());
        }
        return address;
    }

    public Address findAddressById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM addresses WHERE id = ?", new AddressRowMapper(), id);
    }


    public List<Address> findAll() {
        return jdbcTemplate.query("SELECT * FROM addresses", new AddressRowMapper());
    }

    public List<Address> findAddressesOfStableWithMoreThan3Animals() {
        String query = "SELECT a.* " +
                "FROM addresses a " +
                "JOIN stables s ON a.id = s.address_id " +
                "WHERE (SELECT COUNT(*) FROM animals am WHERE am.stable_id = s.id) > 3";

        List<Address> addresses = jdbcTemplate.query(query, new AddressRowMapper());

        addresses.forEach(System.out::println);

        return addresses;
    }
}
