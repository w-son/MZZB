package com.son.mzzb.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.son.mzzb.matzip.MatzipRepository;
import com.son.mzzb.matzip.MatzipService;
import com.son.mzzb.tmi.TmiService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Ignore
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    // serializing
    @Autowired
    protected ObjectMapper objectMapper;

    // model to DTO mapping
    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected MatzipService matzipService;

    @Autowired
    protected TmiService tmiService;

}