package com.Silvador386.ordering.repository;

import com.Silvador386.ordering.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
