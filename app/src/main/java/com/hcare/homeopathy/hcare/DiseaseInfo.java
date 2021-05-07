package com.hcare.homeopathy.hcare;

public class DiseaseInfo {

    Diseases disease;

    public DiseaseInfo(Diseases disease) {
        this.disease = disease;
    }

    public String getDiseaseName() {
        switch(disease) {
            case thyroid:
                return "Thyroid";
            case diabetes:
                return "Diabetes";
            case skin:
                return "Skin";
            case female:
                return "Female";
            case renalProblems:
                return "Renal Problems";
            case weightLossAndGain:
                return "Weight loss & gain";
            case headache:
                return "Headache";
            case hair:
                return "Hair";
            case men:
                return "Men";
            case piles:
                return "Piles";
            case respiratoryProblems:
                return "Respiratory problems";
            case bonesAndJoints:
                return "Bones and joints";
            case depression:
                return "Depression";
            case growth:
                return "Growth";
            case heartProblems:
                return "Heart problems";
            case ENT:
                return "ENT";
            case mouthAndTeeth:
                return "Mouth and teeth";
            case children:
                return "Children";
            case nutritionAndHealth:
                return "Nutrition & health";
            case maternal:
                return "Maternal";
            case others:
                return "Others";

            default:
                return "";
        }
    }

    public String getInfo() {
        switch(disease) {
            default:
                return "Thyroid disease is a medical condition that affects the function of the thyroid gland. The thyroid gland is located at the front of the neck and produces thyroid hormones that travel through the blood to help regulate many other organs, meaning that it is an endocrine organ.";
        }
    }

    public int getDrawable() {
        switch(disease) {
            case thyroid:
                return R.drawable.thyroid;
            case diabetes:
                return R.drawable.diabetes;
            case skin:
                return R.drawable.skin;
            case female:
                return R.drawable.female;
            case renalProblems:
                return R.drawable.renal;
            case weightLossAndGain:
                return R.drawable.weight;
            case headache:
                return R.drawable.diabetes;
            case hair:
                return R.drawable.diabetes;
            case men:
                return R.drawable.diabetes;
            case piles:
                return R.drawable.diabetes;
            case respiratoryProblems:
                return R.drawable.diabetes;
            case bonesAndJoints:
                return R.drawable.diabetes;
            case depression:
                return R.drawable.diabetes;
            case growth:
                return R.drawable.diabetes;
            case heartProblems:
                return R.drawable.diabetes;
            case ENT:
                return R.drawable.diabetes;
            case mouthAndTeeth:
                return R.drawable.diabetes;
            case children:
                return R.drawable.diabetes;
            case nutritionAndHealth:
                return R.drawable.diabetes;
            case maternal:
                return R.drawable.diabetes;
            case others:
                return R.drawable.diabetes;


            default:
                return 0;
        }
    }

}
