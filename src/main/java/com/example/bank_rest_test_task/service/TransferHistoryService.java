package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.entity.TransferHistory;
import com.example.bank_rest_test_task.repository.TransferHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransferHistoryService {
    private final TransferHistoryRepository transferHistoryRepository;

    public TransferHistoryService(TransferHistoryRepository transferHistoryRepository) {
        this.transferHistoryRepository = transferHistoryRepository;
    }

    public void saveTransferHistory(TransferHistory transferHistory) {
        transferHistoryRepository.save(transferHistory);
    }

    public Page<TransferHistory> getTransferHistoryByUsernamePaginated(String username, Pageable pageable) {
        return transferHistoryRepository.findByUser_Username(username, pageable);
    }
}
