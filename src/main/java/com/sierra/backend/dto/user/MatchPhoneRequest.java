package com.sierra.backend.dto.user;

import java.util.List;

public class MatchPhoneRequest {
    List<String> phoneNumbers;

    public MatchPhoneRequest() {}

    public MatchPhoneRequest(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
