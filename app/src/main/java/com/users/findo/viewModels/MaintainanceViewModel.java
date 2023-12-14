package com.users.findo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.users.findo.dataClasses.Misc;

public class MaintainanceViewModel extends ViewModel {
    private MutableLiveData<Misc> data;
    private ListenerRegistration listenerRegistration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public LiveData<Misc> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
        }
        return data;
    }

    public void loadData() {
        final DocumentReference ref = db.collection("Meta").document("Meta");
        listenerRegistration = ref.addSnapshotListener(((value, error) -> {
            assert value != null;
            Misc misc = value.toObject(Misc.class);
            data.setValue(misc);
        }));
    }

    public void stopListeningData(){
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopListeningData();
    }
}