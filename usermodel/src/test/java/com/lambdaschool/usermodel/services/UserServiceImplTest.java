package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    //@MockBean
    //private UserRepository userrepos;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<User> allUsers = userService.findAll();
        for(User acct : allUsers) {
            System.out.println("ID: " + acct.getUserid() + ", User: " + acct.getUsername());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void afindUserById() {
        assertEquals("barnbarn", userService.findUserById(11).getUsername());
    }

    @Test (expected = EntityNotFoundException.class)
    public void bfindUserByIdNotFound() {
        // id not found
        assertEquals("barnbarn", userService.findUserById(111).getUsername());
    }

    @Test
    public void cfindByNameContaining() {
        assertEquals(1, userService.findByNameContaining("bar").size());
        //assertEquals(2, userService.findByNameContaining("mi").size());
    }

    @Test
    public void dfindAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void edelete() {
        userService.delete(14);
    }

    @Test (expected = EntityNotFoundException.class)
    public void fcannotDelete() {
        // id not found
        userService.delete(16);
    }

    @Test
    public void gfindByName() {
        assertEquals(11, userService.findByName("barnbarn").getUserid());
    }

    @Test (expected = EntityNotFoundException.class)
    public void hfindByNameNotFound() {
        assertEquals(13, userService.findByName("admi").getUserid());
        assertEquals(13, userService.findByName("admini").getUserid());
    }

    @Test
    public void isave() {
        User newUser = new User("numbers0580", "uniquepassword", "numbers@letters.net");
        newUser.getUseremails().add(new Useremail(newUser, "thatguy@thatdomain.com"));

        User loadUser = userService.save(newUser);
        assertEquals(loadUser.getUsername(), "numbers0580");
    }

    @Test
    public void jupdate() {
        User newUser = new User("FUJI", "musicman", "nunya@pueblo.local");
        newUser.setUserid(13);

        User updateUser = userService.update(newUser, 13);
        assertEquals(updateUser.getUsername(), "fuji");
    }

    @Test
    public void kdeleteAll() {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}