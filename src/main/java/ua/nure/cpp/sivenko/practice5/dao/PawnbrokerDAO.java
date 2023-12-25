package ua.nure.cpp.sivenko.practice5.dao;

import ua.nure.cpp.sivenko.practice5.model.Pawnbroker;

import java.util.List;

public interface PawnbrokerDAO {
    Pawnbroker getPawnbrokerById(long pawnbrokerId);
    Pawnbroker getPawnbrokerByContactNumber(String contactNumber);
    Pawnbroker getPawnbrokerByEmail(String email);
    List<Pawnbroker> getAllPawnbrokers();

    void addPawnbroker(Pawnbroker pawnbroker);
    void updatePawnbroker(Pawnbroker pawnbroker);
    void deletePawnbroker(long pawnbrokerId);
}
