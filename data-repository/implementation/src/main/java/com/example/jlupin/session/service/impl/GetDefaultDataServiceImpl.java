package com.example.jlupin.session.service.impl;

import com.example.jlupin.session.service.interfaces.GetDefaultDataService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Piotr Heilman
 */
@Service(value = "getDefaultDataService")
public class GetDefaultDataServiceImpl implements GetDefaultDataService {
    @Override
    public Map<String, String> getDefaultSessionParamters() {
        Map<String, String> result = new HashMap<>();

        result.put("DEFAULT_KEY_ONE", "defaultValueOne");
        result.put("DEFAULT_KEY_TWO", "defaultValueTwo");

        return result;
    }
}