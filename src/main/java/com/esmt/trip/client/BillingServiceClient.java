package com.esmt.trip.client;

import com.esmt.trip.dto.DebitRequest;
import com.esmt.trip.dto.DebitResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "billing-service")
public interface BillingServiceClient {
    @PostMapping("/api/billing/debit")
    DebitResponse debit(@RequestBody DebitRequest request);
}