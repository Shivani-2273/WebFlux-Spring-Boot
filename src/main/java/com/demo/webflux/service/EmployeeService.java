package com.demo.webflux.service;

import com.demo.webflux.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    void create(Employee employee);

    Mono<Employee> findById(Integer id);

    Flux<Employee> findByName(String name);

    Flux<Employee> findAll();

    Mono<Employee> update(Employee employee);

    Mono<Void> deleteUser(Employee employee);

}
