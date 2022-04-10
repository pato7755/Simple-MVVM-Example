package com.learning.mvvmexample.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.learning.mvvmexample.models.NicePlace;
import com.learning.mvvmexample.repositories.NicePlaceRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<NicePlace>> mNicePlaces;
    private NicePlaceRepository mRepo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init() {
        if (mNicePlaces != null) {
            return;
        }
        mRepo = NicePlaceRepository.getInstance();
        mNicePlaces = mRepo.getNicePlaces();

    }

    public void addNewPlace(final NicePlace nicePlace){

        isUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>(){

            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                List<NicePlace> currentPlaces = mNicePlaces.getValue();
                currentPlaces.add(nicePlace);
                mNicePlaces.postValue(currentPlaces);
                isUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public LiveData<List<NicePlace>> getNicePlaces() {
        return mNicePlaces;
    }

    public LiveData<Boolean> getIsLoading(){
        return isUpdating;
    }

}
