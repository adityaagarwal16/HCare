package com.hcare.homeopathy.hcare.OrderTreatment;

public class PrescriptionObject {

    public PrescriptionObject(String medicine_name, String medicine_days,
                              String medicine_time, String medicine_Instruction, String instructions) {
        Medicine_name = medicine_name;
        Medicine_days = medicine_days;
        Medicine_time = medicine_time;
        Medicine_Instruction = medicine_Instruction;
        Instructions = instructions;
    }

    public PrescriptionObject(){

    }

    public String getMedicine_name() {
        return Medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        Medicine_name = medicine_name;
    }

    public String getMedicine_days() {
        return Medicine_days;
    }

    public void setMedicine_days(String medicine_days) {
        Medicine_days = medicine_days;
    }

    public String getMedicine_time() {
        return Medicine_time;
    }

    public void setMedicine_time(String medicine_time) {
        Medicine_time = medicine_time;
    }

    public String getMedicine_Instruction() {
        return Medicine_Instruction;
    }

    public void setMedicine_Instruction(String medicine_Instruction) {
        Medicine_Instruction = medicine_Instruction;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    private String Medicine_name, Medicine_days,Medicine_time,Medicine_Instruction,Instructions;

}
