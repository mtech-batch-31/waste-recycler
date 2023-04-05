package com.mtech.recycler.service;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
//@RunWith(SpringRunner.class)
////@Import(DynamoDBTestConfig.class)
//@TestPropertySource(properties = {
//        "amazon.aws.accesskey=test",
//        "amazon.aws.secretkey=test",
//        "amazon.aws.region=us-west-2",
//        "amazon.dynamodb.endpoint=http://localhost:8000"
//})
public class UserServiceImplTest {

    @MockBean
    private AmazonDynamoDB amazonDynamoDB;

//    @Mock
//    private AmazonDynamoDB amazonDynamoDB;

//    @InjectMocks
//    private AmazonDynamoDBClientBuilderWrapper amazonDynamoDBClientBuilderWrapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

//    @TestConfiguration
//    static class TestConfig {
//
//        @Bean
//        public AmazonDynamoDB amazonDynamoDB() {
//            return mock(AmazonDynamoDB.class);
//        }
//    }

//    @BeforeEach
//    public void setupEach() {
//        DescribeTableResult describeTableResult = new DescribeTableResult()
//                .withTable(new TableDescription().withTableName("myTable"));
//        when(amazonDynamoDB.describeTable(any(DescribeTableRequest.class))).thenReturn(describeTableResult);
//    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(amazonDynamoDB.describeTable(any(DescribeTableRequest.class)))
                .thenReturn(new DescribeTableResult().withTable(new TableDescription().withTableName("myTable")));

        when(amazonDynamoDB.describeTable(anyString()))
                .thenReturn(new DescribeTableResult().withTable(new TableDescription().withTableName("myTable")));
    }

    @Test
    void testCreateUser_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword("123");
        userService.createUser(registerRequest);
        verify(userRepository, times(1)).save(any());
    }
}

