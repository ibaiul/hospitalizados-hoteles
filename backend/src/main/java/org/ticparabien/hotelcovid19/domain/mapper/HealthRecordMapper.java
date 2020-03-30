package org.ticparabien.hotelcovid19.domain.mapper;

import org.springframework.stereotype.Component;
import org.ticparabien.hotelcovid19.domain.HealthRecord;
import org.ticparabien.hotelcovid19.domain.dto.HealthRecordDto;

@Component
public class HealthRecordMapper {

    public HealthRecord mapToEntity(HealthRecordDto dto) {
        return HealthRecord.builder()
                .cough(dto.getCough())
                .headache(dto.getHeadache())
                .temperature(dto.getTemperature())
                .throatAche(dto.getThroatAche())
                .build();
    }

    public HealthRecordDto mapToDto(HealthRecord entity) {
        return HealthRecordDto.builder()
                .id(entity.getId())
                .createdOn(entity.getCreatedOn())
                .patientId(entity.getPatient().getId())
                .cough(entity.getCough())
                .headache(entity.getHeadache())
                .temperature(entity.getTemperature())
                .throatAche(entity.getThroatAche())
                .build();
    }
}
