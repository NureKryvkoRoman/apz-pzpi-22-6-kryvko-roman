package ua.nure.kryvko.roman.apz.controller;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ControllerRepository extends JpaRepository<Controller, Integer> {
    List<Controller> findByGreenhouseId(Integer greenhouseId);
}
