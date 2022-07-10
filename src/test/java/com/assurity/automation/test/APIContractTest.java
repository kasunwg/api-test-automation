package com.assurity.automation.test;

import com.assurity.automation.bean.CategoryDetail;
import com.assurity.automation.bean.Promotion;
import com.assurity.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Optional;

@Test
public class APIContractTest {

    private static final String EXPECTED_VALUE_OF_PROPERTY_PROMOTIONS_DESCRIPTION = "Good position in category";
    private static final String EXPECTED_VALUE_OF_PROPERTY_NAME = "Carbon credits";
    private static final Boolean EXPECTED_VALUE_OF_PROPERTY_CAN_RELIST = true;
    private static final String API_ENDPOINT = "endpoint";
    private static final String EXPECTED_VALUE_OF_PROPERTY_PROMOTIONS_NAME = "Gallery";

    private static final Logger logger = LogManager.getLogger(APIContractTest.class);

    ResponseEntity<CategoryDetail> response;

    @BeforeTest
    public void setup() {
        logger.info("API Contract test started.");
        ConfigReader configReader = new ConfigReader();
        String apiEndpoint = configReader.getProperty(API_ENDPOINT);
        RestTemplate restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(apiEndpoint, CategoryDetail.class);
    }

    @Test(description = "Validates the api response, verify status of response and content")
    public void apiResponseStatusValidation() {
        Assert.assertEquals(response.getStatusCode(), (HttpStatus.OK), "Failed to get response.");
        Assert.assertNotNull(response.getBody(), "Response body is NULL");
    }

    @Test(dependsOnMethods = {"apiResponseStatusValidation"}, description = "Name property validation")
    public void apiNamePropertyValidation() {
        Assert.assertEquals(response.getBody().getName(), EXPECTED_VALUE_OF_PROPERTY_NAME,
                "Expected value: " + EXPECTED_VALUE_OF_PROPERTY_NAME + " of property Name is different from actual: " + response.getBody().getName());
    }

    @Test(dependsOnMethods = {"apiNamePropertyValidation"}, description = "CanRelist property validation")
    public void apiCanRelistPropertyValidation() {
        Assert.assertEquals(response.getBody().isCanRelist(), EXPECTED_VALUE_OF_PROPERTY_CAN_RELIST,
                "Expected value: " + EXPECTED_VALUE_OF_PROPERTY_NAME + " of property CanRelist is different from actual: " + response.getBody().isCanRelist());
    }

    @Test(dependsOnMethods = {"apiCanRelistPropertyValidation"}, description = "Promotions property validation")
    public void apiPromotionsPropertyValidation() {
        Optional<Promotion> promotionOptional = response.getBody().getPromotions().stream().filter(it ->
                it.getName().equals(EXPECTED_VALUE_OF_PROPERTY_PROMOTIONS_NAME) && it.getDescription().contains(EXPECTED_VALUE_OF_PROPERTY_PROMOTIONS_DESCRIPTION)
        ).findAny();

        Assert.assertTrue(promotionOptional.isPresent(), "Matching promotion doesn't exist.");
    }

    @AfterTest
    public void teardown() {
        logger.info("API Contract test finished.");
    }

}
