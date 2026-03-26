package com.spm.gameapp.db.repository;

import androidx.lifecycle.MutableLiveData;

import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.SavedGame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedGameRepository {

    private SavedGameDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SavedGameRepository(SavedGameDao savedGameDao) {
        this.dao = savedGameDao;
    }

    //TODO: Insertar un joc

    //TODO: Consultar tots els jocs

    //TODO: Eliminar un joc


}
