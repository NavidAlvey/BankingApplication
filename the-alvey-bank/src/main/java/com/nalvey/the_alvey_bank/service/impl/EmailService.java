package com.nalvey.the_alvey_bank.service.impl;

import com.nalvey.the_alvey_bank.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
