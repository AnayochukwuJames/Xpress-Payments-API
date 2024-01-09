package com.example.xpresspaymentapi.model.repository;

import com.example.xpresspaymentapi.model.entity.TelecomNetworkProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelecomNetworkProviderRepository extends JpaRepository<TelecomNetworkProvider, Long> {
    TelecomNetworkProvider findTelecomNetworkProviderByProviderNameEqualsIgnoreCase(String providerName);
}
