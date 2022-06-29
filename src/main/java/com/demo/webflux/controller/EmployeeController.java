package com.demo.webflux.controller;

import com.demo.webflux.DAO.EmployeeRepository;
import com.demo.webflux.model.Employee;
import com.demo.webflux.model.Message;
import com.demo.webflux.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/flux")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Employee employee){
        employeeService.create(employee);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Employee>> findById(@PathVariable("id") Integer id){
        return employeeService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public Flux<Employee> findByName(@PathVariable("name") String name){
        return employeeService.findByName(name);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Employee> findAll(){
        Flux<Employee> employeeFlux=employeeService.findAll();
        return  employeeFlux;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Employee>> update(@RequestBody Employee employee){
        return employeeService.update(employee).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Message>> delete(@PathVariable("id") int id){
        return employeeService.findById(id)
                .flatMap(existingEmployee ->
                        employeeService.deleteUser(existingEmployee)
                                .then(Mono.just(new ResponseEntity<Message>(new Message("success"),HttpStatus.OK)))
                )

                .defaultIfEmpty(new ResponseEntity<Message>(new Message("Failure"),HttpStatus.NOT_FOUND));
    }
}
