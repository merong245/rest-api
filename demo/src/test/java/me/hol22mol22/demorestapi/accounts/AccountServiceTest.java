package me.hol22mol22.demorestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUsername(){
        // Given
        String username = "hol22mol22@email.com";
        String password = "junhyeok";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountRepository.save(account);


        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);


    }

    // 예외 타입만 확인가능
    @Test(expected = UsernameNotFoundException.class)
    public void findByUsernameFail1(){
        String username = "random@email.com";
        accountService.loadUserByUsername(username);
    }

    // 예외 타입과 메시지 확인가능 but, 코드가 다소 복잡
    @Test
    public void findByUsernameFail2(){
        String username = "random@email.com";
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        }
        catch (UsernameNotFoundException e){
            assertThat(e.getMessage()).containsSequence(username);
        }
    }

    // 코드 간결하고 예외 타입과 메시지 모두 확인 가능
    @Test
    public void findByUsernameFail3(){
        // Expected
        String username = "random@email.com";
        // 예상 결과를 미리 적어줌
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);
    }

}