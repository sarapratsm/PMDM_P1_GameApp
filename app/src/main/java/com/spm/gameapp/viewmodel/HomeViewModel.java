package com.spm.gameapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.spm.gameapp.api.model.Game;

import java.util.List;

/**
 * Per gestionar la cerca de jocs a la API de RAWG i els filtres de la pantalla principal.
 */
public class HomeViewModel extends ViewModel {

    private final GameRepository repository;

    private List<Game> games;

}
