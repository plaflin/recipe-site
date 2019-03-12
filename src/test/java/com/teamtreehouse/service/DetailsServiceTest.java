package com.teamtreehouse.service;

import com.teamtreehouse.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static com.teamtreehouse.data.TestData.user1;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DetailsServiceTest
{
    @InjectMocks
    private DetailsService service = new DetailsService();

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadByUsername_LoadsCorrectUser() throws Exception
    {
        when(userRepository.findByUsernameIgnoreCase("frank")).thenReturn(user1());

        UserDetails userDetails = service.loadUserByUsername("frank");

        assertThat(userDetails.getUsername(), is("Frank"));
    }

}
