package com.mtech.recycler.helper;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.dto.CategoryDto;
import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.dto.PricingRequestDto;
import com.mtech.recycler.dto.RecycleRequestDto;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static PricingRequestDto convertRecycleRequestToPricingRequest(RecycleRequestDto recycleRequestDto) {
        PricingRequestDto pricingRequestDto = new PricingRequestDto();
        pricingRequestDto.setPromoCode(recycleRequestDto.getPromoCode());
        pricingRequestDto.setData(recycleRequestDto.getData());
        return pricingRequestDto;
    }

    public static class ItemListConverter implements DynamoDBTypeConverter<List<RecycleRequest.DbItem>, List<ItemDto>> {
        @Override
        public List<RecycleRequest.DbItem> convert(List<ItemDto> itemDtos) {
            List<RecycleRequest.DbItem> recycleDbItems = new ArrayList<>();
            for (ItemDto itemDto : itemDtos) {
                RecycleRequest.DbItem dbItem = new RecycleRequest.DbItem();
                dbItem.setCategory(itemDto.getCategory());
                dbItem.setQuantity(itemDto.getQuantity());
                dbItem.setUnitPrice(itemDto.getUnitPrice());
                dbItem.setSubTotalPrice(itemDto.getSubTotalPrice());
                dbItem.setDescription(itemDto.getDescription());
                recycleDbItems.add(dbItem);
            }
            return recycleDbItems;
        }
        @Override
        public List<ItemDto> unconvert(List<RecycleRequest.DbItem> recycleDbItems) {
            List<ItemDto> itemDtos = new ArrayList<>();
            for (RecycleRequest.DbItem dbItem : recycleDbItems) {
                ItemDto itemDto = new ItemDto();
                itemDto.setCategory(dbItem.getCategory());
                itemDto.setQuantity(dbItem.getQuantity());
                itemDto.setUnitPrice(dbItem.getUnitPrice());
                itemDto.setSubTotalPrice(dbItem.getSubTotalPrice());
                itemDto.setDescription(dbItem.getDescription());
                itemDtos.add(itemDto);
            }
            return itemDtos;
        }
    }
    /*
    public static void mapDescriptionsFromCategoryToItems (@Valid List<CategoryDto> categories, List<ItemDto> itemDtos) {
        IntStream.range(0, Math.min(categories.size(), itemDtos.size()))
                .forEach(i -> itemDtos.get(i).setDescription(categories.get(i).getDescription()));
    }*/
    /*
    public static void updateSubTotalPriceWithPromotion(@Valid List<ItemDto> itemDtos, double promoPercentage, BigDecimal pricingStrategyMultiplier) {
        itemDtos.stream()
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
    */

}
