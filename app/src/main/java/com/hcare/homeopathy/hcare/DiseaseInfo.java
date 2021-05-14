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

            case thyroid:
                return "Thyroid disease is a medical condition that affects the function of the thyroid gland. The thyroid gland is located at the front of the neck and produces thyroid hormones that travel through the blood to help regulate many other organs, meaning that it is an endocrine organ.";
            case diabetes:
                return "Diabetes is a metabolic disorder characterized by high blood sugar levels. " +
                        "Blood sugar is maintained by the pancreas when they produce sufficient amounts of insulin. " + "There are multiple associated symptoms such as increased appetite, thirst, and frequency of urine.";
            case skin:
                return "It's the largest organ of the human body." +
                        "The skin is one of the most common sites where a disease is expressed initially." +
                        "Some of the common diseases associated with the skin are are Eczema, Psoriasis, Herpes and Allergic reactions.";
            case female:
                return "A majority of females suffer from hormonal imbalances resulting in menstrual irregularities. Common diseases include Polycystic ovary syndrome , Thyroid hormone imbalance, fibroid uterus etc. Most of these cases result in mood swings which affect relationships and cause difficulties in conceiving.";
            case renalProblems:
                return "The most neglected health related problem as most of them get Palliated by drinking sufficient amounts of water. Usual diseases are urinary tract infection, renal calculi stones etc. which are expressed usually by burning or bloody urine and pain. It can lead to life threatening diseases like nephritic/nephrotic syndrome, Renal failure.";
            case weightLossAndGain:
                return "Obesity is usually caused due to a sedentary life style, habits such as a lack of activity and exercise, overeating, eating large amounts of junk food and large amounts of preservatives packed tinned foods. It may also be secondary to some hormonal diseases in females like PCOD or thyroid imbalance.";
            case headache:
                return "One of the most common symptoms which is usually indicative of an underlying health condition. Usually headaches are caused due to Migraine, tension/stress, a very hectic lifestyle and neglecting general health or might be secondary to severe medical conditions such as brain tumour, High Blood Pressure, Hemorrhage etc.";
            case hair:
                return "It is common cosmetic complaint of both male and female. Usual occurs due to stress,dry scalp, dandruff, hereditary causes, use of borewell water, Malnutrition , anemia. And is secondary to thyroid and pcod problems in females.";
            case men:
                return "Most common problems due to the hectic schedule and very material lifestyle. Some of the very common diseases are ED(ERECTION DYSFUNCTION), infertility , hairfall. Where in it becomes difficult to get treated due to lack of privacy and because of identity crisis.";
            case piles:
                return "It is due to the engorged veins in the anal canal due to constipated stools. And usually due to faulty food habits like excess of nonveg or fatty food, spicy food... Low fibre food in diet... Less water consumption on a daily basis.";
            case respiratoryProblems:
                return "It ranges from simple common cold which is expressed by sneezing ,nose block,running nose to cancer of lungs which may result in death. Other common diseases are ASTHAMA, BRONCHITIS, PNEUMONIA which is usually expressed as breathlessness on least physical exertion,cough with expectoration.";
            case bonesAndJoints:
                return "Usually problems seen in the 4th decade of life. Includes bone pain, stiffness of joints, degenerative bone disease like cervical or lumbar spondylosis, osteo arthritis. osteo porosis, gout, accumulation uric acid in the joints .";
            case depression:
                return "Depression";
            case growth:
                return "Growth completely depends on childs genes, growth of the child depends on the nutrition it receives in the early stages of life... In later stages it depends on the physical activities one involves himself in and the diet he chooses. Bt the initial delayed growth or growth retardation can be corrected but prognosis is low in later stages of life.";
            case heartProblems:
                return "Depends on the lifestyle of individual . And they are usually acquired over life time. Stress leads to BP. Eating more junk food and preserved food and fatty food leads to cholesterol problem in turn heart blockages. Smoking leads to infractions. However Malformations and birth defects usually need surgical correction itself.";
            case ENT:
                return "Precursors of respiratory diseases. Better get it solved in this initial stages. Which includes ear-otitis media, otomycosis, wax, hearing disability. Nose- common cold, sinusitis, allergic rhinitis, polyps, DNS. Throat - cough, laryngitis / pharyngitis, throat infection, tonsillitis .";
            case mouthAndTeeth:
                return "Common manifestation are oral ulcers, abscess, bad breath (Halitosis), ranula. Teeth- caries of tooth, bleeding gums, swollen gums, common tooth ache, sensitivity of tooth.";
            case children:
                return "Children";
            case nutritionAndHealth:
                return "Nutrition & health";
            case maternal:
                return "Maternal";
            case others:
                return "Others";

            default:
                return "No information could be found on the particular disease.";
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
