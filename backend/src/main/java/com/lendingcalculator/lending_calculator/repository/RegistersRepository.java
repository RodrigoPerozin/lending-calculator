package com.lendingcalculator.lending_calculator.repository;

import java.util.ArrayList;

import com.lendingcalculator.lending_calculator.models.RowRegister;

public class RegistersRepository {
    private ArrayList<RowRegister> registers = new ArrayList<>();
    
    public void addRegister(RowRegister register) {
        this.registers.add(register);
    }
    public ArrayList<RowRegister> getRegisters() {
        return this.registers;
    }
    public void clearRegisters() {
        this.registers.clear();
    }
    public int getSize() {
        return this.registers.size();
    }
    public RowRegister getRegister(int index) {
        return this.registers.get(index);
    }
    public void removeRegister(int index) {
        this.registers.remove(index);
    }
    public RowRegister getLastRegister() {
        return this.registers.get(this.registers.size() - 1);
    }
}
