package hieu.nv.ktra2_nguyenvanhieu.repository;

import android.app.Application;

import hieu.nv.ktra2_nguyenvanhieu.dao.TransactionDAO;
import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;

import java.util.List;

public class TransactionRepository {
    private TransactionDAO transactionDAO;

    // Constructor nhận Application để khởi tạo DAO
    public TransactionRepository(Application application) {
        transactionDAO = new TransactionDAO(application);
    }

    // Các hàm bọc lại DAO
    public long insert(Transaction transaction) {
        return transactionDAO.insertTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public int update(Transaction transaction) {
        return transactionDAO.updateTransaction(transaction);
    }

    public int delete(int id) {
        return transactionDAO.deleteTransaction(id);
    }
}