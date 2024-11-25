package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.ServiceRequestDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.exception.ResourceNotFoundException;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.model.VService;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.repository.VehicleServiceRepository;
import edu.miu.cse.vsms.service.VehicleService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public VehicleServiceResponseDto assignService(ServiceRequestDto request) {
        // Write your code here
        Optional<Employee> employee=employeeRepository.findById(request.employeeId());
        if(employee.isPresent()) {
            //id,cost,name,vehicleType
            VService vService = new VService(
                    request.serviceName(),
                    request.cost(),
                    request.vehicleType(),
                    employee.get()
            );

            VService vService1=vehicleServiceRepository.save(vService);
            return new VehicleServiceResponseDto(vService1.getId(),vService1.getServiceName(),vService1.getCost(),vService1.getVehicleType());

        }
        throw new ResourceNotFoundException("Employee not found");
    }
}
