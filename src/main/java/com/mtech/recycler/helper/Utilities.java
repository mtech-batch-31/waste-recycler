package com.mtech.recycler.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.Item;
import com.mtech.recycler.model.PricingRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Utilities {

    public static boolean isMatchedPassword(String inputRawPassword, String encodedPasswordFromDatabase) {
        return encoder().matches(inputRawPassword, encodedPasswordFromDatabase);
    }

    public static String encodePassword(String inputRawPassword) {
        return encoder().encode(inputRawPassword);
    }

    private static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");

    public static boolean isValidPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    private static final Pattern VALID_CONTACT_NUMBER_REGEX = Pattern.compile("^(\\+?65)?[689]\\d{7}$");

    public static boolean isValidContactNumber(String contactNumber) {
        Matcher matcher = VALID_CONTACT_NUMBER_REGEX.matcher(contactNumber);
        return matcher.matches();
    }

    private static final Pattern VALID_SG_POSTAL_CODE = Pattern.compile("^\\d{6}$");

    public static boolean isValidPostalCode(String postalCode) {
        Matcher matcher = VALID_SG_POSTAL_CODE.matcher(postalCode);
        return matcher.matches();
    }

    public static List<RecycleRequest.RequestItem> convertToRequestItem(List<Item> items) {
        List<RecycleRequest.RequestItem> recycleRequestItems = new ArrayList<>();
        for (Item item : items) {
            RecycleRequest.RequestItem requestItem = new RecycleRequest.RequestItem();
            requestItem.setCategory(item.getCategory());
            requestItem.setQuantity(item.getQuantity());
            requestItem.setUnitPrice(item.getUnitPrice());
            requestItem.setSubTotalPrice(item.getSubTotalPrice());
            requestItem.setDescription(item.getDescription());
            recycleRequestItems.add(requestItem);
        }
        return recycleRequestItems;
    }

    public static PricingRequest convertRecycleRequestToPricingRequest(com.mtech.recycler.model.RecycleRequest recycleRequest) {
        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode(recycleRequest.getPromoCode());
        pricingRequest.setData(recycleRequest.getData());
        return pricingRequest;
    }

    public static class ItemListConverter implements DynamoDBTypeConverter<List<RecycleRequest.RequestItem>, List<Item>> {
        @Override
        public List<RecycleRequest.RequestItem> convert(List<Item> items) {
            List<RecycleRequest.RequestItem> recycleRequestItems = new ArrayList<>();
            for (Item item : items) {
                RecycleRequest.RequestItem requestItem = new RecycleRequest.RequestItem();
                requestItem.setCategory(item.getCategory());
                requestItem.setQuantity(item.getQuantity());
                requestItem.setUnitPrice(item.getUnitPrice());
                requestItem.setSubTotalPrice(item.getSubTotalPrice());
                requestItem.setDescription(item.getDescription());
                recycleRequestItems.add(requestItem);
            }
            return recycleRequestItems;
        }
        @Override
        public List<Item> unconvert(List<RecycleRequest.RequestItem> recycleRequestItems) {
            List<Item> items = new ArrayList<>();
            for (RecycleRequest.RequestItem requestItem : recycleRequestItems) {
                Item item = new Item();
                item.setCategory(requestItem.getCategory());
                item.setQuantity(requestItem.getQuantity());
                item.setUnitPrice(requestItem.getUnitPrice());
                item.setSubTotalPrice(requestItem.getSubTotalPrice());
                item.setDescription(requestItem.getDescription());
                items.add(item);
            }
            return items;
        }
    }

    public static void mapDescriptionsFromCategoryToItems (@Valid List<Category> categories, List<Item> items) {
        IntStream.range(0, Math.min(categories.size(), items.size()))
                .forEach(i -> items.get(i).setDescription(categories.get(i).getDescription()));
    }

    public static void updateSubTotalPriceWithPromotion(@Valid List<Item> items, double promoPercentage, BigDecimal pricingStrategyMultiplier) {
        items.stream()
                .peek(item -> {
                    item.setDescription(item.getDescription());
                    BigDecimal subTotalPrice = item.getSubTotalPrice();
                    subTotalPrice = subTotalPrice.add(subTotalPrice.multiply(BigDecimal.valueOf(promoPercentage)));
                    subTotalPrice = subTotalPrice.multiply(pricingStrategyMultiplier);
                    subTotalPrice = subTotalPrice.setScale(2, RoundingMode.CEILING);
                    item.setSubTotalPrice(subTotalPrice);
                })
                .forEach(item -> {});
    }


}
