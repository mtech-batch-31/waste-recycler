package com.mtech.recycler.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.entity.RecycleItem;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.Item;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.RecycleRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static PricingRequest convertSubmitRequestToPricingRequest(RecycleRequest submitRequest) {
        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode(submitRequest.getPromoCode());
        pricingRequest.setData(submitRequest.getData());
        return pricingRequest;
    }

    public static class ItemListConverter implements DynamoDBTypeConverter<List<RecycleItem.DbItem>, List<Item>> {
        @Override
        public List<RecycleItem.DbItem> convert(List<Item> items) {
            List<RecycleItem.DbItem> recycleDbItems = new ArrayList<>();
            for (Item item : items) {
                RecycleItem.DbItem dbItem = new RecycleItem.DbItem();
                dbItem.setCategory(item.getCategory());
                dbItem.setQuantity(item.getQuantity());
                dbItem.setUnitPrice(item.getUnitPrice());
                dbItem.setSubTotalPrice(item.getSubTotalPrice());
                recycleDbItems.add(dbItem);
            }
            return recycleDbItems;
        }
        @Override
        public List<Item> unconvert(List<RecycleItem.DbItem> recycleDbItems) {
            List<Item> items = new ArrayList<>();
            for (RecycleItem.DbItem dbItem : recycleDbItems) {
                Item item = new Item();
                item.setCategory(dbItem.getCategory());
                item.setQuantity(dbItem.getQuantity());
                item.setUnitPrice(dbItem.getUnitPrice());
                item.setSubTotalPrice(dbItem.getSubTotalPrice());
                item.setDescription(dbItem.getDescription());
                items.add(item);
            }
            return items;
        }
    }

    public static List<Item> mapDescriptions(@Valid List<Category> categories, List<Item> items) {
        Iterator<Category> pricingIterator = categories.iterator();
        return items.stream()
                .map(item -> {
                    if (pricingIterator.hasNext()) {
                        item.setDescription(pricingIterator.next().getDescription());
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

}
