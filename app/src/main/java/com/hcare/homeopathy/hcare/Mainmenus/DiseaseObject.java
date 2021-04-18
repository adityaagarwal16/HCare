package com.hcare.homeopathy.hcare.Mainmenus;

public class DiseaseObject {

    private String diseaseName;
    private int drawable;

    public DiseaseObject(String diseaseName, int drawable) {
        this.diseaseName = diseaseName;
        this.drawable = drawable;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
