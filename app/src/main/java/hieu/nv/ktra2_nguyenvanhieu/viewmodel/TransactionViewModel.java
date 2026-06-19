package hieu.nv.ktra2_nguyenvanhieu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hieu.nv.ktra2_nguyenvanhieu.model.Transaction;
import hieu.nv.ktra2_nguyenvanhieu.repository.TransactionRepository;

import java.util.List;
public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository repository;
    private MutableLiveData<List<Transaction>> listTransactions;
    private MutableLiveData<String> message;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
        listTransactions = new MutableLiveData<>();
        message = new MutableLiveData<>();
        loadTransactions();
    }
    public MutableLiveData<List<Transaction>> getListTransactions() {
        return listTransactions;
    }
    public MutableLiveData<String> getMessage() {
        return message;
    }
    public void loadTransactions() {
        List<Transaction> data = repository.getAllTransactions();
        listTransactions.setValue(data);
    }
    public void insert(Transaction transaction) {
        long result = repository.insert(transaction);
        if (result > 0) {
            message.setValue("Thêm giao dịch thành công!");
            loadTransactions();
        } else {
            message.setValue("Lỗi khi thêm giao dịch!");
        }
    }
    public void update(Transaction transaction) {
        int result = repository.update(transaction);
        if (result > 0) {
            message.setValue("Cập nhật giao dịch thành công!");
            loadTransactions();
        } else {
            message.setValue("Lỗi khi cập nhật giao dịch!");
        }
    }
    public void delete(int id) {
        int result = repository.delete(id);
        if (result > 0) {
            message.setValue("Đã xóa giao dịch!");
            loadTransactions();
        } else {
            message.setValue("Lỗi khi xóa!");
        }
    }
}