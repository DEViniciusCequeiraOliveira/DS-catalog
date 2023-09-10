package com.devinicius.DsCatolog.resource;

import com.devinicius.DsCatolog.dto.ProductDTO;
import com.devinicius.DsCatolog.services.ProductService;
import com.devinicius.DsCatolog.services.exceptions.EntityNotFoundExceptions;
import com.devinicius.DsCatolog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    final private String resourceMapping = "/products";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @InjectMocks
    private ProductResource productResource;
    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;
    private Long idExists;
    private Long idNotExists;

    @BeforeEach
    public void setUp() {
        idExists = 1L;
        idNotExists = 2L;

        productDTO = Factory.createProductDTO();


        page = new PageImpl<>(List.of(productDTO));
        //Find All
        Mockito.when(productService.findAllPaged(any())).thenReturn(page);
        //Find by Id
        Mockito.when(productService.findbyId(idExists)).thenReturn(productDTO);
        Mockito.when(productService.findbyId(idNotExists)).thenThrow(EntityNotFoundExceptions.class);
        //Insert One
        Mockito.when(productService.insertOne(any())).thenReturn(productDTO);
        //Update
        Mockito.when(productService.updateOne(eq(idExists), any())).thenReturn(productDTO);
        Mockito.when(productService.updateOne(eq(idNotExists), any())).thenThrow(EntityNotFoundExceptions.class);
        //delete
        Mockito.doNothing().when(productService).deleteOne(idExists);
        Mockito.doThrow(EntityNotFoundExceptions.class).when(productService).deleteOne(idNotExists);
    }

    @Test
    public void findAllPagedShouldReturnAllPaged() throws Exception {
        mockMvc.perform(get(resourceMapping)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        mockMvc.perform(get(resourceMapping + "/{id}", idExists)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void insertOneShouldInsertNewProduct() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(post(resourceMapping)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateOneShouldUpdateProductWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put(resourceMapping + "/{id}", idExists)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateOneShouldUpdateProductWhenIdNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put(resourceMapping + "/{id}", idNotExists)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    public void deleteOneShouldDeleteProductWhenIdExists() throws Exception {
        mockMvc.perform(delete(resourceMapping + "/{id}", idExists)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteOneShouldDeleteProductWhenIdNotExists() throws Exception {
        mockMvc.perform(delete(resourceMapping + "/{id}", idNotExists)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
