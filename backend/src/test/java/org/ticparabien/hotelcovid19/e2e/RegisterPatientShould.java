package org.ticparabien.hotelcovid19.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.ticparabien.hotelcovid19.domain.dto.RegisterPatientRequestDto;
import org.ticparabien.hotelcovid19.domain.repositories.PatientRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = "PERSONNEL")
class RegisterPatientShould {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void beforeEach() {
        patientRepository.deleteAll();
    }

    @Test
    void registering_patient_should_allow_employee_to_get_password_and_hand_it_to_patient_and_to_get_patient_info() throws Exception {
        String name = "Eustaquio";
        String personalId = "personalId";
        String phone = "phone";
        Integer age = 20;
        RegisterPatientRequestDto dto = RegisterPatientRequestDto.builder()
                .name(name)
                .personalId(personalId)
                .phone(phone)
                .age(age)
                .build();
        String body = objectMapper.writeValueAsString(dto);

        String patientResourceUrl = mvc.perform(post("/api/patients")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("$.password", notNullValue()))
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.LOCATION);

        mvc.perform(get(patientResourceUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.personalId", is(personalId)))
                .andExpect(jsonPath("$.phone", is(phone)))
                .andExpect(jsonPath("$.age", is(age)));

    }
}


