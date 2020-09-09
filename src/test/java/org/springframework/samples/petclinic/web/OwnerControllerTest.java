package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @InjectMocks
    OwnerController ownerController;

    @Mock
    ClinicService clinicService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void testProcessCreationFormPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("address", "123 Duval St.")
                .param("telephone", "1234567890")
                .param("city", "Key West"))
                .andExpect(status().is3xxRedirection());

        verify(clinicService).saveOwner(any(Owner.class));
    }

    @Test
    void testProcessCreationFormPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("address", "123 Duval St."))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(model().attributeHasFieldErrors("owner", "city"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testLastNameNull() throws Exception {
        mockMvc.perform(get("/owners")
                .param("firstName", "firstName"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }


    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameOneFound() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setLastName("Found");
        given(clinicService.findOwnerByLastName(anyString())).willReturn(Set.of(owner));

        mockMvc.perform(get("/owners")
                .param("lastName", "Found"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void testFindByNameMultipleFound() throws Exception {
        Owner owner1 = new Owner();
        owner1.setId(1);
        owner1.setLastName("Found1");
        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setLastName("Found2");
        Collection<Owner> owners = Set.of(owner1, owner2);

        given(clinicService.findOwnerByLastName(anyString())).willReturn(owners);

        mockMvc.perform(get("/owners")
                .param("lastName", "Found"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selections", owners))
                .andExpect(view().name("owners/ownersList"));
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testProcessUpdateFormPostValid() throws Exception {
        mockMvc.perform(post("/owners/1/edit")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("address", "123 Duval St.")
                .param("telephone", "1234567890")
                .param("city", "Key West"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));

        verify(clinicService).saveOwner(any(Owner.class));
    }

    @Test
    void testProcessUpdateFormPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/1/edit")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("address", "123 Duval St."))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(model().attributeHasFieldErrors("owner", "city"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

}