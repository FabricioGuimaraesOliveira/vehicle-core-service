package com.fiap.vehicle.core.infrastructure.veiculo.gateway;

import com.fiap.vehicle.core.domain.entity.veiculo.gateway.VehicleGateway;
import com.fiap.vehicle.core.domain.entity.veiculo.model.Vehicle;
import com.fiap.vehicle.core.infrastructure.persistence.veiculo.VehicleEntity;
import com.fiap.vehicle.core.infrastructure.persistence.veiculo.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VeiculoDataBaseRepository implements VehicleGateway {

    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    public VeiculoDataBaseRepository(VehicleRepository vehicleRepository, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Vehicle salvar(Vehicle vehicle) {
        VehicleEntity vehicleEntity = modelMapper.map(vehicle, VehicleEntity.class);
        vehicleRepository.saveAndFlush(vehicleEntity);
        return modelMapper.map(vehicleEntity, Vehicle.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> buscarVeiculoPorId(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicleEntity -> modelMapper.map(vehicleEntity, Vehicle.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Vehicle> listarPaginado(Pageable pageable) {
        Page<VehicleEntity> vehicleEntitiesPage = vehicleRepository.findAll(pageable);
        return vehicleEntitiesPage.map(vehicleEntity -> modelMapper.map(vehicleEntity, Vehicle.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> buscarTodos() {
        List<VehicleEntity> vehicleEntities = vehicleRepository.findAll();
        return vehicleEntities.stream()
                .map(vehicleEntity -> modelMapper.map(vehicleEntity, Vehicle.class))
                .collect(Collectors.toList());
    }
}
