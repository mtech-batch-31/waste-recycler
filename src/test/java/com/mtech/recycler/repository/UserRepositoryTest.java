package com.mtech.recycler.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.mtech.recycler.RecyclerApplication;
import com.mtech.recycler.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RecyclerApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=jck2p",
        "amazon.aws.secretkey=edmbb",
        "amazon.aws.region=ap-southeast-1"})
public class UserRepositoryTest {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setup() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

//        CreateTableRequest tableRequest = dynamoDBMapper
//                .generateCreateTableRequest(User.class);
//        tableRequest.setProvisionedThroughput(
//                new ProvisionedThroughput(1L, 1L));
//        amazonDynamoDB.createTable(tableRequest);
//
//        //...
//
//        dynamoDBMapper.batchDelete(
//                (List<ProductInfo>)repository.findAll());
    }

    @Test
    public void givenItemWithExpectedCost_whenRunFindAll_thenItemIsFound() {

        List<User> result = (List<User>) repository.findAll();
        Assert.assertEquals(1, result.size());
    }
}
