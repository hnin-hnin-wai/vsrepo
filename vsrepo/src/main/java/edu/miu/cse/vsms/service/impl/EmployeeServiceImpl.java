package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.EmployeeRequestDto;
import edu.miu.cse.vsms.dto.response.EmployeeResponseDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.model.VService;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto request) {
        // Write your code here
        Employee employee = new Employee();
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setPhone(request.phone());
        employee.setHireDate(request.hireDate());

       List<VService> vServices = new ArrayList<>();
//        VService vService = new VService();
        employee.getVServices().addAll(vServices);

        employeeRepository.save(employee);

        return Optional.of(EmployeeResponseDto.builder().id(employee.getId()).build(),
                EmployeeResponseDto.builder().name(employee.getName()).build(),
                EmployeeResponseDto.builder().email(employee.getEmail()).build(),
                EmployeeResponseDto.builder().phone(employee.getPhone()).build(),
                EmployeeResponseDto.builder().hireDate(employee.getHireDate()).build(),
                EmployeeResponseDto.builder().services(employee.getVServices().stream().map(VService::getClass).collect(Collectors.toList())
                );

       // return null ;

    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        // Write your code here
       List<Employee> employees =employeeRepository.findAll();
       List<EmployeeResponseDto> employeeResponseDtos = new ArrayList<>();
//       for(Employee employee : employees) {
//           employeeResponseDtos.add(EmployeeResponseDto.builder().id(employee.getId()).build());
//           employeeResponseDtos.add(EmployeeResponseDto.builder().name(employee.getName()).build());
//           employeeResponseDtos.add(EmployeeResponseDto.builder().email(employee.getEmail()).build());
//           employeeResponseDtos.add(EmployeeResponseDto.builder().phone(employee.getPhone()).build());
//           employeeResponseDtos.add(EmployeeResponseDto.builder().hireDate(employee.getHireDate()).build());
//           //employeeResponseDtos.add(EmployeeResponseDto.builder().services(employee.getVServices().stream().collect(Collectors.toList()).reversed()).build();
//       }
        return employeeResponseDtos;
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        // Write your code here
        Employee foundEmployee=employeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(foundEmployee!=null) {
            return EmployeeResponseDto.builder()
                    .id(foundEmployee.getId())
                    .name(foundEmployee.getName())
                    .email(foundEmployee.getEmail())
                    .phone(foundEmployee.getPhone())
                    .hireDate(foundEmployee.getHireDate())
                    .build();
        }
        throw new EntityNotFoundException(id + " not found");
    }

    @Override
    public EmployeeResponseDto partiallyUpdateEmployee(Long id, Map<String, Object> updates) {
        // Fetch the employee by ID or throw an exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));

        // Apply each update based on the key
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    // Write your code here
                    employee.setName((String) value);

                    break;
                case "email":
                    // Write your code here
                    employee.setEmail((String) value);

                    break;
                case "phone":
                    // Write your code here
                    employee.setPhone((String) value);

                    break;
                case "hireDate":
                    // Write your code here
                    employee.setHireDate(LocalDate.parse((String) value));

                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        // Write your code here
        employeeRepository.save(employee);
        ///return left

        return null;
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        List<VehicleServiceResponseDto> serviceDtos = employee.getVServices().stream()
                .map(service -> new VehicleServiceResponseDto(
                        service.getId(),
                        service.getServiceName(),
                        service.getCost(),
                        service.getVehicleType()
                )).toList();

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                serviceDtos
        );
    }
}
