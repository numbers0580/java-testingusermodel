package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.RoleService;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private List<User> listofUsers = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        //listofUsers = new ArrayList<>();

        //userService.deleteAll();
        //roleService.deleteAll();
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        /*
        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);
        */
        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(1);

        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(145);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(168);

        //userService.save(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(2);

        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(196);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(212);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(345);
        //userService.save(u2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(3);

        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(4096);
        //userService.save(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(4);

        u4.getRoles().add(new UserRoles(u4, r2));
        //userService.save(u4);

        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(5);

        u5.getRoles().add(new UserRoles(u5, r2));
        //userService.save(u5);

        listofUsers.add(u1);
        listofUsers.add(u2);
        listofUsers.add(u3);
        listofUsers.add(u4);
        listofUsers.add(u5);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(listofUsers);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listofUsers);

        assertEquals(er, tr);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/145";
        //Mockito.when(userService.findUserById(any(Long.class))).thenReturn(listofUsers.get(0));
        Mockito.when(userService.findUserById(145)).thenReturn(listofUsers.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listofUsers.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        String apiUrl = "/users/user/101";
        //Mockito.when(userService.findUserById(any(Long.class))).thenReturn(listofUsers.get(0));
        Mockito.when(userService.findUserById(101)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void getUserByName() throws Exception {
        String apiUrl = "/users/user/name/cinnamon";
        Mockito.when(userService.findByName("cinnamon")).thenReturn(listofUsers.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listofUsers.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void getUserByNameNotFound() throws Exception {
        String apiUrl = "/users/user/name/cinnamony";
        Mockito.when(userService.findByName("cinnamony")).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception {
        String apiUrl = "/users/user/name/like/da";
        Mockito.when(userService.findByNameContaining("da")).thenReturn(listofUsers);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listofUsers);

        assertEquals(er, tr);
    }

    @Test
    public void getUserLikeNameNotFound() throws Exception {
        String apiUrl = "/users/user/name/like/xyz";
        Mockito.when(userService.findByNameContaining("xyz")).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void addNewUser() throws Exception {
        String apiUrl = "/users/user";

        User uX = new User("ginger",
                "calico3660",
                "ginger@sanpedro.court");
        uX.setUserid(6);

        Role role1 = new Role("House cat");
        Role role2 = new Role("Hunter");
        role1.setRoleid(4);
        role2.setRoleid(5);

        uX.getRoles().add(new UserRoles(uX, role1));
        uX.getRoles().add(new UserRoles(uX, role2));
        uX.getUseremails()
                .add(new Useremail(uX,
                        "gingerthecat@mymail.local"));
        uX.getUseremails().get(0).setUseremailid(5175);
        uX.getUseremails()
                .add(new Useremail(uX,
                        "gotclaws@mymail.local"));
        uX.getUseremails().get(1).setUseremailid(6033);
        uX.getUseremails()
                .add(new Useremail(uX,
                        "mousekiller@email.local"));
        uX.getUseremails().get(2).setUseremailid(7474);

        ObjectMapper mapper = new ObjectMapper();
        String catString = mapper.writeValueAsString(uX);

        Mockito.when(userService.save(any(User.class))).thenReturn(uX);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(catString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/users/user/15";

        User ux = new User("johnson", "abcabc", "james@jackson.edu");
        ux.setUserid(27);
        ux.getUseremails().add(new Useremail(ux, "jimmyboy@jacobite.org"));
        ux.getUseremails().get(0).setUseremailid(1234);

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(ux);

        Mockito.when(userService.save(any(User.class))).thenReturn(ux);
        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(er);

        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/users/user/15";

        User ux = new User("wolverine", "adamantium", "logan@weaponx.org");
        ux.setUserid(4);

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(ux);

        Mockito.when(userService.save(any(User.class))).thenReturn(ux);
        RequestBuilder rb = MockMvcRequestBuilders.patch(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(er);

        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() {
    }
}